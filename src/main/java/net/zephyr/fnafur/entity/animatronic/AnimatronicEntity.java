package net.zephyr.fnafur.entity.animatronic;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.GameEventTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.Vibrations;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import net.minecraft.world.event.listener.Vibration;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.entity.animatronic.data.AnimatronicPathNodeMaker;
import net.zephyr.fnafur.entity.animatronic.data.CpuData;
import net.zephyr.fnafur.entity.animatronic.goals.AnimMeleeAttackGoal;
import net.zephyr.fnafur.entity.animatronic.goals.AnimTargetGoal;
import net.zephyr.fnafur.entity.animatronic.goals.AnimWanderAroundFarGoal;
import net.zephyr.fnafur.entity.animatronic.voice.EntityVoiceSoundInstance;
import net.zephyr.fnafur.entity.animatronic.voice.VoiceSource;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.init.block_init.PropInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.item.animatronic.CPUItem;
import net.zephyr.fnafur.networking.entity.*;
import net.zephyr.fnafur.networking.nbt_updates.UpdateEntityNbtC2SGetFromServerPayload;
import net.zephyr.fnafur.util.jsonReaders.animatronics.AnimatronicDataHandler;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IEntityPathfindingHeightOverride;
import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.state.AnimationTest;
import software.bernie.geckolib.animation.object.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.state.KeyFrameEvent;
import software.bernie.geckolib.cache.animation.keyframeevent.CustomInstructionKeyframeData;
import software.bernie.geckolib.cache.animation.keyframeevent.SoundKeyframeData;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;
import java.util.function.BiConsumer;

public class AnimatronicEntity extends PathAwareEntity implements GeoEntity, Vibrations, IEntityPathfindingHeightOverride, VoiceSource {

    boolean forceCrawl = false;

    private final EntityGameEventHandler<VibrationListener> gameEventHandler;
    private Vibrations.ListenerData vibrationListenerData;
    private final Vibrations.Callback vibrationCallback;
    int updateDepth = 0;
    public List<IEntityDataSaver> sources = new ArrayList<>();
    public double force_age = 0;
    public boolean isMenu = false;
    boolean canRunCheck;
    boolean canGlowCheck;

    public BlockPos lastSeenPosition = null;
    public BlockPos lastHeardPosition = null;
    public int timeSinceLastHeard = 0;
    public EntityVoiceSoundInstance currentVoiceSound = null;
    boolean newVoiceSound = false;
    public boolean isRetreating = false;
    public int timeSinceLastMoved = 0;
    public double frozenSpeed = 0;
    public float frozenYaw = 0;
    public float frozenHeadYaw = 0;
    public float frozenBodyYaw = 0;
    public float frozenPitch = 0;
    public boolean isFrozen = false;

    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    List<String> blinkList = List.of(
           "performance",
           "walk_upper",
           "walk_upper_night",
           "backwards_walk_upper",
           "run_upper",
           "idle",
           "player_idle",
           "crawl_idle",
           "stage_idle",
           "haunted_idle",
           "drag_idle",
           "death"
    );
    String currentAnim = "";
    int blinkDelay;
    public AnimatronicEntity(EntityType<? extends PathAwareEntity> entityType, World world){
        super(entityType, world);

        this.navigation = new AnimatronicNavigation(this, world);
        this.vibrationCallback = new AnimatronicEntity.VibrationCallback();
        this.vibrationListenerData = new Vibrations.ListenerData();
        this.gameEventHandler = new EntityGameEventHandler<>(new Vibrations.VibrationListener(this));

        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 16);
        this.setPathfindingPenalty(PathNodeType.STICKY_HONEY, 24);
        this.setPathfindingPenalty(PathNodeType.COCOA, 8);

        //this.reachInHitbox = new AnimatronicPart(this, "reach_in_hitbox", 1.0F, 1.0F);
    }



    @Override
    public void updateEventHandler(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback) {
        if (this.getEntityWorld() instanceof ServerWorld serverWorld) {
            callback.accept(this.gameEventHandler, serverWorld);
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }


    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        //if(!player.getMainHandStack().isEmpty()) return ActionResult.PASS;

        if(getEntityWorld().isClient()){
            getAnimatableInstanceCache().getManagerForId(getId()).getAnimationControllers().forEach((name, controller) -> {
                controller.reset();
            });
            if(player.getMainHandStack().isEmpty() && !player.isSneaking()) {
                String name = this.getAnimatronicAmbientSoundName();
                playVoiceSound(name, this.getAnimatronicAmbientSound(name), this.ambientSoundVolume());
                //playVoiceSound(name, SoundEvents.INTENTIONALLY_EMPTY, this.ambientSoundVolume());
                return ActionResult.SUCCESS;
            }
        }
        else{
            if (player.getMainHandStack().isEmpty() && player.isSneaking()) {
                if (getData().DATA_LIST.containsKey(CpuData.OnReset.getDefault().getKey())) {
                    if(getData().DATA_LIST.get(CpuData.OnReset.getDefault().getKey()) instanceof CpuData.OnReset resetMode){
                        resetAnimatronic(resetMode);
                        return ActionResult.SUCCESS;
                    }
                }
            }
        }
        return super.interactAt(player, hitPos, hand);
    }

    public void resetAnimatronic(CpuData.OnReset mode){
        Vec3d vec3d = getSpawnPos();

        isRetreating = false;
        switch (mode){
            case TELEPORT -> {
                Set<PositionFlag> flags = EnumSet.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z, PositionFlag.X_ROT, PositionFlag.Y_ROT);
                this.getNavigation().stop();
                this.setPosition(vec3d);
                this.lastX = vec3d.x;
                this.lastY = vec3d.y;
                this.lastZ = vec3d.z;
                this.setYaw(this.getSpawnYaw());
                this.setHeadYaw(this.getSpawnYaw());
                this.setBodyYaw(this.getSpawnYaw());
                this.setPitch(0);
                this.setVelocity(0, 0, 0);
                this.lastBodyYaw = this.getSpawnYaw();
                this.lastHeadYaw = this.getSpawnYaw();
                this.lastYaw = this.getSpawnYaw();
                this.lastYaw = this.getSpawnYaw();
                setFrozen(false);
            }
            case WALK -> {
                isRetreating = true;
                int speed = walkingSpeed();
                int maxSpeed = CpuData.MovementSpeed.getDefaultValue() + CpuData.RunSpeed.getDefaultValue();
                float final_speed = MathHelper.lerp(((float) speed / maxSpeed), 0f, 2.5f) / 1.5f;
                this.getNavigation().startMovingTo(vec3d.x, vec3d.y, vec3d.z, 0, final_speed);
            }
            case RUN -> {
                isRetreating = true;
                int speed = runningSpeed();
                int maxSpeed = CpuData.MovementSpeed.getDefaultValue() + CpuData.RunSpeed.getDefaultValue();
                float final_speed = MathHelper.lerp(((float) speed / maxSpeed), 0f, 2.5f) / 1.5f;
                this.getNavigation().startMovingTo(vec3d.x, vec3d.y, vec3d.z, 0, final_speed);
            }
        }

        ((IEntityDataSaver)this).getPersistentData().putBoolean("isRetreating", isRetreating);
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        if(getEntityWorld().isClient()){
            double spawnX = packet.getX();
            double spawnY = packet.getY();
            double spawnZ = packet.getZ() - 1.0D;
            float spawnYaw = packet.getYaw();
            ClientPlayNetworking.send(new SetEntitySpawnDataC2SPayload(getId(), spawnX, spawnY, spawnZ, spawnYaw));
        }
    }

    public BlockPos getSpawnBlockPos() {
        double x = getSpawnPos().x;
        double y = getSpawnPos().y;
        double z = getSpawnPos().z;
        return new BlockPos((int) x, (int) y, (int) z);
    }

    public Vec3d getSpawnPos() {
        double x = ((IEntityDataSaver) this).getPersistentData().getDouble("spawnX").orElse(0.0D);
        double y = ((IEntityDataSaver) this).getPersistentData().getDouble("spawnY").orElse(0.0D);
        double z = ((IEntityDataSaver) this).getPersistentData().getDouble("spawnZ").orElse(0.0D) + 1;
        return new Vec3d(x, y, z);
    }

    public float getSpawnYaw() {
        return ((IEntityDataSaver) this).getPersistentData().getFloat("spawnYaw").orElse(0.0F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("Lower", 3, this::lowerAnimController)
                .setSoundKeyframeHandler(this::lowerSoundKeyframes));
        controllers.add(new AnimationController<>("Upper", 3, this::upperAnimController)
                .setSoundKeyframeHandler(this::upperSoundKeyframes));
        controllers.add(new AnimationController<>("Blink", 0, this::blinkAnimController)
                .setCustomInstructionKeyframeHandler(this::instructionHandler));
        controllers.add(new AnimationController<>("Jaw", 1, this::jawAnimController)
                .additiveAnimations()
        );
    }

    private PlayState jawAnimController(AnimationTest<GeoAnimatable> geoAnimatableAnimationTest) {
        if(currentVoiceSound != null){
            String name = currentVoiceSound.name;
            if(newVoiceSound){
                geoAnimatableAnimationTest.controller().reset();
                newVoiceSound = false;
            }
            return geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenPlay(getAnimationName("animation.talk." + name)));
        }
        return PlayState.STOP;
    }

    private void lowerSoundKeyframes(KeyFrameEvent<AnimatronicEntity, SoundKeyframeData> animatronicEntitySoundKeyframeDataKeyFrameEvent) {
        String sound = animatronicEntitySoundKeyframeDataKeyFrameEvent.keyframeData().getSound();

        if(sound.equals("step")){
            ClientPlayNetworking.send(new WalkSoundPlayerC2SPayload(getId()));
            return;
        }
    }
    private void upperSoundKeyframes(KeyFrameEvent<AnimatronicEntity, SoundKeyframeData> animatronicEntitySoundKeyframeDataKeyFrameEvent) {
        //System.out.println(animatronicEntitySoundKeyframeDataKeyFrameEvent.keyframeData().getSound());
    }

    private void instructionHandler(KeyFrameEvent<GeoAnimatable, CustomInstructionKeyframeData> handler) {
        
    }

    private PlayState blinkAnimController(AnimationTest<GeoAnimatable> geoAnimatableAnimationTest) {

        if(!isMenu && getAnimatronicPose().canBlink()){
            if(blinkDelay <= 0){
                Random random1 = Random.create();
                blinkDelay = random1.nextBetween(100, 250);
                geoAnimatableAnimationTest.controller().reset();
            }
            RawAnimation anim = RawAnimation.begin().thenPlayAndHold(getAnimationName("blink"));
            return geoAnimatableAnimationTest.setAndContinue(anim);
        }
        blinkDelay = 0;
        return PlayState.STOP;
    }

    private PlayState lowerAnimController(AnimationTest<AnimatronicEntity> animatronicEntityAnimationState) {

        animatronicEntityAnimationState.controller().setAnimationSpeed(1);
        animatronicEntityAnimationState.controller().setTransitionTicks(3);
        if(isMenu) {
            animatronicEntityAnimationState.controller().setTransitionTicks(0);
            return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(getAnimationName("loweridle")));
        }
        if(isFrozen){
            animatronicEntityAnimationState.controller().setAnimationSpeed(0);
        }

        String anim = getAnimatronicPose().getLowerIdle();

        double speed = getMovement().horizontalLength() * 20;
        if(animatronicEntityAnimationState.isMoving() && speed > 0){

            anim = isRunning() ? getAnimatronicPose().getLowerRun() : getAnimatronicPose().getLowerWalk();

            if(isRunning()){
                speed *= 0.75f;
            }
            animatronicEntityAnimationState.controller().setAnimationSpeed(speed);
        }

        if(anim.isEmpty()) return PlayState.STOP;

        return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(getAnimationName(anim)));
    }

    private PlayState upperAnimController(AnimationTest<AnimatronicEntity> animatronicEntityAnimationState) {

        animatronicEntityAnimationState.controller().setAnimationSpeed(1);
        animatronicEntityAnimationState.controller().setTransitionTicks(3);

        if(isMenu) {
            animatronicEntityAnimationState.controller().setTransitionTicks(0);
            return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(getAnimationFullName("menu_preview")));
        }
        if(isFrozen){
            animatronicEntityAnimationState.controller().setAnimationSpeed(0);
        }

        String anim = getAnimatronicPose().getUpperIdle();

        double speed = getMovement().horizontalLength() * 20;
        if(animatronicEntityAnimationState.isMoving() && speed > 0){

            anim = isRunning() ? getAnimatronicPose().getUpperRun() : getAnimatronicPose().getUpperWalk();

            if(isRunning()){
                speed *= 0.75f;
            }
            animatronicEntityAnimationState.controller().setAnimationSpeed(speed);
        }

        if(anim.isEmpty()) return PlayState.STOP;

        return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(getAnimationName(anim)));
    }

    public void playWalkSound(World world){
        if(!world.isClient()) {
            this.playSound(SoundsInit.FNAF1_FOOTSTEPS, 1.0f, 1.0f);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        //super.playStepSound(pos, state);
    }

    String getAnimationFullName(String currentAnim){
        this.currentAnim = currentAnim;
        return AnimatronicDataHandler.getAnimationFullName(this.currentAnim, getAnimPrefix());
    }

    String getAnimationName(String currentAnim){
        return AnimatronicDataHandler.getAnimationFullName(currentAnim, getAnimPrefix());
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public float getStepHeight() {
        return 0.9f;
    }

    @Override
    public void tick() {
        if(!(isFrozen)){
            blinkDelay = Math.max(0, blinkDelay - 1);
        }
        if(getEntityWorld().isClient() && currentVoiceSound != null && !currentVoiceSound.getId().equals(SoundManager.INTENTIONALLY_EMPTY_ID) && !MinecraftClient.getInstance().getSoundManager().isPlaying(currentVoiceSound)){
            currentVoiceSound = null;
        };
        if(isMenu){
            age++;
        }

        if(!getEntityWorld().isClient()) {
            ((IEntityDataSaver) this).getPersistentData().putString("getupAnim", "");
        }
        else{
            if (!((IEntityDataSaver) this).getPersistentData().contains("synced")) {
                ClientPlayNetworking.send(new UpdateEntityNbtC2SGetFromServerPayload(getId()));
            }

            //if (((IEntityDataSaver) this).getServerUpdateStatus()) {
            //    //MinecraftClient.getInstance().player.sendMessage(Text.literal("SYNCING PROP"), false);
            //    ClientPlayNetworking.send(new UpdateBlockNbtC2SPayload(getPos().asLong(), ((IEntityDataSaver) this).getPersistentData()));
            //}
        }

        if(!getEntityWorld().isClient()) {
            if (getTarget() == null && canSee()) {
                if (lastHeardPosition != null) {
                    if (timeSinceLastHeard > 0 || getNavigation().isFollowingPath()) {
                        if (timeSinceLastHeard > 0) {
                            timeSinceLastHeard -= 1;
                            getNavigation().stop();
                            if (timeSinceLastHeard == 0) {
                                startMovingToHeardPosition();
                            }
                        }
                        getLookControl().lookAt(lastHeardPosition.getX(), lastHeardPosition.up().getY(), lastHeardPosition.getZ(), 30.0F, 30.0F);

                    } else {
                        lastHeardPosition = null;
                    }
                }
            } else if (getTarget() != null) {
                lastHeardPosition = null;
            }
        }

        if(!getEntityWorld().isClient()) {
            VibrationTicker.tick(this.getEntityWorld(), this.vibrationListenerData, this.vibrationCallback);


            this.tickSightBehavior();

            if (isRetreating) {
                if (this.getNavigation().getCurrentPath() == null || this.getNavigation().isIdle()) {
                    timeSinceLastMoved++;
                    if (getData().DATA_LIST.containsKey(CpuData.OnReset.getDefault().getKey())) {
                        if (getData().DATA_LIST.get(CpuData.OnReset.getDefault().getKey()) instanceof CpuData.OnReset resetMode) {
                            resetAnimatronic(resetMode);
                        }
                    }
                } else {
                    timeSinceLastMoved = 0;
                }
                if (
                        timeSinceLastMoved > 20 ||
                                getEntityPos().isWithinRangeOf(getSpawnPos(), 1f, 1)
                ) {
                    if (getEntityPos().equals(getSpawnPos()) && getYaw() == getSpawnYaw() && getHeadYaw() == getSpawnYaw() && getBodyYaw() == getSpawnYaw()) {

                        isRetreating = false;
                        ((IEntityDataSaver) this).getPersistentData().putBoolean("isRetreating", isRetreating);
                    }
                    resetAnimatronic(CpuData.OnReset.TELEPORT);
                }
            } else {
                timeSinceLastMoved = 0;
            }



            if(getWanderBehavior() == CpuData.WanderBehavior.STAND_AT_SPAWN && !getBlockPos().equals(getSpawnBlockPos()) && getYaw() != getSpawnYaw() && !isRetreating && !(isAggressive() && getTarget() != null) && lastHeardPosition == null){
                if (getData().DATA_LIST.containsKey(CpuData.OnReset.getDefault().getKey())) {
                    if (getData().DATA_LIST.get(CpuData.OnReset.getDefault().getKey()) instanceof CpuData.OnReset resetMode) {
                        resetAnimatronic(resetMode);
                    }
                }
            }
        }

        //this.tickAnimatronicMovement();
        this.tickUpdatePose();
        super.tick();
    }

    public void tickSightBehavior(){
        if(getEntityWorld().isClient()) return;
        switch (getBehaviorWhenSeen()){
            case FREEZE_ON_SIGHT -> {
                if(isBeingWatched() && !isRetreating){
                    freezeMovement();
                }
                else{
                    if(isFrozen){
                        setFrozen(false);
                        getNavigation().setSpeed(frozenSpeed);
                    }
                }
            }
            case RESET_ON_SIGHT -> {
            }
            case FREEZE_ON_CAMERA -> {
            }
            case RESET_ON_CAMERA -> {
            }
            default -> {
            }
        }

        if(isFrozen && !getBlockPos().equals(getSpawnBlockPos())){
            getNavigation().stop();
            setSidewaysSpeed(0.0F);
            setUpwardSpeed(0.0F);
            setMovementSpeed(0.0F);
            setVelocity(0.0, getVelocity().y, 0.0);

            if(getTarget() != null){
                getLookControl().lookAt(getTarget(), 45.0F, 30.0F);
            }
        }
    }

    void setFrozen(boolean frozen){
        isFrozen = frozen;
        for(ServerPlayerEntity player : PlayerLookup.tracking(this)){
            ServerPlayNetworking.send(player, new SetAnimatronicFrozenStatusS2CPayload(getId(), frozen, frozenHeadYaw, frozenBodyYaw, frozenPitch));
        }

    }

    void freezeMovement(){
        if(!isFrozen){
            frozenSpeed = ((AnimatronicNavigation)getNavigation()).getSpeed();
            frozenYaw = getYaw();
            frozenHeadYaw = getHeadYaw();
            frozenBodyYaw = getBodyYaw();
            frozenPitch = getPitch();
            setFrozen(true);
        }
        getNavigation().setSpeed(0);
    }

    boolean isBeingWatched(){
        if(getEntityWorld().isClient()) return false;

        List<PlayerEntity> players = getEntityWorld().getEntitiesByClass(PlayerEntity.class, getBoundingBox().expand(10), (entity) -> (entity instanceof PlayerEntity && !entity.isSpectator() && !entity.isCreative()));

        for(PlayerEntity player : players){
            Vec3d difference = this.getEntityPos().add(player.getEntityPos().multiply(-1));
            float angle = player.getHeadYaw() - difference.getYawAndPitch().y;
            while(angle < 0) angle += 360;
            angle %= 360;
            if(angle <= sightConeAngle() || angle >= 360 - sightConeAngle()){
                if(canSee(player)){
                    return true;
                }
            }
        }

        return false;
    }

    //TODO skilld :)
//    public void tickAnimatronicMovement() {
//        int movementTimer = ((IEntityDataSaver) this).getPersistentData().getInt("movement_timer", 1);
//
//        if (this.isAggressive()) return;
//
//        if (this.aiMovementLevel() <= 0) {
//            this.getNavigation().stop();
//            ((IEntityDataSaver) this).getPersistentData().putInt("movement_timer", 99);
//        } else {
//            int movementChance = 5;
//            movementChance *= this.aiMovementLevel();
//            ((IEntityDataSaver) this).getPersistentData().putInt("movement_timer", movementTimer - 1);
//            if (movementTimer <= 0) {
//                if (random.nextInt(movementChance) == 0) {
//                    // Put pathfinding stuff here
//                    ((IEntityDataSaver) this).getPersistentData().putInt("movement_timer", 99);
//                }
//            } else {
//                this.getNavigation().stop();
//            }
//        }
//    }

    public void tickUpdatePose() {
        World world = this.getEntityWorld();
        if (world == null) return;

        Box box = AnimatronicPose.NONE.getPoseDimensions().getBoxAt(this.getEntityPos());
        Box box2 = AnimatronicPose.CRAWLING.getPoseDimensions().getBoxAt(this.getEntityPos());

        boolean wouldSuffocate = !this.getEntityWorld().isBlockSpaceEmpty(this, box);
        boolean wouldntSuffocateCrawling = this.getEntityWorld().isBlockSpaceEmpty(this, box2);

        if (this.canCrawl() && (this.isCrawlSpaceAvailable(world) && !this.getNavigation().isIdle()) || wouldSuffocate && wouldntSuffocateCrawling) {
            this.setAnimatronicPose(AnimatronicPose.CRAWLING);
        } else {

            Vec3d pos = this.getEntityPos();
            BlockPos blockPos = new BlockPos((int)Math.floor(pos.getX()), (int)Math.floor(pos.getY()), (int)Math.floor(pos.getZ())).offset(this.getHorizontalFacing());
            if (this.isCrawlSpaceAvailable(world) && this.canReachIn() && this.getEntityWorld() instanceof ServerWorld serverWorld) {
                for (PlayerEntity player : this.getEntityWorld().getEntitiesByClass(PlayerEntity.class, new Box(blockPos.toCenterPos(), blockPos.offset(this.getHorizontalFacing()).toCenterPos()), (entity) -> (entity instanceof PlayerEntity))) {
                    if (!(player.isCreative() || player.isSpectator())) player.kill(serverWorld);
                }
            }

            if (isAggressive()) {
                this.setAnimatronicPose(AnimatronicPose.AGGRESSIVE);
            } else {
                this.setAnimatronicPose(AnimatronicPose.NONE);
            }
        }
    }

    // Checks to see if there's a 1 block tall gap for an animatronic to crawl through
    public boolean isCrawlSpaceAvailable(World world) {

        Vec3d pos = this.getEntityPos();
        BlockPos blockPos = new BlockPos((int)Math.floor(pos.getX()), (int)Math.floor(pos.getY()), (int)Math.floor(pos.getZ())).offset(this.getHorizontalFacing());

        return !world.getBlockState(blockPos.up()).isAir()
                && !Block.sideCoversSmallSquare(world, blockPos, this.getHorizontalFacing())
                && !(getAnimatronicPose() == AnimatronicPose.CRAWLING && wouldNotSuffocateInPose(EntityPose.STANDING));
    }

    void startMovingToHeardPosition(){

        RaycastContext context = new RaycastContext(
                getEyePos(),
                lastHeardPosition.toCenterPos(),
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                this
        );
        BlockHitResult result = getEntityWorld().raycast(context);

        if(result.getType() != HitResult.Type.MISS && !result.getBlockPos().equals(lastHeardPosition)) {
            int speed = runningSpeed();
            int maxSpeed = CpuData.MovementSpeed.getDefaultValue() + CpuData.RunSpeed.getDefaultValue();

            float finalspeed = MathHelper.lerp(((float) speed / maxSpeed), 0f, 2.5f) / 1.5f;

            EntityNavigation nav = this.getNavigation();

            nav.stop(); // force recomputation

            Path path = nav.findPathTo(lastHeardPosition, 0);
            if (path != null) {
                nav.startMovingAlong(path, finalspeed);
            }
        }
    }

    public CpuData.MovementMode getMovementMode(){
        CpuData data = getData();

        CpuData.MovementMode mode = CpuData.MovementMode.getDefault();

        if (data.DATA_LIST.get(CpuData.MovementMode.getDefault().getKey()) instanceof CpuData.MovementMode mode2) {
            mode = mode2;
        }

        return mode;
    }
    public CpuData.WanderBehavior getWanderBehavior(){
        CpuData data = getData();

        CpuData.WanderBehavior mode = CpuData.WanderBehavior.getDefault();

        if (data.DATA_LIST.get(CpuData.WanderBehavior.getDefault().getKey()) instanceof CpuData.WanderBehavior mode2) {
            mode = mode2;
        }

        return mode;
    }

    public CpuData.BehaviorWhenSeen getBehaviorWhenSeen(){
        CpuData data = getData();

        CpuData.BehaviorWhenSeen behavior = CpuData.BehaviorWhenSeen.getDefault();

        if (data.DATA_LIST.get(CpuData.BehaviorWhenSeen.getDefault().getKey()) instanceof CpuData.BehaviorWhenSeen behavior2) {
            behavior = behavior2;
        }

        return behavior;
    }

    public boolean isRunning(){
        if(!getEntityWorld().isClient()) {
            CpuData data = getData();

            boolean runBase = getMovementMode() == CpuData.MovementMode.RUN;
            boolean runChase = false;

            if (data.DATA_LIST.containsKey(CpuData.AggressionMode.getDefault().getKey())) {
                runChase = data.DATA_LIST.get(CpuData.AggressionMode.getDefault().getKey()) == CpuData.AggressionMode.CHASE_RUN;
            }

            runChase = runChase && getTarget() != null;

            boolean run = runChase || runBase;
            if(canRunCheck != run){
            canRunCheck = run;
            for(ServerPlayerEntity p : PlayerLookup.world((ServerWorld) getEntityWorld())){
                ServerPlayNetworking.send(p, new SetEntityRunS2CPayload(getId(), canRunCheck));
            }
            }
            return run;
        }
        else{
            return canRunCheck;
        }
    }

    public CpuData.GlowingEyesMode getEyesGlowMode(){
        CpuData data = getData();

        CpuData.GlowingEyesMode mode = CpuData.GlowingEyesMode.getDefault();

        if (data.DATA_LIST.get(CpuData.GlowingEyesMode.getDefault().getKey()) instanceof CpuData.GlowingEyesMode mode2) {
            mode = mode2;
        }

        return mode;
    }

    public CpuData.GlowingEyesColor getEyesGlowColor(){
        CpuData data = getData();

        CpuData.GlowingEyesColor color = CpuData.GlowingEyesColor.getDefault();

        if (data.DATA_LIST.get(CpuData.GlowingEyesColor.getDefault().getKey()) instanceof CpuData.GlowingEyesColor color2) {
            color = color2;
        }

        return color;
    }

    public boolean shouldEyesGlow(){
        if(!getEntityWorld().isClient()) {
            CpuData data = getData();

            boolean shouldGlow = false;

            if (data.DATA_LIST.containsKey(CpuData.GlowingEyesTrigger.getDefault().getKey())) {
                shouldGlow = data.DATA_LIST.get(CpuData.GlowingEyesTrigger.getDefault().getKey()) != CpuData.GlowingEyesTrigger.NEVER;
                if(data.DATA_LIST.get(CpuData.GlowingEyesTrigger.getDefault().getKey()) == CpuData.GlowingEyesTrigger.CHASING){
                    shouldGlow = getTarget() != null || (lastHeardPosition != null && timeSinceLastHeard > 0);
                }
                if(data.DATA_LIST.get(CpuData.GlowingEyesTrigger.getDefault().getKey()) == CpuData.GlowingEyesTrigger.FLICKER){
                    shouldGlow = RandomUtils.nextInt(0, 20) < 12;
                }
            }

            boolean glow = shouldGlow;
            if(canGlowCheck != glow){
                canGlowCheck = glow;
            }
            return glow;
        }
        else{

            ClientPlayNetworking.send(new SetEntityGlowC2SPayload(getId()));
            return canGlowCheck;
        }
    }

    public int runningSpeed(){

        CpuData data = getData();

        int walk = walkingSpeed();
        int run = 0;

        if (data.DATA_LIST.get(CpuData.RunSpeed.getDefault().getKey()) instanceof CpuData.CpuDataRangeArgument range) {
            run = range.getValue();
        }

        if (this.isRetreating) {
            return walk + run;
        } else {
            return isRunning() ? walk + run : walk;
        }
    }

    public int walkingSpeed(){

        CpuData data = getData();

        int walk = 0;

        if (data.DATA_LIST.get(CpuData.MovementSpeed.getDefault().getKey()) instanceof CpuData.CpuDataRangeArgument range) {
            walk = range.getValue();
        }

        return walk;
    }

    public void setCanRun(boolean run){
        canRunCheck = run;
    }
    public void setCanGlow(boolean glow){
        canGlowCheck = glow;
    }

    public int sightRange(){

        CpuData data = getData();

        int sight = 0;

        if (data.DATA_LIST.get(CpuData.SightRange.getDefault().getKey()) instanceof CpuData.CpuDataRangeArgument range) {
            sight = range.getValue();
        }

        return sight;
    }

    public int sightConeAngle(){
        return 75;
    }

    public int aiMovementLevel(){

        CpuData data = getData();

        int level = 0;

        if (data.DATA_LIST.get(CpuData.AIMovementLevel.getDefault().getKey()) instanceof CpuData.CpuDataRangeArgument range) {
            level = range.getValue();
        }

        return level;
    }

    public float ambientSoundVolume(){

        CpuData data = getData();

        float volume = 0;

        if (data.DATA_LIST.get(CpuData.AmbientSoundsVolume.getDefault().getKey()) instanceof CpuData.CpuDataRangeArgument range) {
            volume = range.getFloat01Value();
        }

        return volume;
    }

    public boolean isAggressive(){
        CpuData data = getData();

        boolean aggressive = false;

        if(data.DATA_LIST.containsKey(CpuData.AggressionMode.getDefault().getKey())){
            aggressive = data.DATA_LIST.get(CpuData.AggressionMode.getDefault().getKey()) == CpuData.AggressionMode.CHASE_WALK || data.DATA_LIST.get(CpuData.AggressionMode.getDefault().getKey()) == CpuData.AggressionMode.CHASE_RUN;
        }

        return aggressive;
    }

    public boolean canCrawl(){
        CpuData data = getData();

        boolean crawl = false;

        if(data.DATA_LIST.containsKey(CpuData.VentBehavior.getDefault().getKey())){
            crawl = data.DATA_LIST.get(CpuData.VentBehavior.getDefault().getKey()) == CpuData.VentBehavior.CRAWL;
        }

        return crawl;
    }

    public boolean canReachIn(){
        CpuData data = getData();

        boolean reachIn = false;

        if(data.DATA_LIST.containsKey(CpuData.VentBehavior.getDefault().getKey())){
            reachIn = data.DATA_LIST.get(CpuData.VentBehavior.getDefault().getKey()) == CpuData.VentBehavior.REACH_IN;
        }

        return reachIn;
    }

    public boolean canSee(){
        CpuData data = getData();

        boolean canSee = false;

        if(data.DATA_LIST.containsKey(CpuData.AggressionMode.getDefault().getKey())){
            canSee = data.DATA_LIST.get(CpuData.VisionMode.getDefault().getKey()) != CpuData.VisionMode.BLIND && data.DATA_LIST.get(CpuData.VisionMode.getDefault().getKey()) != CpuData.VisionMode.BLIND_AND_DEAF;
        }

        return canSee;
    }

    public boolean canHear(){
        CpuData data = getData();

        boolean canHear = false;

        if(data.DATA_LIST.containsKey(CpuData.AggressionMode.getDefault().getKey())){
            canHear = data.DATA_LIST.get(CpuData.VisionMode.getDefault().getKey()) != CpuData.VisionMode.DEAF && data.DATA_LIST.get(CpuData.VisionMode.getDefault().getKey()) != CpuData.VisionMode.BLIND_AND_DEAF;
        }

        return canHear;
    }

    public CpuData getData(){
        return CpuData.fromNbt(((IEntityDataSaver)this).getPersistentData().getCompound("data").orElse(new NbtCompound()));
    }

    public void setData(PlayerEntity entity, ItemStack stack){
        if(stack.isOf(ItemInit.CPU)){
            setData(CPUItem.getCpuData(stack));
            canGlowCheck = !canGlowCheck;
            canRunCheck = !canRunCheck;
            entity.sendMessage(Text.literal("UPDATED_DATA"), true);
        }
    }

    public void setData(CpuData data){
        ((IEntityDataSaver)this).getPersistentData().put("data", data.toNbt());
    }

//
//    @Override
//    public double getTick(Object entity) {
//        if(isMenu){
//            return MinecraftClient.getInstance().world.getTime();
//        }
//
//        return age;
//    }

    public static DefaultAttributeContainer.Builder setAttributes() {

        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 50f)
                .add(EntityAttributes.ATTACK_DAMAGE, 9999f)
                .add(EntityAttributes.JUMP_STRENGTH, 0)
                .add(EntityAttributes.ATTACK_SPEED, 1f)
                .add(EntityAttributes.ATTACK_KNOCKBACK, 0f)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 9999f)
                .add(EntityAttributes.FOLLOW_RANGE, 9999D);
    }


    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new AnimMeleeAttackGoal(this, false));
        this.goalSelector.add(2, new AnimWanderAroundFarGoal(this));
        this.targetSelector.add(1, new AnimTargetGoal(this, PlayerEntity.class, true, true));
        this.targetSelector.add(2, new AnimTargetGoal(this, VillagerEntity.class, true, true));
        this.targetSelector.add(3, new RevengeGoal(this));
    }

    public AnimatronicPose getAnimatronicPose() {
        if(forceCrawl) return AnimatronicPose.CRAWLING;
        if (((IEntityDataSaver)this).getPersistentData().contains("pose")) {
            return AnimatronicPose.values()[((IEntityDataSaver)this).getPersistentData().getInt("pose", 0)];
        } else {
            return AnimatronicPose.NONE;
        }
    }

    public void setAnimatronicPose(AnimatronicPose animatronicPose) {
        ((IEntityDataSaver)this).getPersistentData().putInt("pose", animatronicPose.ordinal());
        this.calculateDimensions();
    }

    public void setChara(String chara, @Nullable String alt, @Nullable String eyes){
        AnimatronicDataHandler.Chara chara2 = AnimatronicDataHandler.CHARACTERS.get(chara);
        String alt2 = alt == null || alt.isEmpty() ? chara2.DEFAULT_ALT : alt;
        AnimatronicDataHandler.Alt alt3 = chara2.ALTS.get(alt2);
        String eyes2 = eyes == null || alt.isEmpty() ? alt3.default_eyes() : eyes;
        ((IEntityDataSaver)this).getPersistentData().putString("chara", chara);
        ((IEntityDataSaver)this).getPersistentData().putString("suit", alt2);
        ((IEntityDataSaver)this).getPersistentData().putString("eyes", eyes2);


        getAnimatableInstanceCache().getManagerForId(getId()).getAnimationControllers().forEach((name, controller) -> {
            controller.reset();
        });
    }
    public String getChara(){
        return ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
    }
    public String getAlt(){
        return ((IEntityDataSaver)this).getPersistentData().getString("suit").orElse("");
    }
    public String getEyes(){
        return ((IEntityDataSaver)this).getPersistentData().getString("eyes").orElse("");
    }

    public Identifier getTexture(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("chara")) {
            String chara = ((IEntityDataSaver) this).getPersistentData().getString("chara").orElse("");
            if (!chara.isEmpty()) {
                String alt = ((IEntityDataSaver) this).getPersistentData().getString("suit").orElse("");
                if (!alt.isEmpty()) {
                    return AnimatronicDataHandler.getAltTexture(chara, alt);
                }
            }
        }

        return AnimatronicDataHandler.getDefaultAltTexture();
    }
    public boolean isEmptyEye(){

        if(((IEntityDataSaver)this).getPersistentData().contains("chara")){
            String chara = ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
            if(!chara.isEmpty()){
                String alt = ((IEntityDataSaver)this).getPersistentData().getString("suit").orElse("");
                if(!alt.isEmpty()) {
                    String eyes = ((IEntityDataSaver)this).getPersistentData().getString("eyes").orElse("");
                    if(!eyes.isEmpty()) {
                        return eyes.equals("none");
                    }
                }
            }
        }

        return false;
    }
    public Identifier getEyeTexture(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("chara")){
            String chara = ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
            if(!chara.isEmpty()){
                String alt = ((IEntityDataSaver)this).getPersistentData().getString("suit").orElse("");
                if(!alt.isEmpty()) {
                    String eyes = ((IEntityDataSaver)this).getPersistentData().getString("eyes").orElse("");
                    if(!eyes.isEmpty()) {
                    return AnimatronicDataHandler.getEyeTexture(chara, alt, eyes);
                    }
                }
            }
        }

        return AnimatronicDataHandler.getDefaultEyeTexture();
    }
    public Identifier getEyeMapTexture(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("chara")){
            String chara = ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
            if(!chara.isEmpty()){
                String alt = ((IEntityDataSaver)this).getPersistentData().getString("suit").orElse("");
                if(!alt.isEmpty()) {
                    String eyes = ((IEntityDataSaver)this).getPersistentData().getString("eyes").orElse("");
                    if(!eyes.isEmpty()) {
                        return AnimatronicDataHandler.getEyeMapTexture(chara, alt, eyes);
                    }
                }
            }
        }

        return AnimatronicDataHandler.getDefaultEyeMapTexture();
    }

    public Identifier getReRenderTexture(World world){
        return getTexture(world);
    }
    public Identifier getModel(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("chara")){
            String chara = ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
            if(!chara.isEmpty()){
                String alt = ((IEntityDataSaver)this).getPersistentData().getString("suit").orElse("");
                if(!alt.isEmpty()) {
                    return AnimatronicDataHandler.getModel(chara, alt);
                }
            }
        }

        return AnimatronicDataHandler.getDefaultModel();
    }

    public Identifier getReRenderModel(World world){
        return getModel(world);
    }

    public String getAnimPrefix(){
        if(((IEntityDataSaver)this).getPersistentData().contains("chara")) {
            String chara = ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
            if(!chara.isEmpty()) {
                String alt = ((IEntityDataSaver) this).getPersistentData().getString("suit").orElse("");
                AnimatronicDataHandler.Chara chara1 = AnimatronicDataHandler.CHARACTERS.get(chara);
                if (chara1 != null) {
                    AnimatronicDataHandler.Alt alt1 = chara1.ALTS.get(alt);

                    return Objects.equals(getData().Animation, "default") ? alt1.preview_anim() : getData().Animation;
                }
            }
        }
        return "default";
    }

    public String getAnimationsName(){
        if(((IEntityDataSaver)this).getPersistentData().contains("chara")){
            String chara = ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
            if(!chara.isEmpty()) {
                String alt = ((IEntityDataSaver) this).getPersistentData().getString("suit").orElse("");

                AnimatronicDataHandler.Chara chara1 = AnimatronicDataHandler.CHARACTERS.get(chara);
                if(chara1 != null) {
                    AnimatronicDataHandler.Alt alt1 = chara1.ALTS.get(alt);

                    String animString = Objects.equals(getData().Animation, "default") ? alt1.preview_anim() : getData().Animation;

                    String anim = AnimatronicDataHandler.ALL_ANIMATIONS.get(animString);

                    if (isMenu || currentAnim.isEmpty()) currentAnim = "menu_preview";
                    String animations = AnimatronicDataHandler.getAnimationFilePath(currentAnim, animString);
                    if (!animations.isEmpty()) {
                        return animations;
                    }
                }
            }
        }
        return "";
    }
    public Identifier getAnimations(){

        String animation = getAnimationsName();
        if(!animation.isEmpty()){
            return Identifier.of(FnafUniverseRebuilt.MOD_ID, animation);
        }

        return Identifier.of(FnafUniverseRebuilt.MOD_ID, AnimatronicDataHandler.getAnimationFilePath("default"));
    }

    public String getAnimatronicAmbientSoundName(){
        String ambientSound = getData().AmbientSound;

        if(AnimatronicDataHandler.ALL_SOUNDS.containsKey(ambientSound)){
            int randomSoundIndex = Random.create().nextBetweenExclusive(0, AnimatronicDataHandler.ALL_SOUNDS.get(ambientSound).size());
            return AnimatronicDataHandler.ALL_SOUNDS.get(ambientSound).get(randomSoundIndex);
        }
        return "";
    }

    public SoundEvent getAnimatronicAmbientSound(String ambientSound){

        if(!ambientSound.isEmpty() && !ambientSound.equals("default") && !ambientSound.equals("none")){
            return Registries.SOUND_EVENT.get(Identifier.of(FnafUniverseRebuilt.MOD_ID, ambientSound));
        }

        return SoundEvents.INTENTIONALLY_EMPTY;
    }

    public void playVoiceSound(String name, SoundEvent sound, float volume){
        if(name.isEmpty()) return;
        if(getEntityWorld().isClient()) {
            newVoiceSound = true;
            if(currentVoiceSound != null){
                if(MinecraftClient.getInstance().getSoundManager().isPlaying(currentVoiceSound)){
                    MinecraftClient.getInstance().getSoundManager().stop(currentVoiceSound);
                }
            }
            currentVoiceSound = new EntityVoiceSoundInstance( this, name, sound, volume, 1.0f);
            MinecraftClient.getInstance().getSoundManager().play(currentVoiceSound);
        }
        else{
            for(ServerPlayerEntity p : PlayerLookup.world((ServerWorld) getEntityWorld())){
                ServerPlayNetworking.send(p, new PlayVoiceSoundS2CPayload(getId(), name, sound, volume));
            }
        }
    }

    public RenderLayer getRenderType(Identifier texture){
        return RenderLayers.entityTranslucent(texture);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        view.put("listener", Vibrations.ListenerData.CODEC, this.vibrationListenerData);
        super.writeCustomData(view);
    }

    @Override
    protected void readCustomData(ReadView view) {
        this.vibrationListenerData = view.read("listener", ListenerData.CODEC).orElseGet(ListenerData::new);
        super.readCustomData(view);
    }

    @Override
    public ListenerData getVibrationListenerData() {
        return vibrationListenerData;
    }

    @Override
    public Callback getVibrationCallback() {
        return vibrationCallback;
    }

    @Override
    public EntityDimensions getBaseDimensions(EntityPose pose) {
        return this.getAnimatronicPose().getPoseDimensions();
    }

    @Override
    public float getPathfindingHeightOverride() {
        if (!canCrawl()) return AnimatronicPose.NONE.poseDimensions.height();
        return isMenu ? AnimatronicPose.NONE.poseDimensions.height() : AnimatronicPose.CRAWLING.poseDimensions.height();
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        float favor =  super.getPathfindingFavor(pos, world);

        int radius = 2;

        BlockPos.Mutable checkPos = new BlockPos.Mutable();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                checkPos.set(pos.getX() + dx, pos.getY(), pos.getZ() + dz);

                if (PropInit.AVOIDED_PROPS.contains(world.getBlockState(checkPos).getBlock())) {
                    double dist = Math.sqrt(dx * dx + dz * dz);

                    favor -= (float) (radius - dist) * 6.0F;
                }
            }
        }
        //System.out.println("pos: " + pos.toShortString() + " " + favor);
        return favor;
    }

    @Override
    public float getPathfindingWidthOverride() {
        return getAnimatronicPose().getPoseDimensions().width();
    }

    @Override
    public @Nullable EntityVoiceSoundInstance getVoiceSound() {
        return currentVoiceSound;
    }

    @Override
    public void setVoiceSound(EntityVoiceSoundInstance sound) {
        currentVoiceSound = sound;
    }

    class VibrationCallback implements Vibrations.Callback {
        private static final int RANGE = 16;
        private final PositionSource positionSource = new EntityPositionSource(AnimatronicEntity.this, AnimatronicEntity.this.getStandingEyeHeight());

        @Override
        public int getRange() {
            return 64;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public boolean accepts(ServerWorld world, BlockPos pos, RegistryEntry<GameEvent> event, GameEvent.Emitter emitter) {

            if(AnimatronicEntity.this.getTarget() != null && AnimatronicEntity.this.canSee()) return false;
            if(!AnimatronicEntity.this.isAggressive()) return false;
            if(!AnimatronicEntity.this.canHear()) return false;

            if (emitter.sourceEntity() instanceof PlayerEntity p) {

                //if(p.isSpectator() || p.isCreative()) return false;

                if (event.matches(GameEvent.STEP)) {
                    if (AnimatronicEntity.this.canSee() && !p.isSprinting()) {
                        return false;
                    }
                }
            }

            if (AnimatronicEntity.this.isAiDisabled()) {
                return false;
            } else {
                return pos.isWithinDistance(AnimatronicEntity.this.getBlockPos(), getRange());
            }

        }

        @Override
        public void accept(ServerWorld world, BlockPos pos, RegistryEntry<GameEvent> event, @org.jspecify.annotations.Nullable Entity sourceEntity, @org.jspecify.annotations.Nullable Entity entity, float distance) {

            List<RegistryEntry.Reference<GameEvent>> references = List.of(
                    GameEvent.STEP,
                    GameEvent.PROJECTILE_LAND,
                    GameEvent.HIT_GROUND,
                    GameEvent.ITEM_INTERACT_FINISH,
                    GameEvent.PROJECTILE_SHOOT,
                    GameEvent.INSTRUMENT_PLAY,
                    GameEvent.ENTITY_ACTION,
                    GameEvent.ENTITY_INTERACT,
                    GameEvent.CONTAINER_CLOSE,
                    GameEvent.BLOCK_CLOSE,
                    GameEvent.CONTAINER_OPEN,
                    GameEvent.BLOCK_OPEN,
                    GameEvent.BLOCK_DESTROY,
                    GameEvent.BLOCK_PLACE,
                    GameEvent.ENTITY_PLACE,
                    GameEvent.NOTE_BLOCK_PLAY
            );

            if (sourceEntity instanceof PlayerEntity p) {
                boolean alreadyHeard = lastHeardPosition != null;
                lastHeardPosition = pos;
                timeSinceLastHeard = 30;
                if(alreadyHeard){
                    startMovingToHeardPosition();
                    timeSinceLastHeard = 0;
                }
            }
        }

        @Override
        public TagKey<GameEvent> getTag() {
            return GameEventTags.VIBRATIONS;
        }

        @Override
        public void onListen() {

        }
    }

    public interface VibrationTicker {
        static void tick(World world, Vibrations.ListenerData listenerData, Vibrations.Callback callback) {
            if (world instanceof ServerWorld serverWorld) {
                if (listenerData.getVibration() == null) {
                    tryListen(serverWorld, listenerData, callback);
                }

                if (listenerData.getVibration() != null) {
                    boolean bl = listenerData.getDelay() > 0;
                    //spawnVibrationParticle(serverWorld, listenerData, callback);
                    listenerData.tickDelay();
                    if (listenerData.getDelay() <= 0) {
                        bl = accept(serverWorld, listenerData, callback, listenerData.getVibration());
                    }

                    if (bl) {
                        callback.onListen();
                    }
                }
            }
        }

        private static void tryListen(ServerWorld world, Vibrations.ListenerData listenerData, Vibrations.Callback callback) {
            listenerData.getSelector().getVibrationToTick(world.getTime()).ifPresent(vibration -> {
                listenerData.setVibration(vibration);
                Vec3d vec3d = vibration.pos();
                listenerData.setDelay(callback.getDelay(vibration.distance()));
                //world.spawnParticles(new VibrationParticleEffect(callback.getPositionSource(), listenerData.getDelay()), vec3d.x, vec3d.y, vec3d.z, 1, 0.0, 0.0, 0.0, 0.0);
                callback.onListen();
                listenerData.getSelector().clear();
            });
        }

        private static void spawnVibrationParticle(ServerWorld world, Vibrations.ListenerData listenerData, Vibrations.Callback callback) {
            if (listenerData.shouldSpawnParticle()) {
                if (listenerData.getVibration() == null) {
                    listenerData.setSpawnParticle(false);
                } else {
                    Vec3d vec3d = listenerData.getVibration().pos();
                    PositionSource positionSource = callback.getPositionSource();
                    Vec3d vec3d2 = (Vec3d)positionSource.getPos(world).orElse(vec3d);
                    int i = listenerData.getDelay();
                    int j = callback.getDelay(listenerData.getVibration().distance());
                    double d = 1.0 - (double)i / j;
                    double e = MathHelper.lerp(d, vec3d.x, vec3d2.x);
                    double f = MathHelper.lerp(d, vec3d.y, vec3d2.y);
                    double g = MathHelper.lerp(d, vec3d.z, vec3d2.z);
                    boolean bl = false;
                    if (bl) {
                        listenerData.setSpawnParticle(false);
                    }
                }
            }
        }

        private static boolean accept(ServerWorld world, Vibrations.ListenerData listenerData, Vibrations.Callback callback, Vibration vibration) {
            BlockPos blockPos = BlockPos.ofFloored(vibration.pos());
            BlockPos blockPos2 = (BlockPos)callback.getPositionSource().getPos(world).map(BlockPos::ofFloored).orElse(blockPos);
            if (callback.requiresTickingChunksAround() && !areChunksTickingAround(world, blockPos2)) {
                return false;
            } else {
                callback.accept(
                        world,
                        blockPos,
                        vibration.gameEvent(),
                        (Entity)vibration.getEntity(world).orElse(null),
                        (Entity)vibration.getOwner(world).orElse(null),
                        Vibrations.VibrationListener.getTravelDelay(blockPos, blockPos2)
                );
                listenerData.setVibration(null);
                return true;
            }
        }

        private static boolean areChunksTickingAround(World world, BlockPos pos) {
            ChunkPos chunkPos = new ChunkPos(pos);

            for (int i = chunkPos.x - 1; i <= chunkPos.x + 1; i++) {
                for (int j = chunkPos.z - 1; j <= chunkPos.z + 1; j++) {
                    if (!world.shouldTickBlocksInChunk(ChunkPos.toLong(i, j)) || world.getChunkManager().getWorldChunk(i, j) == null) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public class AnimatronicNavigation extends MobNavigation {

        public AnimatronicNavigation(MobEntity mobEntity, World world) {
            super(mobEntity, world);
        }

        @Override
        protected PathNodeNavigator createPathNodeNavigator(int range) {
            this.nodeMaker = new AnimatronicPathNodeMaker();
            return new PathNodeNavigator(this.nodeMaker, range);
        }

        double getSpeed(){
            return this.speed;
        }

        @Override
        protected void checkTimeouts(Vec3d currentPos) {

            if(this.entity instanceof AnimatronicEntity ent){
                if(ent.isFrozen) return;
            }
            super.checkTimeouts(currentPos);
        }

    }

    public enum AnimatronicPose {
        // BASE 0.8f width, 2.25f height
        NONE("idle",               "loweridle", "walk_upper", "walk_lower", "run_upper", "walk_lower", true, EntityDimensions.fixed(0.8f, 2.25f).withEyeHeight(1.8f)),
        REACHING("idle",               "loweridle", "walk_upper", "walk_lower", "run_upper", "walk_lower", true, EntityDimensions.fixed(0.8f, 2.25f).withEyeHeight(1.8f)),
        PLAYER("player_idle",      "loweridle", "walk_upper", "walk_lower", "run_upper", "walk_lower", true, EntityDimensions.fixed(0.8f, 2.25f).withEyeHeight(1.8f)),
        STAGE("stage_idle",        "loweridle", "walk_upper", "walk_lower", "run_upper", "walk_lower", false, EntityDimensions.fixed(0.8f, 2.25f).withEyeHeight(1.8f)),
        DRAG("drag_idle",          "loweridle", "drag_move",  "walk_lower", "drag_move", "walk_lower", true, EntityDimensions.fixed(0.8f, 2.25f).withEyeHeight(1.8f)),
        AGGRESSIVE("haunted_idle", "loweridle", "walk_upper_night", "walk_lower", "run_upper", "walk_lower", true, EntityDimensions.fixed(0.8f, 2.25f).withEyeHeight(1.8f)),
        CRAWLING("crawl_idle",     "",          "crawl",      "",           "crawl",     "",           true, EntityDimensions.fixed(0.8f, 0.8f).withEyeHeight(0.6f)),
        CROUCHING("idle",               "loweridle", "walk_upper", "walk_lower", "run_upper", "walk_lower", true, EntityDimensions.fixed(0.8f, 1.8f).withEyeHeight(1.5f));

        private final String upperIdle;
        private final String lowerIdle;
        private final String upperWalk;
        private final String lowerWalk;
        private final String upperRun;
        private final String lowerRun;
        private final boolean canBlink;
        private final EntityDimensions poseDimensions;

        AnimatronicPose(String upperIdle, String lowerIdle, String upperWalk, String lowerWalk, String upperRun, String lowerRun, boolean canBlink, EntityDimensions poseDimensions) {
            this.upperIdle = upperIdle;
            this.lowerIdle = lowerIdle;
            this.upperWalk = upperWalk;
            this.lowerWalk = lowerWalk;
            this.upperRun = upperRun;
            this.lowerRun = lowerRun;
            this.canBlink = canBlink;
            this.poseDimensions = poseDimensions;
        }

        public String getUpperIdle(){
            return upperIdle;
        }

        public String getUpperWalk(){
            return upperWalk;
        }

        public String getUpperRun(){
            return upperRun;
        }

        public String getLowerIdle(){
            return lowerIdle;
        }

        public String getLowerWalk(){
            return lowerWalk;
        }

        public String getLowerRun(){
            return lowerRun;
        }

        public boolean canBlink(){
            return canBlink;
        }

        public EntityDimensions getPoseDimensions() {
            return poseDimensions;
        }
    }
}

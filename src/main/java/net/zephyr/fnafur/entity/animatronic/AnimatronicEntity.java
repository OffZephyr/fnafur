package net.zephyr.fnafur.entity.animatronic;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Relative;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Holder;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem.Data;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem.Listener;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem.User;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationInfo;
import net.minecraft.world.phys.Vec3;
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

public class AnimatronicEntity extends PathfinderMob implements GeoEntity, VibrationSystem, IEntityPathfindingHeightOverride, VoiceSource {

    boolean forceCrawl = false;

    private final DynamicGameEventListener<Listener> gameEventHandler;
    private VibrationSystem.Data vibrationListenerData;
    private final VibrationSystem.User vibrationCallback;
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
    public AnimatronicEntity(EntityType<? extends PathfinderMob> entityType, Level world){
        super(entityType, world);

        this.navigation = new AnimatronicNavigation(this, world);
        this.vibrationCallback = new AnimatronicEntity.VibrationCallback();
        this.vibrationListenerData = new VibrationSystem.Data();
        this.gameEventHandler = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));

        this.setPathfindingMalus(PathType.DANGER_FIRE, 16);
        this.setPathfindingMalus(PathType.STICKY_HONEY, 24);
        this.setPathfindingMalus(PathType.COCOA, 8);

        //this.reachInHitbox = new AnimatronicPart(this, "reach_in_hitbox", 1.0F, 1.0F);
    }



    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> callback) {
        if (this.level() instanceof ServerLevel serverWorld) {
            callback.accept(this.gameEventHandler, serverWorld);
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }


    @Override
    public InteractionResult interactAt(Player player, Vec3 hitPos, InteractionHand hand) {
        //if(!player.getMainHandStack().isEmpty()) return InteractionResult.PASS;

        if(level().isClientSide()){
            getAnimatableInstanceCache().getManagerForId(getId()).getAnimationControllers().forEach((name, controller) -> {
                controller.reset();
            });
            if(player.getMainHandItem().isEmpty() && !player.isShiftKeyDown()) {
                String name = this.getAnimatronicAmbientSoundName();
                playVoiceSound(name, this.getAnimatronicAmbientSound(name), this.ambientSoundVolume());
                //playVoiceSound(name, SoundEvents.INTENTIONALLY_EMPTY, this.ambientSoundVolume());
                return InteractionResult.SUCCESS;
            }
        }
        else{
            if (player.getMainHandItem().isEmpty() && player.isShiftKeyDown()) {
                if (getData().DATA_LIST.containsKey(CpuData.OnReset.getDefault().getKey())) {
                    if(getData().DATA_LIST.get(CpuData.OnReset.getDefault().getKey()) instanceof CpuData.OnReset resetMode){
                        resetAnimatronic(resetMode);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return super.interactAt(player, hitPos, hand);
    }

    public void resetAnimatronic(CpuData.OnReset mode){
        Vec3 vec3d = getSpawnPos();

        isRetreating = false;
        switch (mode){
            case TELEPORT -> {
                Set<Relative> flags = EnumSet.of(Relative.X, Relative.Y, Relative.Z, Relative.X_ROT, Relative.Y_ROT);
                this.getNavigation().stop();
                this.setPos(vec3d);
                this.xo = vec3d.x;
                this.yo = vec3d.y;
                this.zo = vec3d.z;
                this.setYRot(this.getSpawnYaw());
                this.setYHeadRot(this.getSpawnYaw());
                this.setYBodyRot(this.getSpawnYaw());
                this.setXRot(0);
                this.setDeltaMovement(0, 0, 0);
                this.yBodyRotO = this.getSpawnYaw();
                this.yHeadRotO = this.getSpawnYaw();
                this.yRotO = this.getSpawnYaw();
                this.yRotO = this.getSpawnYaw();
                setFrozen(false);
            }
            case WALK -> {
                isRetreating = true;
                int speed = walkingSpeed();
                int maxSpeed = CpuData.MovementSpeed.getDefaultValue() + CpuData.RunSpeed.getDefaultValue();
                float final_speed = Mth.lerp(((float) speed / maxSpeed), 0f, 2.5f) / 1.5f;
                this.getNavigation().moveTo(vec3d.x, vec3d.y, vec3d.z, 0, final_speed);
            }
            case RUN -> {
                isRetreating = true;
                int speed = runningSpeed();
                int maxSpeed = CpuData.MovementSpeed.getDefaultValue() + CpuData.RunSpeed.getDefaultValue();
                float final_speed = Mth.lerp(((float) speed / maxSpeed), 0f, 2.5f) / 1.5f;
                this.getNavigation().moveTo(vec3d.x, vec3d.y, vec3d.z, 0, final_speed);
            }
        }

        ((IEntityDataSaver)this).getPersistentData().putBoolean("isRetreating", isRetreating);
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        if(level().isClientSide()){
            double spawnX = packet.getX();
            double spawnY = packet.getY();
            double spawnZ = packet.getZ() - 1.0D;
            float spawnYaw = packet.getYRot();
            ClientPlayNetworking.send(new SetEntitySpawnDataC2SPayload(getId(), spawnX, spawnY, spawnZ, spawnYaw));
        }
    }

    public BlockPos getSpawnBlockPos() {
        double x = getSpawnPos().x;
        double y = getSpawnPos().y;
        double z = getSpawnPos().z;
        return new BlockPos((int) x, (int) y, (int) z);
    }

    public Vec3 getSpawnPos() {
        double x = ((IEntityDataSaver) this).getPersistentData().getDouble("spawnX").orElse(0.0D);
        double y = ((IEntityDataSaver) this).getPersistentData().getDouble("spawnY").orElse(0.0D);
        double z = ((IEntityDataSaver) this).getPersistentData().getDouble("spawnZ").orElse(0.0D) + 1;
        return new Vec3(x, y, z);
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
                RandomSource random1 = RandomSource.create();
                blinkDelay = random1.nextIntBetweenInclusive(100, 250);
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

        double speed = getKnownMovement().horizontalDistance() * 20;
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

        double speed = getKnownMovement().horizontalDistance() * 20;
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

    public void playWalkSound(Level world){
        if(!world.isClientSide()) {
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
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public float maxUpStep() {
        return 0.9f;
    }

    @Override
    public void tick() {
        if(!(isFrozen)){
            blinkDelay = Math.max(0, blinkDelay - 1);
        }
        if(level().isClientSide() && currentVoiceSound != null && !currentVoiceSound.getIdentifier().equals(SoundManager.INTENTIONALLY_EMPTY_SOUND_LOCATION) && !Minecraft.getInstance().getSoundManager().isActive(currentVoiceSound)){
            currentVoiceSound = null;
        };
        if(isMenu){
            tickCount++;
        }

        if(!level().isClientSide()) {
            ((IEntityDataSaver) this).getPersistentData().putString("getupAnim", "");
        }
        else{
            if (!((IEntityDataSaver) this).getPersistentData().contains("synced")) {
                ClientPlayNetworking.send(new UpdateEntityNbtC2SGetFromServerPayload(getId()));
            }

            //if (((IEntityDataSaver) this).getServerUpdateStatus()) {
            //    //Minecraft.getInstance().player.sendMessage(Text.literal("SYNCING PROP"), false);
            //    ClientPlayNetworking.send(new UpdateBlockNbtC2SPayload(getPos().asLong(), ((IEntityDataSaver) this).getPersistentData()));
            //}
        }

        if(!level().isClientSide()) {
            if (getTarget() == null && canSee()) {
                if (lastHeardPosition != null) {
                    if (timeSinceLastHeard > 0 || getNavigation().isInProgress()) {
                        if (timeSinceLastHeard > 0) {
                            timeSinceLastHeard -= 1;
                            getNavigation().stop();
                            if (timeSinceLastHeard == 0) {
                                startMovingToHeardPosition();
                            }
                        }
                        getLookControl().setLookAt(lastHeardPosition.getX(), lastHeardPosition.above().getY(), lastHeardPosition.getZ(), 30.0F, 30.0F);

                    } else {
                        lastHeardPosition = null;
                    }
                }
            } else if (getTarget() != null) {
                lastHeardPosition = null;
            }
        }

        if(!level().isClientSide()) {
            VibrationTicker.tick(this.level(), this.vibrationListenerData, this.vibrationCallback);


            this.tickSightBehavior();

            if (isRetreating) {
                if (this.getNavigation().getPath() == null || this.getNavigation().isDone()) {
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
                                position().closerThan(getSpawnPos(), 1f, 1)
                ) {
                    if (position().equals(getSpawnPos()) && getYRot() == getSpawnYaw() && getYHeadRot() == getSpawnYaw() && getVisualRotationYInDegrees() == getSpawnYaw()) {

                        isRetreating = false;
                        ((IEntityDataSaver) this).getPersistentData().putBoolean("isRetreating", isRetreating);
                    }
                    resetAnimatronic(CpuData.OnReset.TELEPORT);
                }
            } else {
                timeSinceLastMoved = 0;
            }



            if(getWanderBehavior() == CpuData.WanderBehavior.STAND_AT_SPAWN && !blockPosition().equals(getSpawnBlockPos()) && getYRot() != getSpawnYaw() && !isRetreating && !(isAggressive() && getTarget() != null) && lastHeardPosition == null){
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
        if(level().isClientSide()) return;
        switch (getBehaviorWhenSeen()){
            case FREEZE_ON_SIGHT -> {
                if(isBeingWatched() && !isRetreating){
                    freezeMovement();
                }
                else{
                    if(isFrozen){
                        setFrozen(false);
                        getNavigation().setSpeedModifier(frozenSpeed);
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

        if(isFrozen && !blockPosition().equals(getSpawnBlockPos())){
            getNavigation().stop();
            setXxa(0.0F);
            setYya(0.0F);
            setSpeed(0.0F);
            setDeltaMovement(0.0, getDeltaMovement().y, 0.0);

            if(getTarget() != null){
                getLookControl().setLookAt(getTarget(), 45.0F, 30.0F);
            }
        }
    }

    void setFrozen(boolean frozen){
        isFrozen = frozen;
        for(ServerPlayer player : PlayerLookup.tracking(this)){
            ServerPlayNetworking.send(player, new SetAnimatronicFrozenStatusS2CPayload(getId(), frozen, frozenHeadYaw, frozenBodyYaw, frozenPitch));
        }

    }

    void freezeMovement(){
        if(!isFrozen){
            frozenSpeed = ((AnimatronicNavigation)getNavigation()).getSpeed();
            frozenYaw = getYRot();
            frozenHeadYaw = getYHeadRot();
            frozenBodyYaw = getVisualRotationYInDegrees();
            frozenPitch = getXRot();
            setFrozen(true);
        }
        getNavigation().setSpeedModifier(0);
    }

    boolean isBeingWatched(){
        if(level().isClientSide()) return false;

        List<Player> players = level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(10), (entity) -> (entity instanceof Player && !entity.isSpectator() && !entity.isCreative()));

        for(Player player : players){
            Vec3 difference = this.position().add(player.position().scale(-1));
            float angle = player.getYHeadRot() - difference.rotation().y;
            while(angle < 0) angle += 360;
            angle %= 360;
            if(angle <= sightConeAngle() || angle >= 360 - sightConeAngle()){
                if(hasLineOfSight(player)){
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
        Level world = this.level();
        if (world == null) return;

        AABB box = AnimatronicPose.NONE.getPoseDimensions().makeBoundingBox(this.position());
        AABB box2 = AnimatronicPose.CRAWLING.getPoseDimensions().makeBoundingBox(this.position());

        boolean wouldSuffocate = !this.level().noBlockCollision(this, box);
        boolean wouldntSuffocateCrawling = this.level().noBlockCollision(this, box2);

        if (this.canCrawl() && (this.isCrawlSpaceAvailable(world) && !this.getNavigation().isDone()) || wouldSuffocate && wouldntSuffocateCrawling) {
            this.setAnimatronicPose(AnimatronicPose.CRAWLING);
        } else {

            Vec3 pos = this.position();
            BlockPos blockPos = new BlockPos((int)Math.floor(pos.x()), (int)Math.floor(pos.y()), (int)Math.floor(pos.z())).relative(this.getDirection());
            if (this.isCrawlSpaceAvailable(world) && this.canReachIn() && this.level() instanceof ServerLevel serverWorld) {
                for (Player player : this.level().getEntitiesOfClass(Player.class, new AABB(blockPos.getCenter(), blockPos.relative(this.getDirection()).getCenter()), (entity) -> (entity instanceof Player))) {
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
    public boolean isCrawlSpaceAvailable(Level world) {

        Vec3 pos = this.position();
        BlockPos blockPos = new BlockPos((int)Math.floor(pos.x()), (int)Math.floor(pos.y()), (int)Math.floor(pos.z())).relative(this.getDirection());

        return !world.getBlockState(blockPos.above()).isAir()
                && !Block.canSupportCenter(world, blockPos, this.getDirection())
                && !(getAnimatronicPose() == AnimatronicPose.CRAWLING && wouldNotSuffocateAtTargetPose(Pose.STANDING));
    }

    void startMovingToHeardPosition(){

        ClipContext context = new ClipContext(
                getEyePosition(),
                lastHeardPosition.getCenter(),
                net.minecraft.world.level.ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this
        );
        BlockHitResult result = level().clip(context);

        if(result.getType() != HitResult.Type.MISS && !result.getBlockPos().equals(lastHeardPosition)) {
            int speed = runningSpeed();
            int maxSpeed = CpuData.MovementSpeed.getDefaultValue() + CpuData.RunSpeed.getDefaultValue();

            float finalspeed = Mth.lerp(((float) speed / maxSpeed), 0f, 2.5f) / 1.5f;

            PathNavigation nav = this.getNavigation();

            nav.stop(); // force recomputation

            Path path = nav.createPath(lastHeardPosition, 0);
            if (path != null) {
                nav.moveTo(path, finalspeed);
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
        if(!level().isClientSide()) {
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
            for(ServerPlayer p : PlayerLookup.world((ServerLevel) level())){
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
        if(!level().isClientSide()) {
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
        return CpuData.fromNbt(((IEntityDataSaver)this).getPersistentData().getCompound("data").orElse(new CompoundTag()));
    }

    public void setData(Player entity, ItemStack stack){
        if(stack.is(ItemInit.CPU)){
            setData(CPUItem.getCpuData(stack));
            canGlowCheck = !canGlowCheck;
            canRunCheck = !canRunCheck;
            entity.displayClientMessage(Component.literal("UPDATED_DATA"), true);
        }
    }

    public void setData(CpuData data){
        ((IEntityDataSaver)this).getPersistentData().put("data", data.toNbt());
    }

//
//    @Override
//    public double getTick(Object entity) {
//        if(isMenu){
//            return Minecraft.getInstance().level.getTime();
//        }
//
//        return age;
//    }

    public static AttributeSupplier.Builder setAttributes() {

        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50f)
                .add(Attributes.ATTACK_DAMAGE, 9999f)
                .add(Attributes.JUMP_STRENGTH, 0)
                .add(Attributes.ATTACK_SPEED, 1f)
                .add(Attributes.ATTACK_KNOCKBACK, 0f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 9999f)
                .add(Attributes.FOLLOW_RANGE, 9999D);
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new AnimMeleeAttackGoal(this, false));
        this.goalSelector.addGoal(2, new AnimWanderAroundFarGoal(this));
        this.targetSelector.addGoal(1, new AnimTargetGoal(this, Player.class, true, true));
        this.targetSelector.addGoal(2, new AnimTargetGoal(this, Villager.class, true, true));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    public AnimatronicPose getAnimatronicPose() {
        if(forceCrawl) return AnimatronicPose.CRAWLING;
        if (((IEntityDataSaver)this).getPersistentData().contains("pose")) {
            return AnimatronicPose.values()[((IEntityDataSaver)this).getPersistentData().getIntOr("pose", 0)];
        } else {
            return AnimatronicPose.NONE;
        }
    }

    public void setAnimatronicPose(AnimatronicPose animatronicPose) {
        ((IEntityDataSaver)this).getPersistentData().putInt("pose", animatronicPose.ordinal());
        this.refreshDimensions();
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

    public Identifier getTexture(Level world){

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
    public Identifier getEyeTexture(Level world){

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
    public Identifier getEyeMapTexture(Level world){

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

    public Identifier getReRenderTexture(Level world){
        return getTexture(world);
    }
    public Identifier getModel(Level world){

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

    public Identifier getReRenderModel(Level world){
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
            return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, animation);
        }

        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, AnimatronicDataHandler.getAnimationFilePath("default"));
    }

    public String getAnimatronicAmbientSoundName(){
        String ambientSound = getData().AmbientSound;

        if(AnimatronicDataHandler.ALL_SOUNDS.containsKey(ambientSound)){
            int randomSoundIndex = RandomSource.create().nextInt(0, AnimatronicDataHandler.ALL_SOUNDS.get(ambientSound).size());
            return AnimatronicDataHandler.ALL_SOUNDS.get(ambientSound).get(randomSoundIndex);
        }
        return "";
    }

    public SoundEvent getAnimatronicAmbientSound(String ambientSound){

        if(!ambientSound.isEmpty() && !ambientSound.equals("default") && !ambientSound.equals("none")){
            return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, ambientSound));
        }

        return SoundEvents.EMPTY;
    }

    public void playVoiceSound(String name, SoundEvent sound, float volume){
        if(name.isEmpty()) return;
        if(level().isClientSide()) {
            newVoiceSound = true;
            if(currentVoiceSound != null){
                if(Minecraft.getInstance().getSoundManager().isActive(currentVoiceSound)){
                    Minecraft.getInstance().getSoundManager().stop(currentVoiceSound);
                }
            }
            currentVoiceSound = new EntityVoiceSoundInstance( this, name, sound, volume, 1.0f);
            Minecraft.getInstance().getSoundManager().play(currentVoiceSound);
        }
        else{
            for(ServerPlayer p : PlayerLookup.world((ServerLevel) level())){
                ServerPlayNetworking.send(p, new PlayVoiceSoundS2CPayload(getId(), name, sound, volume));
            }
        }
    }

    public RenderType getRenderType(Identifier texture){
        return RenderTypes.entityTranslucent(texture);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {
        view.store("listener", VibrationSystem.Data.CODEC, this.vibrationListenerData);
        super.addAdditionalSaveData(view);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {
        this.vibrationListenerData = view.read("listener", Data.CODEC).orElseGet(Data::new);
        super.readAdditionalSaveData(view);
    }

    @Override
    public Data getVibrationData() {
        return vibrationListenerData;
    }

    @Override
    public User getVibrationUser() {
        return vibrationCallback;
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.getAnimatronicPose().getPoseDimensions();
    }

    @Override
    public float getPathfindingHeightOverride() {
        if (!canCrawl()) return AnimatronicPose.NONE.poseDimensions.height();
        return isMenu ? AnimatronicPose.NONE.poseDimensions.height() : AnimatronicPose.CRAWLING.poseDimensions.height();
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        float favor =  super.getWalkTargetValue(pos, world);

        int radius = 2;

        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();

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

    class VibrationCallback implements VibrationSystem.User {
        private static final int RANGE = 16;
        private final PositionSource positionSource = new EntityPositionSource(AnimatronicEntity.this, AnimatronicEntity.this.getEyeHeight());

        @Override
        public int getListenerRadius() {
            return 64;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public boolean canReceiveVibration(ServerLevel world, BlockPos pos, Holder<GameEvent> event, GameEvent.Context emitter) {

            if(AnimatronicEntity.this.getTarget() != null && AnimatronicEntity.this.canSee()) return false;
            if(!AnimatronicEntity.this.isAggressive()) return false;
            if(!AnimatronicEntity.this.canHear()) return false;

            if (emitter.sourceEntity() instanceof Player p) {

                //if(p.isSpectator() || p.isCreative()) return false;

                if (event.is(GameEvent.STEP)) {
                    if (AnimatronicEntity.this.canSee() && !p.isSprinting()) {
                        return false;
                    }
                }
            }

            if (AnimatronicEntity.this.isNoAi()) {
                return false;
            } else {
                return pos.closerThan(AnimatronicEntity.this.blockPosition(), getListenerRadius());
            }

        }

        @Override
        public void onReceiveVibration(ServerLevel world, BlockPos pos, Holder<GameEvent> event, @org.jspecify.annotations.Nullable Entity sourceEntity, @org.jspecify.annotations.Nullable Entity entity, float distance) {

            List<Holder.Reference<GameEvent>> references = List.of(
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

            if (sourceEntity instanceof Player p) {
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
        public TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.VIBRATIONS;
        }

        @Override
        public void onDataChanged() {

        }
    }

    public interface VibrationTicker {
        static void tick(Level world, VibrationSystem.Data listenerData, VibrationSystem.User callback) {
            if (world instanceof ServerLevel serverWorld) {
                if (listenerData.getCurrentVibration() == null) {
                    tryListen(serverWorld, listenerData, callback);
                }

                if (listenerData.getCurrentVibration() != null) {
                    boolean bl = listenerData.getTravelTimeInTicks() > 0;
                    //spawnVibrationParticle(serverWorld, listenerData, callback);
                    listenerData.decrementTravelTime();
                    if (listenerData.getTravelTimeInTicks() <= 0) {
                        bl = accept(serverWorld, listenerData, callback, listenerData.getCurrentVibration());
                    }

                    if (bl) {
                        callback.onDataChanged();
                    }
                }
            }
        }

        private static void tryListen(ServerLevel world, VibrationSystem.Data listenerData, VibrationSystem.User callback) {
            listenerData.getSelectionStrategy().chosenCandidate(world.getGameTime()).ifPresent(vibration -> {
                listenerData.setCurrentVibration(vibration);
                Vec3 vec3d = vibration.pos();
                listenerData.setTravelTimeInTicks(callback.calculateTravelTimeInTicks(vibration.distance()));
                //world.spawnParticles(new VibrationParticleEffect(callback.getPositionSource(), listenerData.getDelay()), vec3d.x, vec3d.y, vec3d.z, 1, 0.0, 0.0, 0.0, 0.0);
                callback.onDataChanged();
                listenerData.getSelectionStrategy().startOver();
            });
        }

        private static void spawnVibrationParticle(ServerLevel world, VibrationSystem.Data listenerData, VibrationSystem.User callback) {
            if (listenerData.shouldReloadVibrationParticle()) {
                if (listenerData.getCurrentVibration() == null) {
                    listenerData.setReloadVibrationParticle(false);
                } else {
                    Vec3 vec3d = listenerData.getCurrentVibration().pos();
                    PositionSource positionSource = callback.getPositionSource();
                    Vec3 vec3d2 = (Vec3)positionSource.getPosition(world).orElse(vec3d);
                    int i = listenerData.getTravelTimeInTicks();
                    int j = callback.calculateTravelTimeInTicks(listenerData.getCurrentVibration().distance());
                    double d = 1.0 - (double)i / j;
                    double e = Mth.lerp(d, vec3d.x, vec3d2.x);
                    double f = Mth.lerp(d, vec3d.y, vec3d2.y);
                    double g = Mth.lerp(d, vec3d.z, vec3d2.z);
                    boolean bl = false;
                    if (bl) {
                        listenerData.setReloadVibrationParticle(false);
                    }
                }
            }
        }

        private static boolean accept(ServerLevel world, VibrationSystem.Data listenerData, VibrationSystem.User callback, VibrationInfo vibration) {
            BlockPos blockPos = BlockPos.containing(vibration.pos());
            BlockPos blockPos2 = (BlockPos)callback.getPositionSource().getPosition(world).map(BlockPos::containing).orElse(blockPos);
            if (callback.requiresAdjacentChunksToBeTicking() && !areChunksTickingAround(world, blockPos2)) {
                return false;
            } else {
                callback.onReceiveVibration(
                        world,
                        blockPos,
                        vibration.gameEvent(),
                        (Entity)vibration.getEntity(world).orElse(null),
                        (Entity)vibration.getProjectileOwner(world).orElse(null),
                        VibrationSystem.Listener.distanceBetweenInBlocks(blockPos, blockPos2)
                );
                listenerData.setCurrentVibration(null);
                return true;
            }
        }

        private static boolean areChunksTickingAround(Level world, BlockPos pos) {
            ChunkPos chunkPos = new ChunkPos(pos);

            for (int i = chunkPos.x - 1; i <= chunkPos.x + 1; i++) {
                for (int j = chunkPos.z - 1; j <= chunkPos.z + 1; j++) {
                    if (!world.shouldTickBlocksAt(ChunkPos.asLong(i, j)) || world.getChunkSource().getChunkNow(i, j) == null) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public class AnimatronicNavigation extends GroundPathNavigation {

        public AnimatronicNavigation(Mob mobEntity, Level world) {
            super(mobEntity, world);
        }

        @Override
        protected PathFinder createPathFinder(int range) {
            this.nodeEvaluator = new AnimatronicPathNodeMaker();
            return new PathFinder(this.nodeEvaluator, range);
        }

        double getSpeed(){
            return this.speedModifier;
        }

        @Override
        protected void doStuckDetection(Vec3 currentPos) {

            if(this.mob instanceof AnimatronicEntity ent){
                if(ent.isFrozen) return;
            }
            super.doStuckDetection(currentPos);
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

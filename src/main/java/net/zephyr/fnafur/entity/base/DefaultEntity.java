package net.zephyr.fnafur.entity.base;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.computer.ComputerData;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.init.ScreensInit;
import net.zephyr.fnafur.item.EntitySpawnItem;
import net.zephyr.fnafur.networking.nbt_updates.goopy_entity.AIBehaviorUpdateS2CPayload;
import net.zephyr.fnafur.networking.nbt_updates.goopy_entity.UpdateEntityNbtS2CPongPayload;
import net.zephyr.fnafur.networking.nbt_updates.goopy_entity.UpdateJumpscareDataS2CPayload;
import net.zephyr.fnafur.util.Computer.ComputerAI;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IPlayerCustomModel;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class DefaultEntity extends PathAwareEntity implements GeoEntity {
    public DefaultEntityModel<? extends DefaultEntity> model = null;
    public boolean mimic;
    public PlayerEntity mimicPlayer;
    public boolean mimicAggressive = false;
    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public long[] JumpscareCamPos = new long[6];
    public boolean isGUI = false;
    public String killScreenID;
    boolean running = false;
    int freezeTime = 0;
    int runningCooldown = 0;
    public boolean crawling = false;
    int crawlingCooldown = 0;
    boolean deactivated = false;
    public boolean menuTick = false;
    public int serverBehaviorIndex = -1;
    private String behavior;

    public DefaultEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {

        ((IEntityDataSaver)this).getPersistentData().putLong("spawnPos", getBlockPos().asLong());
        ((IEntityDataSaver)this).getPersistentData().putFloat("spawnRot", getHeadYaw());
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Main", 3, this::movementAnimController));
        controllers.add(new AnimationController<>(this, "Attack", 1, this::attackAnimController));
    }

    private <E extends DefaultEntity> PlayState attackAnimController(AnimationState<E> event) {
        if (getWorld().isClient()) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (hasJumpScare() &&
                    player != null &&
                    player.isDead() &&
                    player.getRecentDamageSource() != null &&
                    player.getRecentDamageSource().getAttacker() instanceof DefaultEntity &&
                    ((IEntityDataSaver)player).getPersistentData().getInt("JumpscareID") == this.getId()
            ) {
                event.getController().setAnimationSpeed(1.5);
                this.freezeTime = JumpScareLength();
                return event.setAndContinue(RawAnimation.begin().thenPlay(JumpScareAnim()));
            }
        }

        if(this.handSwinging || (this.mimic && this.mimicPlayer.handSwinging)) {
            event.getController().forceAnimationReset();
            this.handSwinging = false;

            if (event.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
                    this.freezeTime = attackLength();
                    return event.setAndContinue(RawAnimation.begin().thenPlay(attackAnim()));
            }
        }
        return PlayState.CONTINUE;
    }

    public <E extends DefaultEntity> PlayState movementAnimController(final AnimationState<E> event) {
        if(menuTick) {
            return event.setAndContinue(RawAnimation.begin().thenLoop(demoAnim()));
        }
        if(!mimic){
            if (event.getAnimatable().isDead()) {
                event.getController().transitionLength(0);
                return event.setAndContinue(RawAnimation.begin().thenPlayAndHold(dyingAnim()));
            } else {

                event.setControllerSpeed(1);
                event.getController().transitionLength(3);
                String idle = defaultIdleAnim();
                String walking = defaultWalkingAnim();
                String running = defaultRunningAnim();
                String deactivating = deactivatingAnim();
                String deactivated = deactivatedAnim();
                String activating = activatingAnim();
                RawAnimation wakeUp = RawAnimation.begin().thenPlay(activating).thenLoop(idle);
                RawAnimation fallAsleep = RawAnimation.begin().thenPlay(deactivating).thenLoop(deactivated);

                if (boolData(behavior, "custom_moving_animation", this)) {
                    idle = stringData(behavior, "idle_animation", this);
                    walking = stringData(behavior, "walking_animation", this);
                    running = stringData(behavior, "running_animation", this);
                } else {
                    if (boolData(behavior, "aggressive", this)) {
                        idle = aggressiveIdleAnim();
                        walking = aggressiveWalkingAnim();
                        running = aggressiveRunningAnim();
                    }
                    if (boolData(behavior, "performing", this)) {
                        idle = performingAnim();
                    }
                }

                if(!forceIdleAnim().isEmpty()) idle = forceIdleAnim();


                if (this.crawling) {
                    if (event.isMoving()) {
                        event.setControllerSpeed(walkAnimationSpeed());
                        return event.setAndContinue(RawAnimation.begin().thenLoop(crawlingWalkingAnim()));
                    } else
                        return event.setAndContinue(RawAnimation.begin().thenLoop(crawlingIdleAnim()));
                } else {
                    if (this.deactivated) {
                        if (event.isMoving()) {
                            event.setControllerSpeed(walkAnimationSpeed());
                            return event.setAndContinue(RawAnimation.begin().thenLoop(defaultWalkingAnim()));
                        } else {
                            event.getController().transitionLength(0);
                            return event.setAndContinue(fallAsleep);
                        }
                    } else if (event.getController().getCurrentRawAnimation() != null && (event.getController().getCurrentRawAnimation().equals(fallAsleep) || (event.getController().getCurrentRawAnimation().equals(wakeUp) && event.getController().getCurrentAnimation().animation().loopType() != Animation.LoopType.LOOP))) {
                        event.getController().transitionLength(0);
                        return event.setAndContinue(wakeUp);
                    } else if (Objects.equals(behavior, "statue")) {
                        if (event.isMoving()) {
                            event.setControllerSpeed(walkAnimationSpeed());
                            return event.setAndContinue(RawAnimation.begin().thenLoop(defaultWalkingAnim()));
                        } else {
                            String anim = stringData(behavior, "animation", this);
                            return event.setAndContinue(RawAnimation.begin().thenLoop(anim));
                        }
                    } else if (Objects.equals(behavior, "stage")) {
                        if (event.isMoving()) {
                            {
                                event.setControllerSpeed(walkAnimationSpeed());
                                return event.setAndContinue(RawAnimation.begin().thenLoop(defaultWalkingAnim()));
                            }
                        } else {
                            return event.setAndContinue(RawAnimation.begin().thenLoop(idle));
                        }

                    } else if (Objects.equals(behavior, "moving")) {
                        if (event.isMoving()) {
                            if (this.running) {
                                event.setControllerSpeed(runAnimationSpeed());
                                return event.setAndContinue(RawAnimation.begin().thenLoop(running));
                            } else {
                                event.setControllerSpeed(walkAnimationSpeed());
                                return event.setAndContinue(RawAnimation.begin().thenLoop(walking));
                            }
                        } else
                            return event.setAndContinue(RawAnimation.begin().thenLoop(idle));
                    } else {
                        return event.setAndContinue(RawAnimation.begin().thenLoop(idle));
                    }
                }
            }
        }
        else {
            boolean move = mimicPlayer.getVelocity().getZ() != 0 && mimicPlayer.getVelocity().getX() != 0;
            boolean crawl = this.crawling;
            boolean run = this.running;

            String walking = mimicAggressive ? aggressiveWalkingAnim() : defaultWalkingAnim();
            String running = mimicAggressive ? aggressiveRunningAnim() : defaultRunningAnim();
            String crawlingIdle = crawlingIdleAnim();
            String crawling = crawlingWalkingAnim();
            String moving = crawl ? crawling : run ? running : walking;
            String idle = crawl ? crawlingIdle : mimicAggressive ? aggressiveIdleAnim() : defaultIdleAnim();
            String deactivating = deactivatingAnim();
            String deactivated = deactivatedAnim();
            String activating = activatingAnim();

            if(move) {
                float speed = run ? runAnimationSpeed() : walkAnimationSpeed();
                event.setControllerSpeed(speed);
                return event.setAndContinue(RawAnimation.begin().thenLoop(moving));
            }
            else {
                return event.setAndContinue(RawAnimation.begin().thenLoop(idle));
            }
        }
        //return PlayState.STOP;
    }

    @Override
    public boolean tryAttack(Entity target) {
        if(target instanceof ServerPlayerEntity p && !p.isDead()) {
            ((IEntityDataSaver)p).getPersistentData().putInt("JumpscareID", this.getId());
            ServerPlayNetworking.send(p, new UpdateJumpscareDataS2CPayload(getId()));
        }
        return super.tryAttack(target);
    }

    @Override
    public boolean onKilledOther(ServerWorld world, LivingEntity other) {
        this.freezeTime = JumpScareLength();
        if(other instanceof ServerPlayerEntity p) {
            ((IEntityDataSaver)p).getPersistentData().putInt("JumpscareID", this.getId());
            ServerPlayNetworking.send(p, new UpdateJumpscareDataS2CPayload(getId()));
            p.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, getEyePos());
        }
        return super.onKilledOther(world, other);
    }

    @Override
    public void tick() {
        super.tick();

        if(!((IEntityDataSaver)this).getPersistentData().contains("spawnRot")){
            GoopyNetworkingUtils.getEntityNbtFromServer(getId());
        }

        if(model != null) {
            model.updateCamPos(this);
        }

        if(mimic) {

            calculateDimensions();
            calculateBoundingBox();

            boolean continueCrawling = mimicPlayer.isCrawling();
            this.crawling = continueCrawling || getCrawling();
            this.running = getRunning();
        }
        else {
            this.behavior = getAIMode(this, getWorld());
            boolean shouldDeactivate = behavior.isEmpty() || boolData(behavior, "deactivated", this) || ((IEntityDataSaver) this).getPersistentData().getBoolean("Deactivated");
            if (shouldDeactivate != this.deactivated) {
                this.freezeTime = shouldDeactivate ? deactivatingLength() : activatingLength();
            }
            this.deactivated = shouldDeactivate;

            runTick();
            if (canCrawl()) {
                crawlTick();
            }
            if (behavior.equals("statue") || behavior.equals("stage")) {
                goalPosTick();
            }

            if (freezeTime > 0) {
                freezeTime--;
            }
            setAiDisabled(freezeTime > 0);

            if(!getWorld().isClient()) {
                for (ServerPlayerEntity p : PlayerLookup.all(getServer())) {
                    ServerPlayNetworking.send(p, new UpdateEntityNbtS2CPongPayload(getId(), ((IEntityDataSaver) this).getPersistentData()));
                }
            }
        }
    }

    public void setModel(DefaultEntityModel<? extends DefaultEntity> model) {
        this.model = model;
    }

    void goalPosTick() {
        BlockPos goalPos = !boolData(behavior, "spawn_pos", this) ? blockPosData(behavior, "position", this) : BlockPos.fromLong(((IEntityDataSaver) this).getPersistentData().getLong("spawnPos"));
        double offsetX = Math.clamp(intData(behavior, "position_offset_x", this) / 10f, -0.495f, 0.495f);
        double offsetZ = Math.clamp(intData(behavior, "position_offset_z", this) / 10f, -0.495f, 0.495f);
        if (getBlockPos().equals(goalPos)) {
            boolean headRotation = boolData(behavior, "rotate_head", this);
            boolean spawnRot = boolData(behavior, "spawn_rot", this);
            int yaw = spawnRot ? ((IEntityDataSaver) this).getPersistentData().getInt("spawnRot") : intData(behavior, "rotation", this);
            int pitch = headRotation ? intData(behavior, "head_pitch", this) : 0;

            setHeadYaw(yaw);
            setBodyYaw(yaw);
            setYaw(yaw);
            if(headRotation){
                setPitch(pitch);
            }
        } else {
            if(!getWorld().isClient()) {
                boolean teleport = boolData(behavior, "teleport", this);
                boolean aggressive = boolData(behavior, "aggressive", this);

                if ((getNavigation().getCurrentPath() == null || getNavigation().getCurrentPath().getLastNode() == null || getNavigation().getCurrentPath().getLastNode().getBlockPos() != goalPos) && getTarget() == null) {
                    if (teleport && !aggressive) {
                        setPosition(goalPos.getX() + 0.5f + offsetX, getY(), goalPos.getZ() + 0.5f + offsetX);
                        getNavigation().stop();
                    } else {
                        getNavigation().startMovingTo(goalPos.getX() + 0.5f, goalPos.getY(), goalPos.getZ() + 0.5f, getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 5.75f);
                    }
                } if (getNavigation().getCurrentPath() != null && (getNavigation().getCurrentPath().isFinished() || getNavigation().isIdle()) && getTarget() == null) {
                    setPosition(goalPos.getX() + 0.5f + offsetX, getY(), goalPos.getZ() + 0.5f + offsetZ);
                    getNavigation().stop();
                }
            }
        }
    }

    @Override
    public float lerpYaw(float delta) {
        return getBodyYaw();
    }

    public void crawlTick(){
        this.crawling = ((IEntityDataSaver)this).getPersistentData().getBoolean("Crawling");

        calculateDimensions();
        calculateBoundingBox();

        if(!this.getWorld().isClient()) {
            boolean shouldCrawl = shouldCrawl();

            boolean canSwitch = !crawling && shouldCrawl || crawling && !shouldCrawl;
            if(canSwitch){
                crawlingCooldown = Math.max(0, crawlingCooldown - 1);

                if (crawlingCooldown == 0) {
                    setCrawling(shouldCrawl, getWorld());
                }
            }
            else if(crawling) {
                crawlingCooldown = 20;
            }
        }
    }

    public boolean shouldCrawl(){
        BlockPos frontBlock = this.getBlockPos().offset(Direction.fromRotation(this.getHeadYaw()));
        BlockPos frontUpBlock = frontBlock.up();
        BlockPos frontUpUpBlock = frontUpBlock.up();
        BlockPos upBlock = this.getBlockPos().up();

        boolean continueCrawl = containsHitbox(upBlock);

        boolean crawlCondition1 = containsHitbox(frontUpBlock) && !containsHitbox(frontBlock);
        boolean crawlCondition2 = containsHitbox(frontBlock) && !containsHitbox(frontUpBlock) && containsHitbox(frontUpUpBlock);

        return continueCrawl || crawlCondition1 || crawlCondition2;
    }

    public boolean containsHitbox(BlockPos pos){
        float width = super.getBaseDimensions(EntityPose.STANDING).width() / 2f;
        float height = super.getBaseDimensions(EntityPose.STANDING).height();
        Box box = new Box(-width, 0, -width, width, height, width);

        VoxelShape collisionShape = this.getWorld().getBlockState(pos).getCollisionShape(getWorld(), pos);
        boolean bl1 = !collisionShape.isEmpty() && collisionShape.getBoundingBox().intersects(box);

        return bl1;
    }

    public void runTick(){
        this.running = ((IEntityDataSaver)this).getPersistentData().getBoolean("Running");
        runningCooldown = Math.max(0, runningCooldown - 1);
        if(!this.getWorld().isClient()) {
            boolean shouldRun = (this.getNavigation().getCurrentPath() != null && this.getTarget() != null);
            if (runningCooldown == 0 && (!running && shouldRun || running && !shouldRun)) {
                setRunning(shouldRun, getWorld());
                runningCooldown = 10;
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setCrawling(boolean crawling, World world) {
        ((IEntityDataSaver) this).getPersistentData().putBoolean("Crawling", crawling);
        if(world.isClient()){
            GoopyNetworkingUtils.saveEntityData(getId(), ((IEntityDataSaver) this).getPersistentData().copy());
        }
        else {
            for (ServerPlayerEntity p : PlayerLookup.all(getServer())) {
                ServerPlayNetworking.send(p, new UpdateEntityNbtS2CPongPayload(getId(), ((IEntityDataSaver) this).getPersistentData()));
            }
        }
    }
    public void setRunning(boolean running, World world) {
        ((IEntityDataSaver) this).getPersistentData().putBoolean("Running", running);
        if(world.isClient()){
            GoopyNetworkingUtils.saveEntityData(getId(), ((IEntityDataSaver) this).getPersistentData().copy());
        }
        else {
            for (ServerPlayerEntity p : PlayerLookup.all(getServer())) {
                ServerPlayNetworking.send(p, new UpdateEntityNbtS2CPongPayload(getId(), ((IEntityDataSaver) this).getPersistentData()));
            }
        }
    }
    public void setDeactivated(boolean deactivated, World world) {
        ((IEntityDataSaver) this).getPersistentData().putBoolean("Deactivated", deactivated);
        if(world.isClient()){
            GoopyNetworkingUtils.saveEntityData(getId(), ((IEntityDataSaver) this).getPersistentData().copy());
        }
        else {
            for (ServerPlayerEntity p : PlayerLookup.all(getServer())) {
                ServerPlayNetworking.send(p, new UpdateEntityNbtS2CPongPayload(getId(), ((IEntityDataSaver) this).getPersistentData()));
            }
        }
    }

    public boolean getCrawling(){
        if(mimic) {
            return ((IPlayerCustomModel)mimicPlayer).shouldBeCrawling();
        }
        return ((IEntityDataSaver)this).getPersistentData().getBoolean("Crawling");
    }
    public boolean getRunning(){
        return ((IEntityDataSaver)this).getPersistentData().getBoolean("Running");
    }

    @Override
    public EntityDimensions getBaseDimensions(EntityPose pose) {
        boolean dimCrawl = getCrawling();
        EntityDimensions entityDimensions = super.getBaseDimensions(pose);
        if (dimCrawl || pose == EntityPose.SWIMMING) {
            return EntityDimensions.fixed(entityDimensions.width(), crawlHeight()).withEyeHeight(0.5f);
        }
        return entityDimensions;
    }



    @Override
    public double getTick(Object entity) {
        if(this.menuTick)
            return MinecraftClient.getInstance().world.getTime();
        else if(this.mimic)
            return MinecraftClient.getInstance().world.getTime();
        else
            return ((Entity)entity).age;
    }

    public List<?> getDataList(String string){
        return switch (string){
            default -> null;
            case "statue.animation" -> getStatueAnimations();
            case "moving.idle_animation" -> getIdleAnimations();
            case "moving.walking_animation" -> getWalkingAnimations();
            case "moving.running_animation" -> getRunningAnimations();
        };
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        ItemStack stack = player.getMainHandStack();
        if (stack.isOf(ItemInit.CPU)) {
            String animatronic = ItemNbtUtil.getNbt(stack).getString("entity");
            if (!animatronic.isEmpty() && ComputerData.getAIAnimatronic(animatronic) instanceof ComputerData.Initializer.AnimatronicAI ai) {
                if(this.getType() == ai.entityType()) {
                    if (!getDisk(this, getWorld()).isEmpty()) {
                        dropStack(getDisk(this, getWorld()), (float) hitPos.y);
                    }
                    ItemStack disk = player.getMainHandStack();
                    putCPU(this, disk, getWorld());
                    disk.decrementUnlessCreative(1, player);
                    player.sendMessage(Text.translatable("item.fnafur.cpu.entity_updated"), true);
                }
                else {
                    player.sendMessage(Text.translatable("item.fnafur.cpu.wrong_entity"), true);
                }
                return ActionResult.SUCCESS;
            }
        } else if (player.getMainHandStack().isOf(ItemInit.PIPE_WRENCH)) {
            ItemStack disk = getDisk(this, getWorld());
            if (!disk.isEmpty()) {
                dropStack(disk, (float) hitPos.y);
                putCPU(this, ItemStack.EMPTY, getWorld());
                return ActionResult.SUCCESS;
            }
        } else if (player.getMainHandStack().isOf(ItemInit.PAINTBRUSH)) {
            if(player instanceof ServerPlayerEntity p) {
                GoopyNetworkingUtils.setScreen(p, ScreensInit.SKINS, ((IEntityDataSaver)this).getPersistentData(), getId());
            }
            return ActionResult.SUCCESS;
        }
        return super.interactAt(player, hitPos, hand);
    }


    public String getBehavior() {
        return this.behavior;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        /*ItemStack disk = getDisk(this, getWorld());
        if (!disk.isEmpty()) {
            dropStack(disk);
            putFloppyDisk(this, ItemStack.EMPTY, getWorld());
        }*/
        ItemStack spawnItem = getPickBlockStack();

        dropStack(spawnItem);
        super.onDeath(damageSource);
    }

    @Nullable
    @Override
    public ItemStack getPickBlockStack() {
        EntitySpawnItem spawnItem = EntitySpawnItem.forEntity(this.getType());
        if (spawnItem == null) {
            return super.getPickBlockStack();
        }
        ItemStack stack = new ItemStack(spawnItem);
        ItemNbtUtil.setNbt(stack, ((IEntityDataSaver)this).getPersistentData());
        return stack;
    }

    @Override
    protected void updatePostDeath() {
        this.deathTime++;
        if (this.deathTime >= deathLength() && !this.getWorld().isClient() && !this.isRemoved()) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    void putCPU(PathAwareEntity entity, ItemStack disk, World world){
        ((IEntityDataSaver)entity).getPersistentData().put("cpu", disk.encodeAllowEmpty(world.getRegistryManager()));
        if(world.isClient()){
            GoopyNetworkingUtils.saveEntityData(entity.getId(), ((IEntityDataSaver)entity).getPersistentData());
        }
    }
    ItemStack getDisk(PathAwareEntity entity, World world){
        return ItemStack.fromNbtOrEmpty(world.getRegistryManager(), ((IEntityDataSaver)entity).getPersistentData().getCompound("cpu"));
    }
    String getAIMode(PathAwareEntity entity, World world){
        NbtCompound diskData = ItemNbtUtil.getNbt(getDisk(entity, world));
        if(diskData.isEmpty()) return "";

        NbtCompound data = diskData.getCompound("" + getAIHour(world));
        if(data.isEmpty()){
            int i = getAIHour(world);
            while(diskData.getCompound("" + i).getString("Behavior").isEmpty()){
                i--;
            }
            return diskData.getCompound("" + i).getString("Behavior");
        }
        else {
            return data.getString("Behavior");
        }
    }
    public int getAIHour(World world){
        long time = world.getTimeOfDay() * 4;

        long hour = (time / 1000) - ((96000 * (time / 96000)) / 1000);
        int realHour = (int)hour + 24;
        realHour = realHour >= 96 ? realHour - 96 : realHour;
        return realHour;
    }
    public NbtCompound getAIData(PathAwareEntity entity, World world){
        NbtCompound diskData = ItemNbtUtil.getNbt(getDisk(entity, world));
        if(diskData.isEmpty()) return new NbtCompound();
        NbtCompound data = diskData.getCompound("" + getAIHour(world));
        if(data.isEmpty()){
            int i = getAIHour(world);
            while(diskData.getCompound("" + i).getString("Behavior").isEmpty()){
                i--;
            }
            return diskData.getCompound("" + i);
        }
        else {
            return data;
        }
    }
    public boolean boolData(String behavior, String option, PathAwareEntity entity) {
        int index = getIndex(behavior, option);
        return getAIData(entity, entity.getWorld()).getBoolean("" + index);
    }
    public int intData(String behavior, String option, PathAwareEntity entity) {
        int index = getIndex(behavior, option);
        return getAIData(entity, entity.getWorld()).getInt("" + index);
    }
    public BlockPos blockPosData(String behavior, String option, PathAwareEntity entity) {
        int index = getIndex(behavior, option);
        return BlockPos.fromLong(getAIData(entity, entity.getWorld()).getLong("" + index));
    }
    public String stringData(String behavior, String option, PathAwareEntity entity) {
        int index = getIndex(behavior, option);
        return getAIData(entity, entity.getWorld()).getString("" + index);
    }
    public int getIndex(String behavior, String option) {
        if(!this.getWorld().isClient()){
            for(ServerPlayerEntity p : PlayerLookup.all(getServer())) {
                if(behavior != null && option != null) {
                    ServerPlayNetworking.send(p, new AIBehaviorUpdateS2CPayload(behavior, option, getId()));
                }
            }
            return ((IEntityDataSaver)this).getPersistentData().getInt(behavior + "." + option);
        }
        else {
            if (ComputerData.getAIBehavior(behavior) instanceof ComputerAI ai) {
                return ai.getOptionIndex(option);
            }
        }
        return -1;
    }

    float crawlHeight(){
        if(getWorld().isClient() && MinecraftClient.getInstance().player != null) {
            float height = getCrawlHeight();
            ((IEntityDataSaver)this).getPersistentData().putFloat("crawl_height", height);
            GoopyNetworkingUtils.saveEntityData(getId(), ((IEntityDataSaver)this).getPersistentData());
            return height;
        }
        else return ((IEntityDataSaver)this).getPersistentData().getFloat("crawl_height");
    }
    boolean canCrawl(){
        if(getWorld().isClient()) {;
            boolean can_crawl = isAbleToCrawl();
            ((IEntityDataSaver)this).getPersistentData().putBoolean("can_crawl", can_crawl);
            GoopyNetworkingUtils.saveEntityData(getId(), ((IEntityDataSaver)this).getPersistentData());
            return can_crawl;
        }
        else return ((IEntityDataSaver)this).getPersistentData().getBoolean("can_crawl");
    }

    String forceIdleAnim(){
        return "";
    }

    List<String> getStatueAnimations(){
        List<String> list = getIdleAnimations();
        list.add(defaultIdleAnim());
        list.add(defaultWalkingAnim());
        list.add(defaultRunningAnim());
        list.add(performingAnim());
        list.add(deactivatedAnim());
        list.add(aggressiveIdleAnim());
        list.add(aggressiveWalkingAnim());
        list.add(aggressiveRunningAnim());
        list.add(crawlingIdleAnim());
        list.add(crawlingWalkingAnim());
        list.add(attackAnim());
        return list;
    }
    public List<String> getIdleAnimations(){
        List<String> list = new ArrayList<>();
        list.add(defaultIdleAnim());
        list.add(aggressiveIdleAnim());
        list.add(crawlingIdleAnim());
        list.add(deactivatedAnim());
        list.add(performingAnim());
        return list;
    };
    List<String> getWalkingAnimations(){
        List<String> list = new ArrayList<>();
        list.add(defaultWalkingAnim());
        list.add(aggressiveWalkingAnim());
        list.add(crawlingWalkingAnim());
        return list;
    }
    List<String> getRunningAnimations(){
        List<String> list = new ArrayList<>();
        list.add(defaultRunningAnim());
        list.add(aggressiveRunningAnim());
        return list;
    }

    public String demoAnim() {
        return defaultIdleAnim();
    }
    public abstract float demo_scale();
    protected abstract String defaultIdleAnim();
    protected abstract String defaultWalkingAnim();
    protected abstract String defaultRunningAnim();
    protected abstract String performingAnim();
    protected abstract String deactivatingAnim();
    protected abstract int deactivatingLength();
    protected abstract String deactivatedAnim();
    protected abstract String activatingAnim();
    protected abstract int activatingLength();
    protected abstract String aggressiveIdleAnim();
    protected abstract String aggressiveWalkingAnim();
    protected abstract String aggressiveRunningAnim();
    protected abstract String crawlingIdleAnim();
    protected abstract String crawlingWalkingAnim();
    public abstract boolean hasJumpScare();
    protected abstract String JumpScareAnim();
    public abstract int JumpScareLength();
    protected abstract String attackAnim();
    protected abstract int attackLength();
    protected abstract String dyingAnim();
    protected abstract int deathLength();
    protected abstract float walkAnimationSpeed();
    protected abstract float runAnimationSpeed();
    protected abstract boolean isAbleToCrawl();
    protected abstract float getCrawlHeight();
    public EntitySkin defaultSkin(){
        return new EntitySkin("default");
    }
    public abstract List<EntitySkin> getSkins();
    public EntitySkin getSkin(String name){
        if(getSkins() != null) {
            for (EntitySkin skin : getSkins()) {
                if (skin.name.equals(name)) {
                    return skin;
                }
            }
        }
        return defaultSkin();
    }

    public class EntitySkin {
        public static final int CONSTANT = 0;
        public static final int NIGHT = 1;
        public static final int AGGRESSIVE = 2;
        public String name;
        public Identifier icon;
        public Identifier icon_outline;
        public Identifier texture;
        public Identifier glow_texture;
        public Identifier geo;
        public Identifier animations;
        public String killScreenID;
        public int glow_type;
        public EntitySkin(String name){
            this.name = name;
            this.icon = getDefaultIcon();
            this.icon_outline = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/skins/default_outline.png");
            this.glow_texture = null;
            this.texture = getDefaultTexture();
            this.geo = getDefaultGeoModel();
            this.animations = getDefaultAnimations();
            this.killScreenID = getDefaultKillScreenID();
            this.glow_type = CONSTANT;
        }

        public EntitySkin icon(Identifier id){
            this.icon = id;
            return this;
        }

        public EntitySkin icon_outline(Identifier id){
            this.icon_outline = id;
            return this;
        }

        public EntitySkin texture(Identifier id){
            this.texture = id;
            return this;
        }

        public EntitySkin glow_texture(Identifier id){
            this.glow_texture = id;
            return this;
        }

        public EntitySkin geo(Identifier id){
            this.geo = id;
            return this;
        }

        public EntitySkin animations(Identifier id){
            this.animations = id;
            return this;
        }
        public EntitySkin killScreen(String id){
            this.killScreenID = id;
            return this;
        }
        public EntitySkin glow_type(int type){
            this.glow_type = type;
            return this;
        }
    }
    @Override
    public boolean isPersistent() {
        return true;
    }


    public abstract Identifier getDefaultTexture();
    public abstract Identifier getDefaultGeoModel();
    public abstract Identifier getDefaultAnimations();
    public String getDefaultKillScreenID() {
        return ScreensInit.DEFAULT_KILLSCREEN;
    };

    public Identifier getDefaultIcon(){
        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/skins/default_icon.png");
    }

}

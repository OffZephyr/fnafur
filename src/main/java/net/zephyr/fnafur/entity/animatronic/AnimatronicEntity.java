package net.zephyr.fnafur.entity.animatronic;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader.ChipReaderBlockEntity;
import net.zephyr.fnafur.blocks.utility_blocks.server_monitor.ServerMonitorBlockEntity;
import net.zephyr.fnafur.entity.animatronic.block.AnimatronicBlock;
import net.zephyr.fnafur.entity.animatronic.block.AnimatronicBlockEntity;
import net.zephyr.fnafur.entity.animatronic.data.CharacterData;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.entity_init.CharacterInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SGetFromServerPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateEntityNbtC2SPayload;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.cache.GeckoLibResources;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnimatronicEntity extends PathAwareEntity implements GeoEntity, LinkTarget {

    int updateDepth = 0;
    public List<IEntityDataSaver> sources = new ArrayList<>();
    private CharacterData character;
    public double force_age = 0;
    public boolean isMenu = false;
    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AnimatronicEntity(EntityType<? extends PathAwareEntity> entityType, World world){
        super(entityType, world);
        if(!LinkTarget.allTargets.contains((IEntityDataSaver) this)){
            LinkTarget.allTargets.add((IEntityDataSaver) this);
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        cleanSources();
        super.remove(reason);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("Upper", 3, this::upperAnimController));
        controllers.add(new AnimationController<>("Lower", 3, this::lowerAnimController));
        controllers.add(new AnimationController<>("Blink", 0, this::blinkAnimController));
    }

    private PlayState blinkAnimController(AnimationTest<GeoAnimatable> geoAnimatableAnimationTest) {
        return PlayState.CONTINUE;
    }

    private PlayState lowerAnimController(AnimationTest<AnimatronicEntity> animatronicEntityAnimationState) {
        animatronicEntityAnimationState.controller().transitionLength(3);
        if(isMenu) {
            animatronicEntityAnimationState.controller().transitionLength(0);
            return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(prefixAnim("loweridle")));
        }

        String getupAnim = ((IEntityDataSaver)this).getPersistentData().getString("getupAnim", "");
        if(!getupAnim.isEmpty()){
            animatronicEntityAnimationState.controller().transitionLength(0);
            return PlayState.STOP;
        }
        return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(prefixAnim("loweridle")));
    }

    private PlayState upperAnimController(AnimationTest<AnimatronicEntity> animatronicEntityAnimationState) {
        animatronicEntityAnimationState.controller().transitionLength(3);
        if(isMenu) {
            animatronicEntityAnimationState.controller().transitionLength(0);
            return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(prefixAnim("menuidle")));
        }

        String getupAnim = ((IEntityDataSaver)this).getPersistentData().getString("getupAnim", "");
        if(!getupAnim.isEmpty()){
            animatronicEntityAnimationState.controller().transitionLength(0);
            animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenPlayAndHold(prefixAnim(getupAnim)));
            if(animatronicEntityAnimationState.controller().getAnimationState() == AnimationController.State.PAUSED) ((IEntityDataSaver)this).getPersistentData().putString("getupAnim", "");
            return PlayState.CONTINUE;
        }

        return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(prefixAnim("deactivated")));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void tick() {
        if(isMenu){
            age++;
        }

        if(!getWorld().isClient()) {
            ((IEntityDataSaver)this).getPersistentData().putString("getupAnim", "");
            if(getSources().isEmpty()) {
                turnToBlock(null);
                return;
            }
            for (IEntityDataSaver ent : getSources()) {
                if(ent instanceof ServerMonitorBlockEntity ent2){
                    BlockPos readerPos = ent2.getActiveChipReaderPos();
                    if(readerPos == null){
                        turnToBlock(ent2);
                        return;
                    }
                    if(getWorld().getBlockEntity(readerPos) instanceof ChipReaderBlockEntity ent3){
                        if((((IEntityDataSaver)ent3).getPersistentData().getCompound("cpu").orElse(new NbtCompound()).isEmpty())){
                            turnToBlock(ent2);
                            return;
                        }
                    }
                }
            }
        }
        super.tick();
    }

    void turnToBlock(@Nullable ServerMonitorBlockEntity ent2){
        getWorld().setBlockState(getBlockPos(), BlockInit.ANIMATRONIC_BLOCK.getDefaultState().with(AnimatronicBlock.POSES, AnimatronicBlock.getPose(getWorld(), getBlockPos(), getHorizontalFacing())));

        if(getWorld().getBlockEntity(getBlockPos()) instanceof AnimatronicBlockEntity anim){
            NbtCompound thisData = ((IEntityDataSaver)this).getPersistentData();
            ((IEntityDataSaver)anim).getPersistentData().copyFrom(thisData);

            if(ent2 != null){
                ((LinkTarget)anim).getSources().add((IEntityDataSaver)ent2);
                ent2.getTargets().add((IEntityDataSaver) anim);
                ent2.getTargets().remove((IEntityDataSaver) this);
                ent2.markDirty();

                GoopyNetworkingUtils.saveBlockNbt(anim.getPos(), ((IEntityDataSaver)anim).getPersistentData(), getWorld());
                ent2.updateSources(getWorld(), ent2.getPos());
            }

            remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public double getTick(Object entity) {
        if(isMenu){
            return MinecraftClient.getInstance().world.getTime();
        }

        return age;
    }

    public CharacterData getCharacter(){
        if(character == null) return CharacterInit.ENDO_01;
        return character;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {

        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 50f)
                .add(EntityAttributes.ATTACK_DAMAGE, 9999f)
                .add(EntityAttributes.ATTACK_SPEED, 1f)
                .add(EntityAttributes.ATTACK_KNOCKBACK, 0f)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 9999f)
                .add(EntityAttributes.FOLLOW_RANGE, 16D);
    }


    public Identifier getTexture(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("alt")){
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt").orElse(new NbtCompound());
            String texture = nbt.getString("texture").orElse("");
            if(!texture.isEmpty()){
                return Identifier.of(FnafUniverseRebuilt.MOD_ID, texture);
            }
        }

        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/default/endo_01/endo_01.png");
    }
    public Identifier getEyeTexture(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("alt")){
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt").orElse(new NbtCompound());
            String texture = nbt.getString("eyes_texture").orElse("");
            if(!texture.isEmpty()){
                return Identifier.of(FnafUniverseRebuilt.MOD_ID, texture);
            }
        }

        return getTexture(world);
    }
    public Identifier getReRenderTexture(World world){
        return getTexture(world);
    }
    public Identifier getModel(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("alt")){
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt").orElse(new NbtCompound());
            String model = nbt.getString("model").orElse("");
            if(!model.isEmpty()){
                return Identifier.of(FnafUniverseRebuilt.MOD_ID, model);
            }
        }

        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/models/entity/default/endo_01/endo_01.geo.json");
    }

    public Identifier getReRenderModel(World world){
        return getModel(world);
    }

    public String getAnimationsPath(World world){
        if(((IEntityDataSaver)this).getPersistentData().contains("alt")){
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt").orElse(new NbtCompound());
            String animations = nbt.getString("animations").orElse("");
            if(!animations.isEmpty()){
                return animations;
            }
        }
        return "";
    }
    public Identifier getAnimations(World world){

        String animation = getAnimationsPath(world);
        if(!animation.isEmpty()){
            return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/animations/" + animation + ".animation.json");
        }

        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/animations/entity/default.animation.json");
    }

    public String prefixAnim(String animation){
        Identifier location = Identifier.of(FnafUniverseRebuilt.MOD_ID, getAnimationsPath(getWorld()));
        Map<Identifier, BakedAnimations> animations = GeckoLibResources.getBakedAnimations();
        BakedAnimations bakedAnimations = animations.get(location);

        NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt").orElse(new NbtCompound());
        String name = nbt.getString("chara").orElse("");
        String anim = "animation." + name + "." + animation;
        if(bakedAnimations != null && bakedAnimations.animations().containsKey(anim)) {
            return anim;
        }

        return "animation.default." + animation;
    }

    public RenderLayer getRenderType(Identifier texture){
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    public List<IEntityDataSaver> getSources() {
        return sources;
    }

    @Override
    public int getUpdateDepth() {
        return updateDepth;
    }

    @Override
    public void setUpdateDepth(int depth) {
        updateDepth = depth;
    }
}

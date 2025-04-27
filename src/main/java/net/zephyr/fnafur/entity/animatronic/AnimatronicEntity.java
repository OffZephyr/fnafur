package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.entity.animatronic.data.CharacterData;
import net.zephyr.fnafur.init.entity_init.CharacterInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AnimatronicEntity extends PathAwareEntity implements GeoEntity {

    private CharacterData character;
    public boolean isMenu = false;
    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AnimatronicEntity(EntityType<? extends PathAwareEntity> entityType, World world){
        super(entityType, world);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Upper", 1, this::upperAnimController));
        controllers.add(new AnimationController<>(this, "Lower", 1, this::lowerAnimController));
    }

    private PlayState lowerAnimController(AnimationState<AnimatronicEntity> animatronicEntityAnimationState) {
        return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop("loweridle"));
    }

    private PlayState upperAnimController(AnimationState<AnimatronicEntity> animatronicEntityAnimationState) {
        if(isMenu) {
            return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(prefixAnim("menuidle")));
        }
        return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(prefixAnim("performance")));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void tick() {
        super.tick();
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
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt");
            String texture = nbt.getString("texture");
            if(!texture.isEmpty()){
                return Identifier.of(FnafUniverseRebuilt.MOD_ID, texture);
            }
        }

        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/default/endo_01/endo_01.png");
    }
    public Identifier getReRenderTexture(World world){
        return getTexture(world);
    }
    public Identifier getModel(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("alt")){
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt");
            String model = nbt.getString("model");
            if(!model.isEmpty()){
                return Identifier.of(FnafUniverseRebuilt.MOD_ID, model);
            }
        }

        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geo/entity/default/endo_01/endo_01.geo.json");
    }

    public Identifier getReRenderModel(World world){
        return getModel(world);
    }
    public Identifier getAnimations(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("alt")){
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt");
            String animations = nbt.getString("animations");
            if(!animations.isEmpty()){
                return Identifier.of(FnafUniverseRebuilt.MOD_ID, animations);
            }
        }

        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "animations/entity/classic/cl_fred/cl_fred.animation.json");
    }

    public String prefixAnim(String animation){
        Identifier location = getAnimations(getWorld());
        BakedAnimations bakedAnimations = GeckoLibCache.getBakedAnimations().get(location);

        NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt");
        String name = nbt.getString("chara");
        String anim = "animation." + name + "." + animation;
        if(bakedAnimations != null && bakedAnimations.animations().containsKey(anim)) {

            return anim;
        }

        return "animation.default." + animation;
    }

    public RenderLayer getRenderType(){
        return RenderLayer.getEntityTranslucent(getTexture(getWorld()));
    }
}

package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;
import net.zephyr.fnafur.entity.animatronic.data.CharacterData;
import net.zephyr.fnafur.init.entity_init.CharacterInit;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
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
        return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop("animation.cl_fred.loweridle"));
    }

    private PlayState upperAnimController(AnimationState<AnimatronicEntity> animatronicEntityAnimationState) {
        if(isMenu) {
            return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop("animation.cl_fred.menuidle"));
        }
        return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop("animation.cl_fred.performance"));
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

}

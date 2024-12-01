package net.zephyr.fnafur.entity.base;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.util.mixinAccessing.IEditCamera;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public abstract class DefaultEntityModel<T extends DefaultEntity> extends GeoModel<T> {
    public AnimationState<T> animationState;
    public DefaultEntityRenderer<T> entityRenderer;
    @Override
    public Identifier getModelResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        String skin = ((IEntityDataSaver) animatable).getPersistentData().getString("Reskin");
        return animatable.getSkin(skin).geo;
    }

    @Override
    public Identifier getTextureResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        String skin = ((IEntityDataSaver) animatable).getPersistentData().getString("Reskin");

        return animatable.getSkin(skin).texture;
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        String skin = ((IEntityDataSaver) animatable).getPersistentData().getString("Reskin");

        return animatable.getSkin(skin).animations;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        animatable.setModel(this);
        updateCamPos(animatable);
        this.animationState = animationState;
    }
    public abstract float scale();

    public void updateCamPos(DefaultEntity entity){
        String skin = ((IEntityDataSaver) entity).getPersistentData().getString("Reskin");

        if(entity.isBoopable()) {
            GeoBone bone = getAnimationProcessor().getBone("boop");

            if(bone != null) {
                if (entity.isBoopable()) {
                    Box box = new Box(
                            bone.getWorldPosition().x() - (entity.boopSize().getX() / 2f) + entity.boopOffset().getX(),
                            bone.getWorldPosition().y() - (entity.boopSize().getY() / 2f) + entity.boopOffset().getY(),
                            bone.getWorldPosition().z() - (entity.boopSize().getZ() / 2f) + entity.boopOffset().getZ(),
                            bone.getWorldPosition().x() + (entity.boopSize().getX() / 2f) + entity.boopOffset().getX(),
                            bone.getWorldPosition().y() + (entity.boopSize().getY() / 2f) + entity.boopOffset().getY(),
                            bone.getWorldPosition().z() + (entity.boopSize().getZ() / 2f) + entity.boopOffset().getZ()
                    );
                    entity.boopBox = box;
                }
            }
        }
        entity.killScreenID = entity.getSkin(skin).killScreenID;
        GeoBone camera = getAnimationProcessor().getBone("camera");

        Entity jumpscareEntity = MinecraftClient.getInstance().world.getEntityById(((IEntityDataSaver)MinecraftClient.getInstance().player).getPersistentData().getInt("JumpscareID"));

        IEditCamera camera1 = (IEditCamera)MinecraftClient.getInstance().gameRenderer.getCamera();

        if (camera != null && jumpscareEntity != null && entity.getId() == jumpscareEntity.getId() && entity.hasJumpScare()) {

            if(MinecraftClient.getInstance().player.isDead()) {
                camera1.setRotation(
                        -camera.getRotY() * MathHelper.DEGREES_PER_RADIAN + entity.getBodyYaw() + 180,
                        camera.getRotX() * MathHelper.DEGREES_PER_RADIAN,
                        camera.getRotZ() * MathHelper.DEGREES_PER_RADIAN);

                camera1.setPosition(camera.getWorldPosition().x() - camera.getLocalPosition().x() + (camera.getLocalPosition().x() * scale()),
                        camera.getWorldPosition().y() - camera.getLocalPosition().y() + (camera.getLocalPosition().y() * scale()),
                        camera.getWorldPosition().z() - camera.getLocalPosition().z() + (camera.getLocalPosition().z() * scale())
                );
            }
        }
    }
}

package net.zephyr.fnafur.entity.classic.cl_bon;

import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.entity.base.DefaultEntityModel;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;

public class cl_bon_model extends DefaultEntityModel<cl_bon> {

    @Override
    public void setCustomAnimations(cl_bon animatable, long instanceId, AnimationState<cl_bon> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");
        GeoBone eye_left = getAnimationProcessor().getBone("left_eye_main");
        GeoBone eye_right = getAnimationProcessor().getBone("right_eye_main");

        if (head != null && !animatable.crawling) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX((entityData.headPitch()/2f) * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY((entityData.netHeadYaw()/2f) * MathHelper.RADIANS_PER_DEGREE);
        }
        if (eye_left != null && !animatable.crawling) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            eye_left.setRotX((entityData.headPitch()/2f) * MathHelper.RADIANS_PER_DEGREE);
            eye_left.setRotY((entityData.netHeadYaw()/2f) * MathHelper.RADIANS_PER_DEGREE);
        }
        if (eye_right != null && !animatable.crawling) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            eye_right.setRotX((entityData.headPitch()/2f) * MathHelper.RADIANS_PER_DEGREE);
            eye_right.setRotY((entityData.netHeadYaw()/2f) * MathHelper.RADIANS_PER_DEGREE);
        }

        super.setCustomAnimations(animatable, instanceId, animationState);
    }

    @Override
    public float scale() {
        return 0.675f;
    }
}

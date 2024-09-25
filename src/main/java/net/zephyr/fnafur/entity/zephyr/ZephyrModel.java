package net.zephyr.fnafur.entity.zephyr;

import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.entity.base.DefaultEntityModel;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;

public class ZephyrModel extends DefaultEntityModel<ZephyrEntity> {
    @Override
    public void setCustomAnimations(ZephyrEntity animatable, long instanceId, AnimationState<ZephyrEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("Head");

        if (head != null && !animatable.crawling) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }

        super.setCustomAnimations(animatable, instanceId, animationState);
    }

    @Override
    public float scale() {
        return 1;
    }
}

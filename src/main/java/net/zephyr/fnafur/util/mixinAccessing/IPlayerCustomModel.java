package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.entity.LivingEntity;

public interface IPlayerCustomModel {
    LivingEntity getCurrentEntity();
    void setCurrentEntity(LivingEntity entity);
    void resetCurrentEntity();
    float getMimicYaw();
    void setMimicYaw(float yaw);

    boolean shouldBeCrawling();
    void setCrawling(boolean crawling);
}

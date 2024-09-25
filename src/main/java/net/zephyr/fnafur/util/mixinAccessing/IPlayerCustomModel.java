package net.zephyr.fnafur.util.mixinAccessing;

import net.zephyr.fnafur.entity.base.DefaultEntity;

public interface IPlayerCustomModel {
    DefaultEntity getCurrentEntity();
    void setCurrentEntity(DefaultEntity entity);
    void resetCurrentEntity();
    float getMimicYaw();
    void setMimicYaw(float yaw);

    boolean shouldBeCrawling();
    void setCrawling(boolean crawling);
}

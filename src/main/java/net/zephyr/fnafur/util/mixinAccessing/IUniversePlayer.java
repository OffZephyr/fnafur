package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.event.GameEvent;

public interface IUniversePlayer {
    float getMaskDelta();
    void setMaskDelta(float delta);
    boolean hasVanniMaskOn();
    boolean hasVanniMaskEquipped();
    boolean isUsingVanniMask();
    boolean canAnimateMask();
    void setCanAnimateMask(boolean can);
    LivingEntity getCurrentEntity();
    void setCurrentEntity(LivingEntity entity);
    void resetCurrentEntity();
    float getMimicYaw();
    void setMimicYaw(float yaw);

    boolean shouldBeCrawling();
    void setCrawling(boolean crawling);
}

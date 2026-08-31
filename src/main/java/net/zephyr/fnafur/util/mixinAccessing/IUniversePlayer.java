package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;

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

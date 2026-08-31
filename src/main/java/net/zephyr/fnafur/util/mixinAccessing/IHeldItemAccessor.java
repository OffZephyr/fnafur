package net.zephyr.fnafur.util.mixinAccessing;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.HumanoidArm;

public interface IHeldItemAccessor {
    void doSwingArm(float swingProgress, float equipProgress, PoseStack matrices, int armX, HumanoidArm arm);
}

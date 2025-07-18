package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;

public interface IHeldItemAccessor {
    void doSwingArm(float swingProgress, float equipProgress, MatrixStack matrices, int armX, Arm arm);
}

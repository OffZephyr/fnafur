package net.zephyr.fnafur.util;

import net.minecraft.util.math.Vec3d;

public interface IHasArmPos {
    Vec3d getLeftArmPos(boolean isMainStack);
    Vec3d getRightArmPos(boolean isMainStack);
}

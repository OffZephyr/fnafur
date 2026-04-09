package net.zephyr.fnafur.util;

import net.minecraft.world.phys.Vec3;

public interface IHasArmPos {
    Vec3 getLeftArmPos(boolean isMainStack);
    Vec3 getRightArmPos(boolean isMainStack);
}

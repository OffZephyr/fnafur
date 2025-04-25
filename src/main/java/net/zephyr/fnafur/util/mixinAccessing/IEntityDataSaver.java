package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {
    NbtCompound getPersistentData();
    void setServerUpdateStatus(boolean value);
    boolean getServerUpdateStatus();
}

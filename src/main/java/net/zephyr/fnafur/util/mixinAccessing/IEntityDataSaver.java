package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.nbt.CompoundTag;

public interface IEntityDataSaver {
    CompoundTag getPersistentData();
    void setPersistentData(CompoundTag nbt);
    void setServerUpdateStatus(boolean value);
    boolean getServerUpdateStatus();
}

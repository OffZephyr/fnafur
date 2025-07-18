package net.zephyr.fnafur.blocks.linking;

import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public interface EnergySource extends LinkSource {
    boolean isSendingPower(IEntityDataSaver target);
}

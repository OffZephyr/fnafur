package net.zephyr.fnafur.util.mixinAccessing;

import net.zephyr.fnafur.util.jsonReaders.entity_skins.EntityDataManager;
import net.zephyr.fnafur.util.jsonReaders.layered_block.LayeredBlockManager;

public interface IGetClientManagers {
    LayeredBlockManager getLayerManager();
    EntityDataManager getEntityDataManager();
}

package net.zephyr.fnafur.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.zephyr.fnafur.init.entity_init.EntityInit;

public class ZephyrSpawn extends EntitySpawnItem {
    public ZephyrSpawn(Settings settings) {
        super(settings, ItemWithDescription.FLOPPY_DISK);
    }

    @Override
    public EntityType<? extends LivingEntity> entity() {
        return EntityInit.ZEPHYR;
    }
}

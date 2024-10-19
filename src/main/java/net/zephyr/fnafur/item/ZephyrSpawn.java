package net.zephyr.fnafur.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.zephyr.fnafur.init.entity_init.EntityInit;

public class ZephyrSpawn extends EntitySpawnItem {
    public ZephyrSpawn(Settings settings) {
        super(settings, ItemWithDescription.cpu);
    }

    @Override
    public EntityType<? extends LivingEntity> entity() {
        return EntityInit.ZEPHYR;
    }

    @Override
    public Item getCreativeItem() {
        return this;
    }
}

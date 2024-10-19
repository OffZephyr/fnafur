package net.zephyr.fnafur.item.spawn_items.classic;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.zephyr.fnafur.init.entity_init.ClassicInit;
import net.zephyr.fnafur.item.EntitySpawnItem;

public class cl_bon_SpawnItem extends EntitySpawnItem {
    public cl_bon_SpawnItem(Settings settings) {
        super(settings);
    }

    @Override
    public EntityType<? extends LivingEntity> entity() {
        return ClassicInit.CL_BON;
    }

    @Override
    public Item getCreativeItem() {
        NbtCompound nbt = new NbtCompound();
        return this;
    }
}

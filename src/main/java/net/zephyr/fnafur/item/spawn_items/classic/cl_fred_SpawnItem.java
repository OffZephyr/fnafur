package net.zephyr.fnafur.item.spawn_items.classic;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.zephyr.fnafur.init.entity_init.ClassicInit;
import net.zephyr.fnafur.item.EntitySpawnItem;
import net.zephyr.fnafur.util.ItemNbtUtil;

public class cl_fred_SpawnItem extends EntitySpawnItem {
    public cl_fred_SpawnItem(Settings settings) {
        super(settings);
    }

    @Override
    public EntityType<? extends LivingEntity> entity() {
        return ClassicInit.CL_FRED;
    }

    @Override
    public Item getCreativeItem() {
        NbtCompound nbt = new NbtCompound();
        return this;
    }
}

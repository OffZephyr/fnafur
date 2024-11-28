package net.zephyr.fnafur.item;

import com.google.common.collect.Maps;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EntitySpawnItem extends Item {
    private static final Map<EntityType<? extends LivingEntity>, EntitySpawnItem> SPAWN_ITEMS = Maps.newIdentityHashMap();
    public EntityType<? extends LivingEntity> ENTITY_TYPE;
    public EntitySpawnItem(Settings settings) {
        super(settings);
    }
    public EntitySpawnItem setEntity(EntityType<? extends LivingEntity> entity){
        ENTITY_TYPE = entity;
        SPAWN_ITEMS.put(ENTITY_TYPE, this);
        return this;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        NbtCompound nbt = ItemNbtUtil.getNbt(context.getStack());

        BlockPos pos = context.getBlockPos().up();
        float yaw = context.getPlayerYaw() + 180f;
        LivingEntity entity = ENTITY_TYPE.create(context.getWorld(), SpawnReason.TRIGGERED);
        entity.setPosition(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
        entity.setBodyYaw(yaw);
        entity.setHeadYaw(yaw);
        if(!nbt.isEmpty()) {
            ((IEntityDataSaver) entity).getPersistentData().copyFrom(nbt);
        }
        ((IEntityDataSaver)entity).getPersistentData().putLong("spawnPos", entity.getBlockPos().asLong());
        ((IEntityDataSaver)entity).getPersistentData().putFloat("spawnRot", yaw);
        context.getWorld().spawnEntity(entity);
        context.getStack().decrementUnlessCreative(1, context.getPlayer());
        if(entity.getWorld().isClient()){
            GoopyNetworkingUtils.saveEntityData(entity.getId(), ((IEntityDataSaver)entity).getPersistentData().copy());
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    public static EntitySpawnItem forEntity(@Nullable EntityType<?> type) {
        return SPAWN_ITEMS.get(type);
    }
}

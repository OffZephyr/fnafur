package net.zephyr.fnafur.item;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EntitySpawnItem extends Item {
    private static final Map<EntityType<? extends LivingEntity>, EntitySpawnItem> SPAWN_ITEMS = Maps.newIdentityHashMap();
    public EntityType<? extends LivingEntity> ENTITY_TYPE;
    public EntitySpawnItem(Properties settings) {
        super(settings);
    }
    public EntitySpawnItem setEntity(EntityType<? extends LivingEntity> entity){
        ENTITY_TYPE = entity;
        SPAWN_ITEMS.put(ENTITY_TYPE, this);
        return this;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        CompoundTag nbt = ItemUtil.getNbt(context.getItemInHand());

        BlockPos pos = context.getClickedPos().above();
        float yaw = context.getRotation() + 180f;
        LivingEntity entity = ENTITY_TYPE.create(context.getLevel(), EntitySpawnReason.TRIGGERED);
        entity.setPos(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
        entity.setYBodyRot(yaw);
        entity.setYHeadRot(yaw);
        if(!nbt.isEmpty()) {
            ((IEntityDataSaver) entity).getPersistentData().merge(nbt);
        }
        ((IEntityDataSaver)entity).getPersistentData().putLong("spawnPos", entity.blockPosition().asLong());
        ((IEntityDataSaver)entity).getPersistentData().putFloat("spawnRot", yaw);
        context.getLevel().addFreshEntity(entity);
        context.getItemInHand().consume(1, context.getPlayer());
        if(entity.level().isClientSide()){
            GoopyNetworkingUtils.saveEntityData(entity.getId(), ((IEntityDataSaver)entity).getPersistentData().copy());
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    public static EntitySpawnItem forEntity(@Nullable EntityType<?> type) {
        return SPAWN_ITEMS.get(type);
    }
}

package net.zephyr.fnafur.entity.cameramap;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.item_init.ItemInit;

public class CameraMappingEntity extends Entity {
    int delTimer = 10;
    public CameraMappingEntity(EntityType<? extends CameraMappingEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    public void tick() {
        if(!getWorld().isClient()){
            boolean delete = true;
            for(ServerPlayerEntity p : PlayerLookup.tracking(this)){
                boolean mapItem = p.getMainHandStack().isOf(ItemInit.TAPEMEASURE) || p.getMainHandStack().isOf(ItemInit.PAINTBRUSH);

                if(mapItem && p.getMainHandStack().getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getInt("mapEntityID") == getId()){
                    delete = false;
                    if(!this.getBoundingBox().contract(5).contains(p.getPos().getX(), p.getPos().getY(), p.getPos().getZ())){
                        this.updatePosition(p.getBlockPos().getX() + 0.5f,p.getBlockPos().getY() - 20, p.getBlockPos().getZ() + 0.5f);
                    }
                }
            }
            delTimer = delete ? delTimer - 1 : 10;
            if (delTimer <= 0) remove(RemovalReason.DISCARDED);
        }
        super.tick();
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}

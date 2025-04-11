package net.zephyr.fnafur.blocks.props.base;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.GoopyBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtS2CGetFromClientPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtS2CPongPayload;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class PropBlockEntity extends BlockEntity {
    public PropBlockEntity(BlockPos pos, BlockState state) {
        this(BlockEntityInit.PROPS, pos, state);
    }
    public PropBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick(World world, BlockPos blockPos, BlockState state, PropBlockEntity entity){
        if(!world.isClient()){
            if(!((IEntityDataSaver)entity).getPersistentData().getBoolean("synced")){
                for(ServerPlayerEntity p : PlayerLookup.all(entity.getWorld().getServer())){
                    ServerPlayNetworking.send(p, new UpdateBlockNbtS2CGetFromClientPayload(blockPos.asLong()));
                }
                ((IEntityDataSaver)entity).getPersistentData().putBoolean("synced", true);
            }
        }
    }
}

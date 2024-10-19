package net.zephyr.fnafur.blocks.stickers.base;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.GoopyBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtS2CGetFromClientPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtS2CPongPayload;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class StickerBlockEntity extends BlockEntity {
    public StickerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.STICKER_BLOCK, pos, state);
    }

    public void tick(World world, BlockPos blockPos, BlockState state, StickerBlockEntity entity){
        /*if(!world.isClient() && !((IEntityDataSaver)this).getPersistentData().isEmpty()){
            for(ServerPlayerEntity p : PlayerLookup.all(world.getServer())){
                ServerPlayNetworking.send(p, new UpdateBlockNbtS2CPongPayload(blockPos.asLong(), ((IEntityDataSaver)this).getPersistentData().copy()));
            }
        }*/

        if(!world.isClient()){
            for(ServerPlayerEntity p : PlayerLookup.all(world.getServer())){
                ServerPlayNetworking.send(p, new UpdateBlockNbtS2CPongPayload(blockPos.asLong(), ((IEntityDataSaver)this).getPersistentData().copy()));
            }
        }
        else {

        }

    }


}

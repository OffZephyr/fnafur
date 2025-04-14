package net.zephyr.fnafur.blocks.stickers_blocks;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtS2CGetFromClientPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtS2CPongPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class StickerBlockEntity extends BlockEntity {
    public StickerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.STICKER_BLOCK, pos, state);
    }
    public void tick(World world, BlockPos blockPos, BlockState state, StickerBlockEntity entity){
        if(!world.isClient()){
            if(!((IEntityDataSaver)entity).getPersistentData().getBoolean("synced")){
                for(ServerPlayerEntity p : PlayerLookup.all(entity.getWorld().getServer())){
                    ServerPlayNetworking.send(p, new UpdateBlockNbtS2CGetFromClientPayload(blockPos.asLong()));
                }
                ((IEntityDataSaver)entity).getPersistentData().putBoolean("synced", true);
            }
        }

        NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData();
        int holding = nbt.getInt("holding");
        int holdTime = nbt.getInt("holdTime");

        if(holding > 0) {
            ((IEntityDataSaver)this).getPersistentData().putInt("holding", Math.clamp(holding - 1, 0, holding));
        }
        else if (holdTime > 0){
            ((IEntityDataSaver)this).getPersistentData().putInt("holdTime", 0);
        }
    }
}

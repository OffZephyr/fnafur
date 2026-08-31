package net.zephyr.fnafur.blocks.stickers_blocks;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SGetFromServerPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class StickerBlockEntity extends BlockEntity {
    public StickerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.STICKER_BLOCK, pos, state);
    }
    public void tick(Level world, BlockPos blockPos, BlockState state, StickerBlockEntity entity){
        if(world.isClientSide()){
            if(!((IEntityDataSaver)entity).getPersistentData().contains("synced")){
                ClientPlayNetworking.send(new UpdateBlockNbtC2SGetFromServerPayload(getBlockPos().asLong()));
                world.setBlock(blockPos, world.getBlockState(blockPos), Block.UPDATE_ALL_IMMEDIATE);
            }
        }

        CompoundTag nbt = ((IEntityDataSaver)this).getPersistentData();
        int holding = nbt.getInt("holding").orElse(0);
        int holdTime = nbt.getInt("holdTime").orElse(0);

        if(holding > 0) {
            ((IEntityDataSaver)this).getPersistentData().putInt("holding", Math.clamp(holding - 1, 0, holding));
        }
        else if (holdTime > 0){
            ((IEntityDataSaver)this).getPersistentData().putInt("holdTime", 0);
        }

        if(world.isClientSide()){
            if(((IEntityDataSaver)this).getServerUpdateStatus()){
                //Minecraft.getInstance().player.sendMessage(Text.literal("SYNCING STICKER"), false);
                ClientPlayNetworking.send(new UpdateBlockNbtC2SPayload(getBlockPos().asLong(), ((IEntityDataSaver)this).getPersistentData()));
            }
        }
    }
}

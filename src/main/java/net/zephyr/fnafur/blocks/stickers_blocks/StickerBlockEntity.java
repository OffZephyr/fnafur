package net.zephyr.fnafur.blocks.stickers_blocks;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SGetFromServerPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class StickerBlockEntity extends BlockEntity {
    public StickerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.STICKER_BLOCK, pos, state);
    }
    public void tick(World world, BlockPos blockPos, BlockState state, StickerBlockEntity entity){
        if(world.isClient()){
            if(!((IEntityDataSaver)entity).getPersistentData().getBoolean("synced")){
                ClientPlayNetworking.send(new UpdateBlockNbtC2SGetFromServerPayload(getPos().asLong()));
                world.setBlockState(blockPos, world.getBlockState(blockPos), Block.NOTIFY_ALL_AND_REDRAW);
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

        if(world.isClient()){
            if(((IEntityDataSaver)this).getServerUpdateStatus()){
                //MinecraftClient.getInstance().player.sendMessage(Text.literal("SYNCING STICKER"), false);
                ClientPlayNetworking.send(new UpdateBlockNbtC2SPayload(getPos().asLong(), ((IEntityDataSaver)this).getPersistentData()));
            }
        }
    }
}

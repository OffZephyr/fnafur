package net.zephyr.fnafur.blocks.basic_blocks.illusion_block;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.stickers.base.StickerBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtS2CPongPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class MimicFrameBlockEntity extends BlockEntity {
    public MimicFrameBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.MIMIC_FRAME, pos, state);
    }

    public void tick(World world, BlockPos blockPos, BlockState state, MimicFrameBlockEntity entity){
        if(!world.isClient()){
            for(ServerPlayerEntity p : PlayerLookup.all(world.getServer())){
                ServerPlayNetworking.send(p, new UpdateBlockNbtS2CPongPayload(blockPos.asLong(), ((IEntityDataSaver)this).getPersistentData().copy()));
            }
        }
    }
}

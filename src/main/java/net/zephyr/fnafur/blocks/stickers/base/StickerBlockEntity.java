package net.zephyr.fnafur.blocks.stickers.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.GoopyBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class StickerBlockEntity extends BlockEntity {
    public StickerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.STICKER_BLOCK, pos, state);
    }

    public void tick(World world, BlockPos blockPos, BlockState state, StickerBlockEntity entity){
        if(world.isClient()) {
            if (!((IEntityDataSaver) entity).getPersistentData().getBoolean("Update")) {
                GoopyNetworkingUtils.getNbtFromServer(blockPos);
                ((IEntityDataSaver) entity).getPersistentData().putBoolean("Update", true);
            }
        }
    }
}

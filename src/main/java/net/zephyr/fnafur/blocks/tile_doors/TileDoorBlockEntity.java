package net.zephyr.fnafur.blocks.tile_doors;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.tile_doors.beta.TileDoorBlock;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtS2CGetFromClientPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtS2CPongPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class TileDoorBlockEntity extends BlockEntity {
    BlockState state;
    public double delta = 0;
    public TileDoorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.TILE_DOOR, pos, state);
        this.state = state;
    }

    public BlockState getDefaultState(){
        return this.state.getBlock().getDefaultState();
    }


    public void tick(World world, BlockPos pos, BlockState state, net.zephyr.fnafur.blocks.tile_doors.TileDoorBlockEntity blockEntity) {

    }

    public void setStatus(boolean open) {
        BlockPos mainPos = BlockPos.fromLong(((IEntityDataSaver) this).getPersistentData().getLong("main"));
        if (getWorld().getBlockEntity(mainPos) instanceof TileDoorBlockEntity entity) {
            ((IEntityDataSaver) entity).getPersistentData().putBoolean("open", open);
        }
    }
}

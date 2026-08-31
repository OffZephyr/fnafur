package net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.linking.links.LinkTargetBlockEntity;
import net.zephyr.fnafur.blocks.linking.links.energy.EnergyTargetBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.networking.sounds.PlayBlockSoundS2CPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class TileDoorBlockEntity extends EnergyTargetBlockEntity {
    BlockState state;
    public double delta = 0;

    public TileDoorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.TILE_DOOR, pos, state, state.getValue(TileDoorBlock.MAIN));
        this.state = state;
    }

    public BlockState getDefaultState(){
        return this.state.getBlock().defaultBlockState();
    }


    public void tick(Level world, BlockPos pos, BlockState state, net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors.TileDoorBlockEntity blockEntity) {
        if(!state.getValue(TileDoorBlock.MAIN) && LinkTargetBlockEntity.allTargets.contains((IEntityDataSaver)this)) LinkTargetBlockEntity.allTargets.remove((IEntityDataSaver) this);
    }

    @Override
    public void updateStatus(Level world, BlockPos sourcePos, IEntityDataSaver source){
        if(isReceivingPower() != ((IEntityDataSaver)this).getPersistentData().getBooleanOr("open", false)) {
            setStatus(isReceivingPower());
            BlockState state = world.getBlockState(sourcePos);
            world.updateNeighborsAt(worldPosition.above(), state.getBlock());
        }
    }

    public void setStatus(boolean open) {
        BlockPos mainPos = BlockPos.of(((IEntityDataSaver) this).getPersistentData().getLong("main").get());
        if (getLevel().getBlockEntity(mainPos) instanceof TileDoorBlockEntity entity) {
            BlockState state = getLevel().getBlockState(getBlockPos());
            int width = ((IEntityDataSaver) entity).getPersistentData().getInt("width").get();
            int height = ((IEntityDataSaver) entity).getPersistentData().getInt("height").get();

            BlockPos testPos = mainPos.relative(state.getValue(TileDoorBlock.FACING).getCounterClockWise());
            Direction direction = getLevel().getBlockState(testPos).getBlock() instanceof TileDoorBlock ? state.getValue(TileDoorBlock.FACING).getCounterClockWise() : state.getValue(TileDoorBlock.FACING).getClockWise();

            if (getLevel().getBlockState(mainPos).getBlock() instanceof TileDoorBlock block) {
                SoundEvent sound = open ? block.openSound : block.closeSound;
                BlockPos pos = mainPos.relative(direction, (int) (width / 2f));

                if (!getLevel().isClientSide()) {
                    for (ServerPlayer p : PlayerLookup.all(getLevel().getServer())) {
                        ServerPlayNetworking.send(p, new PlayBlockSoundS2CPayload(pos.asLong(), sound.location().getPath(), SoundSource.BLOCKS.getName(), 1f, 1f));
                    }
                }
            }


            for (int x = 0; x <= width; x++) {
                for (int y = 0; y <= height; y++) {
                    BlockPos updatePos = mainPos.above(y).relative(direction, x);

                    if (getLevel().getBlockEntity(updatePos) instanceof TileDoorBlockEntity entity2) {
                        ((IEntityDataSaver) entity2).getPersistentData().putBoolean("open", open);
                    }
                }
            }
        }
    }
}

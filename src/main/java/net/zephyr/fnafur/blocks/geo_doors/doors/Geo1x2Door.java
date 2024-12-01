package net.zephyr.fnafur.blocks.geo_doors.doors;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.geo_doors.GeoDoor;
import net.zephyr.fnafur.init.block_init.GeoBlockEntityInit;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Geo1x2Door extends GeoDoor {
    public Geo1x2Door(Settings settings) {
        super(settings);
    }

    @Override
    public List<BlockPos> doorPos(BlockState state, BlockPos origin){
        List<BlockPos> door = new ArrayList<>();
        door.add(origin);
        door.add(origin.up());
        return door;
    }

    @Override
    public Box getEntityArea(BlockState state, BlockPos pos) {
        Box box;
        if(state.get(GeoDoor.FACING).getAxis() == Direction.Axis.Z){
            box = new Box(pos.getX() - 0.5f, pos.getY() - 1, pos.getZ() - 1.5f, pos.getX() + 1.5f, pos.getY() + 2, pos.getZ() + 2.5f);
        }
        else {
            box = new Box(pos.getX() - 1.5f, pos.getY() - 1, pos.getZ() - 0.5f, pos.getX() + 2.5f, pos.getY() + 2, pos.getZ() + 1.5f);
        }
        return box;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, GeoBlockEntityInit.GEO_DOOR,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch(state.get(FACING).getAxis()){
            default -> VoxelShapes.cuboid(0.4f, 0, 0, 0.6f, 1, 1);
            case Z -> VoxelShapes.cuboid(0, 0, 0.4f, 1, 1, 0.6f);
        };
    }
}

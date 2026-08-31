package net.zephyr.fnafur.blocks.geo_doors.doors;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.geo_doors.GeoDoor;
import net.zephyr.fnafur.init.block_init.GeoBlockEntityInit;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Geo2x3Door extends GeoDoor {
    public Geo2x3Door(Properties settings) {
        super(settings);
    }

    @Override
    public List<BlockPos> doorPos(BlockState state, BlockPos origin){
        List<BlockPos> door = new ArrayList<>();
        Direction direction = state.getValue(FACING);
        door.add(origin);
        door.add(origin.above());
        door.add(origin.above().above());
        BlockPos offset = origin.relative(direction.getCounterClockWise());
        door.add(offset);
        door.add(offset.above());
        door.add(offset.above().above());
        return door;
    }

    @Override
    public AABB getEntityArea(BlockState state, BlockPos pos) {
        AABB box;
        if(state.getValue(GeoDoor.FACING).getAxis() == Direction.Axis.Z){
            box = new AABB(pos.getX() - 1f, pos.getY() - 1, pos.getZ() - 2.5f, pos.getX() + 3f, pos.getY() + 2, pos.getZ() + 3.5f);
        }
        else {
            box = new AABB(pos.getX() - 2.5f, pos.getY() - 1, pos.getZ() - 1f, pos.getX() + 3.5f, pos.getY() + 2, pos.getZ() + 3f);
        }
        return box;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, GeoBlockEntityInit.GEO_DOOR,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch(state.getValue(FACING).getAxis()){
            default -> Shapes.box(0.4f, 0, 0, 0.6f, 1, 1);
            case Z -> Shapes.box(0, 0, 0.4f, 1, 1, 0.6f);
        };
    }
}

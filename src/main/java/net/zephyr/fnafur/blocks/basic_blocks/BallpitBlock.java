package net.zephyr.fnafur.blocks.basic_blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BallpitBlock extends Grid2x2Block {
    private static final VoxelShape FALLING_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.85F, 1.0);

    public BallpitBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityShapeContext) {
            Entity entity = entityShapeContext.getEntity();
            if (entity != null) {
                if (entity.fallDistance > 1F) {
                    return FALLING_SHAPE;
                }
            }
        }

        return Shapes.empty();
    }
    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state) {
        return Shapes.empty();
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.is(this) || super.skipRendering(state, stateFrom,direction);
    }
    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return true;
    }
}

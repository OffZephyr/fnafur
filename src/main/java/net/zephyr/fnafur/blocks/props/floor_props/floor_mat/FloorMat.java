package net.zephyr.fnafur.blocks.props.floor_props.floor_mat;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.floor_props.floor_trash.FloorTrashSkins;
import org.lwjgl.system.Pointer;

public class FloorMat extends FloorPropBlock<DefaultPropColorEnum> {
    public FloorMat(Properties settings) { super(settings);
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.create(new AABB(-0.25, 0, -0.05, 1.25, 0.07, 1.05)));
        return drawingOutline ? shape : Shapes.block();
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.block();
    }


    @Override
    public boolean rotates() {
        return true;
    }
}

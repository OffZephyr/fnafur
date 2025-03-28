package net.zephyr.fnafur.blocks.props.floor_props.kitchen;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;

public class PizzaOven extends FloorPropBlock<DefaultPropColorEnum> {
    public PizzaOven(Settings settings) {
        super(settings);
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(-0.50f, 0f, -0.2f, 1.50f, 1.9f, 1.2f)));
        return drawingOutline ? shape : VoxelShapes.fullCube();
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }


    @Override
    public boolean rotates() {
        return true;
    }
}

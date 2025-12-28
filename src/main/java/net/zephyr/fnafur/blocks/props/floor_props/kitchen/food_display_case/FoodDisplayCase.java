package net.zephyr.fnafur.blocks.props.floor_props.kitchen.food_display_case;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;

public class FoodDisplayCase extends FloorPropBlock<FoodDisplayCaseColors> {
    public FoodDisplayCase(Settings settings) {
        super(settings);
    }

    @Override
    public Class<FoodDisplayCaseColors> COLOR_ENUM() {
        return FoodDisplayCaseColors.class;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(-0.2f, 0f, 0.1f, 1.2f, 0.7f, 0.9f)));
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

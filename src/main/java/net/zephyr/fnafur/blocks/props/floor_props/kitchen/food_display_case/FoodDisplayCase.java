package net.zephyr.fnafur.blocks.props.floor_props.kitchen.food_display_case;

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

public class FoodDisplayCase extends FloorPropBlock<FoodDisplayCaseColors> {
    public FoodDisplayCase(Properties settings) {
        super(settings);
    }

    @Override
    public Class<FoodDisplayCaseColors> COLOR_ENUM() {
        return FoodDisplayCaseColors.class;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.create(new AABB(-0.2f, 0f, 0.1f, 1.2f, 0.7f, 0.9f)));
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

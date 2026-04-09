package net.zephyr.fnafur.blocks.props.wall_props.fnaf1_rules;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;

public class FNaF1Rules extends WallPropBlock<FNaF1RulesTextures> {
    public FNaF1Rules(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(FACING)){
            default -> Shapes.box(0.1f, 0f, 0, 0.9f, 1.05f, 0.1f);
        };
        return drawingOutline ? shape : getInteractionShape(state, world, pos);
    }

    @Override
    public Class<FNaF1RulesTextures> COLOR_ENUM() {
        return FNaF1RulesTextures.class;
    }

    @Override
    public boolean lockY(BlockState state) {
        return false;
    }

    @Override
    public boolean goesOnFloor(BlockItemStateProperties state) {
        return false;
    }
}

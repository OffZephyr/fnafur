package net.zephyr.fnafur.blocks.props.wall_props.exit_sign_wall;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.WallHalfProperty;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.blocks.props.wall_props.restroom_sign.RestroomSignSkins;
import org.jetbrains.annotations.Nullable;

public class ExitSign extends WallPropBlock {
    public ExitSign(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape;
        if(state.getValue(HALF) == WallHalfProperty.CEILING){
            shape = switch (state.getValue(FACING)){
                default -> Shapes.box(0.1f, 0.2f, 0.45f, 0.9f, 1, 0.55f);
                case SOUTH -> Shapes.box(0.1f, 0.2f, 0.45f, 0.9f, 1, 0.55f);
                case WEST, EAST -> Shapes.box(0.45f, 0.2f, 0.1f, 0.55f, 1, 0.9f);
            };
        }
        else{
            shape = switch (state.getValue(FACING)){
                default -> Shapes.box(0.1f, 0.2f, 0, 0.9f, 0.8f, 0.1f);
                case SOUTH -> Shapes.box(0.1f, 0.2f, 0.9f, 0.9f, 0.8f, 1);
                case WEST -> Shapes.box(0, 0.2f, 0.1f, 0.1f, 0.8f, 0.9f);
                case EAST -> Shapes.box(0.9f, 0.2f, 0.1f, 1, 0.8f, 0.9f);
            };
        }
        return drawingOutline ? shape : getInteractionShape(state, world, pos);
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
    }

    @Override
    public boolean lockY(BlockState state) {
        return false;
    }

    @Override
    public boolean goesOnFloor(BlockItemStateProperties state) {
        return true;
    }
}

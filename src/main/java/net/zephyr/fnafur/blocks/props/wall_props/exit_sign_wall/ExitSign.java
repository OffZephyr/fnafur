package net.zephyr.fnafur.blocks.props.wall_props.exit_sign_wall;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.WallHalfProperty;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.blocks.props.wall_props.restroom_sign.RestroomSignSkins;
import org.jetbrains.annotations.Nullable;

public class ExitSign extends WallPropBlock {
    public ExitSign(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape;
        if(state.get(HALF) == WallHalfProperty.CEILING){
            shape = switch (state.get(FACING)){
                default -> VoxelShapes.cuboid(0.1f, 0.2f, 0.45f, 0.9f, 1, 0.55f);
                case SOUTH -> VoxelShapes.cuboid(0.1f, 0.2f, 0.45f, 0.9f, 1, 0.55f);
                case WEST, EAST -> VoxelShapes.cuboid(0.45f, 0.2f, 0.1f, 0.55f, 1, 0.9f);
            };
        }
        else{
            shape = switch (state.get(FACING)){
                default -> VoxelShapes.cuboid(0.1f, 0.2f, 0, 0.9f, 0.8f, 0.1f);
                case SOUTH -> VoxelShapes.cuboid(0.1f, 0.2f, 0.9f, 0.9f, 0.8f, 1);
                case WEST -> VoxelShapes.cuboid(0, 0.2f, 0.1f, 0.1f, 0.8f, 0.9f);
                case EAST -> VoxelShapes.cuboid(0.9f, 0.2f, 0.1f, 1, 0.8f, 0.9f);
            };
        }
        return drawingOutline ? shape : getRaycastShape(state, world, pos);
    }

    @Override
    public Class<RestroomSignSkins> COLOR_ENUM() {
        return null;
    }

    @Override
    public boolean lockY(BlockState state) {
        return false;
    }

    @Override
    public boolean goesOnFloor(BlockStateComponent state) {
        return true;
    }
}

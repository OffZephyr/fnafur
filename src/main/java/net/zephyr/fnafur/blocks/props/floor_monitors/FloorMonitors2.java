package net.zephyr.fnafur.blocks.props.floor_monitors;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;

public class FloorMonitors2 extends FloorPropBlock<FloorMonitorColors> {
    public static final EnumProperty<FloorMonitorColors> COLOR = EnumProperty.of("color", FloorMonitorColors.class);

    public FloorMonitors2(Settings settings) {
        super(settings);
    }

    @Override
    public EnumProperty<FloorMonitorColors> COLOR_PROPERTY() {
        return COLOR;
    }

    @Override
    public boolean rotates() {
        return false;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0.2, 0, 0.2, 0.8, 0.6, 0.8)));
        return drawingOutline ? shape : VoxelShapes.fullCube();
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    @Override
    public boolean isTransparent(BlockState state) { return true;}

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}

package net.zephyr.fnafur.blocks.props.floor_props.party_hats;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;

public class PartyHats extends FloorPropBlock<PartyHatsColors> {
    public PartyHats(Settings settings) {
        super(settings);
    }

    @Override
    public Class<PartyHatsColors> COLOR_ENUM() {
        return PartyHatsColors.class;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0.25, 0, 0.25, 0.75, 0.6, 0.75)));
        return drawingOutline ? shape : VoxelShapes.fullCube();
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }


    @Override
    public boolean rotates() {
        return false;
    }
}

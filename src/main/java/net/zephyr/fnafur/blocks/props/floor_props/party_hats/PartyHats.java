package net.zephyr.fnafur.blocks.props.floor_props.party_hats;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;

public class PartyHats extends FloorPropBlock<PartyHatsColors> {
    public PartyHats(Properties settings) {
        super(settings);
    }

    @Override
    public Class<PartyHatsColors> COLOR_ENUM() {
        return PartyHatsColors.class;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.create(new AABB(0.25, 0, 0.25, 0.75, 0.6, 0.75)));
        return drawingOutline ? shape : Shapes.block();
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.block();
    }


    @Override
    public boolean rotates() {
        return false;
    }
}

package net.zephyr.fnafur.blocks.props.floor_props.plushies;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;

public class BephPlushieBlock extends FloorPropBlock<DefaultPropColorEnum> {

    public BephPlushieBlock(Properties settings) {
        super(settings);
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
    }

    @Override
    public boolean rotates() {
        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.create(new AABB(0.25, 0.0, 0.35, 0.75, 0.5, 0.70)));
        shape = Shapes.or(shape, Shapes.create(new AABB(0.25, 0.5, 0.25, 0.75, 1, 0.75)));
        shape = Shapes.or(shape, Shapes.create(new AABB(0.4, 1, 0.4, 0.6, 1.25, 0.6)));
        return drawingOutline ? shape : Shapes.block();
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }
}

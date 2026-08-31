package net.zephyr.fnafur.blocks.props.floor_props.chairs;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.special.SeatBlock;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class WoodenChair extends FloorPropBlock<DefaultPropColorEnum> implements SeatBlock {
    public WoodenChair(Properties settings) {
        super(settings);
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if(!isUsed(world, pos) && player.getMainHandItem().isEmpty()){
            if(player.position().distanceTo(hit.getLocation()) < 1.25f) {
                player.startRiding(sit(player, pos));
                return InteractionResult.SUCCESS;
            }
        }

        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.create(new AABB(0f, 0f, 0f, 1f, 1f, 1f)));
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

    @Override
    public float getSittingOffset(Level world, BlockPos pos) {
        return 0.1f;
    }
    @Override
    public float getSittingHeight(Level world, BlockPos pos) {
        return 0.1f;
    }
}

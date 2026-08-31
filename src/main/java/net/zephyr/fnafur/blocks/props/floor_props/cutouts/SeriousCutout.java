package net.zephyr.fnafur.blocks.props.floor_props.cutouts;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import org.jetbrains.annotations.Nullable;

public class SeriousCutout extends FloorPropBlock<SeriousCutoutColors> {
    public SeriousCutout(Properties settings) {
        super(settings);
    }

    @Override
    public Class<SeriousCutoutColors> COLOR_ENUM() {
        return SeriousCutoutColors.class;
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        if(state.getValue(COLOR_PROPERTY()).getPlace() != null){
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), state.getValue(COLOR_PROPERTY()).getPlace(), SoundSource.BLOCKS, 1f, 1f);
        }
        super.affectNeighborsAfterRemoval(state, world, pos, moved);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if(player.getMainHandItem().isEmpty()){
            if(state.getValue(COLOR_PROPERTY()).getSound() != null){
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), state.getValue(COLOR_PROPERTY()).getSound(), SoundSource.BLOCKS, 1f, 1f);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        if(state.getValue(COLOR_PROPERTY()).getPlace() != null){
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), state.getValue(COLOR_PROPERTY()).getPlace(), SoundSource.BLOCKS, 1f, 1f);
        }

        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.create(new AABB(0, 0, 0, 1, 2, 1)));
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

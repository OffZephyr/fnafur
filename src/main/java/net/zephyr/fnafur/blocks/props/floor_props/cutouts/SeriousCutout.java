package net.zephyr.fnafur.blocks.props.floor_props.cutouts;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import org.jetbrains.annotations.Nullable;

public class SeriousCutout extends FloorPropBlock<SeriousCutoutColors> {
    public SeriousCutout(Settings settings) {
        super(settings);
    }

    @Override
    public Class<SeriousCutoutColors> COLOR_ENUM() {
        return SeriousCutoutColors.class;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(player.getMainHandStack().isEmpty()){
            if(state.get(COLOR_PROPERTY()).getSound() != null){
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), state.get(COLOR_PROPERTY()).getSound(), SoundCategory.BLOCKS, 1f, 1f, true);
                return ActionResult.SUCCESS;
            }
        }
        if(super.onUse(state, world, pos, player, hit) == ActionResult.SUCCESS){
            BlockState newState = state.cycle(COLOR_PROPERTY());
            if(newState.get(COLOR_PROPERTY()).getPlace() != null){
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), newState.get(COLOR_PROPERTY()).getPlace(), SoundCategory.BLOCKS, 1f, 1f, true);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        if(state.get(COLOR_PROPERTY()).getPlace() != null){
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), state.get(COLOR_PROPERTY()).getPlace(), SoundCategory.BLOCKS, 1f, 1f, true);
        }

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0, 0, 0, 1, 2, 1)));
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

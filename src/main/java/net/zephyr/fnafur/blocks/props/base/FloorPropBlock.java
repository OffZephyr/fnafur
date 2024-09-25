package net.zephyr.fnafur.blocks.props.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.ColorEnumInterface;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public abstract class FloorPropBlock<T extends Enum<T> & ColorEnumInterface & StringIdentifiable> extends PropBlock<T> {

    protected FloorPropBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        double x = ctx.getHitPos().getX() - ctx.getBlockPos().getX();
        double z = ctx.getHitPos().getZ() - ctx.getBlockPos().getZ();

        x = Math.clamp(x, 0, 1);
        z = Math.clamp(z, 0, 1);

        if(ctx.getPlayer().isSneaking()){
            x = Math.round(x / PropBlock.gridSnap) * PropBlock.gridSnap;
            z = Math.round(z / PropBlock.gridSnap) * PropBlock.gridSnap;
        }
        return getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(OFFSET_X, (int)(x * offset_grid_size))
                .with(OFFSET_Z, (int)(z * offset_grid_size));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(world.getBlockEntity(pos) != null && placer != null) {

            float rotation = placer.getHeadYaw();
            if(placer.isSneaking()){
                rotation = Math.round(rotation / angleSnap) * angleSnap;
            }
            ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().putFloat("Rotation", rotation);

        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if(COLOR_PROPERTY() != null) {
            builder.add(COLOR_PROPERTY());
        }
        builder.add(FACING);
        builder.add(OFFSET_X, OFFSET_Z);
    }
}

package net.zephyr.fnafur.blocks.props.base;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public abstract class FloorPropBlock<T extends Enum<T> & ColorEnumInterface & StringRepresentable> extends PropBlock<T> {

    protected FloorPropBlock(Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = defaultBlockState().getValue(FACING);
        return defaultBlockState()
                .setValue(FACING, facing);
    }

    @Override
    public boolean snapsVertically() {
        return true;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.getBlockEntity(pos) != null && placer instanceof Player player) {
            if (player.getMainHandItem() != null && player.getMainHandItem().getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof FloorPropBlock && world.isClientSide()) {
                    FloorPropBlock.drawingOutline = true;
                    HitResult blockHit = Minecraft.getInstance().hitResult;
                    if (blockHit.getType() == HitResult.Type.BLOCK && blockHit instanceof BlockHitResult blockHitResult) {

                        double x = blockHitResult.getLocation().x() - blockHitResult.getBlockPos().getX();
                        double y = blockHitResult.getLocation().y() - blockHitResult.getBlockPos().getY();
                        double z = blockHitResult.getLocation().z() - blockHitResult.getBlockPos().getZ();

                        y = blockHitResult.getDirection().getAxis() != Direction.Axis.Y ? y + 1 : y;

                        float rotation = player.getYHeadRot();

                        x = Math.clamp(x, 0, 1);
                        y = Math.clamp(y, 0, 1);
                        z = Math.clamp(z, 0, 1);

                        if (player.isShiftKeyDown()) {
                            x = Math.round(x / PropBlock.gridSnap) * PropBlock.gridSnap;
                            y = Math.round(y / PropBlock.gridSnap) * PropBlock.gridSnap;
                            z = Math.round(z / PropBlock.gridSnap) * PropBlock.gridSnap;

                            rotation = Math.round(rotation / angleSnap) * angleSnap;
                        }
                        ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().putFloat("Rotation", rotation);
                        ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().putDouble("xOffset", x);
                        ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().putDouble("yOffset", y);
                        ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().putDouble("zOffset", z);

                    }
                }
            }
        }
        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        if(COLOR_ENUM() != null) {
            builder.add(COLOR_PROPERTY());
        }
        builder.add(FACING);
    }
}

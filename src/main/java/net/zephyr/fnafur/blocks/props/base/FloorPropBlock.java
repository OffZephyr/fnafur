package net.zephyr.fnafur.blocks.props.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public abstract class FloorPropBlock<@Nullable T extends Enum<T> & ColorEnumInterface & StringIdentifiable> extends PropBlock<T> {

    protected FloorPropBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing = rotates() ? ctx.getHorizontalPlayerFacing().getOpposite() : getDefaultState().get(FACING);
        return getDefaultState()
                .with(FACING, facing);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.getBlockEntity(pos) != null && placer instanceof PlayerEntity player) {
            if (player.getMainHandStack() != null && player.getMainHandStack().getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof FloorPropBlock && world.isClient()) {
                    FloorPropBlock.drawingOutline = true;
                    HitResult blockHit = MinecraftClient.getInstance().crosshairTarget;
                    if (blockHit.getType() == HitResult.Type.BLOCK && blockHit instanceof BlockHitResult blockHitResult) {

                        double x = blockHitResult.getPos().getX() - blockHitResult.getBlockPos().getX();
                        double y = blockHitResult.getPos().getY() - blockHitResult.getBlockPos().getY();
                        double z = blockHitResult.getPos().getZ() - blockHitResult.getBlockPos().getZ();

                        float rotation = player.getHeadYaw();

                        x = Math.clamp(x, 0, 1);
                        y = Math.clamp(y, 0, 1);
                        z = Math.clamp(z, 0, 1);

                        if (player.isSneaking()) {
                            x = Math.round(x / PropBlock.gridSnap) * PropBlock.gridSnap;
                            z = Math.round(z / PropBlock.gridSnap) * PropBlock.gridSnap;

                            rotation = Math.round(rotation / angleSnap) * angleSnap;
                        }
                        ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().putFloat("Rotation", rotation);
                        ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().putDouble("xOffset", x);
                        ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().putDouble("zOffset", z);

                    }
                }
            }
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if(COLOR_ENUM() != null) {
            builder.add(COLOR_PROPERTY());
        }
        builder.add(FACING);
    }
}

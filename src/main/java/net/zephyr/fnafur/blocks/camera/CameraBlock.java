package net.zephyr.fnafur.blocks.camera;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CameraBlock extends BlockWithEntity {
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
    public static final BooleanProperty POWERED = LeverBlock.POWERED;
    public static final BooleanProperty CEILING = BooleanProperty.of("ceiling");
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public CameraBlock(Settings settings) {
        super(settings.luminance(Blocks.createLightLevelFromLitBlockState(15)));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CameraBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(LIT, false);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, CEILING, LIT, POWERED);
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            default -> Block.createCuboidShape(6, 6, 0, 10, 16, 12);
            case NORTH -> Block.createCuboidShape(6, 6, 4, 10, 16, 16);
            case EAST -> Block.createCuboidShape(0, 6, 6, 12, 16, 10);
            case WEST -> Block.createCuboidShape(4, 6, 6, 16, 16, 10);
        };
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return true;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        NbtCompound data = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData();
        data.putDouble("pitch", 0);
        data.putDouble("yaw", 0);
        data.putString("Name", Text.translatable("fnafur.screens.camera_tablet.default_name").getString());
        data.putBoolean("isUsed", false);
        data.putBoolean("Lit", false);
        data.putBoolean("Flashlight", true);
        data.putBoolean("Action", false);
        data.putBoolean("Active", true);
        data.putByte("NightVision", (byte)0);
        data.putByte("ModeX", (byte)0);
        data.putByte("ModeY", (byte)0);
        data.putDouble("minYaw", -30d);
        data.putDouble("maxYaw", 30d);
        data.putDouble("minPitch", -30d);
        data.putDouble("maxPitch", 30d);
        data.putFloat("panningXProgress", 0);
        data.putFloat("panningYProgress", 0);
        data.putBoolean("panningXReverse", false);
        data.putBoolean("panningYReverse", false);
        data.putByte("yawSpeed", (byte)4);
        data.putByte("pitchSpeed", (byte)4);
        data.putBoolean("Powered", false);
        data.putString("ActionName", Text.translatable("fnafur.screens.camera_tablet.default_action_name").getString());
        BlockPos oppositePos = pos.offset(state.get(FACING).getOpposite());
        world.setBlockState(pos, state.with(CEILING, !world.getBlockState(oppositePos).isOpaqueFullCube()), 3);
        super.onPlaced(world, pos, state, placer, itemStack);
    }


    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if(direction == state.get(FACING).getOpposite()){
            return state.with(CEILING, !neighborState.isOpaqueFullCube());
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        ItemStack itemStack = super.getPickStack(world, pos, state);
        world.getBlockEntity(pos, BlockEntityInit.CAMERA).ifPresent((blockEntity) -> {
            blockEntity.setStackNbt(itemStack, world.getRegistryManager());
        });
        return itemStack;
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        List<ItemStack> item = new ArrayList<>();
        item.add(getPickStack(builder.getWorld(), blockEntity.getPos(), state));
        return item;
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {

        return validateTicker(type, BlockEntityInit.CAMERA, (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) && (direction == state.get(FACING) || state.get(CEILING)) ? 15 : 0;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return false;
    }
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
        if (moved || !state.isOf(newState.getBlock())) {
            return;
        }
        if (state.get(POWERED) != newState.get(POWERED)) {
            this.updateNeighbors(state, world, pos);
        }
    }
    private void updateNeighbors(BlockState state, World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
        world.updateNeighborsAlways(pos.offset(state.get(FACING).getOpposite()), this);
    }
}

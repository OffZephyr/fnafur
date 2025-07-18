package net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.zephyr.fnafur.blocks.stickers_blocks.BlockWithSticker;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DiagonalMimicFrame extends BlockWithSticker {
    public static final BooleanProperty NORTH = BooleanProperty.of("n");
    public static final BooleanProperty NORTH_WEST = BooleanProperty.of("nw");
    public static final BooleanProperty NORTH_EAST = BooleanProperty.of("ne");
    public static final BooleanProperty WEST = BooleanProperty.of("w");
    public static final BooleanProperty EAST = BooleanProperty.of("e");
    public static final BooleanProperty SOUTH = BooleanProperty.of("s");
    public static final BooleanProperty SOUTH_WEST = BooleanProperty.of("sw");
    public static final BooleanProperty SOUTH_EAST = BooleanProperty.of("se");
    public static final Map<Direction, BooleanProperty> DIRECTION_MAP = Map.of(
            Direction.NORTH, DiagonalMimicFrame.NORTH,
            Direction.EAST, DiagonalMimicFrame.EAST,
            Direction.SOUTH, DiagonalMimicFrame.SOUTH,
            Direction.WEST, DiagonalMimicFrame.WEST
    );
    public static final Map<Direction, BooleanProperty> NEXT_MAP = Map.of(
            Direction.NORTH, DiagonalMimicFrame.NORTH_EAST,
            Direction.EAST, DiagonalMimicFrame.SOUTH_EAST,
            Direction.SOUTH, DiagonalMimicFrame.SOUTH_WEST,
            Direction.WEST, DiagonalMimicFrame.NORTH_WEST
    );

    public static boolean drawingOutline = false;
    public DiagonalMimicFrame(Settings settings) {
        super(settings);
        setDefaultState(
                getDefaultState()
                        .with(NORTH, false)
                        .with(NORTH_WEST, false)
                        .with(NORTH_EAST, false)
                        .with(WEST, false)
                        .with(EAST, false)
                        .with(SOUTH, false)
                        .with(SOUTH_WEST, false)
                        .with(SOUTH_EAST, false)
        );
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        BlockEntity entity = world.getBlockEntity(pos);
        ItemStack stack = player.getMainHandStack();
        Block currentBlock = null;
        if(entity != null) {
            NbtCompound nbt = ((IEntityDataSaver) entity).getPersistentData();
            if(nbt.contains("BlockData")) {
                ItemStack blockStack = nbt.get("BlockData", ItemStack.CODEC).orElse(ItemStack.EMPTY);
                if (blockStack.getItem() instanceof BlockItem blockItem) {
                    currentBlock = blockItem.getBlock();
                }
            }


            if (stack != null && stack.isOf(ItemInit.SCRAPER)) {
                if (currentBlock != null) {
                    nbt.remove("BlockData");
                    saveBlockTexture(
                            nbt,
                            world,
                            pos
                    );

                    if (!world.isClient()) {
                        world.playSound(null, pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1, 1.25f);
                    } else {
                        ((IEntityDataSaver) world.getBlockEntity(pos)).setServerUpdateStatus(true);
                    }
                    return ActionResult.SUCCESS;
                }
            }
            else if (stack != null && stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock().getDefaultState().isSolidBlock(world, pos)) {
                if (!(blockItem.getBlock() instanceof BlockWithSticker) && currentBlock == null) {

                    nbt.put("BlockData", ItemStack.CODEC, stack);
                    saveBlockTexture(
                            nbt,
                            world,
                            pos
                    );
                    world.updateListeners(pos, state, state, 3);

                    return ActionResult.SUCCESS;
                }

            }
        }

        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {

        VoxelShape shape1 = VoxelShapes.cuboid(0, 0, 0, 0.5f, 1, 0.5f);
        VoxelShape shape2 = VoxelShapes.cuboid(0.5f, 0, 0, 1, 1, 0.5f);
        VoxelShape shape3 = VoxelShapes.cuboid(0, 0, 0.5f, 0.5f, 1, 1);
        VoxelShape shape4 = VoxelShapes.cuboid(0.5f, 0, 0.5f, 1, 1, 1);

        boolean bl1 = (!state.get(NORTH) && !state.get(EAST) && state.get(SOUTH) && state.get(WEST));
        boolean bl2 = (!state.get(NORTH) && state.get(EAST) && state.get(SOUTH) && !state.get(WEST));
        boolean bl3 = (state.get(NORTH) && !state.get(EAST) && !state.get(SOUTH) && state.get(WEST));
        boolean bl4 = (state.get(NORTH) && state.get(EAST) && !state.get(SOUTH) && !state.get(WEST));

        VoxelShape shape = VoxelShapes.fullCube();
        if (bl1 || bl2 || bl3 || bl4) {
            shape = VoxelShapes.empty();
            if (!bl2) shape = VoxelShapes.union(shape, shape1);
            if (!bl1) shape = VoxelShapes.union(shape, shape2);
            if (!bl4) shape = VoxelShapes.union(shape, shape3);
            if (!bl3) shape = VoxelShapes.union(shape, shape4);
        }
        return shape;
    }

    public boolean isDiagonal(BlockState state){
        boolean bl1 = (!state.get(NORTH) && !state.get(EAST) && state.get(SOUTH) && state.get(WEST));
        boolean bl2 = (!state.get(NORTH) && state.get(EAST) && state.get(SOUTH) && !state.get(WEST));
        boolean bl3 = (state.get(NORTH) && !state.get(EAST) && !state.get(SOUTH) && state.get(WEST));
        boolean bl4 = (state.get(NORTH) && state.get(EAST) && !state.get(SOUTH) && !state.get(WEST));

        return bl1 || bl2 || bl3 || bl4;
    }

    public BooleanProperty getDiagonalDirection(BlockState state){
        boolean bl1 = (!state.get(NORTH) && !state.get(EAST) && state.get(SOUTH) && state.get(WEST));
        boolean bl2 = (!state.get(NORTH) && state.get(EAST) && state.get(SOUTH) && !state.get(WEST));
        boolean bl3 = (state.get(NORTH) && !state.get(EAST) && !state.get(SOUTH) && state.get(WEST));
        boolean bl4 = (state.get(NORTH) && state.get(EAST) && !state.get(SOUTH) && !state.get(WEST));

        if(bl1) return NORTH_EAST;
        if(bl2) return NORTH_WEST;
        if(bl3) return SOUTH_EAST;
        if(bl4) return SOUTH_WEST;

        return NORTH_EAST;
    }
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getNeighborState(ctx.getWorld(), ctx.getBlockPos(), true);
    }

    BlockState getNeighborState(World world, BlockPos checkPos, boolean updateNeighbors) {
        BlockState newState = getDefaultState();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (j == 0 && i == 0) continue;
                //boolean isDiagonalCheck = i != 0 && j != 0;
                BlockPos pos = checkPos.offset(Direction.Axis.Z, j).offset(Direction.Axis.X, i);
                BlockState checkState = world.getBlockState(pos);
                newState = newState.with(getDirectionProp(i, j), checkState.isFullCube(world, checkPos) || (checkState.isOf(BlockInit.MIMIC_FRAME_DIAGONAL)));
                if (updateNeighbors) world.updateNeighbors(pos, world.getBlockState(pos).getBlock());
            }
        }
        return newState;
    }

    BooleanProperty getDirectionProp(int x, int z) {
        if (x == -1) {
            if (z == -1) {
                return NORTH_WEST;
            } else if (z == 0) {
                return WEST;
            } else if (z == 1) {
                return SOUTH_WEST;
            }
        } else if (x == 0) {
            if (z == -1) {
                return NORTH;
            } else if (z == 1) {
                return SOUTH;
            }
        } else if (x == 1) {
            if (z == -1) {
                return NORTH_EAST;
            } else if (z == 0) {
                return EAST;
            } else if (z == 1) {
                return SOUTH_EAST;
            }
        }
        return NORTH;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        return getNeighborState((World) world, pos, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(NORTH, NORTH_EAST, NORTH_WEST, WEST, EAST, SOUTH, SOUTH_EAST, SOUTH_WEST));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BlockEntityInit.STICKER_BLOCK,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }

    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(this.asItem());
    }

    public void saveBlockTexture(NbtCompound nbt, World world, BlockPos pos){

        if(!world.isClient()) {
            world.playSound(null, pos, SoundEvents.BLOCK_COPPER_GRATE_PLACE, SoundCategory.BLOCKS, 1,1);
        }

        if(world.isClient()) {
            ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().copyFrom(nbt);
            ((IEntityDataSaver)world.getBlockEntity(pos)).setServerUpdateStatus(true);
        }

        world.setBlockState(pos, world.getBlockState(pos), Block.NOTIFY_ALL_AND_REDRAW);
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NOTIFY_ALL_AND_REDRAW);
    }

}

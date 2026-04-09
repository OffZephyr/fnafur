package net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.zephyr.fnafur.blocks.stickers_blocks.BlockWithSticker;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DiagonalMimicFrame extends BlockWithSticker {
    public static final BooleanProperty NORTH = BooleanProperty.create("n");
    public static final BooleanProperty NORTH_WEST = BooleanProperty.create("nw");
    public static final BooleanProperty NORTH_EAST = BooleanProperty.create("ne");
    public static final BooleanProperty WEST = BooleanProperty.create("w");
    public static final BooleanProperty EAST = BooleanProperty.create("e");
    public static final BooleanProperty SOUTH = BooleanProperty.create("s");
    public static final BooleanProperty SOUTH_WEST = BooleanProperty.create("sw");
    public static final BooleanProperty SOUTH_EAST = BooleanProperty.create("se");
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
    public DiagonalMimicFrame(Properties settings) {
        super(settings);
        registerDefaultState(
                defaultBlockState()
                        .setValue(NORTH, false)
                        .setValue(NORTH_WEST, false)
                        .setValue(NORTH_EAST, false)
                        .setValue(WEST, false)
                        .setValue(EAST, false)
                        .setValue(SOUTH, false)
                        .setValue(SOUTH_WEST, false)
                        .setValue(SOUTH_EAST, false)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        BlockEntity entity = world.getBlockEntity(pos);
        ItemStack stack = player.getMainHandItem();
        Block currentBlock = null;
        if(entity != null) {
            CompoundTag nbt = ((IEntityDataSaver) entity).getPersistentData();
            if(nbt.contains("BlockData")) {
                ItemStack blockStack = nbt.read("BlockData", ItemStack.CODEC).orElse(ItemStack.EMPTY);
                if (blockStack.getItem() instanceof BlockItem blockItem) {
                    currentBlock = blockItem.getBlock();
                }
            }


            if (stack != null && stack.is(ItemInit.SCRAPER)) {
                if (currentBlock != null) {
                    nbt.remove("BlockData");
                    saveBlockTexture(
                            nbt,
                            world,
                            pos
                    );

                    if (!world.isClientSide()) {
                        world.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1, 1.25f);
                    } else {
                        ((IEntityDataSaver) world.getBlockEntity(pos)).setServerUpdateStatus(true);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            else if (stack != null && stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock().defaultBlockState().isRedstoneConductor(world, pos)) {
                if (!(blockItem.getBlock() instanceof BlockWithSticker) && currentBlock == null) {

                    nbt.store("BlockData", ItemStack.CODEC, stack);
                    saveBlockTexture(
                            nbt,
                            world,
                            pos
                    );
                    world.sendBlockUpdated(pos, state, state, 3);

                    return InteractionResult.SUCCESS;
                }

            }
        }

        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

        VoxelShape shape1 = Shapes.box(0, 0, 0, 0.5f, 1, 0.5f);
        VoxelShape shape2 = Shapes.box(0.5f, 0, 0, 1, 1, 0.5f);
        VoxelShape shape3 = Shapes.box(0, 0, 0.5f, 0.5f, 1, 1);
        VoxelShape shape4 = Shapes.box(0.5f, 0, 0.5f, 1, 1, 1);

        boolean bl1 = (!state.getValue(NORTH) && !state.getValue(EAST) && state.getValue(SOUTH) && state.getValue(WEST));
        boolean bl2 = (!state.getValue(NORTH) && state.getValue(EAST) && state.getValue(SOUTH) && !state.getValue(WEST));
        boolean bl3 = (state.getValue(NORTH) && !state.getValue(EAST) && !state.getValue(SOUTH) && state.getValue(WEST));
        boolean bl4 = (state.getValue(NORTH) && state.getValue(EAST) && !state.getValue(SOUTH) && !state.getValue(WEST));

        VoxelShape shape = Shapes.block();
        if (bl1 || bl2 || bl3 || bl4) {
            shape = Shapes.empty();
            if (!bl2) shape = Shapes.or(shape, shape1);
            if (!bl1) shape = Shapes.or(shape, shape2);
            if (!bl4) shape = Shapes.or(shape, shape3);
            if (!bl3) shape = Shapes.or(shape, shape4);
        }
        return shape;
    }

    public boolean isDiagonal(BlockState state){
        boolean bl1 = (!state.getValue(NORTH) && !state.getValue(EAST) && state.getValue(SOUTH) && state.getValue(WEST));
        boolean bl2 = (!state.getValue(NORTH) && state.getValue(EAST) && state.getValue(SOUTH) && !state.getValue(WEST));
        boolean bl3 = (state.getValue(NORTH) && !state.getValue(EAST) && !state.getValue(SOUTH) && state.getValue(WEST));
        boolean bl4 = (state.getValue(NORTH) && state.getValue(EAST) && !state.getValue(SOUTH) && !state.getValue(WEST));

        return bl1 || bl2 || bl3 || bl4;
    }

    public BooleanProperty getDiagonalDirection(BlockState state){
        boolean bl1 = (!state.getValue(NORTH) && !state.getValue(EAST) && state.getValue(SOUTH) && state.getValue(WEST));
        boolean bl2 = (!state.getValue(NORTH) && state.getValue(EAST) && state.getValue(SOUTH) && !state.getValue(WEST));
        boolean bl3 = (state.getValue(NORTH) && !state.getValue(EAST) && !state.getValue(SOUTH) && state.getValue(WEST));
        boolean bl4 = (state.getValue(NORTH) && state.getValue(EAST) && !state.getValue(SOUTH) && !state.getValue(WEST));

        if(bl1) return NORTH_EAST;
        if(bl2) return NORTH_WEST;
        if(bl3) return SOUTH_EAST;
        if(bl4) return SOUTH_WEST;

        return NORTH_EAST;
    }
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return getNeighborState(ctx.getLevel(), ctx.getClickedPos(), true);
    }

    BlockState getNeighborState(Level world, BlockPos checkPos, boolean updateNeighbors) {
        BlockState newState = defaultBlockState();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (j == 0 && i == 0) continue;
                //boolean isDiagonalCheck = i != 0 && j != 0;
                BlockPos pos = checkPos.relative(Direction.Axis.Z, j).relative(Direction.Axis.X, i);
                BlockState checkState = world.getBlockState(pos);
                newState = newState.setValue(getDirectionProp(i, j), checkState.isCollisionShapeFullBlock(world, checkPos) || (checkState.is(BlockInit.MIMIC_FRAME_DIAGONAL)));
                if (updateNeighbors) world.updateNeighborsAt(pos, world.getBlockState(pos).getBlock());
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
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return getNeighborState((Level) world, pos, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(NORTH, NORTH_EAST, NORTH_WEST, WEST, EAST, SOUTH, SOUTH_EAST, SOUTH_WEST));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityInit.STICKER_BLOCK,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(this.asItem());
    }

    public void saveBlockTexture(CompoundTag nbt, Level world, BlockPos pos){

        if(!world.isClientSide()) {
            world.playSound(null, pos, SoundEvents.COPPER_GRATE_PLACE, SoundSource.BLOCKS, 1,1);
        }

        if(world.isClientSide()) {
            ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().merge(nbt);
            ((IEntityDataSaver)world.getBlockEntity(pos)).setServerUpdateStatus(true);
        }

        world.setBlock(pos, world.getBlockState(pos), Block.UPDATE_ALL_IMMEDIATE);
        world.sendBlockUpdated(pos, world.getBlockState(pos), world.getBlockState(pos), Block.UPDATE_ALL_IMMEDIATE);
    }

}

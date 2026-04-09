package net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.redstone.Orientation;
import net.zephyr.fnafur.blocks.dynamic.tiling.VerticalTileStates;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.item.tools.WrenchItem;
import net.zephyr.fnafur.networking.block.TileDoorUpdateS2CPayload;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class TileDoorBlock extends BaseEntityBlock {
    public SoundEvent openSound;
    public SoundEvent closeSound;

    public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);
    public static final BooleanProperty MAIN = BooleanProperty.create("main");
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public TileDoorDirection doorDirection;
    public boolean isInverted;
    public static final EnumProperty<VerticalTileStates> TYPE = EnumProperty.create("type", VerticalTileStates.class);
    public TileDoorBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(OPEN, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        CompoundTag nbt = ItemUtil.getNbt(itemStack);

        if(nbt.contains("pos2") && placer != null) {
            BlockPos pos1 = BlockPos.of(nbt.getLong("pos1").get());
            BlockPos pos2 = BlockPos.of(nbt.getLong("pos2").get());

            Direction dir = placer.getDirection().getOpposite();
            Vec3i distance = TileDoorItem.getDistance(pos1, pos2, dir);

            int h = Math.clamp(Math.max(distance.getX(), distance.getZ()), 0, 16);
            int v = Math.clamp(distance.getY(), 0, 16);

            Vec3i minPos = TileDoorItem.getMin(pos1, pos2);
            if(minPos.getY() < pos1.getY()) v = 0;

            for(int x = 0; x <= h; x++){
                for(int y = 0; y <= v; y++){

                    BlockPos placePos = new BlockPos(
                            h == distance.getZ() ? pos1.getX() : minPos.getX() + (Math.abs(dir.getUnitVec3i().getZ()) * x),
                            pos1.getY() + y,
                            h == distance.getX() ? pos1.getZ() :minPos.getZ() + (Math.abs(dir.getUnitVec3i().getX()) * x)
                    );

                    if(world.isUnobstructed(state.setValue(MAIN, placePos.equals(pos1)), placePos, CollisionContext.of(placer))){

                        world.setBlock(placePos, state.setValue(OPEN, isInverted).setValue(MAIN, placePos.equals(pos1)).setValue(TYPE, VerticalTileStates.get(y != v, x != h, y != 0, x != 0)), Block.UPDATE_CLIENTS);

                        if(world.getBlockEntity(placePos) instanceof BlockEntity ent){
                            ((IEntityDataSaver)ent).getPersistentData().putBoolean("inverted", isInverted);
                            ((IEntityDataSaver)ent).getPersistentData().putString("direction", doorDirection.getSerializedName());

                            ((IEntityDataSaver)ent).getPersistentData().putLong("main", pos1.asLong());
                            ((IEntityDataSaver)ent).getPersistentData().putInt("width", h);
                            ((IEntityDataSaver)ent).getPersistentData().putInt("height", v);
                            ((IEntityDataSaver)ent).getPersistentData().putFloat("speed", 2);
                        }
                    }
                }
            }

            nbt.remove("pos1");
            nbt.remove("pos2");
            ItemUtil.setNbt(itemStack, nbt.copy());
        }

        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    @Override
    protected void attack(BlockState state, Level world, BlockPos pos, Player player) {
        super.attack(state, world, pos, player);
    }
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if (world.getBlockEntity(pos) instanceof TileDoorBlockEntity ent) {
            BlockPos mainPos = BlockPos.of(((IEntityDataSaver) ent).getPersistentData().getLong("main").orElse(0L));
            if (world.getBlockEntity(mainPos) instanceof TileDoorBlockEntity ent2) {

                InteractionResult result2 = ent2.tryEndLink(player, world, ent2.getBlockPos());
                if (result2 != null) return result2;
                
            }
        }

        if(stack.getItem() instanceof WrenchItem){
            if(!world.isClientSide()) {

                if (world.getBlockEntity(pos) instanceof TileDoorBlockEntity ent) {
                    BlockPos mainPos = BlockPos.of(((IEntityDataSaver) ent).getPersistentData().getLong("main").orElse(0L));
                    if (world.getBlockEntity(mainPos) instanceof TileDoorBlockEntity ent2) {
                        GoopyNetworkingUtils.setScreen(player, "door_edit", ((IEntityDataSaver) ent2).getPersistentData(), mainPos);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if(world.getBlockEntity(pos) instanceof BlockEntity ent){
            BlockPos mainPos = BlockPos.of(((IEntityDataSaver)ent).getPersistentData().getLong("main").orElse(0L));
            int width = ((IEntityDataSaver)ent).getPersistentData().getInt("width").orElse(0);
            int height = ((IEntityDataSaver)ent).getPersistentData().getInt("height").orElse(0);


            BlockPos testPos = mainPos.relative(state.getValue(TileDoorBlock.FACING).getCounterClockWise());
            Direction direction = world.getBlockState(testPos).getBlock() instanceof TileDoorBlock ? state.getValue(TileDoorBlock.FACING).getCounterClockWise() : state.getValue(TileDoorBlock.FACING).getClockWise();

            for(int x = 0; x <= width; x++){
                for(int y = 0; y <= height; y++){
                    BlockPos breakPos = mainPos.above(y).relative(direction, x);
                    world.setBlockAndUpdate(breakPos, Blocks.AIR.defaultBlockState());
                }
            }
        }

        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {

        //if(!world.isClient()) {
        //    world.playSound(null, pos.toCenterPos().getX(), pos.toCenterPos().getY(), pos.toCenterPos().getZ(), openSound, SoundCategory.MASTER, 1, 1);
        //}

        if (world.getBlockEntity(pos) instanceof TileDoorBlockEntity ent) {
            BlockPos mainPos = BlockPos.of(((IEntityDataSaver) ent).getPersistentData().getLong("main").orElse(0L));

            if(world.getBlockEntity(mainPos) instanceof TileDoorBlockEntity ent2) {

                int width = ((IEntityDataSaver) ent2).getPersistentData().getInt("width").orElse(0);
                int height = ((IEntityDataSaver) ent2).getPersistentData().getInt("height").orElse(0);

                BlockPos testPos = mainPos.relative(state.getValue(TileDoorBlock.FACING).getCounterClockWise());
                Direction direction = world.getBlockState(testPos).getBlock() instanceof TileDoorBlock ? state.getValue(TileDoorBlock.FACING).getCounterClockWise() : state.getValue(TileDoorBlock.FACING).getClockWise();

                boolean powered = ((IEntityDataSaver) ent2).getPersistentData().getBoolean("open").orElse(false);

                boolean inverted = ((IEntityDataSaver) ent2).getPersistentData().getBoolean("inverted").orElse(isInverted);
                powered = inverted != powered;
                world.setBlock(mainPos, world.getBlockState(mainPos).setValue(OPEN, powered), 0);

                for (int x = 0; x <= width; x++) {
                    for (int y = 0; y <= height; y++) {
                        BlockPos updatePos = mainPos.above(y).relative(direction, x);

                        boolean connectUp = world.getBlockState(updatePos.above()).is(state.getBlock());
                        boolean connectRight = world.getBlockState(updatePos.relative(state.getValue(FACING).getClockWise())).is(state.getBlock());
                        boolean connectDown = world.getBlockState(updatePos.below()).is(state.getBlock());
                        boolean connectLeft = world.getBlockState(updatePos.relative(state.getValue(FACING).getCounterClockWise())).is(state.getBlock());

                        world.setBlock(updatePos, world.getBlockState(updatePos).setValue(MAIN, updatePos.equals(mainPos)).setValue(OPEN, powered).setValue(TYPE, VerticalTileStates.get(connectUp, connectRight, connectDown, connectLeft)), 0);
                        for (ServerPlayer p : PlayerLookup.all(world.getServer())) {
                            ServerPlayNetworking.send(p, new TileDoorUpdateS2CPayload(updatePos.asLong(), powered));
                        }

                    }
                }
            }
        }
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileDoorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityInit.TILE_DOOR,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, OPEN, MAIN, TYPE));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx).setValue(FACING, ctx.getHorizontalDirection().getOpposite());

        boolean connectUp = ctx.getLevel().getBlockState(ctx.getClickedPos().above()).is(state.getBlock());
        boolean connectRight = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(state.getValue(FACING).getClockWise())).is(state.getBlock());
        boolean connectDown = ctx.getLevel().getBlockState(ctx.getClickedPos().below()).is(state.getBlock());
        boolean connectLeft = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(state.getValue(FACING).getCounterClockWise())).is(state.getBlock());

        return state.setValue(TYPE, VerticalTileStates.get(connectUp, connectRight, connectDown, connectLeft));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING).getAxis()){
            case X -> Shapes.box(0.35f, 0, 0, 0.65f, 1, 1);
            default -> Shapes.box(0, 0, 0.35f, 1, 1, 0.65f);
        };
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(OPEN) ? Shapes.empty() : getShape(state, world, pos, context);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return state.getValue(OPEN);
    }

    public TileDoorBlock setDirection(TileDoorDirection direction) {
        this.doorDirection = direction;
        return this;
    }
    public TileDoorBlock setInverted(boolean inverted) {
        this.isInverted = inverted;
        return this;
    }
    public TileDoorBlock setOpenCloseSounds(SoundEvent openSound, SoundEvent closeSound) {
        this.openSound = openSound;
        this.closeSound = closeSound;
        return this;
    }
}

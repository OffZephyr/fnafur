package net.zephyr.fnafur.blocks.tile_doors;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import net.zephyr.fnafur.client.rendering.TileDoorPlacingRenderer;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.networking.block.TileDoorUpdateS2CPayload;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class TileDoorBlock extends BlockWithEntity {
    public static final EnumProperty<Direction> FACING = EnumProperty.of("facing", Direction.class);
    public static final BooleanProperty MAIN = BooleanProperty.of("main");
    public static final BooleanProperty OPEN = BooleanProperty.of("open");
    public TileDoorBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(OPEN, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        NbtCompound nbt = ItemNbtUtil.getNbt(itemStack);

        if(nbt.contains("pos2") && placer != null) {
            BlockPos pos1 = BlockPos.fromLong(nbt.getLong("pos1"));
            BlockPos pos2 = BlockPos.fromLong(nbt.getLong("pos2"));

            Direction dir = placer.getHorizontalFacing().getOpposite();
            Vec3i distance = TileDoorPlacingRenderer.getDistance(pos1, pos2, dir);

            int h = Math.clamp(Math.max(distance.getX(), distance.getZ()), 0, 16);
            int v = Math.clamp(distance.getY(), 0, 16);

            Vec3i minPos = TileDoorPlacingRenderer.getMin(pos1, pos2);

            for(int x = 0; x <= h; x++){
                for(int y = 0; y <= v; y++){

                    BlockPos placePos = new BlockPos(
                            h == distance.getZ() ? pos1.getX() : minPos.getX() + (Math.abs(dir.getVector().getZ()) * x),
                            minPos.getY() + y,
                            h == distance.getX() ? pos1.getZ() :minPos.getZ() + (Math.abs(dir.getVector().getX()) * x)
                    );

                    if(world.canPlace(state.with(MAIN, placePos.equals(pos1)), placePos, ShapeContext.of(placer))){
                        world.setBlockState(placePos, state.with(MAIN, placePos.equals(pos1)),Block.NOTIFY_LISTENERS);

                        if(world.getBlockEntity(placePos) instanceof BlockEntity ent){
                            ((IEntityDataSaver)ent).getPersistentData().putLong("main", pos1.asLong());
                            ((IEntityDataSaver)ent).getPersistentData().putInt("width", h);
                            ((IEntityDataSaver)ent).getPersistentData().putInt("height", v);
                        }
                    }
                }
            }

            nbt.remove("pos1");
            nbt.remove("pos2");
            ItemNbtUtil.setNbt(itemStack, nbt.copy());
        }

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if(world.getBlockEntity(pos) instanceof BlockEntity ent){
            BlockPos mainPos = BlockPos.fromLong(((IEntityDataSaver)ent).getPersistentData().getLong("main"));
            int width = ((IEntityDataSaver)ent).getPersistentData().getInt("width");
            int height = ((IEntityDataSaver)ent).getPersistentData().getInt("height");

            for(int x = 0; x <= width; x++){
                for(int y = 0; y <= height; y++){
                    BlockPos breakPos = mainPos.up(y).offset(state.get(FACING).rotateYCounterclockwise(), x);
                    world.setBlockState(breakPos, Blocks.AIR.getDefaultState());
                }
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {

        if (world.getBlockEntity(pos) instanceof TileDoorBlockEntity ent) {
            BlockPos mainPos = BlockPos.fromLong(((IEntityDataSaver) ent).getPersistentData().getLong("main"));

            if(world.getBlockEntity(mainPos) instanceof TileDoorBlockEntity ent2) {
                boolean powered = world.isReceivingRedstonePower(pos);

                int width = ((IEntityDataSaver) ent2).getPersistentData().getInt("width");
                int height = ((IEntityDataSaver) ent2).getPersistentData().getInt("height");

                world.setBlockState(mainPos, world.getBlockState(mainPos).with(OPEN, powered), 0);

                for (int x = 0; x <= width; x++) {
                    for (int y = 0; y <= height; y++) {
                        BlockPos updatePos = mainPos.up(y).offset(state.get(FACING).rotateYCounterclockwise(), x);
                        world.setBlockState(updatePos, world.getBlockState(updatePos).with(MAIN, updatePos.equals(mainPos)).with(OPEN, powered), 0);
                        for (ServerPlayerEntity p : PlayerLookup.all(world.getServer())) {
                            ServerPlayNetworking.send(p, new TileDoorUpdateS2CPayload(updatePos.asLong(), powered));
                        }

                    }
                }
            }
        }
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TileDoorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BlockEntityInit.TILE_DOOR,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(FACING, OPEN, MAIN));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING).getAxis()){
            case X -> VoxelShapes.cuboid(0.35f, 0, 0, 0.65f, 1, 1);
            default -> VoxelShapes.cuboid(0, 0, 0.35f, 1, 1, 0.65f);
        };
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(OPEN) ? VoxelShapes.empty() : getOutlineShape(state, world, pos, context);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return !state.get(OPEN);
    }
}

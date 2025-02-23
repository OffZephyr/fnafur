package net.zephyr.fnafur.blocks.props.other.pirates_cove.stage;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.Direction;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.block_init.GeoBlockInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class PiratesCoveStage extends BlockWithEntity {
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty MAIN = BooleanProperty.of("main");

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(FACING).add(MAIN));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(MAIN, true);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        BlockEntity entity2 = world.getBlockEntity(pos);
        ((IEntityDataSaver) entity2).getPersistentData().putLong("center", pos.asLong());

        for(int i = -2; i < 3; i++){
            for(int j = -2; j < 3; j++){
                BlockPos pos2 = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ() + j);
                if(world.getBlockState(pos2).isAir()) {
                    world.setBlockState(pos2, state.with(MAIN, false));
                    BlockEntity entity = world.getBlockEntity(pos2);
                    if(entity instanceof PiratesCoveStageBlockEntity) {
                        ((IEntityDataSaver) entity).getPersistentData().putLong("center", pos.asLong());
                    }
                }
            }
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos pos2 = BlockPos.fromLong(((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getLong("center"));
        clearStage(world, pos2);
        return super.onBreak(world, pos, state, player);
    }

    public static void clearStage(World world, BlockPos pos){
        for(int i = -2; i < 3; i++){
            for(int j = -2; j < 3; j++){
                BlockPos pos2 = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ() + j);
                if(world.getBlockState(pos2).isOf(GeoBlockInit.PIRATES_COVE_STAGE)) {
                    world.setBlockState(pos2, Blocks.AIR.getDefaultState());
                }
            }
        }
    }
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0, 0, 0, 1, 0.5f, 1);
    }

    public PiratesCoveStage(Settings settings) {
        super(settings);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PiratesCoveStageBlockEntity(pos, state);
    }
}

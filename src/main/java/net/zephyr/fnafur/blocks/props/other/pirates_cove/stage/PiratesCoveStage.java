package net.zephyr.fnafur.blocks.props.other.pirates_cove.stage;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.init.block_init.GeoBlockInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class PiratesCoveStage extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty MAIN = BooleanProperty.create("main");

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING).add(MAIN));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(MAIN, true);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        BlockEntity entity2 = world.getBlockEntity(pos);
        ((IEntityDataSaver) entity2).getPersistentData().putLong("center", pos.asLong());

        for(int i = -2; i < 3; i++){
            for(int j = -2; j < 3; j++){
                BlockPos pos2 = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ() + j);
                if(world.getBlockState(pos2).isAir()) {
                    world.setBlockAndUpdate(pos2, state.setValue(MAIN, false));
                    BlockEntity entity = world.getBlockEntity(pos2);
                    if(entity instanceof PiratesCoveStageBlockEntity) {
                        ((IEntityDataSaver) entity).getPersistentData().putLong("center", pos.asLong());
                    }
                }
            }
        }
        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        BlockPos pos2 = BlockPos.of(((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getLong("center").orElse(0L));
        clearStage(world, pos2);
        return super.playerWillDestroy(world, pos, state, player);
    }

    public static void clearStage(Level world, BlockPos pos){
        for(int i = -2; i < 3; i++){
            for(int j = -2; j < 3; j++){
                BlockPos pos2 = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ() + j);
                if(world.getBlockState(pos2).is(GeoBlockInit.PIRATES_COVE_STAGE)) {
                    world.setBlockAndUpdate(pos2, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.box(0, 0, 0, 1, 0.5f, 1);
    }

    public PiratesCoveStage(Properties settings) {
        super(settings);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PiratesCoveStageBlockEntity(pos, state);
    }
}

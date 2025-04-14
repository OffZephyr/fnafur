package net.zephyr.fnafur.blocks.props.other.hanging_stars;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.zephyr.fnafur.init.item_init.ItemInit;
import org.jetbrains.annotations.Nullable;

public class HangingStarsBlock extends HorizontalFacingBlock {
    public static final EnumProperty<HangingStarSkins> COLOR = EnumProperty.of("color", HangingStarSkins.class);
    public static final BooleanProperty STRING = BooleanProperty.of("string");
    public HangingStarsBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState lowState = ctx.getWorld().getBlockState(ctx.getBlockPos().down());

        if(lowState.getBlock() instanceof HangingStarsBlock){
            return lowState.with(STRING, true);
        }

        return super.getPlacementState(ctx).with(STRING, false).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {

        BlockState lowState = world.getBlockState(pos.down());

        if(lowState.getBlock() instanceof HangingStarsBlock){
            return lowState.with(STRING, true);
        }

        return state.with(STRING, false);
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!state.get(STRING) && stack.isOf(ItemInit.PAINTBRUSH)){
             world.setBlockState(pos, state.cycle(COLOR));
             return ActionResult.SUCCESS;
        }

        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(FACING, COLOR, STRING));
    }

    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = super.getPickStack(world, pos, state, includeData);

        BlockStateComponent component = BlockStateComponent.DEFAULT;

        if(state.contains(COLOR)) {
            component = component.with(COLOR, state.get(COLOR));
        }
        itemStack.set(DataComponentTypes.BLOCK_STATE, component);

        return itemStack;
    }
}

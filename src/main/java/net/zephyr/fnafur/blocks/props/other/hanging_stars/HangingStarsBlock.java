package net.zephyr.fnafur.blocks.props.other.hanging_stars;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.zephyr.fnafur.client.gui.screens.editing.PaintbrushAltPickerScreen;
import net.zephyr.fnafur.init.item_init.ItemInit;
import org.jetbrains.annotations.Nullable;

public class HangingStarsBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<HangingStarSkins> COLOR = EnumProperty.create("color", HangingStarSkins.class);
    public static final BooleanProperty STRING = BooleanProperty.create("string");
    public HangingStarsBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState lowState = ctx.getLevel().getBlockState(ctx.getClickedPos().below());

        if(lowState.getBlock() instanceof HangingStarsBlock){
            return lowState.setValue(STRING, true);
        }

        return super.getStateForPlacement(ctx).setValue(STRING, false).setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {

        BlockState lowState = world.getBlockState(pos.below());

        if(lowState.getBlock() instanceof HangingStarsBlock){
            return lowState.setValue(STRING, true);
        }

        return state.setValue(STRING, false);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!state.getValue(STRING) && stack.is(ItemInit.PAINTBRUSH)){
             world.setBlockAndUpdate(pos, state.cycle(COLOR));
             return InteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, COLOR, STRING));
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = super.getCloneItemStack(world, pos, state, includeData);

        BlockItemStateProperties component = BlockItemStateProperties.EMPTY;

        if(state.hasProperty(COLOR)) {
            component = component.with(COLOR, state.getValue(COLOR));
        }
        itemStack.set(DataComponents.BLOCK_STATE, component);

        return itemStack;
    }
}

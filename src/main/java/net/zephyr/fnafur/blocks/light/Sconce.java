package net.zephyr.fnafur.blocks.light;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.redstone.Orientation;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;
import net.zephyr.fnafur.client.gui.screens.editing.PaintbrushAltPickerScreen;
import net.zephyr.fnafur.init.item_init.ItemInit;
import org.jetbrains.annotations.Nullable;

public class Sconce extends HorizontalFacingLight {
    public static final EnumProperty<SconceColors> COLOR = EnumProperty.create("color", SconceColors.class);
    public Sconce(Properties settings) {
        super(settings);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(stack.is(ItemInit.PAINTBRUSH)){
            if(world.isClientSide()){
                Minecraft.getInstance().setScreen(new PaintbrushAltPickerScreen<>(Component.literal("guh"), pos, COLOR, state.getValue(COLOR)));
            }
            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, world, pos, player, hand, hit);
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

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(COLOR));
    }
}

package net.zephyr.fnafur.blocks.utility_blocks.cpu_config_panel;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class CpuConfigPanelBlock extends BlockWithEntity {
    public static final BooleanProperty TOP_CPU = BooleanProperty.of("top");
    public static final BooleanProperty BOTTOM_CPU = BooleanProperty.of("bottom");
    public static final BooleanProperty PATHING_CHIP = BooleanProperty.of("path");
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public CpuConfigPanelBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(TOP_CPU, false).with(BOTTOM_CPU, false).with(PATHING_CHIP, false));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(player.getMainHandStack().isOf(ItemInit.CPU)){
            return ActionResult.SUCCESS;
        } else {
            if(world.getBlockEntity(pos) instanceof CpuConfigPanelBlockEntity ent){
                GoopyNetworkingUtils.setScreen(player, "cpu_config", ((IEntityDataSaver)ent).getPersistentData(), pos);
            }
            return ActionResult.SUCCESS;
        }
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CpuConfigPanelBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(FACING).add(TOP_CPU).add(BOTTOM_CPU).add(PATHING_CHIP));
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
}

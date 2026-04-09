package net.zephyr.fnafur.blocks.energy.blocks.receivers;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.energy.blocks.switches.BaseFloorSwitchBlock;
import net.zephyr.fnafur.blocks.energy.blocks.switches.office_buttons.OfficeButtonsBlockEntity;
import net.zephyr.fnafur.blocks.energy.enums.EnergyNodeType;
import net.zephyr.fnafur.blocks.linking.presets.SimpleEnergyTargetPropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import org.jetbrains.annotations.Nullable;

public class RedstoneConverterBlock extends FloorPropBlock<DefaultPropColorEnum> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public RedstoneConverterBlock(BlockBehaviour.Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {

        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof SimpleEnergyTargetPropBlockEntity ent) {

            InteractionResult result2 = ent.tryEndLink(player, world, pos);
            if (result2 != null) return result2;
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
    }

    @Override
    public boolean rotates() {
        return true;
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return (Boolean)state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return state.getSignal(world, pos, direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SimpleEnergyTargetPropBlockEntity(pos, state);
    }
    @Nullable
    @Override
    public <Q extends BlockEntity> BlockEntityTicker<Q> getTicker(Level world, BlockState state, BlockEntityType<Q> type) {
        return createTickerHelper(type, BlockEntityInit.SIMPLE_PROP_ENERGY_TARGET,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }
}

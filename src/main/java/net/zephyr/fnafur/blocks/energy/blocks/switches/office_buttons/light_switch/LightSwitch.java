package net.zephyr.fnafur.blocks.energy.blocks.switches.office_buttons.light_switch;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
import net.minecraft.world.level.redstone.Orientation;
import net.zephyr.fnafur.blocks.energy.blocks.switches.office_buttons.OfficeButtonsBlockEntity;
import net.zephyr.fnafur.blocks.linking.EnergySource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.blocks.linking.presets.SimpleEnergySwitchPropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.networking.sounds.PlayBlockSoundS2CPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class LightSwitch extends WallPropBlock<DefaultPropColorEnum> {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public LightSwitch(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Nullable
    @Override
    public <Q extends BlockEntity> BlockEntityTicker<Q> getTicker(Level world, BlockState state, BlockEntityType<Q> type) {
        return createTickerHelper(type, BlockEntityInit.SIMPLE_PROP_ENERGY_SWITCH,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SimpleEnergySwitchPropBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        float pitch = (Boolean) state.getValue(POWERED) ? 1f : 1.1f;

        if (world.getBlockEntity(pos) instanceof SimpleEnergySwitchPropBlockEntity ent) {

            InteractionResult result2 = ent.tryEndLink(player, world, pos);
            if (result2 != null) return result2;

            InteractionResult result3 = ent.tryStartLink(player, pos);
            if (result3 != null) return result3;
        }

        if (!world.isClientSide()) {
            for (ServerPlayer p : PlayerLookup.all(world.getServer())) {
                ServerPlayNetworking.send(p, new PlayBlockSoundS2CPayload(pos.asLong(), "switch_flip", SoundSource.BLOCKS.getName(), .3f, pitch));
            }
        }
        world.setBlockAndUpdate(pos, state.cycle(POWERED));
        if(world.getBlockEntity(pos) instanceof EnergySource s){
            for(IEntityDataSaver ent : s.getTargets()){
                ((LinkTarget)ent).updateStatus(world, pos, ((IEntityDataSaver) s));
            }
        }
        this.updateNeighbors(state, world, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(FACING)){
            default -> Shapes.box(0.2f, 0.1f, 0, 0.8f, 0.9f, 0.1f);
            case SOUTH -> Shapes.box(0.2f, 0.1f, 0.9f, 0.8f, 0.9f, 1);
            case WEST -> Shapes.box(0, 0.1f, 0.2f, 0.1f, 0.9f, 0.8f);
            case EAST -> Shapes.box(0.9f, 0.1f, 0.2f, 1, 0.9f, 0.8f);
        };
        return drawingOutline ? shape : getInteractionShape(state, world, pos);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return state.getValue(POWERED);
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) && direction == state.getValue(FACING) ? 15 : 0;
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        if (!moved && (Boolean)state.getValue(POWERED)) {
            this.updateNeighbors(state, world, pos);
        }
    }

    private void updateNeighbors(BlockState state, Level world, BlockPos pos) {
        Direction direction = state.getValue(FACING).getOpposite();
        Orientation wireOrientation = ExperimentalRedstoneUtils.initialOrientation(
                world, direction, direction.getAxis().isHorizontal() ? Direction.UP : state.getValue(FACING)
        );
        world.updateNeighborsAt(pos, this, wireOrientation);
        world.updateNeighborsAt(pos.relative(direction), this, wireOrientation);
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
    }

    @Override
    public boolean lockY(BlockState state) {
        return false;
    }

    @Override
    public boolean goesOnFloor(BlockItemStateProperties state) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED));
    }
}

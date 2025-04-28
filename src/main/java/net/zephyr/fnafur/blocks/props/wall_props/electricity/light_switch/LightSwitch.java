package net.zephyr.fnafur.blocks.props.wall_props.electricity.light_switch;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.sound.Sound;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.OrientationHelper;
import net.minecraft.world.block.WireOrientation;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.networking.sounds.PlayBlockSoundS2CPayload;
import net.zephyr.fnafur.networking.sounds.PlaySoundS2CPayload;

public class LightSwitch extends WallPropBlock<DefaultPropColorEnum> {
    public static final BooleanProperty POWERED = Properties.POWERED;
    public LightSwitch(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(POWERED, false));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        float pitch = (Boolean) state.get(POWERED) ? 1f : 1.1f;

        if (!world.isClient()) {
            for (ServerPlayerEntity p : PlayerLookup.all(world.getServer())) {
                ServerPlayNetworking.send(p, new PlayBlockSoundS2CPayload(pos.asLong(), "switch_flip", SoundCategory.BLOCKS.getName(), .3f, pitch));
            }
        }
        world.setBlockState(pos, state.cycle(POWERED));
        this.updateNeighbors(state, world, pos);
        return ActionResult.SUCCESS;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = switch (state.get(FACING)){
            default -> VoxelShapes.cuboid(0.2f, 0.1f, 0, 0.8f, 0.9f, 0.1f);
            case SOUTH -> VoxelShapes.cuboid(0.2f, 0.1f, 0.9f, 0.8f, 0.9f, 1);
            case WEST -> VoxelShapes.cuboid(0, 0.1f, 0.2f, 0.1f, 0.9f, 0.8f);
            case EAST -> VoxelShapes.cuboid(0.9f, 0.1f, 0.2f, 1, 0.9f, 0.8f);
        };
        return drawingOutline ? shape : getRaycastShape(state, world, pos);
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return state.get(POWERED);
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) ? 15 : 0;
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) && direction == state.get(FACING) ? 15 : 0;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!moved && !state.isOf(newState.getBlock())) {
            if ((Boolean)state.get(POWERED)) {
                this.updateNeighbors(state, world, pos);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
    private void updateNeighbors(BlockState state, World world, BlockPos pos) {
        Direction direction = state.get(FACING).getOpposite();
        WireOrientation wireOrientation = OrientationHelper.getEmissionOrientation(
                world, direction, direction.getAxis().isHorizontal() ? Direction.UP : state.get(FACING)
        );
        world.updateNeighborsAlways(pos, this, wireOrientation);
        world.updateNeighborsAlways(pos.offset(direction), this, wireOrientation);
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
    public boolean goesOnFloor(BlockStateComponent state) {
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(POWERED));
    }
}

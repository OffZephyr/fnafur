package net.zephyr.fnafur.blocks.props.base;

import com.google.common.reflect.TypeToken;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.zephyr.fnafur.blocks.props.ColorEnumInterface;
import net.zephyr.fnafur.blocks.props.FloorMonitors.FloorMonitorColors1;
import org.jetbrains.annotations.Nullable;

public abstract class PropBlock<T extends Enum<T> & ColorEnumInterface & StringIdentifiable> extends BlockWithEntity {
    public static final int offset_grid_size = 16;
    public static boolean drawingOutline = false;
    public static final float angleSnap = 22.5f;
    public static final float gridSnap = 0.25f;

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty OFFSET_X = IntProperty.of("x_offset", 0, offset_grid_size);
    public static final IntProperty OFFSET_Y = IntProperty.of("y_offset", 0, offset_grid_size);
    public static final IntProperty OFFSET_Z = IntProperty.of("z_offset", 0, offset_grid_size);

    protected PropBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return super.onUse(state, world, pos, player, hit);
    }

    public abstract EnumProperty<T> COLOR_PROPERTY();

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PropBlockEntity(pos, state);
    }


    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return true;
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        ItemStack itemStack = new ItemStack(this);
        if(COLOR_PROPERTY() != null) {
            itemStack.set(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT.with(COLOR_PROPERTY(), state.get(COLOR_PROPERTY())));
        }
        return itemStack;
    }
}

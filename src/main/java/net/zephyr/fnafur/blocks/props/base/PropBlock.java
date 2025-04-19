package net.zephyr.fnafur.blocks.props.base;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class PropBlock<T extends Enum<T> & ColorEnumInterface & StringIdentifiable>  extends BlockWithEntity {

    public static boolean drawingOutline = false;
    public static final float angleSnap = 22.5f;
    public static final float gridSnap = 0.25f;

    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    private EnumProperty<T> COLOR;
    protected PropBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    public boolean canChangeState(Item item){
        return item == ItemInit.PAINTBRUSH;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ItemStack stack = player.getMainHandStack();
        if(stack != null && canChangeState(stack.getItem()) && state.contains(COLOR_PROPERTY())) {
            world.setBlockState(pos, state.cycle(COLOR_PROPERTY()));
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hit);
    }
    public abstract Class<T> COLOR_ENUM();
    public EnumProperty<T> COLOR_PROPERTY(){
        if(COLOR_ENUM() == null) return COLOR;
        if(COLOR == null) COLOR = EnumProperty.of("color", COLOR_ENUM());
        return COLOR;
    }

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

    public abstract boolean rotates();
    public abstract boolean snapsVertically();

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return true;
    }


    @Nullable
    @Override
    public <Q extends BlockEntity> BlockEntityTicker<Q> getTicker(World world, BlockState state, BlockEntityType<Q> type) {

        return validateTicker(type, BlockEntityInit.PROPS,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }

    public List<Box> getClickHitBoxes(BlockState state){
        return new ArrayList<>();
    }

    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = super.getPickStack(world, pos, state, includeData);

        BlockStateComponent component = BlockStateComponent.DEFAULT;
        /*for(Property property : state.getProperties()){
            component = component.with(property, state.get(property));
        }*/

        if(state.contains(COLOR)) {
            component = component.with(COLOR, state.get(COLOR));
        }
        itemStack.set(DataComponentTypes.BLOCK_STATE, component);

        return itemStack;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public boolean isTransparent(BlockState state) { return true;}

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
}

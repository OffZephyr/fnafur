package net.zephyr.fnafur.blocks.utility_blocks.computer;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.zephyr.fnafur.init.ScreensInit;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ComputerBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public ComputerBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ComputerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {

        return validateTicker(type, BlockEntityInit.COMPUTER,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }
    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        NbtCompound nbt =  ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData();
        nbt.putString("wallpaper", "blue_checker");
    }
    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = super.getPickStack(world, pos, state, includeData);

        BlockStateComponent component = BlockStateComponent.DEFAULT;
        for(Property property : state.getProperties()){
            component = component.with(property, state.get(property));
        }

        itemStack.set(DataComponentTypes.BLOCK_STATE, component);

        return itemStack;
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);

        List<ItemStack> item = new ArrayList<>();
        item.add(getPickStack(builder.getWorld(), blockEntity.getPos(), state, true));
        return item;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        //NbtCompound data = world.getBlockEntity(pos) instanceof ComputerBlockEntity ent ? ent.getCustomData() : new NbtCompound();
        NbtCompound data = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData();

        if(player.getMainHandStack().isOf(ItemInit.CPU) || player.getMainHandStack().isOf(ItemInit.ILLUSIONDISC)){
            emptyDisk(data, world, pos, state);
            data.put("ai_data", player.getMainHandStack().toNbtAllowEmpty(world.getRegistryManager()));
            player.getMainHandStack().decrementUnlessCreative(1, player);
            return ActionResult.SUCCESS;
        }
        else {
            GoopyNetworkingUtils.setScreen(player, ScreensInit.COMPUTER_BOOT, data, pos);
        }
        return ActionResult.SUCCESS;
    }
    public void ejectFloppy(World world, BlockPos pos){
        NbtCompound data = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData();
        emptyDisk(data, world, pos, world.getBlockState(pos));
    }

    ActionResult emptyDisk(NbtCompound blockData, World world, BlockPos pos, BlockState state){
        ItemStack disk = ItemStack.fromNbtOrEmpty(world.getRegistryManager(), blockData.getCompound("ai_data"));

        if(disk != ItemStack.EMPTY) {
            Block.dropStack(world, pos, state.get(FACING), disk);
            blockData.put("ai_data", ItemStack.EMPTY.toNbtAllowEmpty(world.getRegistryManager()));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}

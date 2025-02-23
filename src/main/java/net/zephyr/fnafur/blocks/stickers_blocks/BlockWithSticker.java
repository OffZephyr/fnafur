package net.zephyr.fnafur.blocks.stickers_blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class BlockWithSticker extends BlockWithEntity {
    public BlockWithSticker(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx);
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StickerBlockEntity(pos, state);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = super.getPickStack(world, pos, state, includeData);

        BlockStateComponent component = BlockStateComponent.DEFAULT;
        for(Property property : state.getProperties()){
            component = component.with(property, state.get(property));
        }

        itemStack.set(DataComponentTypes.BLOCK_STATE, component);
        NbtCompound nbt = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData();

        ItemStack stack = ItemStack.fromNbtOrEmpty(MinecraftClient.getInstance().world.getRegistryManager(), nbt.getCompound("BlockState"));
        itemStack.set(DataComponentTypes.ITEM_NAME, Text.literal(stack.getName().getString() + Text.translatable("block.fnafur.has_sticker").getString()));

        ItemNbtUtil.setNbt(itemStack, nbt);


        return itemStack;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        NbtCompound data = ItemNbtUtil.getNbt(itemStack);

        world.setBlockState(pos, state);
        if (world.getBlockEntity(pos) instanceof BlockEntity entity) {
            ((IEntityDataSaver) entity).getPersistentData().copyFrom(data);
        }
        if (world.isClient()) {
            GoopyNetworkingUtils.getNbtFromServer(pos);
            world.updateListeners(pos, getDefaultState(), getDefaultState(), 3);
        }
    }

    @Override
    protected boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        return super.onSyncedBlockEvent(state, world, pos, type, data);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BlockEntityInit.STICKER_BLOCK,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }
}

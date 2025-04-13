package net.zephyr.fnafur.blocks.stickers_blocks;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrames;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SPayload;
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

        //itemStack.set(DataComponentTypes.BLOCK_STATE, component);
        NbtCompound nbt = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData();

        ItemStack stack = ItemStack.fromNbtOrEmpty(MinecraftClient.getInstance().world.getRegistryManager(), nbt.getCompound("BlockState"));
        BlockState newState = state.getBlock() instanceof BlockWithSticker && !stack.isEmpty() ? stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT).applyToState(((BlockItem)stack.getItem()).getBlock().getDefaultState()) : state;

        if(!(state.getBlock() instanceof MimicFrames)){
            itemStack.set(DataComponentTypes.ITEM_NAME, Text.literal(newState.getBlock().getName().getString() + Text.translatable("block.fnafur.has_sticker").getString()));
        }

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
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(stack.isOf(ItemInit.SCRAPER)){

            if(world.getBlockEntity(pos) instanceof StickerBlockEntity ent){
                NbtCompound nbt = ((IEntityDataSaver)ent).getPersistentData();
                String side = hit.getSide().name();

                NbtList list = nbt.getList(side, NbtElement.STRING_TYPE);
                NbtList offset_list = nbt.getList(side + "_offset", NbtElement.FLOAT_TYPE);

                if(list.isEmpty()) return ActionResult.PASS;

                list.removeLast();
                offset_list.removeLast();

                if(list.isEmpty()) nbt.remove(side);
                else nbt.put(side, list);

                if(offset_list.isEmpty()) nbt.remove(side + "_offset");
                else nbt.put(side + "_offset", offset_list);

                world.playSound(pos.toCenterPos().getX(), pos.toCenterPos().getY(), pos.toCenterPos().getZ(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 0.125f, 1.25f, true);
                world.playSound(pos.toCenterPos().getX(), pos.toCenterPos().getY(), pos.toCenterPos().getZ(), SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 0.5f, 1.1f, true);

                if(hasNoStickers(nbt)){
                    ItemStack blockStack = ItemStack.fromNbtOrEmpty(MinecraftClient.getInstance().world.getRegistryManager(), nbt.getCompound("BlockState"));

                    BlockState newState = !blockStack.isEmpty() ? blockStack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT).applyToState(((BlockItem)blockStack.getItem()).getBlock().getDefaultState()) : state;

                    world.setBlockState(pos, newState, Block.NOTIFY_ALL_AND_REDRAW);
                }
                else{

                    if(world.isClient()){
                        ClientPlayNetworking.send(new UpdateBlockNbtC2SPayload(pos.asLong(), nbt));
                    }
                    world.setBlockState(pos, state, Block.NOTIFY_ALL_AND_REDRAW);
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL_AND_REDRAW);
                }

                return ActionResult.SUCCESS;
            }
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    boolean hasNoStickers(NbtCompound nbt){
        boolean empty = true;

        for(Direction d : Direction.values()){
            if(nbt.contains(d.getName().toUpperCase())) empty = false;
        }

        return empty;
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

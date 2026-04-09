package net.zephyr.fnafur.blocks.stickers_blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.MimicFrames;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class BlockWithSticker extends BaseEntityBlock {
    public BlockWithSticker(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StickerBlockEntity(pos, state);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    @Override
    protected ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = super.getCloneItemStack(world, pos, state, includeData);

        BlockItemStateProperties component = BlockItemStateProperties.EMPTY;
        for(Property property : state.getProperties()){
            component = component.with(property, state.getValue(property));
        }

        //itemStack.set(DataComponentTypes.BLOCK_STATE, component);
        CompoundTag nbt = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData();

        ItemStack stack = nbt.read("BlockState", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        BlockState newState = state.getBlock() instanceof BlockWithSticker && !stack.isEmpty() ? stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY).apply(((BlockItem)stack.getItem()).getBlock().defaultBlockState()) : state;

        if(!(state.getBlock() instanceof MimicFrames)){
            itemStack.set(DataComponents.ITEM_NAME, Component.literal(newState.getBlock().getName().getString() + Component.translatable("block.fnafur.has_sticker").getString()));
        }

        ItemUtil.setNbt(itemStack, nbt);


        return itemStack;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        CompoundTag data = ItemUtil.getNbt(itemStack);

        world.setBlockAndUpdate(pos, state);
        if (world.isClientSide()) {
            if (world.getBlockEntity(pos) instanceof BlockEntity entity) {
                ((IEntityDataSaver) entity).getPersistentData().merge(data);
                ((IEntityDataSaver)entity).setServerUpdateStatus(true);
            }
            world.sendBlockUpdated(pos, defaultBlockState(), defaultBlockState(), 3);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(stack.is(ItemInit.SCRAPER)){
            System.out.println("SCRAPE");

            if(world.getBlockEntity(pos) instanceof StickerBlockEntity ent){
                System.out.println("SCRAPE2");
                CompoundTag nbt = ((IEntityDataSaver)ent).getPersistentData();
                String side = hit.getDirection().name();

                ListTag list = nbt.getList(side).orElse(new ListTag());
                ListTag offset_list = nbt.getList(side + "_offset").orElse(new ListTag());

                if(!list.isEmpty()) {
                    System.out.println("SCRAPE3");

                    list.removeLast();
                    offset_list.removeLast();

                    if (list.isEmpty()) nbt.remove(side);
                    else nbt.put(side, list);

                    if (offset_list.isEmpty()) nbt.remove(side + "_offset");
                    else nbt.put(side + "_offset", offset_list);

                    world.playSound(player, pos.getCenter().x(), pos.getCenter().y(), pos.getCenter().z(), SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.125f, 1.25f);
                    world.playSound(player, pos.getCenter().x(), pos.getCenter().y(), pos.getCenter().z(), SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 0.5f, 1.1f);

                    if(world.isClientSide()) {
                        if (hasNoStickers(nbt) && !(asBlock() instanceof MimicFrames)) {
                            System.out.println("SCRAPE5");
                            ItemStack blockStack = nbt.read("BlockState", ItemStack.CODEC).orElse(ItemStack.EMPTY);

                            state = !blockStack.isEmpty() ? blockStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY).apply(((BlockItem) blockStack.getItem()).getBlock().defaultBlockState()) : state;
                        } else {
                            ((IEntityDataSaver) ent).setServerUpdateStatus(true);
                            ((IEntityDataSaver) ent).getPersistentData().putBoolean("synced", false);
                        }
                        world.setBlock(pos, state, Block.UPDATE_ALL_IMMEDIATE);
                        world.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL_IMMEDIATE);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    boolean hasNoStickers(CompoundTag nbt){
        boolean empty = true;

        for(Direction d : Direction.values()){
            if(nbt.contains(d.getName().toUpperCase())) empty = false;
        }

        return empty;
    }

    @Override
    protected boolean triggerEvent(BlockState state, Level world, BlockPos pos, int type, int data) {
        return super.triggerEvent(state, world, pos, type, data);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityInit.STICKER_BLOCK,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }
}

package net.zephyr.fnafur.blocks.illusion_block;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlock;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtS2CPongPayload;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class MimicFrames extends StickerBlock {
    public MimicFrames(Settings settings) {
        super(settings);
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MimicFrameBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        BlockEntity entity = world.getBlockEntity(pos);
        ItemStack stack = player.getMainHandStack();
        if(entity != null) {

            NbtCompound nbt = ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData();
            Block currentBlock = getCurrentBlock(nbt, world, hit.getSide());

            if (stack != null && stack.getItem() instanceof BlockItem blockItem) {
                if(!(blockItem.getBlock() instanceof MimicFrames) && currentBlock == null) {

                    saveBlockTexture(
                            setBlockTexture(nbt, stack, hit.getSide(), world),
                            world,
                            pos,
                            player.getServer()
                    );

                    world.updateListeners(pos, getDefaultState(), getDefaultState(), 3);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }

    public Block getCurrentBlock(NbtCompound nbt, World world, Direction direction){
        return getBlockFromNbt(nbt.getCompound(direction.getName()), world);
    }

    public NbtCompound setBlockTexture(NbtCompound nbt, ItemStack stack, Direction direction, World world){

        nbt.put(direction.getName(), stack.toNbtAllowEmpty(world.getRegistryManager()));
        return nbt;
    }
    public void saveBlockTexture(NbtCompound nbt, World world, BlockPos pos, MinecraftServer server){

        if(!world.isClient()) {
            world.playSound(null, pos, SoundEvents.BLOCK_COPPER_GRATE_PLACE, SoundCategory.BLOCKS, 1, 1);
            for (ServerPlayerEntity p : PlayerLookup.all(server)) {
                ServerPlayNetworking.send(p, new UpdateBlockNbtS2CPongPayload(pos.asLong(), nbt));
            }
        }

        world.updateListeners(pos, getDefaultState(), getDefaultState(), 3);
    }

    public static Block getBlockFromNbt(NbtCompound nbt, World world){
        ItemStack stack = ItemStack.fromNbtOrEmpty(world.getRegistryManager(), nbt);
        if(stack.getItem() instanceof BlockItem blockItem){
            return blockItem.getBlock();
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BlockEntityInit.MIMIC_FRAME,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }
    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {

        ItemStack itemStack = super.getPickStack(world, pos, state);
        world.getBlockEntity(pos, BlockEntityInit.MIMIC_FRAME).ifPresent((blockEntity) -> {
            blockEntity.setStackNbt(itemStack, world.getRegistryManager());
        });

        return itemStack;
    }
}

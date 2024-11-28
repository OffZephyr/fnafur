package net.zephyr.fnafur.blocks.stickers_blocks;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class StickerBlock extends BlockWithEntity {
    public String name;
    private Identifier top;
    private Identifier bottom;
    private Identifier north;
    private Identifier east;
    private Identifier south;
    private Identifier west;
    private Identifier particle;

    public StickerBlock(Settings settings) {
        super(settings);
    }
    public StickerBlock textures(Identifier north, Identifier east, Identifier south, Identifier west, Identifier top, Identifier bottom) {
        this.top = top;
        this.bottom = bottom;
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
        this.particle = north;

        return this;
    }

    @Environment(EnvType.CLIENT)
    public Identifier getTop(){
        return top == null ? MissingSprite.getMissingSpriteId() : top;
    }

    @Environment(EnvType.CLIENT)
    public Identifier getBottom(){
        return bottom == null ? MissingSprite.getMissingSpriteId() : bottom;
    }

    @Environment(EnvType.CLIENT)
    public Identifier getNorth(){
        return north == null ? MissingSprite.getMissingSpriteId() : north;
    }

    @Environment(EnvType.CLIENT)
    public Identifier getEast(){
        return east == null ? MissingSprite.getMissingSpriteId() : east;
    }

    @Environment(EnvType.CLIENT)
    public Identifier getSouth(){
        return south == null ? MissingSprite.getMissingSpriteId() : south;
    }

    @Environment(EnvType.CLIENT)
    public Identifier getWest(){
        return west == null ? MissingSprite.getMissingSpriteId() : west;
    }

    @Environment(EnvType.CLIENT)
    public Identifier getParticle(){
        return particle == null ? MissingSprite.getMissingSpriteId() : particle;
    }

    @Environment(EnvType.CLIENT)
    public Map<Direction, Identifier> sprites() {
        Map<Direction, Identifier> map = new HashMap<>();
        map.put(Direction.NORTH, getNorth());
        map.put(Direction.EAST, getEast());
        map.put(Direction.SOUTH, getSouth());
        map.put(Direction.WEST, getWest());
        map.put(Direction.UP, getTop());
        map.put(Direction.DOWN, getBottom());
        return map;
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
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {

        ItemStack itemStack = super.getPickStack(world, pos, state);
        world.getBlockEntity(pos, BlockEntityInit.STICKER_BLOCK).ifPresent((blockEntity) -> {
            blockEntity.setStackNbt(itemStack, world.getRegistryManager());
        });

        return itemStack;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        NbtCompound data = itemStack.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT).copyNbt().getCompound("fnafur.persistent");

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

    public void setName(String name) {
        this.name = name;
    }
    public String getBlockName() {
        return name;
    }
}

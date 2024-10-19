package net.zephyr.fnafur.item.stickers.base;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.zephyr.fnafur.blocks.stickers.base.Sticker;
import net.zephyr.fnafur.blocks.stickers.base.StickerBlock;
import net.zephyr.fnafur.blocks.stickers.base.StickerBlockEntity;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public abstract class StickerItem extends Item {
    public static int MAX_STICKER_AMOUNT = 5;
    public StickerItem(Settings settings) {
        super(settings);
    }

    public abstract String sticker_name();
    public abstract boolean isWallSticker();
    public abstract boolean isStackable();
    public abstract float mouseOffset();

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof StickerBlock block){
            if(!isWallSticker() || (isWallSticker() && context.getSide() != Direction.UP && context.getSide() != Direction.DOWN)){
                BlockEntity entity = context.getWorld().getBlockEntity(context.getBlockPos());
                NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();
                String side = context.getSide().name();

                Direction direction = context.getSide();
                String name = sticker_name();
                Sticker sticker = Sticker.getSticker(name);

                if(sticker == null) return ActionResult.PASS;

                NbtList list = nbt.getList(side, NbtElement.STRING_TYPE);
                NbtList offset_list = nbt.getList(side + "_offset", NbtElement.FLOAT_TYPE);

                Vec3d hitPos = context.getHitPos();
                BlockPos pos = context.getBlockPos();

                Vec3d stickerPos = stickerPos(pos, hitPos, direction, this, context.getPlayer(), context.getWorld());

                float offset = 0;

                if (sticker.getDirection() == Sticker.Movable.VERTICAL) {
                    offset = (float)stickerPos.getY();
                }
                else {
                    offset = direction.getAxis() == Direction.Axis.Z ? (float)stickerPos.getX() :
                            direction.getAxis() == Direction.Axis.X ? (float)stickerPos.getZ() : offset;
                }

                if((isStackable() && list.size() < MAX_STICKER_AMOUNT) || (!isStackable() && list.isEmpty())) {
                    list.add(NbtString.of(sticker_name()));
                    offset_list.add(NbtFloat.of(offset));

                    nbt.put(side, list);
                    nbt.put(side + "_offset", offset_list);


                    context.getWorld().playSound(context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ(), SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1, 1, true);

                    if(context.getWorld().isClient()){
                        GoopyNetworkingUtils.saveBlockNbt(entity.getPos(), nbt);
                    }
                    context.getWorld().updateListeners(pos, block.getDefaultState(), block.getDefaultState(), 3);

                    return ActionResult.success(context.getWorld().isClient);
                }

            }
        }
        return ActionResult.PASS;
    }



    public static Vec3d stickerPos(BlockPos pos, Vec3d hitPos, Direction direction, StickerItem item, PlayerEntity player, World world){

        boolean snapBelow = world.getBlockState(pos.down()).getBlock() instanceof StickerBlock;
        boolean snapAbove = world.getBlockState(pos.up()).getBlock() instanceof StickerBlock;

        String name = item.sticker_name();
        Sticker sticker = Sticker.getSticker(name);

        double yOffset = hitPos.getY() - pos.getY() - item.mouseOffset();

        double xOffset = hitPos.getX() - pos.getX();
        double zOffset = hitPos.getZ() - pos.getZ();

        float grid = sticker.getPixelDensity();
        float space = sticker.getSize();

        double x = 0;
        double y = 0;
        double z = 0;

        float SnapGrid = 1f / grid;
        /*if (player.isSneaking()) {
            SnapGrid = 1f / (grid / 12f);
        }*/

        if (sticker.getDirection() == Sticker.Movable.VERTICAL) {
            y = yOffset;

            y = Math.round(y / SnapGrid) * SnapGrid;

            if (player.isSneaking()) {
                y = ((Math.round((y) / 0.2f) * 0.25f) / grid) * space;
                y = Math.clamp(y, -space / grid, 0);
            }
            if(!snapBelow) y = Math.clamp(y, -space / grid, 1);
            if(!snapAbove) y = Math.clamp(y, -1, 0);
        }
        if (sticker.getDirection() == Sticker.Movable.HORIZONTAL) {
            x = (xOffset / grid) * space;

            x = Math.round(x / SnapGrid) * SnapGrid;
            x = Math.clamp(x, 0, space);

            z = (zOffset / grid) * space;

            z = Math.round(z / SnapGrid) * SnapGrid;
            z = Math.clamp(z, 0, space);
        }

        if(direction == Direction.EAST || direction == Direction.WEST){
            x = 0;
        }
        if(direction == Direction.SOUTH || direction == Direction.NORTH){
            z = 0;
        }

        return new Vec3d(x, y, z);
    }
}

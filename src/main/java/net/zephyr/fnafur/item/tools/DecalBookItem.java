package net.zephyr.fnafur.item.tools;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.stickers_blocks.BlockWithSticker;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.DecalInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class DecalBookItem extends Item {
    public static final int MAX_STICKER_AMOUNT = 5;
    public DecalBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean allowContinuingBlockBreaking(PlayerEntity player, ItemStack oldStack, ItemStack newStack) {
        return super.allowContinuingBlockBreaking(player, oldStack, newStack);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if(user.isSneaking()){
            NbtCompound nbt = ItemNbtUtil.getNbt(user.getMainHandStack());
            nbt.putString("activeDecal", "");
            ItemNbtUtil.setNbt(user.getMainHandStack(), nbt);
            user.sendMessage(Text.translatable("decal_book.clear"), true);
            return ActionResult.SUCCESS;
        }
        GoopyNetworkingUtils.setScreen(user, "decal_book_edit");
        return ActionResult.SUCCESS;
    }

    public static DecalInit.Decal getDecal(ItemStack stack){
        String name = ItemNbtUtil.getNbt(stack).getString("activeDecal");
        if(!name.isEmpty()){
            return DecalInit.getDecal(name);
        }
        return null;
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getWorld().getBlockState(context.getBlockPos()).isSideSolidFullSquare(context.getWorld(), context.getBlockPos(), context.getSide())){

            DecalInit.Decal decal = getDecal(context.getStack());

            if(decal != null && (!decal.isWallSticker() || (context.getSide() != Direction.UP && context.getSide() != Direction.DOWN))){
                BlockState blockState = context.getWorld().getBlockState(context.getBlockPos());

                BlockEntity entity = context.getWorld().getBlockEntity(context.getBlockPos());

                if(!(blockState.getBlock() instanceof BlockWithSticker)) {
                    context.getWorld().setBlockState(context.getBlockPos(), BlockInit.STICKER_BLOCK.getDefaultState());

                    entity = context.getWorld().getBlockEntity(context.getBlockPos());
                    NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();

                    ItemStack itemStack = new ItemStack(blockState.getBlock().asItem());
                    BlockStateComponent component = BlockStateComponent.DEFAULT;

                    for (Property<?> property : blockState.getProperties()) {
                        component = component.with(property, blockState);
                    }

                    itemStack.set(DataComponentTypes.BLOCK_STATE, component);

                    nbt.put("BlockState", itemStack.toNbtAllowEmpty(context.getWorld().getRegistryManager()));
                }

                if(context.getWorld().isClient()) {
                    NbtCompound nbt = ((IEntityDataSaver) entity).getPersistentData();

                    String side = context.getSide().name();

                    Direction direction = context.getSide();
                    String name = decal.name();

                    NbtList list = nbt.getList(side, NbtElement.STRING_TYPE);
                    NbtList offset_list = nbt.getList(side + "_offset", NbtElement.FLOAT_TYPE);

                    Vec3d hitPos = context.getHitPos();
                    BlockPos pos = context.getBlockPos();

                    Vec3d stickerPos = stickerPos(pos, hitPos, direction, decal, context.getPlayer(), context.getWorld());

                    float offset = 0;

                    if (decal.getDirection() == DecalInit.Movable.VERTICAL) {
                        offset = (float) stickerPos.getY();
                    } else {
                        offset = direction.getAxis() == Direction.Axis.Z ? (float) stickerPos.getX() :
                                direction.getAxis() == Direction.Axis.X ? (float) stickerPos.getZ() : offset;
                    }

                    if ((decal.isStackable() && list.size() < MAX_STICKER_AMOUNT) || (!decal.isStackable() && list.isEmpty())) {
                        list.add(NbtString.of(name));
                        offset_list.add(NbtFloat.of(offset));

                        context.getWorld().playSound(context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ(), SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1, 1, true);

                        nbt.put(side, list);
                        nbt.put(side + "_offset", offset_list);

                        ((IEntityDataSaver)entity).setServerUpdateStatus(true);
                        context.getWorld().updateListeners(pos, blockState, blockState, 3);

                        return ActionResult.SUCCESS;
                    }
                }
            }
        }
        return ActionResult.PASS;
    }

    public static Vec3d stickerPos(BlockPos pos, Vec3d hitPos, Direction direction, DecalInit.Decal decal, PlayerEntity player, World world){

        boolean snapBelow = world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), direction);
        boolean snapAbove = world.getBlockState(pos.up()).isSideSolidFullSquare(world, pos.up(), direction);

        String name = decal.name();

        double yOffset = hitPos.getY() - pos.getY() - decal.mouseOffset();

        double xOffset = hitPos.getX() - pos.getX();
        double zOffset = hitPos.getZ() - pos.getZ();

        float grid = decal.getPixelDensity();
        float space = decal.getSize();

        double x = 0;
        double y = 0;
        double z = 0;

        float SnapGrid = 1f / grid;
        /*if (player.isSneaking()) {
            SnapGrid = 1f / (grid / 12f);
        }*/

        if (decal.getDirection() == DecalInit.Movable.VERTICAL) {
            y = yOffset;

            y = Math.round(y / SnapGrid) * SnapGrid;

            if (player.isSneaking()) {
                y = ((Math.round((y) / 0.2f) * 0.5f) / grid) * space;
                //space = space / 4f;

                y = Math.clamp(y, -space / grid, 0);
            }
            if(!snapBelow) y = Math.clamp(y, -space / grid, 1);
            if(!snapAbove) y = Math.clamp(y, -1, 0);
        }
        if (decal.getDirection() == DecalInit.Movable.HORIZONTAL) {
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

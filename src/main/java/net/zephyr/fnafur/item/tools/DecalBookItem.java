package net.zephyr.fnafur.item.tools;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.zephyr.fnafur.decals.DecalManager;
import net.zephyr.fnafur.init.decal_init.DecalInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemUtil;

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
            NbtCompound nbt = ItemUtil.getNbt(user.getMainHandStack());
            nbt.putString("activeDecal", "");
            ItemUtil.setNbt(user.getMainHandStack(), nbt);
            user.sendMessage(Text.translatable("decal_book.clear"), true);
            return ActionResult.SUCCESS;
        }
        NbtCompound nbt = ItemUtil.getNbt(user.getMainHandStack());
        if(!nbt.getBoolean("isHolding", false)){
            GoopyNetworkingUtils.setScreen(user, "decal_book_edit");
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public static DecalInit.Decal getDecal(ItemStack stack){
        String name = ItemUtil.getNbt(stack).getString("activeDecal").orElse("");
        if(!name.isEmpty()){
            return DecalInit.getDecal(name);
        }
        return null;
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if(context.getWorld().isClient()){
            if(DecalManager.PREVIEW_DECAL != null){

                if(DecalManager.PREVIEW_DECAL.addToWorld()){
                    context.getWorld().playSound(context.getPlayer(), context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ(), SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1, 1);
                    return ActionResult.SUCCESS;
                }

                return ActionResult.FAIL;
            }
            NbtCompound nbt = ItemUtil.getNbt(context.getStack());
            nbt.putBoolean("isHolding", true);
            ItemUtil.setNbt(context.getStack(), nbt);
        }
        return context.getPlayer().isSneaking() || DecalBookItem.getDecal(context.getPlayer().getMainHandStack()) == null ? ActionResult.PASS : ActionResult.FAIL;
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        NbtCompound nbt = ItemUtil.getNbt(stack);
        nbt.putBoolean("isHolding", false);
        ItemUtil.setNbt(stack, nbt);
        return super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    public static BlockPos getStartBlockPos(BlockPos pos, Direction direction, DecalInit.Movable movementMode){
        BlockPos checkedPos = pos;


        if(movementMode == DecalInit.Movable.VERTICAL){
            while(MinecraftClient.getInstance().world.getBlockState(checkedPos.offset(direction.rotateYClockwise())).getCollisionShape(MinecraftClient.getInstance().world, checkedPos) != VoxelShapes.empty() && MinecraftClient.getInstance().world.getBlockState(checkedPos.offset(direction.rotateYClockwise()).offset(direction)).getCollisionShape(MinecraftClient.getInstance().world, checkedPos) == VoxelShapes.empty()){
                checkedPos = checkedPos.offset(direction.rotateYClockwise());
            }
            if(direction == Direction.NORTH || direction == Direction.EAST){
                checkedPos = checkedPos.offset(direction.rotateYClockwise());
            }
        }
        else if(movementMode == DecalInit.Movable.HORIZONTAL){
            while(MinecraftClient.getInstance().world.getBlockState(checkedPos.down()).getCollisionShape(MinecraftClient.getInstance().world, checkedPos) != VoxelShapes.empty() && MinecraftClient.getInstance().world.getBlockState(checkedPos.down().offset(direction)).getCollisionShape(MinecraftClient.getInstance().world, checkedPos) == VoxelShapes.empty()){
                checkedPos = checkedPos.down();
            }
        }

        switch (movementMode){
            case HORIZONTAL:{
                break;
            }
            case VERTICAL:{
                break;
            }
        }

        return checkedPos;
    }

    public static int getDecalBlockLength(BlockPos pos, Direction direction, DecalInit.Movable movementMode){
        int i = 0;
        BlockPos checkedPos = pos;

        if(movementMode == DecalInit.Movable.VERTICAL){
            while (MinecraftClient.getInstance().world.getBlockState(checkedPos.offset(direction.rotateYCounterclockwise())).getCollisionShape(MinecraftClient.getInstance().world, checkedPos) != VoxelShapes.empty() && MinecraftClient.getInstance().world.getBlockState(checkedPos.offset(direction.rotateYCounterclockwise()).offset(direction)).getCollisionShape(MinecraftClient.getInstance().world, checkedPos) == VoxelShapes.empty()) {
                checkedPos = checkedPos.offset(direction.rotateYCounterclockwise());
                i++;
            }
            if (direction == Direction.NORTH || direction == Direction.EAST) {
                i++;
            }
        }
        else if(movementMode == DecalInit.Movable.HORIZONTAL){
            while (MinecraftClient.getInstance().world.getBlockState(checkedPos.up()).getCollisionShape(MinecraftClient.getInstance().world, checkedPos) != VoxelShapes.empty() && MinecraftClient.getInstance().world.getBlockState(checkedPos.up().offset(direction)).getCollisionShape(MinecraftClient.getInstance().world, checkedPos) == VoxelShapes.empty()) {
                checkedPos = checkedPos.up();
                i++;
            }
        }

        switch (movementMode) {
            case HORIZONTAL: {
                break;
            }
            case VERTICAL: {
                break;
            }
        }

        return i;
    }

    public static Vec3d stickerPos(BlockPos pos, Vec3d hitPos, Direction direction, DecalInit.Decal decal, PlayerEntity player, World world){

        boolean snapBelow = world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), direction);
        boolean snapAbove = world.getBlockState(pos.up()).isSideSolidFullSquare(world, pos.up(), direction);

        String name = decal.name();

        double yOffset = hitPos.getY() - pos.getY() - decal.mouseOffset();

        double xOffset = hitPos.getX() - pos.getX() - decal.mouseOffset();
        double zOffset = hitPos.getZ() - pos.getZ() - decal.mouseOffset();

        float grid = decal.getPixelDensity();
        float space = decal.getSize();

        double x = 0;
        double y = 0;
        double z = 0;

        float SnapGrid = 1f / grid;
        /*if (player.isSneaking()) {
            SnapGrid = 1f / (grid / 12f);
        }*/

        if (decal.getDirection() == DecalInit.Movable.VERTICAL || decal.getDirection() == DecalInit.Movable.FREE) {
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
        if (decal.getDirection() == DecalInit.Movable.HORIZONTAL || decal.getDirection() == DecalInit.Movable.FREE) {
            x = xOffset;

            x = Math.round(x / SnapGrid) * SnapGrid;

            if (player.isSneaking()) {
                x = ((Math.round((x) / 0.2f) * 0.5f) / grid) * space;
                //space = space / 4f;

                x = Math.clamp(x, -space / grid, 0);
            }

            z = zOffset;

            z = Math.round(z / SnapGrid) * SnapGrid;

            if (player.isSneaking()) {
                z = ((Math.round((z) / 0.2f) * 0.5f) / grid) * space;
                //space = space / 4f;

                z = Math.clamp(z, -space / grid, 0);
            }
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

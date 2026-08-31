package net.zephyr.fnafur.item.tools;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.rendering.decals.DecalManager;
import net.zephyr.fnafur.init.decal_init.DecalInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemUtil;
import org.jspecify.annotations.Nullable;

public class DecalBookItem extends Item {
    public static final int MAX_STICKER_AMOUNT = 5;
    public DecalBookItem(Properties settings) {
        super(settings);
    }

    @Override
    public boolean allowContinuingBlockBreaking(Player player, ItemStack oldStack, ItemStack newStack) {
        return super.allowContinuingBlockBreaking(player, oldStack, newStack);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        if(user.isShiftKeyDown()){
            CompoundTag nbt = ItemUtil.getNbt(user.getMainHandItem());
            nbt.putString("activeDecal", "");
            ItemUtil.setNbt(user.getMainHandItem(), nbt);
            user.sendOverlayMessage(Component.translatable("decal_book.clear"));
            return InteractionResult.SUCCESS;
        }
        CompoundTag nbt = ItemUtil.getNbt(user.getMainHandItem());
        if(!nbt.getBooleanOr("isHolding", false)){
            GoopyNetworkingUtils.setScreen(user, "decal_book_edit");
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static DecalInit.Decal getDecal(ItemStack stack){
        String name = ItemUtil.getNbt(stack).getString("activeDecal").orElse("");
        if(!name.isEmpty()){
            return DecalInit.getDecal(name);
        }
        return null;
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {

        if(context.getLevel().isClientSide()){
            if(DecalManager.PREVIEW_DECAL != null){

                if(DecalManager.PREVIEW_DECAL.addToWorld()){
                    context.getLevel().playSound(context.getPlayer(), context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(), SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1, 1);
                    return InteractionResult.SUCCESS;
                }

                return InteractionResult.FAIL;
            }
            CompoundTag nbt = ItemUtil.getNbt(context.getItemInHand());
            nbt.putBoolean("isHolding", true);
            ItemUtil.setNbt(context.getItemInHand(), nbt);
        }
        return context.getPlayer().isShiftKeyDown() || DecalBookItem.getDecal(context.getPlayer().getMainHandItem()) == null ? InteractionResult.PASS : InteractionResult.FAIL;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        CompoundTag nbt = ItemUtil.getNbt(stack);
        nbt.putBoolean("isHolding", false);
        ItemUtil.setNbt(stack, nbt);

        return super.releaseUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot slot) {
        CompoundTag nbt = ItemUtil.getNbt(stack);
        nbt.putBoolean("isHolding", false);
        ItemUtil.setNbt(stack, nbt);

        super.inventoryTick(stack, world, entity, slot);
    }

    public static BlockPos getStartBlockPos(BlockPos pos, Direction direction, DecalInit.Movable movementMode){
        BlockPos checkedPos = pos;


        if(movementMode == DecalInit.Movable.VERTICAL){
            while(Minecraft.getInstance().level.getBlockState(checkedPos.relative(direction.getClockWise())).getCollisionShape(Minecraft.getInstance().level, checkedPos) != Shapes.empty() && Minecraft.getInstance().level.getBlockState(checkedPos.relative(direction.getClockWise()).relative(direction)).getCollisionShape(Minecraft.getInstance().level, checkedPos) == Shapes.empty()){
                checkedPos = checkedPos.relative(direction.getClockWise());
            }
            if(direction == Direction.NORTH || direction == Direction.EAST){
                checkedPos = checkedPos.relative(direction.getClockWise());
            }
        }
        else if(movementMode == DecalInit.Movable.HORIZONTAL){
            while(Minecraft.getInstance().level.getBlockState(checkedPos.below()).getCollisionShape(Minecraft.getInstance().level, checkedPos) != Shapes.empty() && Minecraft.getInstance().level.getBlockState(checkedPos.below().relative(direction)).getCollisionShape(Minecraft.getInstance().level, checkedPos) == Shapes.empty()){
                checkedPos = checkedPos.below();
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
            while (Minecraft.getInstance().level.getBlockState(checkedPos.relative(direction.getCounterClockWise())).getCollisionShape(Minecraft.getInstance().level, checkedPos) != Shapes.empty() && Minecraft.getInstance().level.getBlockState(checkedPos.relative(direction.getCounterClockWise()).relative(direction)).getCollisionShape(Minecraft.getInstance().level, checkedPos) == Shapes.empty()) {
                checkedPos = checkedPos.relative(direction.getCounterClockWise());
                i++;
            }
            if (direction == Direction.NORTH || direction == Direction.EAST) {
                i++;
            }
        }
        else if(movementMode == DecalInit.Movable.HORIZONTAL){
            while (Minecraft.getInstance().level.getBlockState(checkedPos.above()).getCollisionShape(Minecraft.getInstance().level, checkedPos) != Shapes.empty() && Minecraft.getInstance().level.getBlockState(checkedPos.above().relative(direction)).getCollisionShape(Minecraft.getInstance().level, checkedPos) == Shapes.empty()) {
                checkedPos = checkedPos.above();
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

    public static Vec3 stickerPos(BlockPos pos, Vec3 hitPos, Direction direction, DecalInit.Decal decal, Player player, Level world){

        boolean snapBelow = world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), direction);
        boolean snapAbove = world.getBlockState(pos.above()).isFaceSturdy(world, pos.above(), direction);

        String name = decal.name();

        double yOffset = hitPos.y() - pos.getY() - decal.mouseOffset();

        double xOffset = hitPos.x() - pos.getX() - decal.mouseOffset();
        double zOffset = hitPos.z() - pos.getZ() - decal.mouseOffset();

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

            if (player.isShiftKeyDown()) {
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

            if (player.isShiftKeyDown()) {
                x = ((Math.round((x) / 0.2f) * 0.5f) / grid) * space;
                //space = space / 4f;

                x = Math.clamp(x, -space / grid, 0);
            }

            z = zOffset;

            z = Math.round(z / SnapGrid) * SnapGrid;

            if (player.isShiftKeyDown()) {
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

        return new Vec3(x, y, z);
    }
}

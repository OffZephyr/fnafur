package net.zephyr.fnafur.blocks.curtain;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class CurtainBlockItem extends BlockItem {
    public CurtainBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof CurtainBlock curtainBlock){
            NbtCompound nbt = ItemUtil.getNbt(context.getStack());
            if(nbt.contains("linkPos")){
                BlockPos pos = BlockPos.fromLong(nbt.getLong("linkPos").orElse(0L));
                if(pos.equals(context.getBlockPos())){
                    clearLink(context.getStack());
                    return ActionResult.SUCCESS;
                }
            }
            else{
                BlockPos pos = context.getBlockPos();
                nbt.putLong("linkPos", pos.asLong());
                ItemUtil.setNbt(context.getStack(), nbt);
                Text text = Text.literal(pos.getX() + " " + pos.getY() + " " + pos.getZ());
                ItemUtil.setLore(context.getStack(), text);
                return ActionResult.SUCCESS;
            }
        }
        return super.useOnBlock(context);
    }

    void clearLink(ItemStack stack){
        NbtCompound nbt = ItemUtil.getNbt(stack);
        nbt.remove("linkPos");
        ItemUtil.setNbt(stack, nbt);
        ItemUtil.setLore(stack, new Text[0]);
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        return super.place(context);
    }

    @Override
    protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        NbtCompound nbt = ItemUtil.getNbt(stack);
        if(nbt.contains("linkPos")){
            BlockPos prePos = BlockPos.fromLong(nbt.getLong("linkPos").orElse(0L));
            if(world.getBlockEntity(prePos) instanceof CurtainBlockEntity pre) {
                if (world.getBlockEntity(pos) instanceof CurtainBlockEntity post) {
                    ((IEntityDataSaver)pre).getPersistentData().putLong("next", pos.asLong());
                    ((IEntityDataSaver)post).getPersistentData().putLong("previous", prePos.asLong());
                    if(world.isClient()){
                        ((IEntityDataSaver)pre).getPersistentData().putBoolean("synced", true);
                        ((IEntityDataSaver)post).getPersistentData().putBoolean("synced", true);
                        GoopyNetworkingUtils.saveBlockNbt(prePos, ((IEntityDataSaver)pre).getPersistentData());
                        GoopyNetworkingUtils.saveBlockNbt(pos, ((IEntityDataSaver)post).getPersistentData());
                    }
                }
            }
            clearLink(stack);
        }
        return super.postPlacement(pos, world, player, stack, state);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {

        if (entity instanceof PlayerEntity p) {
            NbtCompound nbt = ItemUtil.getNbt(stack);
            if (nbt.contains("linkPos")) {
                if (stack != p.getMainHandStack()) {
                    clearLink(stack);
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot);
    }
}

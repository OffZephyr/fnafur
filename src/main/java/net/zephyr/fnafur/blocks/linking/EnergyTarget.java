package net.zephyr.fnafur.blocks.linking;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.item.tools.WrenchItem;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public interface EnergyTarget extends LinkTarget {

    boolean isReceivingPower();

    @Override
    default ActionResult tryEndLink(PlayerEntity player, World world, BlockPos pos) {
        ItemStack stack = player.getMainHandStack();
        if(player.getMainHandStack().getItem() instanceof WrenchItem) {
            NbtCompound nbt = ItemUtil.getNbt(stack);

            BlockPos startPos = nbt.get("startLink", BlockPos.CODEC).orElse(BlockPos.ORIGIN);
            if(world.getBlockEntity(startPos) instanceof LinkSource source){
                ActionResult result = LinkTarget.super.tryEndLink(player, world, pos);
                if(result != null){
                    updateStatus(world, pos, ((IEntityDataSaver)source));
                }
                return result;
            }
        }
        return null;
    }
}

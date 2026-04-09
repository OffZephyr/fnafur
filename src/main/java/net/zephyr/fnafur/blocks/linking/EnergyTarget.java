package net.zephyr.fnafur.blocks.linking;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.item.tools.WrenchItem;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public interface EnergyTarget extends LinkTarget {

    boolean isReceivingPower();

    @Override
    default InteractionResult tryEndLink(Player player, Level world, BlockPos pos) {
        ItemStack stack = player.getMainHandItem();
        if(player.getMainHandItem().getItem() instanceof WrenchItem) {
            CompoundTag nbt = ItemUtil.getNbt(stack);

            BlockPos startPos = nbt.read("startLink", BlockPos.CODEC).orElse(BlockPos.ZERO);
            if(world.getBlockEntity(startPos) instanceof LinkSource source){
                InteractionResult result = LinkTarget.super.tryEndLink(player, world, pos);
                if(result != null){
                    updateStatus(world, pos, ((IEntityDataSaver)source));
                }
                return result;
            }
        }
        return null;
    }
}

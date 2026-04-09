package net.zephyr.fnafur.item.animatronic;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.entity.animatronic.data.CpuData;
import net.zephyr.fnafur.util.ItemUtil;

public class CPUItem extends Item {
    public CPUItem(Properties settings) {
        super(settings);
    }

    @Override
    public Component getName(ItemStack stack) {

        String extra = ItemUtil.getNbt(stack).isEmpty() ? "Empty " : "";

        return Component.translatable(this.getDescriptionId(), extra);

    }

    public static CpuData getCpuData(ItemStack stack){

        CompoundTag nbt = ItemUtil.getNbt(stack).getCompound("cpu_data").orElse(new CpuData().toNbt());
        return CpuData.fromNbt(nbt);
    }

    public static ItemStack putCpuData(ItemStack stack, CpuData data){

        CompoundTag nbt = new CompoundTag();
        nbt.put("cpu_data", data.toNbt());

        return ItemUtil.setNbt(stack, nbt);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if(entity instanceof AnimatronicEntity ent){
            ent.setData(user, stack);
        }
        return super.interactLivingEntity(stack, user, entity, hand);
    }
}

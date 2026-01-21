package net.zephyr.fnafur.item.animatronic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.entity.animatronic.data.CpuData;
import net.zephyr.fnafur.util.ItemUtil;

public class CPUItem extends Item {
    public CPUItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {

        String extra = ItemUtil.getNbt(stack).isEmpty() ? "Empty " : "";

        return Text.translatable(this.getTranslationKey(), extra);

    }

    public static CpuData getCpuData(ItemStack stack){

        NbtCompound nbt = ItemUtil.getNbt(stack).getCompound("cpu_data").orElse(new CpuData().toNbt());
        return CpuData.fromNbt(nbt);
    }

    public static ItemStack putCpuData(ItemStack stack, CpuData data){

        NbtCompound nbt = new NbtCompound();
        nbt.put("cpu_data", data.toNbt());

        return ItemUtil.setNbt(stack, nbt);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(entity instanceof AnimatronicEntity ent){
            ent.setData(user, stack);
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}

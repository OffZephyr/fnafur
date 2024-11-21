package net.zephyr.fnafur.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.zephyr.fnafur.blocks.computer.ComputerData;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.List;

public class IllusionDisc extends Item {
    public IllusionDisc(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) throws NumberFormatException {
        String animatronic = ItemNbtUtil.getNbt(stack).getString("entity");
        if (!animatronic.isEmpty() && ComputerData.getAIAnimatronic(animatronic) instanceof ComputerData.Initializer.AnimatronicAI ai) {
            Text before = Text.translatable("item.fnafur.cpu.entity_name", "§7" + ai.entityType().getName().getString());
            Text text = Text.literal(before.getString());
            tooltip.add(text);
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(entity instanceof DefaultEntity ent) {
            for (ComputerData.Initializer.AnimatronicAI ai: ComputerData.getAIAnimatronics())
            {
                if(ai.entityType() == entity.getType()) {
                    String entityID = ai.id();
                    NbtCompound nbt = ItemNbtUtil.getNbt(stack);
                    nbt.putString("entity", entityID);
                    nbt.put("entityData", ((IEntityDataSaver)ent).getPersistentData());
                    ItemNbtUtil.setNbt(stack, nbt.copy());
                    ItemNbtUtil.setNbt(user.getMainHandStack(), nbt.copy());
                    user.sendMessage(Text.translatable("item.fnafur.illusion_disc.entity_updated"), true);
                    System.out.println(ItemNbtUtil.getNbt(stack).getCompound("entityData").asString());
                    return ActionResult.SUCCESS;
                }
            }
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}

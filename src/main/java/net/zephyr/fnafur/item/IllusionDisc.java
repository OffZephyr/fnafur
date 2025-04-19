package net.zephyr.fnafur.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.List;

public class IllusionDisc extends Item {
    public IllusionDisc(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) throws NumberFormatException {
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        /*if(entity instanceof DefaultEntity ent) {
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
                    FnafUniverseRebuilt.print(ItemNbtUtil.getNbt(stack).getCompound("entityData").asString());
                    return ActionResult.SUCCESS;
                }
            }
        }*/
        return super.useOnEntity(stack, user, entity, hand);
    }
}

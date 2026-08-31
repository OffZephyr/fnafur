package net.zephyr.fnafur.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

import java.util.List;

public class IllusionDisc extends Item {
    public IllusionDisc(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        /*if(entity instanceof DefaultEntity ent) {
            for (ComputerData.Initializer.AnimatronicAI ai: ComputerData.getAIAnimatronics())
            {
                if(ai.entityType() == entity.getType()) {
                    String entityID = ai.id();
                    CompoundTag nbt = ItemNbtUtil.getNbt(stack);
                    nbt.putString("entity", entityID);
                    nbt.put("entityData", ((IEntityDataSaver)ent).getPersistentData());
                    ItemNbtUtil.setNbt(stack, nbt.copy());
                    ItemNbtUtil.setNbt(user.getMainHandStack(), nbt.copy());
                    user.sendMessage(Text.translatable("item.fnafur.illusion_disc.entity_updated"), true);
                    FnafUniverseRebuilt.print(ItemNbtUtil.getNbt(stack).getCompound("entityData").asString());
                    return InteractionResult.SUCCESS;
                }
            }
        }*/
        return super.interactLivingEntity(stack, user, entity, hand);
    }
}

package net.zephyr.fnafur.util.hooks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ItemRenderingHook {
    public static boolean renderFirstPersonItem(
            AbstractClientPlayerEntity player,
            float tickProgress,
            float pitch,
            Hand hand,
            float swingProgress,
            ItemStack item,
            float equipProgress,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            HeldItemRenderer renderer
    ) {
        if(!((IUniversePlayer)player).hasVanniMaskOn()){
            ((IUniversePlayer)player).setMaskDelta(0);
        }
        ItemStack maskStack = player.getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET);
        if (player.getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET).getItem() instanceof VanniMaskItem) {
            boolean bl = hand == Hand.MAIN_HAND;
            if (!bl) {
                return true;
            }
            if(((IUniversePlayer)player).getMaskDelta() > 1.5f){
                return false;
            }
            matrices.push();
            ((IUniversePlayer)player).setMaskDelta(((IUniversePlayer)player).getMaskDelta() + (MinecraftClient.getInstance().getRenderTickCounter().getDynamicDeltaTicks() / 20f));
            Arm arm = player.getMainArm();

            boolean bl2 = arm == Arm.RIGHT;
            int l = bl2 ? 1 : -1;

            matrices.push();
            renderArm(matrices, vertexConsumers, light, Arm.LEFT);
            matrices.pop();
            matrices.push();
            renderArm(matrices, vertexConsumers, light, Arm.RIGHT);
            matrices.pop();


            renderer.swingArm(0, 0, matrices, l, arm);

            renderer.renderItem(
                    player, maskStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, matrices, vertexConsumers, light
            );
            matrices.pop();

            if(((IUniversePlayer)player).hasVanniMaskOn() && !((IUniversePlayer)player).isUsingVanniMask()){
                return true;
            }
        }
        return false;
    }

    public static void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm){
        boolean bl = arm != Arm.LEFT;

        AbstractClientPlayerEntity abstractClientPlayerEntity = MinecraftClient.getInstance().player;
        PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer)MinecraftClient.getInstance().getEntityRenderDispatcher()
                .<AbstractClientPlayerEntity>getRenderer(abstractClientPlayerEntity);
        Identifier identifier = abstractClientPlayerEntity.getSkinTextures().texture();
        if (bl) {
            //playerEntityRenderer.renderRightArm(matrices, vertexConsumers, light, identifier, abstractClientPlayerEntity.isPartVisible(PlayerModelPart.RIGHT_SLEEVE));
        } else {
            //playerEntityRenderer.renderLeftArm(matrices, vertexConsumers, light, identifier, abstractClientPlayerEntity.isPartVisible(PlayerModelPart.LEFT_SLEEVE));
        }
    }
}

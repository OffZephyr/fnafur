package net.zephyr.fnafur.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.util.hooks.ItemRenderingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    //PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer)this.entityRenderDispatcher.<AbstractClientPlayerEntity>getRenderer(this.client.player);
    @Shadow
    public void renderItem(
            LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light
    ){

    }

    @Shadow
    private void swingArm(float swingProgress, float equipProgress, MatrixStack matrices, int armX, Arm arm) {

    }
    @Shadow
    private void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm){

    }
    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void renderFirstPersonItem(
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
            CallbackInfo ci
    ) {

        if (ItemRenderingHook.renderFirstPersonItem(player, tickProgress, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light, ((HeldItemRenderer)(Object)this))){
            ci.cancel();
        }
        //ItemStack maskStack = player.getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET);
        //if(player.getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET).getItem() instanceof VanniMaskItem){
        //    boolean bl = hand == Hand.MAIN_HAND;
        //    if(!bl){
        //        return;
        //    }
        //    Arm arm = player.getMainArm();
//
        //    boolean bl2 = arm == Arm.RIGHT;
        //    int l = bl2 ? 1 : -1;
//
        //    matrices.push();
        //    renderArm(matrices, vertexConsumers, light, Arm.LEFT);
        //    matrices.pop();
//
//
        //    this.swingArm(0, 0, matrices, l, arm);
//
        //    this.renderItem(
        //            player, maskStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, matrices, vertexConsumers, light
        //    );
//
        //    ci.cancel();
        //}
    }
}

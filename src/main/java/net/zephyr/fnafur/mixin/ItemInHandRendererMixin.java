package net.zephyr.fnafur.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.InteractionHand;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.util.hooks.ItemRenderingHook;
import net.zephyr.fnafur.util.mixinAccessing.IHeldItemAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin implements IHeldItemAccessor {
    //PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer)this.entityRenderDispatcher.<AbstractClientPlayerEntity>getRenderer(this.client.player);


    @Shadow protected abstract void renderMapHand(PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, int light, HumanoidArm arm);

    @Shadow public abstract void renderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, int light);

    @Shadow
    public abstract void swingArm(float swingProgress, PoseStack matrixStack, int i, HumanoidArm arm);

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void renderFirstPersonItem(
            AbstractClientPlayer player, float tickProgress, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, int light, CallbackInfo ci
    ) {

        if (ItemRenderingHook.renderFirstPersonItem(player, tickProgress, pitch, hand, swingProgress, item, equipProgress, matrices, orderedRenderCommandQueue, light, ((ItemInHandRenderer)(Object)this))){
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

    @Override
    public void doSwingArm(float swingProgress, float equipProgress, PoseStack matrices, int armX, HumanoidArm arm) {
        swingArm(swingProgress, matrices, armX, arm);
    }
}

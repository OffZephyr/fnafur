package net.zephyr.fnafur.mixin;

import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin{


    /*@Inject(method = "getArmPose(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState$HandState;Lnet/minecraft/util/Hand;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;", at = @At("HEAD"), cancellable = true)
    private static void getArmPose(PlayerEntityRenderState state, PlayerEntityRenderState.HandState handState, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> ci){
        ItemStack itemStack = state.getMainHandStack();
        if(!state.handSwinging && itemStack.isOf(ItemInit.TABLET) && MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen) {
            ci.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
        }
    }*/
}

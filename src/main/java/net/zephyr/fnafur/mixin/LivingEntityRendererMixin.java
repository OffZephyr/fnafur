package net.zephyr.fnafur.mixin;

import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.util.IHasArmPos;
import net.zephyr.fnafur.util.mixinAccessing.ILivingEntityMaskRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Shadow
    protected EntityModel<? super HumanoidRenderState> model;
    @Shadow @Final protected ItemModelResolver itemModelResolver;
    LivingEntity player;
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    public <T extends LivingEntity, S extends LivingEntityRenderState> void updateRenderState(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci){
        if(livingEntity instanceof AbstractClientPlayer p){
            player = p;
            ItemStack itemStack = p.getInventory().getItem(FnafInventoryScreen.SLOTS_OFFSET);

            if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractSkullBlock abstractSkullBlock) {
                ((ILivingEntityMaskRenderState)livingEntityRenderState).getState().clear();
            } else {
                livingEntityRenderState.wornHeadType = null;
                livingEntityRenderState.wornHeadProfile = null;
                if (!HumanoidArmorLayer.shouldRender(itemStack, EquipmentSlot.HEAD)) {
                    this.itemModelResolver.updateForLiving(((ILivingEntityMaskRenderState)livingEntityRenderState).getState(), itemStack, ItemDisplayContext.HEAD, livingEntity);
                } else {
                    ((ILivingEntityMaskRenderState)livingEntityRenderState).getState().clear();
                }
            }
        }
    }
    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;isBodyVisible(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Z"))
    public <T extends LivingEntity, S extends LivingEntityRenderState> void renderArms(S livingEntityRenderState, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci){

        if(player != null) {
            rotateArms(player.getOffhandItem(), true);
            rotateArms(player.getMainHandItem(), false);
        }
    }

    @Unique
    public void rotateArms(ItemStack stack, boolean isOffhand){
        HumanoidModel<HumanoidRenderState> armModel = ((HumanoidModel<HumanoidRenderState>) this.model);
        if(stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof IHasArmPos hasArmPos) {
            Vec3 leftArmPos = hasArmPos.getLeftArmPos(player.getMainArm() == HumanoidArm.LEFT || (isOffhand && player.getMainArm() == HumanoidArm.RIGHT));
            Vec3 rightArmPos = hasArmPos.getRightArmPos(player.getMainArm() == HumanoidArm.RIGHT || (isOffhand && player.getMainArm() == HumanoidArm.LEFT));

            armModel.rightArm.yRot = (float) rightArmPos.x * Mth.DEG_TO_RAD;
            armModel.rightArm.xRot = (float) rightArmPos.y * Mth.DEG_TO_RAD;
            armModel.rightArm.zRot = (float) rightArmPos.z * Mth.DEG_TO_RAD;

            armModel.leftArm.yRot = (float) leftArmPos.x * Mth.DEG_TO_RAD;
            armModel.leftArm.xRot = (float) leftArmPos.y * Mth.DEG_TO_RAD;
            armModel.leftArm.zRot = (float) leftArmPos.z * Mth.DEG_TO_RAD;
        }
        else if(stack.getItem() instanceof IHasArmPos hasArmPos) {
            Vec3 leftArmPos = hasArmPos.getLeftArmPos(player.getMainArm() == HumanoidArm.LEFT || (isOffhand && player.getMainArm() == HumanoidArm.RIGHT));
            Vec3 rightArmPos = hasArmPos.getRightArmPos(player.getMainArm() == HumanoidArm.RIGHT || (isOffhand && player.getMainArm() == HumanoidArm.LEFT));

            armModel.rightArm.yRot = (float) rightArmPos.x * Mth.DEG_TO_RAD;
            armModel.rightArm.xRot = (float) rightArmPos.y * Mth.DEG_TO_RAD;
            armModel.rightArm.zRot = (float) rightArmPos.z * Mth.DEG_TO_RAD;

            armModel.leftArm.yRot = (float) leftArmPos.x * Mth.DEG_TO_RAD;
            armModel.leftArm.xRot = (float) leftArmPos.y * Mth.DEG_TO_RAD;
            armModel.leftArm.zRot = (float) leftArmPos.z * Mth.DEG_TO_RAD;
        }
    }

    //@Inject(method = "render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At("HEAD"), cancellable = true)
    //public <T extends LivingEntity, S extends LivingEntityRenderState> void render(EntityRenderState renderState, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState, CallbackInfo ci){
//
    //    Minecraft client = Minecraft.getInstance();
    //    /*if(player != null) {
    //        T entity = (T) ((IPlayerCustomModel) player).getCurrentEntity();
    //        if (entity != null) {
//
    //            EntityType<T> entityType = (EntityType<T>) entity.getType();
//
    //            EntityRendererFactory.Context context = new EntityRendererFactory.Context(
    //                    client.getEntityRenderDispatcher(), client.getItemModelManager(), client.getMapRenderer(), client.getBlockRenderManager(), client.getResourceManager(), client.getLoadedEntityModels(), new EquipmentModelLoader(), client.textRenderer
    //            );
//
    //            EntityRenderer<T, ?> renderer = (EntityRenderer<T, ?>) FnafUniverseRebuilt.RENDERER_FACTORIES.get(entityType).create(context);
//
    //            if (renderer != null) {
    //                DefaultEntityRenderer<T> entityRenderer = (DefaultEntityRenderer<T>)renderer;
//
    //                entity.setHeadYaw(entity.mimicPlayer.getBodyYaw());
    //                float bodyYawDiff = entity.mimicPlayer.getBodyYaw() - ((IPlayerCustomModel) entity.mimicPlayer).getMimicYaw();
    //                float max = 45;
    //                if (MathHelper.abs(bodyYawDiff) > max) {
    //                    float diff = ((IPlayerCustomModel) entity.mimicPlayer).getMimicYaw() > entity.mimicPlayer.getHeadYaw() ? max : -max;
    //                    ((IPlayerCustomModel) entity.mimicPlayer).setMimicYaw(entity.mimicPlayer.getBodyYaw() + diff);
    //                }
    //                if (entity.mimicPlayer.forwardSpeed > 0) {
    //                    ((IPlayerCustomModel) entity.mimicPlayer).setMimicYaw(entity.mimicPlayer.getBodyYaw());
    //                }
    //                entity.setBodyYaw(((IPlayerCustomModel) entity.mimicPlayer).getMimicYaw());
    //                entity.setPitch(entity.mimicPlayer.getPitch());
//
//
    //                EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
//
    //                entityRenderDispatcher.render(entity, 0, 0, 0, 1.0F, matrixStack, vertexConsumerProvider, i);
//
    //               //entityRenderer.render(entity, entityRenderer.createRenderState(), matrixStack, vertexConsumerProvider, i);
    //            }
    //            ci.cancel();
    //        }
    //    }*/
    //}
}

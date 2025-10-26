package net.zephyr.fnafur.mixin;

import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
    protected EntityModel<? super BipedEntityRenderState> model;
    @Shadow @Final protected ItemModelManager itemModelResolver;
    LivingEntity player;
    @Inject(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    public <T extends LivingEntity, S extends LivingEntityRenderState> void updateRenderState(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci){
        if(livingEntity instanceof AbstractClientPlayerEntity p){
            player = p;
            ItemStack itemStack = p.getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET);

            if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractSkullBlock abstractSkullBlock) {
                ((ILivingEntityMaskRenderState)livingEntityRenderState).getState().clear();
            } else {
                livingEntityRenderState.wearingSkullType = null;
                livingEntityRenderState.wearingSkullProfile = null;
                if (!ArmorFeatureRenderer.hasModel(itemStack, EquipmentSlot.HEAD)) {
                    this.itemModelResolver.updateForLivingEntity(((ILivingEntityMaskRenderState)livingEntityRenderState).getState(), itemStack, ItemDisplayContext.HEAD, livingEntity);
                } else {
                    ((ILivingEntityMaskRenderState)livingEntityRenderState).getState().clear();
                }
            }
        }
    }
    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;isVisible(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;)Z"))
    public <T extends LivingEntity, S extends LivingEntityRenderState> void renderArms(S livingEntityRenderState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci){

        if(player != null) {
            rotateArms(player.getOffHandStack(), true);
            rotateArms(player.getMainHandStack(), false);
        }
    }

    @Unique
    public void rotateArms(ItemStack stack, boolean isOffhand){
        BipedEntityModel<BipedEntityRenderState> armModel = ((BipedEntityModel<BipedEntityRenderState>) this.model);
        if(stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof IHasArmPos hasArmPos) {
            Vec3d leftArmPos = hasArmPos.getLeftArmPos(player.getMainArm() == Arm.LEFT || (isOffhand && player.getMainArm() == Arm.RIGHT));
            Vec3d rightArmPos = hasArmPos.getRightArmPos(player.getMainArm() == Arm.RIGHT || (isOffhand && player.getMainArm() == Arm.LEFT));

            armModel.rightArm.yaw = (float) rightArmPos.x * MathHelper.RADIANS_PER_DEGREE;
            armModel.rightArm.pitch = (float) rightArmPos.y * MathHelper.RADIANS_PER_DEGREE;
            armModel.rightArm.roll = (float) rightArmPos.z * MathHelper.RADIANS_PER_DEGREE;

            armModel.leftArm.yaw = (float) leftArmPos.x * MathHelper.RADIANS_PER_DEGREE;
            armModel.leftArm.pitch = (float) leftArmPos.y * MathHelper.RADIANS_PER_DEGREE;
            armModel.leftArm.roll = (float) leftArmPos.z * MathHelper.RADIANS_PER_DEGREE;
        }
        else if(stack.getItem() instanceof IHasArmPos hasArmPos) {
            Vec3d leftArmPos = hasArmPos.getLeftArmPos(player.getMainArm() == Arm.LEFT || (isOffhand && player.getMainArm() == Arm.RIGHT));
            Vec3d rightArmPos = hasArmPos.getRightArmPos(player.getMainArm() == Arm.RIGHT || (isOffhand && player.getMainArm() == Arm.LEFT));

            armModel.rightArm.yaw = (float) rightArmPos.x * MathHelper.RADIANS_PER_DEGREE;
            armModel.rightArm.pitch = (float) rightArmPos.y * MathHelper.RADIANS_PER_DEGREE;
            armModel.rightArm.roll = (float) rightArmPos.z * MathHelper.RADIANS_PER_DEGREE;

            armModel.leftArm.yaw = (float) leftArmPos.x * MathHelper.RADIANS_PER_DEGREE;
            armModel.leftArm.pitch = (float) leftArmPos.y * MathHelper.RADIANS_PER_DEGREE;
            armModel.leftArm.roll = (float) leftArmPos.z * MathHelper.RADIANS_PER_DEGREE;
        }
    }
    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At("HEAD"), cancellable = true)
    public <T extends LivingEntity, S extends LivingEntityRenderState> void render(EntityRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState, CallbackInfo ci){

        MinecraftClient client = MinecraftClient.getInstance();
        /*if(player != null) {
            T entity = (T) ((IPlayerCustomModel) player).getCurrentEntity();
            if (entity != null) {

                EntityType<T> entityType = (EntityType<T>) entity.getType();

                EntityRendererFactory.Context context = new EntityRendererFactory.Context(
                        client.getEntityRenderDispatcher(), client.getItemModelManager(), client.getMapRenderer(), client.getBlockRenderManager(), client.getResourceManager(), client.getLoadedEntityModels(), new EquipmentModelLoader(), client.textRenderer
                );

                EntityRenderer<T, ?> renderer = (EntityRenderer<T, ?>) FnafUniverseRebuilt.RENDERER_FACTORIES.get(entityType).create(context);

                if (renderer != null) {
                    DefaultEntityRenderer<T> entityRenderer = (DefaultEntityRenderer<T>)renderer;

                    entity.setHeadYaw(entity.mimicPlayer.getBodyYaw());
                    float bodyYawDiff = entity.mimicPlayer.getBodyYaw() - ((IPlayerCustomModel) entity.mimicPlayer).getMimicYaw();
                    float max = 45;
                    if (MathHelper.abs(bodyYawDiff) > max) {
                        float diff = ((IPlayerCustomModel) entity.mimicPlayer).getMimicYaw() > entity.mimicPlayer.getHeadYaw() ? max : -max;
                        ((IPlayerCustomModel) entity.mimicPlayer).setMimicYaw(entity.mimicPlayer.getBodyYaw() + diff);
                    }
                    if (entity.mimicPlayer.forwardSpeed > 0) {
                        ((IPlayerCustomModel) entity.mimicPlayer).setMimicYaw(entity.mimicPlayer.getBodyYaw());
                    }
                    entity.setBodyYaw(((IPlayerCustomModel) entity.mimicPlayer).getMimicYaw());
                    entity.setPitch(entity.mimicPlayer.getPitch());


                    EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();

                    entityRenderDispatcher.render(entity, 0, 0, 0, 1.0F, matrixStack, vertexConsumerProvider, i);

                   //entityRenderer.render(entity, entityRenderer.createRenderState(), matrixStack, vertexConsumerProvider, i);
                }
                ci.cancel();
            }
        }*/
    }
}

package net.zephyr.fnafur.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.equipment.EquipmentModelLoader;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.entity.base.DefaultEntityRenderer;
import net.zephyr.fnafur.init.block_init.PropInit;
import net.zephyr.fnafur.util.mixinAccessing.IPlayerCustomModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Shadow
    protected EntityModel<? super BipedEntityRenderState> model;
    LivingEntity player;
    @Inject(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    public <T extends LivingEntity, S extends LivingEntityRenderState> void updateRenderState(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci){
        if(livingEntity instanceof AbstractClientPlayerEntity p){
            player = p;
        }
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;isVisible(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;)Z"))
    public <T extends DefaultEntity, S extends LivingEntityRenderState> void renderArms(S livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci){

        if(player != null) {
            if(player.getMainHandStack().isOf(PropInit.COSMO_GIFT.asItem())) {
                ((BipedEntityModel<BipedEntityRenderState>) this.model).rightArm.pitch = -45;
                ((BipedEntityModel<BipedEntityRenderState>) this.model).rightArm.yaw = 0;
                ((BipedEntityModel<BipedEntityRenderState>) this.model).rightArm.roll = 0;
                ((BipedEntityModel<BipedEntityRenderState>) this.model).leftArm.pitch = -45;
                ((BipedEntityModel<BipedEntityRenderState>) this.model).leftArm.yaw = 0;
                ((BipedEntityModel<BipedEntityRenderState>) this.model).leftArm.roll = 0;
            }
        }
    }
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public <T extends DefaultEntity, S extends LivingEntityRenderState> void render(S livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci){

        MinecraftClient client = MinecraftClient.getInstance();
        if(player != null) {
            T entity = (T) ((IPlayerCustomModel) player).getCurrentEntity();
            if (entity != null) {

                EntityType<T> entityType = (EntityType<T>) entity.getType();

                EntityRendererFactory.Context context = new EntityRendererFactory.Context(
                        client.getEntityRenderDispatcher(), client.getItemModelManager(), client.getMapRenderer(), client.getBlockRenderManager(), client.getResourceManager(), client.getLoadedEntityModels(), new EquipmentModelLoader(), client.textRenderer
                );

                EntityRenderer<T, ?> renderer = (EntityRenderer<T, ?>) FnafUniverseResuited.RENDERER_FACTORIES.get(entityType).create(context);

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
        }
    }
}

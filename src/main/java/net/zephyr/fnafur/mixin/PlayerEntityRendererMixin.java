package net.zephyr.fnafur.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.client.gui.screens.CameraTabletScreen;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.entity.base.DefaultEntityRenderer;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.mixinAccessing.IPlayerCustomModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin{
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public <T extends DefaultEntity> void render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float partialTick, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci){
        T entity = (T) ((IPlayerCustomModel) abstractClientPlayerEntity).getCurrentEntity();
        MinecraftClient client = MinecraftClient.getInstance();
        if(entity != null) {
            EntityType<T> entityType = (EntityType<T>) entity.getType();

            EntityRendererFactory.Context context = new EntityRendererFactory.Context(
                    client.getEntityRenderDispatcher(), client.getItemRenderer(), client.getBlockRenderManager(), client.getEntityRenderDispatcher().getHeldItemRenderer(), client.getResourceManager(), client.getEntityModelLoader(), client.textRenderer
            );

            EntityRenderer<T> renderer = (EntityRenderer<T>) FnafUniverseResuited.RENDERER_FACTORIES.get(entityType).create(context);

            if(renderer instanceof DefaultEntityRenderer<T> entityRenderer) {

                entity.setHeadYaw(entity.mimicPlayer.getBodyYaw());
                float bodyYawDiff = entity.mimicPlayer.getBodyYaw() - ((IPlayerCustomModel)entity.mimicPlayer).getMimicYaw();
                float max = 45;
                if(MathHelper.abs(bodyYawDiff) > max) {
                    float diff = ((IPlayerCustomModel)entity.mimicPlayer).getMimicYaw() > entity.mimicPlayer.getHeadYaw() ? max : -max;
                    ((IPlayerCustomModel)entity.mimicPlayer).setMimicYaw(entity.mimicPlayer.getBodyYaw() + diff);
                }
                if(entity.mimicPlayer.forwardSpeed > 0) {
                    ((IPlayerCustomModel)entity.mimicPlayer).setMimicYaw(entity.mimicPlayer.getBodyYaw());
                }
                entity.setBodyYaw(((IPlayerCustomModel)entity.mimicPlayer).getMimicYaw());
                entity.setPitch(entity.mimicPlayer.getPitch());

                entityRenderer.render(entity, f, 1, matrixStack, vertexConsumerProvider, i);
            }
            ci.cancel();
        }
    }

    /**
     * @author zephyr
     * @reason geckolib didn't do it, so I did.
     */
    @Overwrite
    private static BipedEntityModel.ArmPose getArmPose(AbstractClientPlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isEmpty()) {
            return BipedEntityModel.ArmPose.EMPTY;
        } else {
            if (player.getActiveHand() == hand && player.getItemUseTimeLeft() > 0) {
                UseAction useAction = itemStack.getUseAction();
                if (useAction == UseAction.BLOCK) {
                    return BipedEntityModel.ArmPose.BLOCK;
                }

                if (useAction == UseAction.BOW) {
                    return BipedEntityModel.ArmPose.BOW_AND_ARROW;
                }

                if (useAction == UseAction.SPEAR) {
                    return BipedEntityModel.ArmPose.THROW_SPEAR;
                }

                if (useAction == UseAction.CROSSBOW && hand == player.getActiveHand()) {
                    return BipedEntityModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (useAction == UseAction.SPYGLASS) {
                    return BipedEntityModel.ArmPose.SPYGLASS;
                }

                if (useAction == UseAction.TOOT_HORN) {
                    return BipedEntityModel.ArmPose.TOOT_HORN;
                }

                if (useAction == UseAction.BRUSH) {
                    return BipedEntityModel.ArmPose.BRUSH;
                }
            } else if (!player.handSwinging && itemStack.isOf(Items.CROSSBOW) && CrossbowItem.isCharged(itemStack)) {
                return BipedEntityModel.ArmPose.CROSSBOW_HOLD;
            }
            else if (!player.handSwinging && itemStack.isOf(ItemInit.TABLET) && MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen) {
                return BipedEntityModel.ArmPose.CROSSBOW_HOLD;
            }

            return BipedEntityModel.ArmPose.ITEM;
        }
    }
}

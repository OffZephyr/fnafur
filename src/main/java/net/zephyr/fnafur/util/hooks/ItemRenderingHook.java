package net.zephyr.fnafur.util.hooks;

import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Direction;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.util.mixinAccessing.IHeadFeatureRendererAccessor;
import net.zephyr.fnafur.util.mixinAccessing.IHeldItemAccessor;
import net.zephyr.fnafur.util.mixinAccessing.ILivingEntityMaskRenderState;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ItemRenderingHook {
    public static boolean renderFirstPersonItem(
            AbstractClientPlayer player,
            float tickProgress,
            float pitch,
            InteractionHand hand,
            float swingProgress,
            ItemStack item,
            float equipProgress,
            PoseStack matrices,
            SubmitNodeCollector orderedRenderCommandQueue,
            int light,
            ItemInHandRenderer renderer
    ) {
        if(!((IUniversePlayer)player).hasVanniMaskEquipped()){
            ((IUniversePlayer)player).setMaskDelta(0);
        }
        ItemStack maskStack = player.getInventory().getItem(FnafInventoryScreen.SLOTS_OFFSET);
        if (((IUniversePlayer)player).hasVanniMaskEquipped()) {
            boolean bl = hand == InteractionHand.MAIN_HAND;
            if (!bl) {
                return true;
            }

            if(!((IUniversePlayer)player).hasVanniMaskOn()){
                ((IUniversePlayer)player).setMaskDelta(Math.clamp(((IUniversePlayer)player).getMaskDelta() - (Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks() / 20f), 0, 1f));
            }
            else{
                ((IUniversePlayer)player).setMaskDelta(Math.clamp(((IUniversePlayer)player).getMaskDelta() + (Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks() / 20f), 0, 1.5f));
            }

            if(((IUniversePlayer)player).getMaskDelta() > 1.42f){
                return false;
            }
            matrices.pushPose();
            HumanoidArm arm = player.getMainArm();

            boolean bl2 = arm == HumanoidArm.RIGHT;
            int l = bl2 ? 1 : -1;

            matrices.pushPose();
            renderArm(matrices, orderedRenderCommandQueue, light, HumanoidArm.LEFT);
            matrices.popPose();
            matrices.pushPose();
            renderArm(matrices, orderedRenderCommandQueue, light, HumanoidArm.RIGHT);
            matrices.popPose();


            ((IHeldItemAccessor)renderer).doSwingArm(0, 0, matrices, l, arm);

            renderer.renderItem(
                    player, maskStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, matrices, orderedRenderCommandQueue, light
            );
            matrices.popPose();

            if(((IUniversePlayer)player).hasVanniMaskOn() && !((IUniversePlayer)player).isUsingVanniMask()){
                return true;
            }
            else{
                return ((IUniversePlayer)player).getMaskDelta() > 0.00;
            }
        }
        return false;
    }

    public static void renderArm(PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, int light, HumanoidArm arm){
        boolean bl = arm != HumanoidArm.LEFT;

        AbstractClientPlayer abstractClientPlayerEntity = Minecraft.getInstance().player;
        AvatarRenderer playerEntityRenderer = (AvatarRenderer) Minecraft.getInstance().getEntityRenderDispatcher()
                .<AbstractClientPlayer>getRenderer(abstractClientPlayerEntity);
        Identifier identifier = abstractClientPlayerEntity.getSkin().body().texturePath();
        if (bl) {
            //playerEntityRenderer.renderRightArm(matrices, vertexConsumers, light, identifier, abstractClientPlayerEntity.isPartVisible(PlayerModelPart.RIGHT_SLEEVE));
        } else {
            //playerEntityRenderer.renderLeftArm(matrices, vertexConsumers, light, identifier, abstractClientPlayerEntity.isPartVisible(PlayerModelPart.LEFT_SLEEVE));
        }
    }

    public static <S extends LivingEntityRenderState, M extends EntityModel<S> & HeadedModel> void renderOnHead(CustomHeadLayer<S, M> fRenderer, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, int i, S livingEntityRenderState, float f, float g){
        ItemStackRenderState maskItemRenderState = ((ILivingEntityMaskRenderState)livingEntityRenderState).getState();

        if (!maskItemRenderState.isEmpty()) {

            if(Minecraft.getInstance().player != null && ((IUniversePlayer) Minecraft.getInstance().player).hasVanniMaskEquipped() && !((IUniversePlayer) Minecraft.getInstance().player).hasVanniMaskOn()){
                return;
            }

            matrixStack.pushPose();
            matrixStack.scale(((IHeadFeatureRendererAccessor)fRenderer).getHeadTransformation().horizontalScale(), 1.0F,  ((IHeadFeatureRendererAccessor)fRenderer).getHeadTransformation().horizontalScale());
            M entityModel = fRenderer.getParentModel();
            entityModel.root().translateAndRotate(matrixStack);
            entityModel.getHead().translateAndRotate(matrixStack);
            if (livingEntityRenderState.wornHeadType != null) {
                matrixStack.translate(0.0F,  ((IHeadFeatureRendererAccessor)fRenderer).getHeadTransformation().skullYOffset(), 0.0F);
                matrixStack.scale(1.1875F, -1.1875F, -1.1875F);
                matrixStack.translate(-0.5, 0.0, -0.5);
                SkullBlock.Type skullType = livingEntityRenderState.wornHeadType;
                SkullModelBase skullBlockEntityModel = (SkullModelBase) ((IHeadFeatureRendererAccessor)fRenderer).getHeadModels().apply(skullType);
                RenderType renderLayer = fRenderer.resolveSkullRenderType(livingEntityRenderState, skullType);
                SkullBlockRenderer.submitSkull((Direction)null, 180.0F, livingEntityRenderState.wornHeadAnimationPos, matrixStack, orderedRenderCommandQueue, i, skullBlockEntityModel, renderLayer, livingEntityRenderState.outlineColor, (ModelFeatureRenderer.CrumblingOverlay)null);
            } else {
                ((IHeadFeatureRendererAccessor)fRenderer).doTranslate(matrixStack, ((IHeadFeatureRendererAccessor)fRenderer).getHeadTransformation());
                maskItemRenderState.submit(matrixStack, orderedRenderCommandQueue, i, OverlayTexture.NO_OVERLAY, livingEntityRenderState.outlineColor);
            }

            matrixStack.popPose();
        }
    }
}

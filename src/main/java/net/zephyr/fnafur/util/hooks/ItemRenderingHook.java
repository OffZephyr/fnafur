package net.zephyr.fnafur.util.hooks;

import net.minecraft.block.SkullBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.util.mixinAccessing.IHeadFeatureRendererAccessor;
import net.zephyr.fnafur.util.mixinAccessing.IHeldItemAccessor;
import net.zephyr.fnafur.util.mixinAccessing.ILivingEntityMaskRenderState;
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
            OrderedRenderCommandQueue orderedRenderCommandQueue,
            int light,
            HeldItemRenderer renderer
    ) {
        if(!((IUniversePlayer)player).hasVanniMaskEquipped()){
            ((IUniversePlayer)player).setMaskDelta(0);
        }
        ItemStack maskStack = player.getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET);
        if (((IUniversePlayer)player).hasVanniMaskEquipped()) {
            boolean bl = hand == Hand.MAIN_HAND;
            if (!bl) {
                return true;
            }

            if(!((IUniversePlayer)player).hasVanniMaskOn()){
                ((IUniversePlayer)player).setMaskDelta(Math.clamp(((IUniversePlayer)player).getMaskDelta() - (MinecraftClient.getInstance().getRenderTickCounter().getDynamicDeltaTicks() / 20f), 0, 1f));
            }
            else{
                ((IUniversePlayer)player).setMaskDelta(Math.clamp(((IUniversePlayer)player).getMaskDelta() + (MinecraftClient.getInstance().getRenderTickCounter().getDynamicDeltaTicks() / 20f), 0, 1.5f));
            }

            if(((IUniversePlayer)player).getMaskDelta() > 1.42f){
                return false;
            }
            matrices.push();
            Arm arm = player.getMainArm();

            boolean bl2 = arm == Arm.RIGHT;
            int l = bl2 ? 1 : -1;

            matrices.push();
            renderArm(matrices, orderedRenderCommandQueue, light, Arm.LEFT);
            matrices.pop();
            matrices.push();
            renderArm(matrices, orderedRenderCommandQueue, light, Arm.RIGHT);
            matrices.pop();


            ((IHeldItemAccessor)renderer).doSwingArm(0, 0, matrices, l, arm);

            renderer.renderItem(
                    player, maskStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, matrices, orderedRenderCommandQueue, light
            );
            matrices.pop();

            if(((IUniversePlayer)player).hasVanniMaskOn() && !((IUniversePlayer)player).isUsingVanniMask()){
                return true;
            }
            else{
                return ((IUniversePlayer)player).getMaskDelta() > 0.00;
            }
        }
        return false;
    }

    public static void renderArm(MatrixStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, int light, Arm arm){
        boolean bl = arm != Arm.LEFT;

        AbstractClientPlayerEntity abstractClientPlayerEntity = MinecraftClient.getInstance().player;
        PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer)MinecraftClient.getInstance().getEntityRenderDispatcher()
                .<AbstractClientPlayerEntity>getRenderer(abstractClientPlayerEntity);
        Identifier identifier = abstractClientPlayerEntity.getSkin().body().texturePath();
        if (bl) {
            //playerEntityRenderer.renderRightArm(matrices, vertexConsumers, light, identifier, abstractClientPlayerEntity.isPartVisible(PlayerModelPart.RIGHT_SLEEVE));
        } else {
            //playerEntityRenderer.renderLeftArm(matrices, vertexConsumers, light, identifier, abstractClientPlayerEntity.isPartVisible(PlayerModelPart.LEFT_SLEEVE));
        }
    }

    public static <S extends LivingEntityRenderState, M extends EntityModel<S> & ModelWithHead> void renderOnHead(HeadFeatureRenderer<S, M> fRenderer, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, int i, S livingEntityRenderState, float f, float g){
        ItemRenderState maskItemRenderState = ((ILivingEntityMaskRenderState)livingEntityRenderState).getState();

        if (!maskItemRenderState.isEmpty()) {

            if(MinecraftClient.getInstance().player != null && ((IUniversePlayer)MinecraftClient.getInstance().player).hasVanniMaskEquipped() && !((IUniversePlayer)MinecraftClient.getInstance().player).hasVanniMaskOn()){
                return;
            }

            matrixStack.push();
            matrixStack.scale(((IHeadFeatureRendererAccessor)fRenderer).getHeadTransformation().horizontalScale(), 1.0F,  ((IHeadFeatureRendererAccessor)fRenderer).getHeadTransformation().horizontalScale());
            M entityModel = fRenderer.getContextModel();
            entityModel.getRootPart().applyTransform(matrixStack);
            entityModel.getHead().applyTransform(matrixStack);
            if (livingEntityRenderState.wearingSkullType != null) {
                matrixStack.translate(0.0F,  ((IHeadFeatureRendererAccessor)fRenderer).getHeadTransformation().skullYOffset(), 0.0F);
                matrixStack.scale(1.1875F, -1.1875F, -1.1875F);
                matrixStack.translate(-0.5, 0.0, -0.5);
                SkullBlock.SkullType skullType = livingEntityRenderState.wearingSkullType;
                SkullBlockEntityModel skullBlockEntityModel = (SkullBlockEntityModel) ((IHeadFeatureRendererAccessor)fRenderer).getHeadModels().apply(skullType);
                RenderLayer renderLayer = fRenderer.getRenderLayer(livingEntityRenderState, skullType);
                SkullBlockEntityRenderer.render((Direction)null, 180.0F, livingEntityRenderState.headItemAnimationProgress, matrixStack, orderedRenderCommandQueue, i, skullBlockEntityModel, renderLayer, livingEntityRenderState.outlineColor, (ModelCommandRenderer.CrumblingOverlayCommand)null);
            } else {
                ((IHeadFeatureRendererAccessor)fRenderer).doTranslate(matrixStack, ((IHeadFeatureRendererAccessor)fRenderer).getHeadTransformation());
                maskItemRenderState.render(matrixStack, orderedRenderCommandQueue, i, OverlayTexture.DEFAULT_UV, livingEntityRenderState.outlineColor);
            }

            matrixStack.pop();
        }
    }
}

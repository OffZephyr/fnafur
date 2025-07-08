package net.zephyr.fnafur.mixin;


import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.zephyr.fnafur.util.mixinAccessing.ILivingEntityMaskRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(HeadFeatureRenderer.class)
public class HeadFeatureRendererMixin {
    @Shadow
    private HeadFeatureRenderer.HeadTransformation headTransformation;
    @Shadow
    private Function<SkullBlock.SkullType, SkullBlockEntityModel> headModels;
    @Shadow
    public static void translate(MatrixStack matrices, HeadFeatureRenderer.HeadTransformation transformation){

    }
    @Inject(method = "render", at = @At("HEAD"))
    <S extends LivingEntityRenderState, M extends EntityModel<S> & ModelWithHead> void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, S livingEntityRenderState, float f, float g, CallbackInfo ci){
        ItemRenderState maskItemRenderState = ((ILivingEntityMaskRenderState)livingEntityRenderState).getState();

        if (!maskItemRenderState.isEmpty()) {
            matrixStack.push();
            matrixStack.scale(this.headTransformation.horizontalScale(), 1.0F, this.headTransformation.horizontalScale());
            M entityModel = ((FeatureRenderer<S, M>)(Object)this).getContextModel();
            entityModel.getRootPart().applyTransform(matrixStack);
            entityModel.getHead().applyTransform(matrixStack);
            if (livingEntityRenderState.wearingSkullType != null) {
                matrixStack.translate(0.0F, this.headTransformation.skullYOffset(), 0.0F);
                matrixStack.scale(1.1875F, -1.1875F, -1.1875F);
                matrixStack.translate(-0.5, 0.0, -0.5);
                SkullBlock.SkullType skullType = livingEntityRenderState.wearingSkullType;
                SkullBlockEntityModel skullBlockEntityModel = (SkullBlockEntityModel)this.headModels.apply(skullType);
                RenderLayer renderLayer = SkullBlockEntityRenderer.getRenderLayer(skullType, livingEntityRenderState.wearingSkullProfile);
                SkullBlockEntityRenderer.renderSkull(
                        null, 180.0F, livingEntityRenderState.headItemAnimationProgress, matrixStack, vertexConsumerProvider, i, skullBlockEntityModel, renderLayer
                );
            } else {
                translate(matrixStack, this.headTransformation);
                maskItemRenderState.render(matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV);
            }

            matrixStack.pop();
        }
    }
}

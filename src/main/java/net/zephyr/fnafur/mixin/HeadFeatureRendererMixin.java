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
import net.zephyr.fnafur.util.hooks.ItemRenderingHook;
import net.zephyr.fnafur.util.mixinAccessing.IHeadFeatureRendererAccessor;
import net.zephyr.fnafur.util.mixinAccessing.ILivingEntityMaskRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(HeadFeatureRenderer.class)
public class HeadFeatureRendererMixin implements IHeadFeatureRendererAccessor {
    @Shadow
    private HeadFeatureRenderer.HeadTransformation headTransformation;
    @Shadow
    private Function<SkullBlock.SkullType, SkullBlockEntityModel> headModels;
    @Shadow
    public static void translate(MatrixStack matrices, HeadFeatureRenderer.HeadTransformation transformation){

    }
    @Inject(method = "render", at = @At("HEAD"))
    <S extends LivingEntityRenderState, M extends EntityModel<S> & ModelWithHead> void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, S livingEntityRenderState, float f, float g, CallbackInfo ci){
        ItemRenderingHook.renderOnHead(((HeadFeatureRenderer)(Object)this), matrixStack, vertexConsumerProvider, i, livingEntityRenderState, f, g);
    }

    @Override
    public HeadFeatureRenderer.HeadTransformation getHeadTransformation() {
        return headTransformation;
    }

    @Override
    public Function<SkullBlock.SkullType, SkullBlockEntityModel> getHeadModels() {
        return headModels;
    }

    @Override
    public void doTranslate(MatrixStack matrices, HeadFeatureRenderer.HeadTransformation transformation) {
        translate(matrices, transformation);
    }
}

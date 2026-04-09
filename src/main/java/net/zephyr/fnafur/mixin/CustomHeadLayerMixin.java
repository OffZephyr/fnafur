package net.zephyr.fnafur.mixin;


import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.zephyr.fnafur.util.hooks.ItemRenderingHook;
import net.zephyr.fnafur.util.mixinAccessing.IHeadFeatureRendererAccessor;
import net.zephyr.fnafur.util.mixinAccessing.ILivingEntityMaskRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(CustomHeadLayer.class)
public class CustomHeadLayerMixin implements IHeadFeatureRendererAccessor {
    @Shadow
    private CustomHeadLayer.Transforms transforms;
    @Shadow
    private Function<SkullBlock.Type, SkullModelBase> skullModels;
    @Shadow
    public static void translateToHead(PoseStack matrices, CustomHeadLayer.Transforms transformation){

    }
    @Inject(method = "submit", at = @At("HEAD"))
    <S extends LivingEntityRenderState, M extends EntityModel<S> & HeadedModel> void render(PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, int i, S livingEntityRenderState, float f, float g, CallbackInfo ci){
        ItemRenderingHook.renderOnHead(((CustomHeadLayer)(Object)this), matrixStack, orderedRenderCommandQueue, i, livingEntityRenderState, f, g);
    }

    @Override
    public CustomHeadLayer.Transforms getHeadTransformation() {
        return transforms;
    }

    @Override
    public Function<SkullBlock.Type, SkullModelBase> getHeadModels() {
        return skullModels;
    }

    @Override
    public void doTranslate(PoseStack matrices, CustomHeadLayer.Transforms transformation) {
        translateToHead(matrices, transformation);
    }
}

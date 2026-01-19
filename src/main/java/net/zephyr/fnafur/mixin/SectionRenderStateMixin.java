package net.zephyr.fnafur.mixin;

import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.render.BlockRenderLayerGroup;
import net.minecraft.client.render.SectionRenderState;
import net.zephyr.fnafur.decals.DecalManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SectionRenderState.class)
public class SectionRenderStateMixin {
    @Inject(method = "renderSection", at = @At(value = "HEAD"), cancellable = true)
    void renderSectionHook(BlockRenderLayerGroup group, GpuSampler sampler, CallbackInfo ci){
        DecalManager.DecalRenderHook((SectionRenderState)(Object)this, group, sampler);
        ci.cancel();
    }
}

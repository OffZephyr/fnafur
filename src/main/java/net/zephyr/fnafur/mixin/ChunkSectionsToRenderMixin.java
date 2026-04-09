package net.zephyr.fnafur.mixin;

import com.mojang.blaze3d.textures.GpuSampler;
import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.zephyr.fnafur.rendering.TerrainRenderHook;
import net.zephyr.fnafur.rendering.decals.DecalManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkSectionsToRender.class)
public class ChunkSectionsToRenderMixin {
    @Inject(method = "renderGroup", at = @At(value = "HEAD"), cancellable = true)
    void renderSectionHook(ChunkSectionLayerGroup group, GpuSampler sampler, CallbackInfo ci){
        TerrainRenderHook.render((ChunkSectionsToRender)(Object)this, group, sampler);
//        DecalManager.DecalRenderHook((SectionRenderState)(Object)this, group, sampler);
        ci.cancel();
    }
}

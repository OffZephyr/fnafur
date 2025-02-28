package net.zephyr.fnafur.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.*;
import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.zephyr.fnafur.blocks.camera_desk.CameraRenderer;
import net.zephyr.fnafur.client.gui.screens.CameraTabletScreen;
import net.zephyr.fnafur.client.lighting.RGBLightMap;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Unique
    RGBLightMap rgbLightMap = new RGBLightMap();
    @Shadow
    private boolean dirty;
    @Shadow
    private MinecraftClient client;
    @Shadow
    private float flickerIntensity;
    @Shadow
    private SimpleFramebuffer lightmapFramebuffer;
    @Shadow
    private GameRenderer renderer;

    @Shadow
    private float getDarknessFactor(float delta) {
        return 0.0f;
    }
    @Shadow
    private float getDarkness(LivingEntity entity, float factor, float delta) {
        return 0.0f;
    }

    @Inject(method = "enable", at = @At("TAIL"))
    public void enable(CallbackInfo ci){
        rgbLightMap.enable();
    }
    @Inject(method = "disable", at = @At("TAIL"))
    public void disable(CallbackInfo ci){
        rgbLightMap.disable();
    }
    @Inject(method = "close", at = @At("TAIL"))
    public void close(CallbackInfo ci){
        rgbLightMap.close();
    }

    /**
     * @author Zephyr
     * @reason COLORED LIGHTIIIIING
     */
    @Overwrite
    public void update(float delta) {
        if (true) {
            this.dirty = false;
            Profiler profiler = Profilers.get();
            profiler.push("lightTex");
            ClientWorld clientWorld = this.client.world;
            if (clientWorld != null) {
                float f = clientWorld.getSkyBrightness(1.0F);
                float g;
                if (clientWorld.getLightningTicksLeft() > 0) {
                    g = 1.0F;
                } else {
                    g = f * 0.95F + 0.05F;
                }

                float h = this.client.options.getDarknessEffectScale().getValue().floatValue();
                float i = this.getDarknessFactor(delta) * h;
                float j = this.getDarkness(this.client.player, i, delta) * h;
                float k = this.client.player.getUnderwaterVisibility();
                float l;
                if (this.client.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
                    l = GameRenderer.getNightVisionStrength(this.client.player, delta);
                } else if (k > 0.0F && this.client.player.hasStatusEffect(StatusEffects.CONDUIT_POWER)) {
                    l = k;
                } else {
                    l = 0.0F;
                }

                Vector3f vector3f = new Vector3f(f, f, 1.0F).lerp(new Vector3f(1.0F, 1.0F, 1.0F), 0.35F);
                float m = this.flickerIntensity + 1.5F;
                float n = clientWorld.getDimension().ambientLight();
                boolean bl = clientWorld.getDimensionEffects().shouldBrightenLighting();
                float o = this.client.options.getGamma().getValue().floatValue();
                ShaderProgram shaderProgram = (ShaderProgram) Objects.requireNonNull(RenderSystem.setShader(ShaderProgramKeys.LIGHTMAP), "Lightmap shader not loaded");

                rgbLightMap.updateShader(shaderProgram, n, g, m, bl, vector3f, l, j, this.renderer.getSkyDarkness(delta), o, i);

                rgbLightMap.renderBuffer(shaderProgram);
                profiler.pop();
            }
        }
    }
}

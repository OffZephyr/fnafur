package net.zephyr.fnafur.mixin;

import com.google.common.collect.Lists;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.PostEffectProcessor;
import net.zephyr.fnafur.util.mixinAccessing.IPostProcessorUniform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PostEffectProcessor.class)
public class PostEffectProcessorMixin implements IPostProcessorUniform {
    @Shadow
    List<PostEffectPass> passes = Lists.newArrayList();

    @Override
    public void set2FloatUniforms(String name, float value1, float value2) {
        for (PostEffectPass postEffectPass : this.passes) {
            postEffectPass.getProgram().getUniformByNameOrDummy(name).set(value1, value2);
        }
    }

    public void set3FloatUniforms(String name, float value1, float value2, float value3){
        for (PostEffectPass postEffectPass : this.passes) {
            postEffectPass.getProgram().getUniformByNameOrDummy(name).set(value1, value2, value3);
        }
    }
}

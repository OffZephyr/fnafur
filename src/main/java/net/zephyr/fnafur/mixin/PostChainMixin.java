package net.zephyr.fnafur.mixin;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.PostChain;
import net.zephyr.fnafur.util.mixinAccessing.IPostProcessorUniform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PostChain.class)
public class PostChainMixin implements IPostProcessorUniform {
    @Shadow
    List<PostPass> passes = Lists.newArrayList();

    @Override
    public void set2FloatUniforms(String name, float value1, float value2) {
        for (PostPass postEffectPass : this.passes) {
            //postEffectPass.getProgram().getUniform(name).set(value1, value2);
        }
    }

    public void set3FloatUniforms(String name, float value1, float value2, float value3){
        for (PostPass postEffectPass : this.passes) {
            //postEffectPass.getProgram().getUniform(name).set(value1, value2, value3);
        }
    }
}

package net.zephyr.fnafur.mixin;

import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.client.lighting.ILightColorGetter;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Block.class)
public class BlockMixin implements ILightColorGetter {
    @Unique
    Vector3f lightColor = new Vector3f(1.0f, 1.0f, 1.0f);


    @Override
    public Vector3f getLightColor() {
        return lightColor;
    }

    @Override
    public void setLightColor(Vector3f vec) {
        lightColor = vec;
    }
}

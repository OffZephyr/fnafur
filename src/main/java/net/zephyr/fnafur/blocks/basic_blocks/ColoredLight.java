package net.zephyr.fnafur.blocks.basic_blocks;

import net.minecraft.block.RedstoneLampBlock;
import net.zephyr.fnafur.client.lighting.ILightColorGetter;
import org.joml.Vector3f;

public class ColoredLight extends RedstoneLampBlock {
    public ColoredLight(Settings settings) {
        super(settings);
        ((ILightColorGetter)this).setLightColor(new Vector3f(1.0f, 0.0f, 0.0f));
    }
}

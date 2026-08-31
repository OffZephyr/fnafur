package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.function.Function;

public interface IHeadFeatureRendererAccessor {
    CustomHeadLayer.Transforms getHeadTransformation();
    Function<SkullBlock.Type, SkullModelBase> getHeadModels();

    void doTranslate(PoseStack matrices, CustomHeadLayer.Transforms transformation);
}

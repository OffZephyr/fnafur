package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.Function;

public interface IHeadFeatureRendererAccessor {
    HeadFeatureRenderer.HeadTransformation getHeadTransformation();
    Function<SkullBlock.SkullType, SkullBlockEntityModel> getHeadModels();

    void doTranslate(MatrixStack matrices, HeadFeatureRenderer.HeadTransformation transformation);
}

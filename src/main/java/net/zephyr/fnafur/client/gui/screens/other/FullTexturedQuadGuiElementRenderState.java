package net.zephyr.fnafur.client.gui.screens.other;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.render.TextureSetup;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

@Environment(EnvType.CLIENT)
public record FullTexturedQuadGuiElementRenderState(
        RenderPipeline pipeline,
        TextureSetup textureSetup,
        Matrix3x2f pose,
        int x1,
        int y1,
        int x2,
        int y2,
        int x3,
        int y3,
        int x4,
        int y4,
        float u1,
        float u2,
        float u3,
        float u4,
        float v1,
        float v2,
        float v3,
        float v4,
        int color,
        @Nullable ScreenRectangle scissorArea,
        @Nullable ScreenRectangle bounds
) implements GuiElementRenderState {
    public FullTexturedQuadGuiElementRenderState(
            RenderPipeline pipeline,
            TextureSetup textureSetup,
            Matrix3x2f pose,
            int x1,
            int y1,
            int x2,
            int y2,
            int x3,
            int y3,
            int x4,
            int y4,
            float u1,
            float u2,
            float u3,
            float u4,
            float v1,
            float v2,
            float v3,
            float v4,
            int color,
            @Nullable ScreenRectangle scissorArea
    ) {
        this(pipeline, textureSetup, pose, x1, y1, x2, y2, x3, y3, x4, y4, u1, u2, u3, u4, v1, v2, v3, v4, color, scissorArea, createBounds(x1, y1, x2, y2, pose, scissorArea));
    }

    @Override
    public void buildVertices(VertexConsumer vertices) {
        vertices.addVertexWith2DPose(this.pose(), this.x1(), this.y1()).setUv(this.u1(), this.v1()).setColor(this.color());
        vertices.addVertexWith2DPose(this.pose(), this.x2(), this.y2()).setUv(this.u2(), this.v2()).setColor(this.color());
        vertices.addVertexWith2DPose(this.pose(), this.x3(), this.y3()).setUv(this.u3(), this.v3()).setColor(this.color());
        vertices.addVertexWith2DPose(this.pose(), this.x4(), this.y4()).setUv(this.u4(), this.v4()).setColor(this.color());
    }

    @Nullable
    private static ScreenRectangle createBounds(int x1, int y1, int x3, int y3, Matrix3x2f pose, @Nullable ScreenRectangle scissorArea) {
        ScreenRectangle screenRect = new ScreenRectangle(x1, y1, x3 - x1, y3 - y1).transformMaxBounds(pose);
        return scissorArea != null ? scissorArea.intersection(screenRect) : screenRect;
    }
}
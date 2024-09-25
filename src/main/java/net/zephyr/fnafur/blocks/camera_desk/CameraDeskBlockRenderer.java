package net.zephyr.fnafur.blocks.camera_desk;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.model.*;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.client.JavaModels;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;

public class CameraDeskBlockRenderer  implements BlockEntityRenderer<CameraDeskBlockEntity> {
    MinecraftClient client;
    Framebuffer framebuffer;
    private static final String SCREEN = "screen";
    private final ModelPart model;




    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(SCREEN, ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 32, 32);
    }
    public CameraDeskBlockRenderer(BlockEntityRendererFactory.Context context){
        client = MinecraftClient.getInstance();
        //framebuffer = new WindowFramebuffer(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
        framebuffer = client.getFramebuffer();
        ModelPart modelPart = context.getLayerModelPart(JavaModels.CAMERA_SCREEN);
        this.model = modelPart.getChild(SCREEN);

    }
    @Override
    public void render(CameraDeskBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

    }
}

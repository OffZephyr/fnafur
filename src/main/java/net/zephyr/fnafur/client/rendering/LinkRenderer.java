package net.zephyr.fnafur.client.rendering;

import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.item.tools.WrenchItem;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class LinkRenderer {

    static int frameCount = 0;
    static float moveLerp = 0;



    public static void renderLinks(WorldRenderContext worldRenderContext) {
        MatrixStack matrices = worldRenderContext.matrices();
        matrices.push();
        Vec3d camPos = MinecraftClient.getInstance().gameRenderer.getCamera().getCameraPos();
        matrices.translate(camPos.multiply(-1));
        render(matrices, worldRenderContext.commandQueue(), worldRenderContext.consumers());
        matrices.pop();
    }

    public static void render(MatrixStack matrices, OrderedRenderCommandQueue queue, VertexConsumerProvider vertexConsumers) {

        moveLerp += MinecraftClient.getInstance().getRenderTickCounter().getDynamicDeltaTicks();
        int cutLerp = (int) moveLerp / 5;
        frameCount = cutLerp - ((cutLerp / 4) * 4);

        if (MinecraftClient.getInstance().player != null && !((IUniversePlayer) MinecraftClient.getInstance().player).isUsingVanniMask()) {
            return;
        }

        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        matrices.push();
        BlockPos selectPos = BlockPos.ORIGIN;
        if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.getMainHandStack().getItem() instanceof WrenchItem) {
            NbtCompound nbt = ItemUtil.getNbt(MinecraftClient.getInstance().player.getMainHandStack());
            if (nbt.contains("startLink")) selectPos = nbt.get("startLink", BlockPos.CODEC).orElse(BlockPos.ORIGIN);

            IEntityDataSaver ent = null;
            for (IEntityDataSaver ent2 : LinkSource.allSources) {
                if (ent2 instanceof BlockEntity bent) {
                    if (bent.getPos().equals(selectPos)) {
                        ent = ent2;
                        break;
                    }
                } else if (ent2 instanceof Entity ent3) {
                    if (ent3.getBlockPos().equals(selectPos)) {
                        ent = (IEntityDataSaver) ent3;
                        break;
                    }
                }
            }

            if (ent != null) {
                matrices.push();

                matrices.translate(0, 0.25, 0);

                Identifier texture = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/other/link/link_" + frameCount + ".png");

                Vec3d vec1;
                if (ent instanceof BlockEntity bent) {
                    vec1 = bent.getPos().toCenterPos();
                } else {
                    Entity ent2 = (Entity) ent;
                    vec1 = new Vec3d(ent2.getX(), ent2.getEyeY(), ent2.getZ());
                }
                assert MinecraftClient.getInstance().crosshairTarget != null;
                Vec3d vec2 = MinecraftClient.getInstance().crosshairTarget.getPos();

                queue.submitCustom(matrices, RenderLayers.outlineNoCull(texture), ((matricesEntry, vertexConsumer) -> {
                    drawLink(vec1, vec2, vertexConsumer, matricesEntry, true);
                }));

                matrices.pop();
            }
        }

        List<IEntityDataSaver> blueTargets = new ArrayList<>();
        for (IEntityDataSaver ent : LinkSource.allSources) {

            for (int i = 0; i < ((LinkSource) ent).getTargets().size(); i++) {
                IEntityDataSaver target = ((LinkSource) ent).getTargets().get(i);


                matrices.push();

                matrices.translate(0, 0.25, 0);

                Identifier texture = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/other/link/link_" + frameCount + ".png");

                queue.submitCustom(matrices, RenderLayers.outlineNoCull(texture), ((matricesEntry, vertexConsumer) -> {

                    Vec3d vec1 = Vec3d.ZERO;
                    if (ent instanceof BlockEntity bent) {
                        vec1 = bent.getPos().toCenterPos();
                    } else if (ent instanceof Entity ent2) {
                        vec1 = new Vec3d(ent2.getX(), ent2.getEyeY(), ent2.getZ());
                    }
                    Vec3d vec2 = Vec3d.ZERO;
                    if (target instanceof BlockEntity bent) {
                        vec2 = bent.getPos().toCenterPos();
                    } else if (target instanceof Entity ent2) {
                        vec2 = new Vec3d(ent2.getX(), ent2.getEyeY(), ent2.getZ());
                    }

                    drawLink(vec1, vec2, vertexConsumer, matricesEntry, false);
                }));

                matrices.pop();
            }
        }

        for (IEntityDataSaver ent : LinkSource.allSources) {

            matrices.push();

            Vec3d vec;
            BlockPos checkPos = BlockPos.ORIGIN;
            if (ent instanceof BlockEntity bent) {
                vec = bent.getPos().toCenterPos();
                checkPos = bent.getPos();
            } else if (ent instanceof Entity ent2) {
                vec = new Vec3d(ent2.getX(), ent2.getEyeY(), ent2.getZ());
                checkPos = ent2.getBlockPos();
            } else {
                vec = Vec3d.ZERO;
            }

            boolean bl = !(selectPos != BlockPos.ORIGIN && selectPos.equals(checkPos));

            boolean isTarget = ent instanceof LinkTarget;
            String name = isTarget ? "source_target_" : "source_";
            String color = ((LinkSource) ent).getTargets().isEmpty() && bl ? "red" : "blue";
            String t = "textures/other/link/" + name + color;
            if (isTarget) t = isTarget && !((LinkTarget) ent).getSources().isEmpty() ? t + "_link" : t;
            Identifier texture = Identifier.of(FnafUniverseRebuilt.MOD_ID, t + ".png");

            float offset = (float) Math.cos(moveLerp / 20f);
            matrices.translate(0, (offset / 10) - 0.25f, 0);

            float distance2 = 0;
            for (IEntityDataSaver ent2 : ((LinkSource) ent).getTargets()) {
                if (ent2 instanceof BlockEntity bent) {
                    distance2 = Math.max(distance2, (float) camera.getCameraPos().distanceTo(bent.getPos().toCenterPos()));
                } else if (ent2 instanceof Entity ent3) {
                    distance2 = Math.max(distance2, (float) camera.getCameraPos().distanceTo(new Vec3d(ent3.getX(), ent3.getEyeY(), ent3.getZ())));
                }
            }

            float distance = (float) camera.getCameraPos().distanceTo(vec);
            float scale = Math.clamp(4 - distance, 0, 1.2f);
            float scale2 = !bl ? 1.2f : distance2 == 0 ? 0 : Math.clamp(4 - distance2, 0, 1.2f);

            queue.submitCustom(matrices, RenderLayers.outlineNoCull(texture), ((matricesEntry, vertexConsumer) -> {
            }));
            drawIcon(matrices, vec, queue, RenderLayers.outlineNoCull(texture), Math.max(scale, scale2));

            if (ent.getPersistentData().contains("connectionIndex")) {
                int index = ent.getPersistentData().getInt("connectionIndex", 0);

                StyleSpriteSource spriteFont = new StyleSpriteSource.Font(Identifier.of(FnafUniverseRebuilt.MOD_ID, "metropolis"));
                Style style = Style.EMPTY.withFont(spriteFont);
                Text text = Text.literal("" + index).setStyle(style);

                for (IEntityDataSaver target : ((LinkSource) ent).getTargets()) {
                    if (target instanceof BlockEntity) {
                        int textColor = target.getPersistentData().getInt("usedConnectionIndex", 0) == index ? 0xFF48A7E7 : 0xFFE53E32;
                        drawText(text, matrices, vec, vertexConsumers, Math.max(scale, scale2), textColor);
                    }
                }
            }

            matrices.pop();

            blueTargets.addAll(((LinkSource) ent).getTargets());
        }
        for (int i = 0; i < LinkTarget.allTargets.size(); i++) {
            IEntityDataSaver ent = LinkTarget.allTargets.get(i);

            if (ent instanceof Entity ent2 && !(MinecraftClient.getInstance().world.getEntityById(ent2.getId()) instanceof Entity))
                continue;
            if (ent instanceof LinkSource) continue;
            matrices.push();

            String name = "target_";
            String color = blueTargets.contains(ent) ? "blue" : "red";
            Identifier texture = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/other/link/" + name + color + ".png");

            float offset = (float) Math.sin(moveLerp / 20f);
            matrices.translate(0, (offset / 10) - 0.25f, 0);

            Vec3d vec = Vec3d.ZERO;
            if (ent instanceof BlockEntity bent) {
                vec = bent.getPos().toCenterPos();
            } else if (ent instanceof Entity ent2) {
                vec = new Vec3d(ent2.getX(), ent2.getEyeY(), ent2.getZ());
            }

            float distance2 = 0;
            for (IEntityDataSaver ent2 : ((LinkTarget) ent).getSources()) {
                if (ent2 instanceof BlockEntity bent) {
                    distance2 = Math.max(distance2, (float) camera.getCameraPos().distanceTo(bent.getPos().toCenterPos()));
                } else if (ent2 instanceof Entity ent3) {
                    distance2 = Math.max(distance2, (float) camera.getCameraPos().distanceTo(new Vec3d(ent3.getX(), ent3.getEyeY(), ent3.getZ())));
                }
            }

            float distance = (float) camera.getCameraPos().distanceTo(vec);
            float scale = Math.clamp(4 - distance, 0, 1.2f);
            float scale2 = distance2 == 0 ? 0 : Math.clamp(4 - distance2, 0, 1.2f);

            drawIcon(matrices, vec, queue, RenderLayers.outlineNoCull(texture), Math.max(scale, scale2));

            matrices.pop();
        }
        matrices.pop();
    }

    private static void drawLink(Vec3d vec1, Vec3d vec2, VertexConsumer buffer, MatrixStack.Entry entry, boolean forceScale) {

        Vec3d length = (vec2.add(vec1.multiply(-1)));

        double lengthD = length.length();

        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        Vector3f normal = new Vector3f(0, 0, 1);


        float distance1 = (float) camera.getCameraPos().distanceTo(vec1);
        float distance2 = (float) camera.getCameraPos().distanceTo(vec2);

        float scale1 = Math.clamp(4 - distance1 , 0, 1.2f);
        float scale2 = forceScale ? 1.2f : Math.clamp(4 - distance2 , 0, 1.2f);
        float scale = Math.max(scale1, scale2);

        if(scale > 0){

            float offset = -moveLerp / 64f;
            float tWidth = (float) (0.5f * lengthD);
            float tHeight = 0.5f;
            float vHeight = 0.5f * scale;

            buffer.vertex(entry.getPositionMatrix(), (float) vec1.x, (float) vec1.y + vHeight, (float) vec1.z)
                    .texture(0.5f - tWidth + offset, 0.5f - tHeight)
                    .color(0xFFFFFFFF)
                    .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                    .overlay(OverlayTexture.DEFAULT_UV)
                    .normal(normal.x(), normal.y(), normal.z())
            ;
            buffer.vertex(entry.getPositionMatrix(), (float) vec1.x, (float) vec1.y - vHeight, (float) vec1.z)
                    .texture(0.5f - tWidth + offset, 0.5f + tHeight)
                    .color(0xFFFFFFFF)
                    .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                    .overlay(OverlayTexture.DEFAULT_UV)
                    .normal(normal.x(), normal.y(), normal.z())
            ;

            buffer.vertex(entry.getPositionMatrix(), (float) vec2.x, (float) vec2.y - vHeight, (float) vec2.z)
                    .texture(0.5f + tWidth + offset, 0.5f + tHeight)
                    .color(0xFFFFFFFF)
                    .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                    .overlay(OverlayTexture.DEFAULT_UV)
                    .normal(normal.x(), normal.y(), normal.z())
            ;
            buffer.vertex(entry.getPositionMatrix(), (float) vec2.x, (float) vec2.y + vHeight, (float) vec2.z)
                    .texture(0.5f + tWidth + offset, 0.5f - tHeight)
                    .color(0xFFFFFFFF)
                    .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                    .overlay(OverlayTexture.DEFAULT_UV)
                    .normal(normal.x(), normal.y(), normal.z())
            ;
        }
    }

    static void drawIcon(MatrixStack matrices, Vec3d vec, OrderedRenderCommandQueue queue, RenderLayer layer, float scale){
        matrices.push();
        matrices.translate(vec);

        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        Vector3f normal = new Vector3f(0, 0, 1);

        matrices.translate(0, 0.25f, 0);
        matrices.multiply(new Quaternionf().rotationXYZ(0, (float) -Math.toRadians(camera.getCameraYaw()), (float) Math.PI));
        matrices.translate(0, -0.25f, 0);


        if(scale > 0){
            queue.submitCustom(matrices, layer, ((matricesEntry, vertexConsumer) -> {
                drawQuad(matricesEntry, vertexConsumer, normal, scale);
            }));
        }
        matrices.pop();
    }
    static void drawText(Text text, MatrixStack matrices, Vec3d vec, VertexConsumerProvider vertexConsumers, float scale, int color){
        matrices.push();
        matrices.translate(vec);

        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();

        matrices.translate(0, 1, 0);
        matrices.scale(0.05f * scale, 0.05f * scale, 0.05f * scale);
        matrices.translate(0, 0.25f, 0);
        matrices.multiply(new Quaternionf().rotationXYZ(0, (float) -Math.toRadians(camera.getCameraYaw()), (float) Math.PI));
        matrices.translate(0, -0.25f, 0);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        float width = textRenderer.getWidth(text);
        if(scale > 0){
            textRenderer.draw(
                            text,
                            0.0f - width/2f,
                            -8,
                    color,
                            false,
                            matrices.peek().getPositionMatrix(),
                            vertexConsumers,
                            TextRenderer.TextLayerType.SEE_THROUGH,
                            0,
                            LightmapTextureManager.MAX_LIGHT_COORDINATE
                    );
        }

        matrices.pop();
    }

    public static void drawQuad(MatrixStack.Entry entry, VertexConsumer buffer, Vector3f normal, float scale){

        float tWidth = 0.5f;
        float tHeight = 0.5f;
        float vWidth = 0.5f * scale;
        float vHeight1 = 0.75f * scale;
        float vHeight2 = 0.25f * scale;
        buffer.vertex(entry.getPositionMatrix(), -vWidth , -vHeight1, 0.0f)
                .texture(0.5f - tWidth, 0.5f - tHeight)
                .color(0xFFFFFFFF)
                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                .overlay(OverlayTexture.DEFAULT_UV)
                .normal(normal.x(), normal.y(), normal.z())
        ;
        buffer.vertex(entry.getPositionMatrix(), -vWidth, vHeight2, 0.0f)
                .texture(0.5f - tWidth, 0.5f + tHeight)
                .color(0xFFFFFFFF)
                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                .overlay(OverlayTexture.DEFAULT_UV)
                .normal(normal.x(), normal.y(), normal.z())
        ;
        buffer.vertex(entry.getPositionMatrix(), vWidth, vHeight2, 0.0f)
                .texture(0.5f + tWidth, 0.5f + tHeight)
                .color(0xFFFFFFFF)
                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                .overlay(OverlayTexture.DEFAULT_UV)
                .normal(normal.x(), normal.y(), normal.z())
        ;
        buffer.vertex(entry.getPositionMatrix(), vWidth, -vHeight1, 0.0f)
                .texture(0.5f + tWidth, 0.5f - tHeight)
                .color(0xFFFFFFFF)
                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                .overlay(OverlayTexture.DEFAULT_UV)
                .normal(normal.x(), normal.y(), normal.z())
        ;
    }
}

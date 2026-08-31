package net.zephyr.fnafur.client.rendering;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
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
        PoseStack matrices = worldRenderContext.matrices();
        matrices.pushPose();
        Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().position();
        matrices.translate(camPos.scale(-1));
        render(matrices, worldRenderContext.commandQueue(), worldRenderContext.consumers());
        matrices.popPose();
    }

    public static void render(PoseStack matrices, SubmitNodeCollector queue, MultiBufferSource vertexConsumers) {

        moveLerp += Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();
        int cutLerp = (int) moveLerp / 5;
        frameCount = cutLerp - ((cutLerp / 4) * 4);

        if (Minecraft.getInstance().player != null && !((IUniversePlayer) Minecraft.getInstance().player).isUsingVanniMask()) {
            return;
        }

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        matrices.pushPose();
        BlockPos selectPos = BlockPos.ZERO;
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getMainHandItem().getItem() instanceof WrenchItem) {
            CompoundTag nbt = ItemUtil.getNbt(Minecraft.getInstance().player.getMainHandItem());
            if (nbt.contains("startLink")) selectPos = nbt.read("startLink", BlockPos.CODEC).orElse(BlockPos.ZERO);

            IEntityDataSaver ent = null;
            for (IEntityDataSaver ent2 : LinkSource.allSources) {
                if (ent2 instanceof BlockEntity bent) {
                    if (bent.getBlockPos().equals(selectPos)) {
                        ent = ent2;
                        break;
                    }
                } else if (ent2 instanceof Entity ent3) {
                    if (ent3.blockPosition().equals(selectPos)) {
                        ent = (IEntityDataSaver) ent3;
                        break;
                    }
                }
            }

            if (ent != null) {
                matrices.pushPose();

                matrices.translate(0, 0.25, 0);

                Identifier texture = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/other/link/link_" + frameCount + ".png");

                Vec3 vec1;
                if (ent instanceof BlockEntity bent) {
                    vec1 = bent.getBlockPos().getCenter();
                } else {
                    Entity ent2 = (Entity) ent;
                    vec1 = new Vec3(ent2.getX(), ent2.getEyeY(), ent2.getZ());
                }
                assert Minecraft.getInstance().hitResult != null;
                Vec3 vec2 = Minecraft.getInstance().hitResult.getLocation();

                queue.submitCustomGeometry(matrices, RenderTypes.outline(texture), ((matricesEntry, vertexConsumer) -> {
                    drawLink(vec1, vec2, vertexConsumer, matricesEntry, true);
                }));

                matrices.popPose();
            }
        }

        List<IEntityDataSaver> blueTargets = new ArrayList<>();
        for (IEntityDataSaver ent : LinkSource.allSources) {

            for (int i = 0; i < ((LinkSource) ent).getTargets().size(); i++) {
                IEntityDataSaver target = ((LinkSource) ent).getTargets().get(i);


                matrices.pushPose();

                matrices.translate(0, 0.25, 0);

                Identifier texture = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/other/link/link_" + frameCount + ".png");

                queue.submitCustomGeometry(matrices, RenderTypes.outline(texture), ((matricesEntry, vertexConsumer) -> {

                    Vec3 vec1 = Vec3.ZERO;
                    if (ent instanceof BlockEntity bent) {
                        vec1 = bent.getBlockPos().getCenter();
                    } else if (ent instanceof Entity ent2) {
                        vec1 = new Vec3(ent2.getX(), ent2.getEyeY(), ent2.getZ());
                    }
                    Vec3 vec2 = Vec3.ZERO;
                    if (target instanceof BlockEntity bent) {
                        vec2 = bent.getBlockPos().getCenter();
                    } else if (target instanceof Entity ent2) {
                        vec2 = new Vec3(ent2.getX(), ent2.getEyeY(), ent2.getZ());
                    }

                    drawLink(vec1, vec2, vertexConsumer, matricesEntry, false);
                }));

                matrices.popPose();
            }
        }

        for (IEntityDataSaver ent : LinkSource.allSources) {

            matrices.pushPose();

            Vec3 vec;
            BlockPos checkPos = BlockPos.ZERO;
            if (ent instanceof BlockEntity bent) {
                vec = bent.getBlockPos().getCenter();
                checkPos = bent.getBlockPos();
            } else if (ent instanceof Entity ent2) {
                vec = new Vec3(ent2.getX(), ent2.getEyeY(), ent2.getZ());
                checkPos = ent2.blockPosition();
            } else {
                vec = Vec3.ZERO;
            }

            boolean bl = !(selectPos != BlockPos.ZERO && selectPos.equals(checkPos));

            boolean isTarget = ent instanceof LinkTarget;
            String name = isTarget ? "source_target_" : "source_";
            String color = ((LinkSource) ent).getTargets().isEmpty() && bl ? "red" : "blue";
            String t = "textures/other/link/" + name + color;
            if (isTarget) t = isTarget && !((LinkTarget) ent).getSources().isEmpty() ? t + "_link" : t;
            Identifier texture = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, t + ".png");

            float offset = (float) Math.cos(moveLerp / 20f);
            matrices.translate(0, (offset / 10) - 0.25f, 0);

            float distance2 = 0;
            for (IEntityDataSaver ent2 : ((LinkSource) ent).getTargets()) {
                if (ent2 instanceof BlockEntity bent) {
                    distance2 = Math.max(distance2, (float) camera.position().distanceTo(bent.getBlockPos().getCenter()));
                } else if (ent2 instanceof Entity ent3) {
                    distance2 = Math.max(distance2, (float) camera.position().distanceTo(new Vec3(ent3.getX(), ent3.getEyeY(), ent3.getZ())));
                }
            }

            float distance = (float) camera.position().distanceTo(vec);
            float scale = Math.clamp(4 - distance, 0, 1.2f);
            float scale2 = !bl ? 1.2f : distance2 == 0 ? 0 : Math.clamp(4 - distance2, 0, 1.2f);

            queue.submitCustomGeometry(matrices, RenderTypes.outline(texture), ((matricesEntry, vertexConsumer) -> {
            }));
            drawIcon(matrices, vec, queue, RenderTypes.outline(texture), Math.max(scale, scale2));

            if (ent.getPersistentData().contains("connectionIndex")) {
                int index = ent.getPersistentData().getIntOr("connectionIndex", 0);

                FontDescription spriteFont = new FontDescription.Resource(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "metropolis"));
                Style style = Style.EMPTY.withFont(spriteFont);
                Component text = Component.literal("" + index).setStyle(style);

                for (IEntityDataSaver target : ((LinkSource) ent).getTargets()) {
                    if (target instanceof BlockEntity) {
                        int textColor = target.getPersistentData().getIntOr("usedConnectionIndex", 0) == index ? 0xFF48A7E7 : 0xFFE53E32;
                        drawText(text, matrices, vec, vertexConsumers, Math.max(scale, scale2), textColor);
                    }
                }
            }

            matrices.popPose();

            blueTargets.addAll(((LinkSource) ent).getTargets());
        }
        for (int i = 0; i < LinkTarget.allTargets.size(); i++) {
            IEntityDataSaver ent = LinkTarget.allTargets.get(i);

            if (ent instanceof Entity ent2 && !(Minecraft.getInstance().level.getEntity(ent2.getId()) instanceof Entity))
                continue;
            if (ent instanceof LinkSource) continue;
            matrices.pushPose();

            String name = "target_";
            String color = blueTargets.contains(ent) ? "blue" : "red";
            Identifier texture = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/other/link/" + name + color + ".png");

            float offset = (float) Math.sin(moveLerp / 20f);
            matrices.translate(0, (offset / 10) - 0.25f, 0);

            Vec3 vec = Vec3.ZERO;
            if (ent instanceof BlockEntity bent) {
                vec = bent.getBlockPos().getCenter();
            } else if (ent instanceof Entity ent2) {
                vec = new Vec3(ent2.getX(), ent2.getEyeY(), ent2.getZ());
            }

            float distance2 = 0;
            for (IEntityDataSaver ent2 : ((LinkTarget) ent).getSources()) {
                if (ent2 instanceof BlockEntity bent) {
                    distance2 = Math.max(distance2, (float) camera.position().distanceTo(bent.getBlockPos().getCenter()));
                } else if (ent2 instanceof Entity ent3) {
                    distance2 = Math.max(distance2, (float) camera.position().distanceTo(new Vec3(ent3.getX(), ent3.getEyeY(), ent3.getZ())));
                }
            }

            float distance = (float) camera.position().distanceTo(vec);
            float scale = Math.clamp(4 - distance, 0, 1.2f);
            float scale2 = distance2 == 0 ? 0 : Math.clamp(4 - distance2, 0, 1.2f);

            drawIcon(matrices, vec, queue, RenderTypes.outline(texture), Math.max(scale, scale2));

            matrices.popPose();
        }
        matrices.popPose();
    }

    private static void drawLink(Vec3 vec1, Vec3 vec2, VertexConsumer buffer, PoseStack.Pose entry, boolean forceScale) {

        Vec3 length = (vec2.add(vec1.scale(-1)));

        double lengthD = length.length();

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vector3f normal = new Vector3f(0, 0, 1);


        float distance1 = (float) camera.position().distanceTo(vec1);
        float distance2 = (float) camera.position().distanceTo(vec2);

        float scale1 = Math.clamp(4 - distance1 , 0, 1.2f);
        float scale2 = forceScale ? 1.2f : Math.clamp(4 - distance2 , 0, 1.2f);
        float scale = Math.max(scale1, scale2);

        if(scale > 0){

            float offset = -moveLerp / 64f;
            float tWidth = (float) (0.5f * lengthD);
            float tHeight = 0.5f;
            float vHeight = 0.5f * scale;

            buffer.addVertex(entry.pose(), (float) vec1.x, (float) vec1.y + vHeight, (float) vec1.z)
                    .setUv(0.5f - tWidth + offset, 0.5f - tHeight)
                    .setColor(0xFFFFFFFF)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setNormal(normal.x(), normal.y(), normal.z())
            ;
            buffer.addVertex(entry.pose(), (float) vec1.x, (float) vec1.y - vHeight, (float) vec1.z)
                    .setUv(0.5f - tWidth + offset, 0.5f + tHeight)
                    .setColor(0xFFFFFFFF)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setNormal(normal.x(), normal.y(), normal.z())
            ;

            buffer.addVertex(entry.pose(), (float) vec2.x, (float) vec2.y - vHeight, (float) vec2.z)
                    .setUv(0.5f + tWidth + offset, 0.5f + tHeight)
                    .setColor(0xFFFFFFFF)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setNormal(normal.x(), normal.y(), normal.z())
            ;
            buffer.addVertex(entry.pose(), (float) vec2.x, (float) vec2.y + vHeight, (float) vec2.z)
                    .setUv(0.5f + tWidth + offset, 0.5f - tHeight)
                    .setColor(0xFFFFFFFF)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setNormal(normal.x(), normal.y(), normal.z())
            ;
        }
    }

    static void drawIcon(PoseStack matrices, Vec3 vec, SubmitNodeCollector queue, RenderType layer, float scale){
        matrices.pushPose();
        matrices.translate(vec);

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vector3f normal = new Vector3f(0, 0, 1);

        matrices.translate(0, 0.25f, 0);
        matrices.mulPose(new Quaternionf().rotationXYZ(0, (float) -Math.toRadians(camera.yaw()), (float) Math.PI));
        matrices.translate(0, -0.25f, 0);


        if(scale > 0){
            queue.submitCustomGeometry(matrices, layer, ((matricesEntry, vertexConsumer) -> {
                drawQuad(matricesEntry, vertexConsumer, normal, scale);
            }));
        }
        matrices.popPose();
    }
    static void drawText(Component text, PoseStack matrices, Vec3 vec, MultiBufferSource vertexConsumers, float scale, int color){
        matrices.pushPose();
        matrices.translate(vec);

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        matrices.translate(0, 1, 0);
        matrices.scale(0.05f * scale, 0.05f * scale, 0.05f * scale);
        matrices.translate(0, 0.25f, 0);
        matrices.mulPose(new Quaternionf().rotationXYZ(0, (float) -Math.toRadians(camera.yaw()), (float) Math.PI));
        matrices.translate(0, -0.25f, 0);

        Font textRenderer = Minecraft.getInstance().font;

        float width = textRenderer.width(text);
        if(scale > 0){
            textRenderer.drawInBatch(
                            text,
                            0.0f - width/2f,
                            -8,
                    color,
                            false,
                            matrices.last().pose(),
                            vertexConsumers,
                            Font.DisplayMode.SEE_THROUGH,
                            0,
                            LightTexture.FULL_BRIGHT
                    );
        }

        matrices.popPose();
    }

    public static void drawQuad(PoseStack.Pose entry, VertexConsumer buffer, Vector3f normal, float scale){

        float tWidth = 0.5f;
        float tHeight = 0.5f;
        float vWidth = 0.5f * scale;
        float vHeight1 = 0.75f * scale;
        float vHeight2 = 0.25f * scale;
        buffer.addVertex(entry.pose(), -vWidth , -vHeight1, 0.0f)
                .setUv(0.5f - tWidth, 0.5f - tHeight)
                .setColor(0xFFFFFFFF)
                .setLight(LightTexture.FULL_BRIGHT)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(normal.x(), normal.y(), normal.z())
        ;
        buffer.addVertex(entry.pose(), -vWidth, vHeight2, 0.0f)
                .setUv(0.5f - tWidth, 0.5f + tHeight)
                .setColor(0xFFFFFFFF)
                .setLight(LightTexture.FULL_BRIGHT)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(normal.x(), normal.y(), normal.z())
        ;
        buffer.addVertex(entry.pose(), vWidth, vHeight2, 0.0f)
                .setUv(0.5f + tWidth, 0.5f + tHeight)
                .setColor(0xFFFFFFFF)
                .setLight(LightTexture.FULL_BRIGHT)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(normal.x(), normal.y(), normal.z())
        ;
        buffer.addVertex(entry.pose(), vWidth, -vHeight1, 0.0f)
                .setUv(0.5f + tWidth, 0.5f - tHeight)
                .setColor(0xFFFFFFFF)
                .setLight(LightTexture.FULL_BRIGHT)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(normal.x(), normal.y(), normal.z())
        ;
    }
}

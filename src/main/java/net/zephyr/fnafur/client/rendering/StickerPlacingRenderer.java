package net.zephyr.fnafur.client.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.DecalInit;
import net.zephyr.fnafur.item.tools.DecalBookItem;

public class StickerPlacingRenderer {
        public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {

            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            if (player.getMainHandStack() != null && player.getMainHandStack().getItem() instanceof DecalBookItem) {
                DecalInit.Decal decal = DecalBookItem.getDecal(player.getMainHandStack());
                HitResult blockHit = client.crosshairTarget;
                if (decal != null && blockHit.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = ((BlockHitResult) blockHit).getBlockPos();
                    Direction direction = ((BlockHitResult) blockHit).getSide();
                    if (client.world.getBlockState(pos).isSideSolidFullSquare(client.world, pos, direction)) {

                        if (decal.isWallSticker() && (((BlockHitResult) blockHit).getSide() == Direction.UP || ((BlockHitResult) blockHit).getSide() == Direction.DOWN)) {
                            return;
                        }

                        Vec3d hitPos = blockHit.getPos();

                        Vec3d stickerPos = DecalBookItem.stickerPos(pos, hitPos, direction, decal, player, client.world);

                        matrices.push();
                        matrices.translate(-cameraX + 0.5f, -cameraY, -cameraZ + 0.5f);
                        matrices.translate(stickerPos.getX(), stickerPos.getY(), stickerPos.getZ());
                        matrices.translate(pos.getX(), pos.getY(), pos.getZ());
                        matrices.translate(0, 0.5f, 0);
                        matrices.multiply(((BlockHitResult) blockHit).getSide().getRotationQuaternion());
                        matrices.translate(0, 0.506f, 0);

                        int dirPos = direction == Direction.NORTH || direction == Direction.SOUTH ? Math.abs(pos.getX()) :
                                direction == Direction.WEST || direction == Direction.EAST ? Math.abs(pos.getZ()) :
                                        0;
                        int num = dirPos % decal.getTextures().length;
                        String path = "textures/" + decal.getTextures()[num].getPath() + ".png";
                        Identifier identifier = Identifier.of(FnafUniverseRebuilt.MOD_ID, path);

                        float tWidth = 0.5f;
                        float tHeight = 0.5f;
                        float vWidth = 0.5f;
                        float vHeight = 0.5f;

                        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(identifier));

                        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), -vWidth, 0.0f, -vHeight)
                                .texture(0.5f - tWidth, 0.5f - tHeight)
                                .color(0xFFFFFFFF)
                                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                                .overlay(OverlayTexture.DEFAULT_UV)
                                .normal(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ())
                        ;
                        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), -vWidth, 0.0f, vHeight)
                                .texture(0.5f - tWidth, 0.5f + tHeight)
                                .color(0xFFFFFFFF)
                                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                                .overlay(OverlayTexture.DEFAULT_UV)
                                .normal(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ())
                        ;
                        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, vHeight)
                                .texture(0.5f + tWidth, 0.5f + tHeight)
                                .color(0xFFFFFFFF)
                                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                                .overlay(OverlayTexture.DEFAULT_UV)
                                .normal(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ())
                        ;
                        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, -vHeight)
                                .texture(0.5f + tWidth, 0.5f - tHeight)
                                .color(0xFFFFFFFF)
                                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                                .overlay(OverlayTexture.DEFAULT_UV)
                                .normal(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ())
                        ;

                        matrices.pop();
                    }
                }
            }
        }
}

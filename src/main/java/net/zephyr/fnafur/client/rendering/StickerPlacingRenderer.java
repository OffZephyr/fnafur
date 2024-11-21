package net.zephyr.fnafur.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlock;
import net.zephyr.fnafur.init.item_init.StickerInit;
import net.zephyr.fnafur.item.StickerItem;

public class StickerPlacingRenderer {
        public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {

            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            if (player.getMainHandStack() != null && player.getMainHandStack().getItem() instanceof StickerItem item) {
                HitResult blockHit = client.crosshairTarget;
                if (blockHit.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = ((BlockHitResult) blockHit).getBlockPos();
                    if (client.world.getBlockState(pos).getBlock() instanceof StickerBlock) {

                        if (item.isWallSticker() && (((BlockHitResult) blockHit).getSide() == Direction.UP || ((BlockHitResult) blockHit).getSide() == Direction.DOWN)) {
                            return;
                        }

                        Vec3d hitPos = blockHit.getPos();
                        Direction direction = ((BlockHitResult) blockHit).getSide();
                        String name = item.sticker_name();
                        StickerInit.Sticker sticker = StickerInit.getSticker(name);

                        if (sticker == null) return;

                        Vec3d stickerPos = StickerItem.stickerPos(pos, hitPos, direction, item, player, player.getWorld());

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
                        int num = dirPos % sticker.getTextures().length;
                        String path = "textures/" + sticker.getTextures()[num].getPath() + ".png";
                        Identifier identifier = Identifier.of(FnafUniverseResuited.MOD_ID, path);

                        float tWidth = 0.5f;
                        float tHeight = 0.5f;
                        float vWidth = 0.5f;
                        float vHeight = 0.5f;

                        RenderSystem.enableDepthTest();
                        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);

                        var buffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

                        RenderSystem.setShaderTexture(0, identifier);
                        RenderSystem.setShaderColor(1, 1, 1, 0.6f);

                        buffer.vertex(matrices.peek().getPositionMatrix(), -vWidth, 0.0f, -vHeight)
                                .texture(0.5f - tWidth, 0.5f - tHeight)
                                .color(0xFFFFFFFF);
                        buffer.vertex(matrices.peek().getPositionMatrix(), -vWidth, 0.0f, vHeight)
                                .texture(0.5f - tWidth, 0.5f + tHeight)
                                .color(0xFFFFFFFF);
                        buffer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, vHeight)
                                .texture(0.5f + tWidth, 0.5f + tHeight)
                                .color(0xFFFFFFFF);
                        buffer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, -vHeight)
                                .texture(0.5f + tWidth, 0.5f - tHeight)
                                .color(0xFFFFFFFF);

                        RenderSystem.setShaderColor(1, 1, 1, 1f);

                        BufferRenderer.drawWithGlobalProgram(buffer.end());

                        RenderSystem.disableDepthTest();

                        matrices.pop();
                    }
                }
            }
        }
}

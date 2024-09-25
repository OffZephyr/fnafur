package net.zephyr.fnafur.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.stickers.base.Sticker;
import net.zephyr.fnafur.blocks.stickers.base.StickerBlock;
import net.zephyr.fnafur.item.stickers.base.StickerItem;

public class StickerPlacingRenderer {
        public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            if (player.getMainHandStack() != null && player.getMainHandStack().getItem() instanceof StickerItem item) {
                HitResult blockHit = client.crosshairTarget;
                if (blockHit.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = ((BlockHitResult) blockHit).getBlockPos();
                    if (client.world.getBlockState(pos).getBlock() instanceof StickerBlock) {
                        Vec3d hitPos = blockHit.getPos().add(0, 1, 0);

                        if (item.isWallSticker() && (((BlockHitResult) blockHit).getSide() == Direction.UP || ((BlockHitResult) blockHit).getSide() == Direction.DOWN)) {
                            return;
                        }

                        Direction direction = ((BlockHitResult) blockHit).getSide();
                        String name = item.sticker_name();
                        Sticker sticker = Sticker.getSticker(name);

                        double yOffset = hitPos.getY() - pos.getY() - 1;

                        double xOffset = hitPos.getX() - pos.getX();
                        double zOffset = hitPos.getZ() - pos.getZ();

                        float grid = sticker.getPixelDensity();
                        float space = sticker.getSize();

                        double x = 0;
                        double y = 0;
                        double z = 0;

                        float SnapGrid = 1f / grid;
                        if (player.isSneaking()) {
                            SnapGrid = 1f / (grid / 12f);
                        }

                        if (sticker.getDirection() == Sticker.Movable.VERTICAL) {
                            y = (yOffset / grid) * space;

                            y = Math.round(y / SnapGrid) * SnapGrid;
                            y = Math.clamp(y, 0, space);
                        }
                        if (sticker.getDirection() == Sticker.Movable.HORIZONTAL) {
                            x = (xOffset / grid) * space;

                            x = Math.round(x / SnapGrid) * SnapGrid;
                            x = Math.clamp(x, 0, space);

                            z = (zOffset / grid) * space;

                            z = Math.round(z / SnapGrid) * SnapGrid;
                            z = Math.clamp(z, 0, space);
                        }

                        if(direction == Direction.EAST || direction == Direction.WEST){
                            x = 0;
                        }
                        if(direction == Direction.SOUTH || direction == Direction.NORTH){
                            z = 0;
                        }

                        matrices.push();
                        matrices.translate(x, y, z);
                        matrices.translate(pos.getX(), pos.getY(), pos.getZ());
                        matrices.translate(0, 0.5f, 0);
                        matrices.multiply(((BlockHitResult) blockHit).getSide().getRotationQuaternion());
                        matrices.translate(0, 0.501f, 0);

                        if (sticker != null) {
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

                            RenderSystem.enableBlend();
                            RenderSystem.enableDepthTest();
                            RenderSystem.setShader(GameRenderer::getPositionTexProgram);

                            var buffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

                            RenderSystem.setShaderTexture(0, identifier);
                            RenderSystem.setShaderColor(1, 1, 1, 0.6f);

                            buffer.vertex(matrices.peek().getPositionMatrix(), -vWidth, 0.0f, -vHeight).texture(0.5f - tWidth, 0.5f - tHeight).color(0x66FFFFFF);
                            buffer.vertex(matrices.peek().getPositionMatrix(), -vWidth, 0.0f, vHeight).texture(0.5f - tWidth, 0.5f + tHeight).color(0x66FFFFFF);
                            buffer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, vHeight).texture(0.5f + tWidth, 0.5f + tHeight).color(0x66FFFFFF);
                            buffer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, -vHeight).texture(0.5f + tWidth, 0.5f - tHeight).color(0x66FFFFFF);

                            RenderSystem.setShaderColor(1, 1, 1, 1f);

                            BufferRenderer.drawWithGlobalProgram(buffer.end());
                            matrices.pop();
                        }
                    }
                }
            }
        }
}

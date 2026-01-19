package net.zephyr.fnafur.client.rendering;

import net.minecraft.block.BlockState;
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
import net.minecraft.util.shape.VoxelShapes;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrame;
import net.zephyr.fnafur.decals.DecalInstance;
import net.zephyr.fnafur.decals.DecalManager;
import net.zephyr.fnafur.init.decal_init.DecalInit;
import net.zephyr.fnafur.item.tools.DecalBookItem;
import org.joml.Vector3f;

public class StickerPlacingRenderer {
    double height = 0;
    int block_height = 0;

        public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {

            MinecraftClient client = MinecraftClient.getInstance();
            DecalManager.PREVIEW_DECAL = null;
            ClientPlayerEntity player = client.player;
            if (player.getMainHandStack() != null && player.getMainHandStack().getItem() instanceof DecalBookItem) {
                DecalInit.Decal decal = DecalBookItem.getDecal(player.getMainHandStack());
                HitResult blockHit = client.crosshairTarget;
                if (decal != null && blockHit.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = ((BlockHitResult) blockHit).getBlockPos();
                    Direction direction = ((BlockHitResult) blockHit).getSide();
                    BlockState checkedState = client.world.getBlockState(pos);
                    if (checkedState.isSideSolidFullSquare(client.world, pos, direction) || checkedState.getBlock() instanceof DiagonalMimicFrame) {

                        if (decal.isWallSticker() && (((BlockHitResult) blockHit).getSide() == Direction.UP || ((BlockHitResult) blockHit).getSide() == Direction.DOWN)) {
                            return;
                        }

                        Vec3d hitPos = blockHit.getPos();

                        Vec3d stickerPos = DecalBookItem.stickerPos(pos, hitPos, direction, decal, player, client.world);

                        float offsetX = (float) (direction.getAxis() == Direction.Axis.X ? stickerPos.z : stickerPos.x);
                        float offsetY = (float) stickerPos.y;

                        if(decal.getDirection() == DecalInit.Movable.VERTICAL){
                            if(MinecraftClient.getInstance().options.useKey.isPressed()){
                                offsetY = (float) height;
                                pos = new BlockPos(pos.getX(), block_height, pos.getZ());
                            }
                            else{
                                height = offsetY;
                                block_height = pos.getY();
                            }
                        }

                        BlockPos decalPos = DecalBookItem.getStartBlockPos(pos, direction, decal.getDirection());
                        DecalInstance instance = new DecalInstance(decalPos, direction, offsetX, offsetY, DecalBookItem.getDecalBlockLength(decalPos, direction, decal.getDirection()) , decal.name());


                        DecalManager.PREVIEW_DECAL = instance;
                        matrices.push();
                        matrices.translate(-cameraX + 0.5f, -cameraY, -cameraZ + 0.5f);
                        //matrices.translate(stickerPos.getX(), stickerPos.getY(), stickerPos.getZ());
                        //matrices.translate(pos.getX(), pos.getY(), pos.getZ());
                        //matrices.translate(0, 0.5f, 0);
                        //matrices.multiply(((BlockHitResult) blockHit).getSide().getRotationQuaternion());
                        //matrices.translate(0, 0.506f, 0);

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

                        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayers.entityCutout(identifier));
                        VertexConsumer ourlineVertexConsumer = vertexConsumers.getBuffer(RenderLayers.secondaryBlockOutline());

                        if(false) {
                            VertexRendering.drawOutline(
                                    matrices,
                                    ourlineVertexConsumer,
                                    VoxelShapes.cuboid(instance.getHitbox()),
                                    0,
                                    0,
                                    0,
                                    0xFFFFFFFF,
                                    3

                            );
                        }

                        if(false) {
                            float y = 0;
                            Vector3f normal = new Vector3f(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
                            if (checkedState.getBlock() instanceof DiagonalMimicFrame f) {
                                Direction d = ((BlockHitResult) blockHit).getSide();
                                if (f.isDiagonal(checkedState)) {
                                    if (DiagonalMimicFrame.NEXT_MAP.get(d) == f.getDiagonalDirection(checkedState)) {
                                        y = -1;
                                        normal.rotateY((float) Math.toRadians(45f));
                                    }
                                    if (DiagonalMimicFrame.NEXT_MAP.get(d.rotateYCounterclockwise()) == f.getDiagonalDirection(checkedState)) {
                                        y = -1;
                                        vWidth = -0.5f;
                                        vHeight = -0.5f;
                                        tWidth = -0.5f;
                                        tHeight = -0.5f;

                                        normal.rotateY((float) Math.toRadians(-45f));
                                    }
                                }
                            }
                            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), -vWidth, y, -vHeight)
                                    .texture(0.5f - tWidth, 0.5f - tHeight)
                                    .color(0xFFFFFFFF)
                                    .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                                    .overlay(OverlayTexture.DEFAULT_UV)
                                    .normal(normal.x(), normal.y(), normal.z())
                            ;
                            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), -vWidth, y, vHeight)
                                    .texture(0.5f - tWidth, 0.5f + tHeight)
                                    .color(0xFFFFFFFF)
                                    .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                                    .overlay(OverlayTexture.DEFAULT_UV)
                                    .normal(normal.x(), normal.y(), normal.z())
                            ;
                            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, vHeight)
                                    .texture(0.5f + tWidth, 0.5f + tHeight)
                                    .color(0xFFFFFFFF)
                                    .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                                    .overlay(OverlayTexture.DEFAULT_UV)
                                    .normal(normal.x(), normal.y(), normal.z())
                            ;
                            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, -vHeight)
                                    .texture(0.5f + tWidth, 0.5f - tHeight)
                                    .color(0xFFFFFFFF)
                                    .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                                    .overlay(OverlayTexture.DEFAULT_UV)
                                    .normal(normal.x(), normal.y(), normal.z())
                            ;
                        }
                        matrices.pop();
                    }
                }
            }
        }
}

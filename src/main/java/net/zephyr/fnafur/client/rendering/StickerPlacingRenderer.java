package net.zephyr.fnafur.client.rendering;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrame;
import net.zephyr.fnafur.rendering.decals.DecalInstance;
import net.zephyr.fnafur.rendering.decals.DecalManager;
import net.zephyr.fnafur.init.decal_init.DecalInit;
import net.zephyr.fnafur.item.tools.DecalBookItem;
import org.joml.Vector3f;

public class StickerPlacingRenderer {
    double height = 0;
    int block_height = 0;

        public void render(PoseStack matrices, MultiBufferSource.BufferSource vertexConsumers, double cameraX, double cameraY, double cameraZ) {

            Minecraft client = Minecraft.getInstance();
            DecalManager.PREVIEW_DECAL = null;
            LocalPlayer player = client.player;
            if (player.getMainHandItem() != null && player.getMainHandItem().getItem() instanceof DecalBookItem) {
                DecalInit.Decal decal = DecalBookItem.getDecal(player.getMainHandItem());
                HitResult blockHit = client.hitResult;
                if (decal != null && blockHit.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = ((BlockHitResult) blockHit).getBlockPos();
                    Direction direction = ((BlockHitResult) blockHit).getDirection();
                    BlockState checkedState = client.level.getBlockState(pos);
                    if (checkedState.isFaceSturdy(client.level, pos, direction) || checkedState.getBlock() instanceof DiagonalMimicFrame) {

                        if (decal.isWallSticker() && (((BlockHitResult) blockHit).getDirection() == Direction.UP || ((BlockHitResult) blockHit).getDirection() == Direction.DOWN)) {
                            return;
                        }

                        Vec3 hitPos = blockHit.getLocation();

                        Vec3 stickerPos = DecalBookItem.stickerPos(pos, hitPos, direction, decal, player, client.level);

                        float offsetX = (float) (direction.getAxis() == Direction.Axis.X ? stickerPos.z : stickerPos.x);
                        float offsetY = (float) stickerPos.y;

                        if(decal.getDirection() == DecalInit.Movable.VERTICAL){
                            if(Minecraft.getInstance().options.keyUse.isDown()){
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
                        matrices.pushPose();
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
                        Identifier identifier = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, path);

                        float tWidth = 0.5f;
                        float tHeight = 0.5f;
                        float vWidth = 0.5f;
                        float vHeight = 0.5f;

                        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderTypes.entityCutout(identifier));
                        VertexConsumer ourlineVertexConsumer = vertexConsumers.getBuffer(RenderTypes.secondaryBlockOutline());

                        if(false) {
                            ShapeRenderer.renderShape(
                                    matrices,
                                    ourlineVertexConsumer,
                                    Shapes.create(instance.getHitbox()),
                                    0,
                                    0,
                                    0,
                                    0xFFFFFFFF,
                                    3

                            );
                        }

                        if(false) {
                            float y = 0;
                            Vector3f normal = new Vector3f(direction.getStepX(), direction.getStepY(), direction.getStepZ());
                            if (checkedState.getBlock() instanceof DiagonalMimicFrame f) {
                                Direction d = ((BlockHitResult) blockHit).getDirection();
                                if (f.isDiagonal(checkedState)) {
                                    if (DiagonalMimicFrame.NEXT_MAP.get(d) == f.getDiagonalDirection(checkedState)) {
                                        y = -1;
                                        normal.rotateY((float) Math.toRadians(45f));
                                    }
                                    if (DiagonalMimicFrame.NEXT_MAP.get(d.getCounterClockWise()) == f.getDiagonalDirection(checkedState)) {
                                        y = -1;
                                        vWidth = -0.5f;
                                        vHeight = -0.5f;
                                        tWidth = -0.5f;
                                        tHeight = -0.5f;

                                        normal.rotateY((float) Math.toRadians(-45f));
                                    }
                                }
                            }
                            vertexConsumer.addVertex(matrices.last().pose(), -vWidth, y, -vHeight)
                                    .setUv(0.5f - tWidth, 0.5f - tHeight)
                                    .setColor(0xFFFFFFFF)
                                    .setLight(LightCoordsUtil.FULL_BRIGHT)
                                    .setOverlay(OverlayTexture.NO_OVERLAY)
                                    .setNormal(normal.x(), normal.y(), normal.z())
                            ;
                            vertexConsumer.addVertex(matrices.last().pose(), -vWidth, y, vHeight)
                                    .setUv(0.5f - tWidth, 0.5f + tHeight)
                                    .setColor(0xFFFFFFFF)
                                    .setLight(LightCoordsUtil.FULL_BRIGHT)
                                    .setOverlay(OverlayTexture.NO_OVERLAY)
                                    .setNormal(normal.x(), normal.y(), normal.z())
                            ;
                            vertexConsumer.addVertex(matrices.last().pose(), vWidth, 0.0f, vHeight)
                                    .setUv(0.5f + tWidth, 0.5f + tHeight)
                                    .setColor(0xFFFFFFFF)
                                    .setLight(LightCoordsUtil.FULL_BRIGHT)
                                    .setOverlay(OverlayTexture.NO_OVERLAY)
                                    .setNormal(normal.x(), normal.y(), normal.z())
                            ;
                            vertexConsumer.addVertex(matrices.last().pose(), vWidth, 0.0f, -vHeight)
                                    .setUv(0.5f + tWidth, 0.5f - tHeight)
                                    .setColor(0xFFFFFFFF)
                                    .setLight(LightCoordsUtil.FULL_BRIGHT)
                                    .setOverlay(OverlayTexture.NO_OVERLAY)
                                    .setNormal(normal.x(), normal.y(), normal.z())
                            ;
                        }
                        matrices.popPose();
                    }
                }
            }
        }
}

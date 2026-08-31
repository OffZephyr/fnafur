package net.zephyr.fnafur.client.rendering;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.stickers_blocks.BlockWithSticker;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockModel;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class SpecialBlockPlacingRenderer {
        public void render(PoseStack matrices, MultiBufferSource.BufferSource vertexConsumers, double cameraX, double cameraY, double cameraZ) {

            Minecraft client = Minecraft.getInstance();
            LocalPlayer player = client.player;

            if(!player.getAbilities().mayBuild) return;

            if (player.getMainHandItem() != null && player.getMainHandItem().getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof BlockWithSticker block) {
                    FloorPropBlock.drawingOutline = true;
                    HitResult blockHit = client.hitResult;
                    if (blockHit.getType() == HitResult.Type.BLOCK) {
                        BlockPos pos = ((BlockHitResult) blockHit).getBlockPos().relative(((BlockHitResult) blockHit).getDirection());

                        BlockState state = block.defaultBlockState();
                        VoxelShape shape = state.getShape(client.level, pos, CollisionContext.empty());

                        BlockStateModel model = client.getModelManager().getBlockStateModelSet().get(state);

                        if(!client.level.isUnobstructed(state, pos, CollisionContext.of(client.player)) || !client.level.getBlockState(pos).canBeReplaced()) return;

                        if(ItemUtil.getNbt(client.player.getMainHandItem()).isEmpty()) return;
                        matrices.pushPose();
                        matrices.translate(-cameraX, -cameraY, -cameraZ);
                        matrices.translate(pos.getX(), pos.getY(), pos.getZ());

                        if(model instanceof StickerBlockModel m){
                            BlockEntity entity = block.newBlockEntity(BlockPos.ZERO, state);

                            ((IEntityDataSaver)entity).getPersistentData().merge(ItemUtil.getNbt(client.player.getMainHandItem()));

                            m.forceEnt = entity;
                            //client.getBlockRenderer().getModelRenderer().render(client.level, m, state, pos, matrices, RenderLayerHelper.movingDelegate(vertexConsumers), false, 0, OverlayTexture.NO_OVERLAY);
                            m.forceEnt = null;


                            ShapeRenderer.renderShape(matrices, vertexConsumers.getBuffer(RenderTypes.LINES), shape, 0, 0, 0, 0x88FFFFFF, Minecraft.getInstance().getWindow().getAppropriateLineWidth());
                        }

                        matrices.popPose();
                    }
                }
            }
        }
}

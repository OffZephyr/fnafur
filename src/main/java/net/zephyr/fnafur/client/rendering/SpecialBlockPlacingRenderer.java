package net.zephyr.fnafur.client.rendering;

import net.fabricmc.fabric.api.renderer.v1.render.RenderLayerHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.stickers_blocks.BlockWithSticker;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockModel;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class SpecialBlockPlacingRenderer {
        public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {

            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;

            if(!player.getAbilities().allowModifyWorld) return;

            if (player.getMainHandStack() != null && player.getMainHandStack().getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof BlockWithSticker block) {
                    FloorPropBlock.drawingOutline = true;
                    HitResult blockHit = client.crosshairTarget;
                    if (blockHit.getType() == HitResult.Type.BLOCK) {
                        BlockPos pos = ((BlockHitResult) blockHit).getBlockPos().offset(((BlockHitResult) blockHit).getSide());

                        BlockState state = block.getDefaultState();
                        VoxelShape shape = state.getOutlineShape(client.world, pos, ShapeContext.absent());

                        BlockStateModel model = client.getBakedModelManager().getBlockModels().getModel(state);

                        if(!client.world.canPlace(state, pos, ShapeContext.of(client.player)) || !client.world.getBlockState(pos).isReplaceable()) return;

                        if(ItemUtil.getNbt(client.player.getMainHandStack()).isEmpty()) return;
                        matrices.push();
                        matrices.translate(-cameraX, -cameraY, -cameraZ);
                        matrices.translate(pos.getX(), pos.getY(), pos.getZ());

                        if(model instanceof StickerBlockModel m){
                            BlockEntity entity = block.createBlockEntity(BlockPos.ORIGIN, state);

                            ((IEntityDataSaver)entity).getPersistentData().copyFrom(ItemUtil.getNbt(client.player.getMainHandStack()));

                            m.forceEnt = entity;
                            client.getBlockRenderManager().getModelRenderer().render(client.world, m, state, pos, matrices, RenderLayerHelper.movingDelegate(vertexConsumers), false, 0, OverlayTexture.DEFAULT_UV);
                            m.forceEnt = null;


                            VertexRendering.drawOutline(matrices, vertexConsumers.getBuffer(RenderLayers.LINES), shape, 0, 0, 0, 0x88FFFFFF, MinecraftClient.getInstance().getWindow().getMinimumLineWidth());
                        }

                        matrices.pop();
                    }
                }
            }
        }
}

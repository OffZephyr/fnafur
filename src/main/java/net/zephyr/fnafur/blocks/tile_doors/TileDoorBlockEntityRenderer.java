package net.zephyr.fnafur.blocks.tile_doors;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class TileDoorBlockEntityRenderer implements BlockEntityRenderer<TileDoorBlockEntity> {

    public TileDoorBlockEntityRenderer(BlockEntityRendererFactory.Context context){

    }
    @Override
    public void render(TileDoorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        if(((IEntityDataSaver)entity).getPersistentData().isEmpty()){
            GoopyNetworkingUtils.getNbtFromServer(entity.getPos());
        }

        BlockState state = entity.getWorld().getBlockState(entity.getPos());

        if(state.getBlock() instanceof TileDoorBlock && state.get(TileDoorBlock.MAIN)){
            int width = ((IEntityDataSaver) entity).getPersistentData().getInt("width");
            int height = ((IEntityDataSaver) entity).getPersistentData().getInt("height");

            float openDelta = ((IEntityDataSaver) entity).getPersistentData().getFloat("openDelta");
            if(state.get(TileDoorBlock.OPEN) && openDelta != 1) {
                ((IEntityDataSaver) entity).getPersistentData().putFloat("openDelta", Math.clamp(openDelta + tickDelta/100f, 0, 1));
            }
            else if(!state.get(TileDoorBlock.OPEN) && openDelta != 0){
                ((IEntityDataSaver) entity).getPersistentData().putFloat("openDelta", Math.clamp(openDelta - tickDelta/100f, 0, 1));
            }

            float translateMaxHeight = height + 0.5f;
            float translateHeight = MathHelper.lerp(openDelta, 0, translateMaxHeight);

            BlockPos testPos = entity.getPos().offset(state.get(TileDoorBlock.FACING).rotateYCounterclockwise());
            Direction direction = entity.getWorld().getBlockState(testPos).getBlock() instanceof TileDoorBlock ? state.get(TileDoorBlock.FACING).rotateYCounterclockwise() : state.get(TileDoorBlock.FACING).rotateYClockwise();

            matrices.push();
            matrices.translate(0, translateHeight, 0);
            for (int x = 0; x <= width; x++) {
                for (int y = 0; y <= height - (int)translateHeight; y++) {

                    BlockPos updatePos = entity.getPos().up(y).offset(direction, x);

                    BlockState posState = entity.getWorld().getBlockState(updatePos);
                    BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(posState);

                    matrices.push();
                    matrices.translate(updatePos.getX() - entity.getPos().getX(),updatePos.getY() - entity.getPos().getY(),updatePos.getZ() - entity.getPos().getZ());
                    MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)), state, model, 1, 1, 1, light, overlay);
                    matrices.pop();
                }
            }
            matrices.pop();
        }

    }
}

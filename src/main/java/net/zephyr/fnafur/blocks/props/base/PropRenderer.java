package net.zephyr.fnafur.blocks.props.base;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

@Environment(EnvType.CLIENT)
public class PropRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    MinecraftClient client;
    BlockRenderManager manager;

    public PropRenderer(BlockEntityRendererFactory.Context context){
        client = MinecraftClient.getInstance();
        manager = context.getRenderManager();
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockPos pos = entity.getPos();
        BlockState state = client.world.getBlockState(pos);

        if(state.getBlock() instanceof FloorPropBlock) {
            matrices.push();
            if(!((IEntityDataSaver)entity).getPersistentData().contains("Rotation")){
                GoopyNetworkingUtils.getNbtFromServer(pos);
            }
            float rotation = ((IEntityDataSaver)entity).getPersistentData().getFloat("Rotation");
            //System.out.println(rotation);
            float offsetRotation = state.get(FloorPropBlock.FACING).asRotation();

            double offsetX = ((double) state.get(FloorPropBlock.OFFSET_X) / PropBlock.offset_grid_size);
            double offsetY = state.contains(PropBlock.OFFSET_Y) ? ((double) state.get(FloorPropBlock.OFFSET_Y) / PropBlock.offset_grid_size) : 0;
            double offsetZ = ((double) state.get(FloorPropBlock.OFFSET_Z) / PropBlock.offset_grid_size);

            matrices.translate(-0.5f, 0, -0.5f);
            matrices.translate(offsetX, offsetY, offsetZ);

            matrices.translate(0.5f, 0, 0.5f);
            //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(offsetRotation));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rotation));
            matrices.translate(-0.5f, 0, -0.5f);
            this.renderModel(pos, state, matrices, vertexConsumers, entity.getWorld(), false, overlay);
            matrices.pop();
        }
    }

    private void renderModel(BlockPos pos, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, boolean cull, int overlay) {
        RenderLayer renderLayer = RenderLayers.getMovingBlockLayer(state);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
        this.manager
                .getModelRenderer()
                .render(world, this.manager.getModel(state), state, pos, matrices, vertexConsumer, cull, Random.create(), state.getRenderingSeed(pos), overlay);
    }
}

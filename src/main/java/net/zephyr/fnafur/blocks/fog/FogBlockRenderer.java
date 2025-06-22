package net.zephyr.fnafur.blocks.fog;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FogBlockRenderer implements BlockEntityRenderer<FogBlockEntity> {
    MinecraftClient client;
    BlockRenderManager manager;

    public FogBlockRenderer(BlockEntityRendererFactory.Context context){
        client = MinecraftClient.getInstance();
        manager = context.getRenderManager();
    }
    private void renderModel(BlockPos pos, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, boolean cull, int overlay) {
        this.manager
                .getModelRenderer()
                .render(world, this.manager.getModel(state), state, pos, matrices, vertexConsumers, cull, state.getRenderingSeed(pos), overlay);
    }

    @Override
    public void render(FogBlockEntity entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        BlockPos pos = entity.getPos();
        BlockState state = client.world.getBlockState(pos);

        if(state.getBlock() instanceof FogBlock block) {
            if(entity.visible) {
                renderModel(pos, state, matrices, vertexConsumers, client.world, false, overlay);
            }
        }
    }
}

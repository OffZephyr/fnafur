package net.zephyr.fnafur.blocks.props.base;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.renderer.v1.render.RenderLayerHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.zephyr.fnafur.networking.nbt_updates.SyncBlockNbtC2SPayload;
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
    public int getRenderDistance() {
        return 32;
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        BlockPos pos = entity.getPos();
        BlockState state = client.world.getBlockState(pos);
        NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();

        if(state.getBlock() instanceof PropBlock<?> block) {
            matrices.push();

            if(!nbt.contains("synced")){
                ClientPlayNetworking.send(new SyncBlockNbtC2SPayload(pos.asLong()));

                nbt = ((IEntityDataSaver)entity).getPersistentData();
            }

            float rotation = nbt.getFloat("Rotation").orElse(0.0f);
            float offsetRotation = !block.rotates() ? 0 : state.get(FloorPropBlock.FACING).getOpposite().getPositiveHorizontalDegrees();

            double offsetX = nbt.getDouble("xOffset").orElse(0.0);
            double offsetY = nbt.getDouble("yOffset").orElse(0.0);
            double offsetZ = nbt.getDouble("zOffset").orElse(0.0);

            matrices.translate(-0.5f, 0, -0.5f);
            matrices.translate(offsetX, 0, offsetZ);
            if(!block.snapsVertically()) matrices.translate(0, offsetY, 0);
            if(state.getBlock() instanceof WallPropBlock<?>) {
                matrices.translate(0, -0.5f, 0);
            }
            else {
                matrices.translate(0.5f, 0, 0.5f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(offsetRotation));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rotation));
                matrices.translate(-0.5f, 0, -0.5f);
            }
            this.renderModel(pos, state, matrices, vertexConsumers, entity.getWorld(), false, overlay);
            matrices.pop();

            if(client.getEntityRenderDispatcher().shouldRenderHitboxes()) {

                matrices.push();
                matrices.translate(-0.5f, 0, -0.5f);
                if(!block.snapsVertically()) matrices.translate(0, offsetY, 0);
                if(state.getBlock() instanceof WallPropBlock<?>) {
                    matrices.translate(0, -0.5f, 0);
                }
                matrices.translate(offsetX, 0, offsetZ);
                for (Box box : block.getClickHitBoxes(state)) {
                    VertexRendering.drawOutline(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), VoxelShapes.cuboid(box), 0, 0, 0, 0xFF00FF00);
                }
                matrices.pop();
            }
        }
    }

    private void renderModel(BlockPos pos, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, boolean cull, int overlay) {
        this.manager
                .getModelRenderer()
                .render(world, this.manager.getModel(state), state, pos, matrices, RenderLayerHelper.movingDelegate(vertexConsumers), cull, state.getRenderingSeed(pos), overlay);
    }
}

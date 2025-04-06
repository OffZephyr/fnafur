package net.zephyr.fnafur.blocks.props.base.geo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.blocks.utility_blocks.cosmo_gift.GeoPropAddedLayer;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@Environment(EnvType.CLIENT)
public class GeoPropRenderer<T extends GeoPropBlockEntity> extends GeoBlockRenderer<T> implements BlockEntityRenderer<T> {
    MinecraftClient client;
    BlockRenderManager manager;
    boolean loadedLayers = false;
    public GeoPropRenderer(BlockEntityRendererFactory.Context context) {
        super(new GeoPropModel<>());
        client = MinecraftClient.getInstance();
        manager = client.getBlockRenderManager();
    }
    public void render(T entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        entity.item = true;
        if(getGeoModel().getModelResource(entity, this) == null) return;
        super.render(entity, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true), matrices, vertexConsumers, light, overlay);
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        BlockPos pos = entity.getPos();
        BlockState state = client.world.getBlockState(pos);

        if(state.getBlock() instanceof PropBlock<?> block) {
            matrices.push();
            if(!((IEntityDataSaver)entity).getPersistentData().isEmpty()){
                GoopyNetworkingUtils.getNbtFromServer(pos);
            }

            if(((IEntityDataSaver)entity).getPersistentData().isEmpty()){
                GoopyNetworkingUtils.getNbtFromServer(pos);
            }
            float rotation = ((IEntityDataSaver)entity).getPersistentData().getFloat("Rotation");

            double offsetX = ((IEntityDataSaver)entity).getPersistentData().getDouble("xOffset");
            double offsetY = ((IEntityDataSaver)entity).getPersistentData().getDouble("yOffset");
            double offsetZ = ((IEntityDataSaver)entity).getPersistentData().getDouble("zOffset");

            matrices.translate(-0.5f, 0, -0.5f);
            matrices.translate(offsetX, offsetY, offsetZ);
            if(state.getBlock() instanceof WallPropBlock<?>) {
                matrices.translate(0, -0.5f, 0);
            }
            else {
                float offsetRotation = state.get(FloorPropBlock.FACING).getOpposite().getPositiveHorizontalDegrees();
                matrices.translate(0.5f, 0, 0.5f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(offsetRotation));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rotation));
                matrices.translate(-0.5f, 0, -0.5f);
            }
            super.render(entity, tickDelta, matrices, vertexConsumers, light, overlay);
            //this.renderModel(pos, state, matrices, vertexConsumers, entity.getWorld(), false, overlay);
            matrices.pop();

            if(MinecraftClient.getInstance().getEntityRenderDispatcher().shouldRenderHitboxes()) {

                matrices.push();
                matrices.translate(-0.5f, 0, -0.5f);
                if(state.getBlock() instanceof WallPropBlock<?>) {
                    matrices.translate(0, -0.5f, 0);
                }
                matrices.translate(offsetX, offsetY, offsetZ);
                for (Box box : block.getClickHitBoxes(state)) {
                    VertexRendering.drawOutline(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), VoxelShapes.cuboid(box), 0, 0, 0, 0xFF00FF00);
                }
                matrices.pop();
            }
        }
    }
}

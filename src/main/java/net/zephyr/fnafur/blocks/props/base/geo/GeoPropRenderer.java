package net.zephyr.fnafur.blocks.props.base.geo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
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
import net.zephyr.fnafur.entity.animatronic.block.AnimatronicBlockEntity;
import net.zephyr.fnafur.entity.animatronic.block.AnimatronicBlockModel;
import net.zephyr.fnafur.networking.nbt_updates.SyncBlockNbtC2SPayload;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
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

    public GeoPropRenderer(GeoModel<T> animatronicBlockEntityAnimatronicBlockModel) {
        super(animatronicBlockEntityAnimatronicBlockModel);
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
        NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();

        if(state.getBlock() instanceof PropBlock<?> block) {
            matrices.push();

            if(!nbt.contains("synced")){
                ClientPlayNetworking.send(new SyncBlockNbtC2SPayload(pos.asLong()));

                nbt = ((IEntityDataSaver)entity).getPersistentData();
            }

            float rotation = nbt.getFloat("Rotation");

            double offsetX = nbt.getDouble("xOffset");
            double offsetY = nbt.getDouble("yOffset");
            double offsetZ = nbt.getDouble("zOffset");

            matrices.translate(-0.5f, 0, -0.5f);
            matrices.translate(offsetX, 0, offsetZ);
            if(!block.snapsVertically()) {
                matrices.translate(0, offsetY, 0);
                matrices.translate(0, -1, 0);
            }
            if(state.getBlock() instanceof WallPropBlock<?>) {
                matrices.translate(0, 0.5f, 0);
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
                if(!block.snapsVertically()) {
                    matrices.translate(0, -2, 0);
                    matrices.translate(0, offsetY, 0);
                }
                if(state.getBlock() instanceof WallPropBlock<?>) {
                    matrices.translate(0, 0.5f, 0);
                }
                matrices.translate(offsetX, 0, offsetZ);
                for (Box box : block.getClickHitBoxes(state)) {
                    VertexRendering.drawOutline(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), VoxelShapes.cuboid(box), 0, 0, 0, 0xFF00FF00);
                }
                matrices.pop();
            }
        }
    }
}

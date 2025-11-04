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
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.*;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.networking.nbt_updates.SyncBlockNbtC2SPayload;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IGetClientManagers;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)
public class GeoPropRenderer<T extends GeoPropBlockEntity, R extends BlockEntityRenderState & GeoRenderState> extends GeoBlockRenderer<T, R> implements BlockEntityRenderer<T, R> {
    MinecraftClient client;
    BlockRenderManager manager;
    float delta = 0;
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

    @Override
    public R fillRenderState(T animatable, Void relatedObject, R renderState, float partialTick) {
        super.fillRenderState(animatable, relatedObject, renderState, partialTick);

        if(animatable.item){
            double time = (System.currentTimeMillis() - ((IGetClientManagers)MinecraftClient.getInstance()).getStartTime()) / 200.0;
            double index = Math.sin(time);
            double alpha = 128 + (64 * index);
            renderState.addGeckolibData(DataTickets.RENDER_COLOR, ColorHelper.getArgb((int)alpha, 255, 255, 255));
        }

        NbtCompound nbt = ((IEntityDataSaver)animatable).getPersistentData().copy();
        renderState.addGeckolibData(CustomDataTickets.ROTATION, nbt.getFloat("Rotation").orElse(0f));
        renderState.addGeckolibData(CustomDataTickets.X_OFFSET, nbt.getDouble("xOffset").orElse(0.0));
        renderState.addGeckolibData(CustomDataTickets.Y_OFFSET, nbt.getDouble("yOffset").orElse(0.0));
        renderState.addGeckolibData(CustomDataTickets.Z_OFFSET, nbt.getDouble("zOffset").orElse(0.0));
        renderState.addGeckolibData(CustomDataTickets.FACING, getFacing(animatable));
        renderState.addGeckolibData(CustomDataTickets.TEXTURE, animatable.getTexture(animatable.getWorld()));
        renderState.addGeckolibData(CustomDataTickets.MODEL, animatable.getModel(animatable.getWorld()));
        renderState.addGeckolibData(CustomDataTickets.RE_RENDER_TEXTURE, animatable.getReRenderTexture(animatable.getWorld()));
        renderState.addGeckolibData(CustomDataTickets.RE_RENDER_MODEL, animatable.getReRenderModel(animatable.getWorld()));
        renderState.addGeckolibData(CustomDataTickets.RENDER_LAYER, animatable.item ? RenderLayer.getItemEntityTranslucentCull(animatable.getTexture(animatable.getWorld())) : animatable.getRenderType());

        return renderState;
    }

    @Override
    public void render(R renderState, MatrixStack matrices, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraRenderState) {
        BlockPos pos = renderState.pos;
        BlockState state = client.world.getBlockState(pos);
        Direction facing = renderState.getGeckolibData(CustomDataTickets.FACING);

        if(state.getBlock() instanceof PropBlock<?> block) {
            matrices.push();

            float rotation = renderState.getGeckolibData(CustomDataTickets.ROTATION);

            double offsetX = renderState.getGeckolibData(CustomDataTickets.X_OFFSET);
            double offsetY = renderState.getGeckolibData(CustomDataTickets.Y_OFFSET);
            double offsetZ = renderState.getGeckolibData(CustomDataTickets.Z_OFFSET);

            matrices.translate(-0.5f, 0, -0.5f);
            matrices.translate(offsetX, 0, offsetZ);
            if(!block.snapsVertically()) {
                matrices.translate(0, offsetY, 0);
                matrices.translate(0, -1, 0);
            }
            if(state.getBlock() instanceof WallPropBlock<?>) {
                matrices.translate(0, 0.5f, 0);

                matrices.translate(0.5f, 0, 0.5f);
                matrices.translate(-0.5f * facing.getVector().getX(), 0, -0.5f * facing.getVector().getZ());
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
                matrices.translate(0.15f * facing.getVector().getX(), 0, 0.15f * facing.getVector().getZ());
                matrices.translate(-0.5f, 0, -0.5f);
            }
            else {
                float offsetRotation = state.get(FloorPropBlock.FACING).getOpposite().getPositiveHorizontalDegrees();
                matrices.translate(0.5f, 0, 0.5f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(offsetRotation));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rotation));
                matrices.translate(-0.5f, 0, -0.5f);
            }
            super.render(renderState, matrices, renderTasks, cameraRenderState);
            //this.renderModel(pos, state, matrices, vertexConsumers, entity.getWorld(), false, overlay);
            matrices.pop();

//            if(MinecraftClient.getInstance().getEntityRenderDispatcher().shouldRenderHitboxes()) {

//                matrices.push();
//                matrices.translate(-0.5f, 0, -0.5f);
//                if(!block.snapsVertically()) {
//                    matrices.translate(0, -2, 0);
//                    matrices.translate(0, offsetY, 0);
                //}
//                if(state.getBlock() instanceof WallPropBlock<?>) {
//                    matrices.translate(0, 0.5f, 0);
                //}
//                matrices.translate(offsetX, 0, offsetZ);
//                for (Box box : block.getClickHitBoxes(state)) {
//                    VertexRendering.drawOutline(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), VoxelShapes.cuboid(box), 0, 0, 0, 0xFF00FF00);
                //}
//                matrices.pop();
//            }
        }
    }
    public void renderPreview(R renderState, MatrixStack matrices, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraRenderState) {
        super.render(renderState, matrices, renderTasks, cameraRenderState);
    }

    @Override
    public void createVerticesOfQuad(R renderState, GeoQuad quad, Matrix4f poseState, Vector3f normal, VertexConsumer buffer, int packedOverlay, int packedLight, int renderColor) {
        if(renderState.hasGeckolibData(DataTickets.RENDER_COLOR)){
            renderColor = renderState.getGeckolibData(DataTickets.RENDER_COLOR);
        }
        super.createVerticesOfQuad(renderState, quad, poseState, normal, buffer, packedOverlay, packedLight, renderColor);
    }
}

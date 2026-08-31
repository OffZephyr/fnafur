package net.zephyr.fnafur.blocks.props.base.geo;

import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IGetClientManagers;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.geckolib.cache.model.GeoQuad;
import com.geckolib.constant.DataTickets;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.GeoBlockRenderer;
import com.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)
public class GeoPropRenderer<T extends GeoPropBlockEntity, R extends BlockEntityRenderState & GeoRenderState> extends GeoBlockRenderer<T, R> implements BlockEntityRenderer<T, R> {
    Minecraft client;
    ModelManager manager;
    float delta = 0;
    boolean loadedLayers = false;
    public GeoPropRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new GeoPropModel<>());
        client = Minecraft.getInstance();
        manager = client.getModelManager();
    }

    @Override
    public R fillRenderState(T animatable, Void relatedObject, R renderState, float partialTick) {
        super.fillRenderState(animatable, relatedObject, renderState, partialTick);

        if(animatable.item){
            double time = (System.currentTimeMillis() - ((IGetClientManagers) Minecraft.getInstance()).getStartTime()) / 200.0;
            double index = Math.sin(time);
            double alpha = 128 + (64 * index);
            renderState.addGeckolibData(DataTickets.RENDER_COLOR, ARGB.color((int)alpha, 255, 255, 255));
            //renderState.addGeckolibData(DataTickets.RENDER_COLOR, ColorHelper.getArgb(255, 255, 255, 255));
        }

        CompoundTag nbt = ((IEntityDataSaver)animatable).getPersistentData().copy();
        renderState.addGeckolibData(CustomDataTickets.ROTATION, nbt.getFloat("Rotation").orElse(0f));
        renderState.addGeckolibData(CustomDataTickets.X_OFFSET, nbt.getDouble("xOffset").orElse(0.0));
        renderState.addGeckolibData(CustomDataTickets.Y_OFFSET, nbt.getDouble("yOffset").orElse(0.0));
        renderState.addGeckolibData(CustomDataTickets.Z_OFFSET, nbt.getDouble("zOffset").orElse(0.0));
        renderState.addGeckolibData(CustomDataTickets.FACING, getBlockStateDirection(animatable));
        renderState.addGeckolibData(CustomDataTickets.TEXTURE, animatable.getTexture(animatable.getLevel()));
        renderState.addGeckolibData(CustomDataTickets.MODEL, animatable.getModel(animatable.getLevel()));
        renderState.addGeckolibData(CustomDataTickets.RE_RENDER_TEXTURE, animatable.getReRenderTexture(animatable.getLevel()));
        renderState.addGeckolibData(CustomDataTickets.RE_RENDER_MODEL, animatable.getReRenderModel(animatable.getLevel()));
        renderState.addGeckolibData(CustomDataTickets.RENDER_LAYER, animatable.item ? RenderTypes.itemTranslucent(animatable.getTexture(animatable.getLevel())) : animatable.getRenderType());

        return renderState;
    }

    @Override
    public void submit(R renderState, PoseStack matrices, SubmitNodeCollector renderTasks, CameraRenderState cameraRenderState) {
        BlockPos pos = renderState.blockPos;
        BlockState state = client.level.getBlockState(pos);
        Direction facing = renderState.getGeckolibData(CustomDataTickets.FACING);

        if(state.getBlock() instanceof PropBlock<?> block) {
            matrices.pushPose();

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
                matrices.mulPose(Axis.YP.rotationDegrees(rotation));
                matrices.translate(-0.5f, 0, -0.5f);
            }
            else {
                float offsetRotation = state.getValue(FloorPropBlock.FACING).getOpposite().toYRot();
                matrices.translate(0.5f, 0, 0.5f);
                matrices.mulPose(Axis.YP.rotationDegrees(offsetRotation));
                matrices.mulPose(Axis.YP.rotationDegrees(-rotation));
                matrices.translate(-0.5f, 0, -0.5f);
            }
            super.submit(renderState, matrices, renderTasks, cameraRenderState);
            //this.renderModel(pos, state, matrices, vertexConsumers, entity.getWorld(), false, overlay);
            matrices.popPose();

//            if(Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitboxes()) {

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


    @Override
    public @Nullable RenderType getRenderType(R renderState, Identifier texture) {
        return renderState.getGeckolibData(CustomDataTickets.RENDER_LAYER) == null ? super.getRenderType(renderState, texture) : renderState.getGeckolibData(CustomDataTickets.RENDER_LAYER);
    }

    public void renderPreview(R renderState, PoseStack matrices, SubmitNodeCollector renderTasks, CameraRenderState cameraRenderState) {

        float rotation = renderState.getGeckolibData(CustomDataTickets.ROTATION);

        double offsetX = renderState.getGeckolibData(CustomDataTickets.X_OFFSET);
        double offsetY = renderState.getGeckolibData(CustomDataTickets.Y_OFFSET);
        double offsetZ = renderState.getGeckolibData(CustomDataTickets.Z_OFFSET);
        matrices.pushPose();
        matrices.translate(-0.5f, 0, -0.5f);
        matrices.translate(offsetX, 0, offsetZ);
        matrices.translate(0, offsetY, 0);
        matrices.translate(0, -1, 0);
        matrices.translate(0.5f, 0, 0.5f);
        matrices.mulPose(Axis.YP.rotationDegrees(rotation));
        matrices.translate(-0.5f, 0, -0.5f);
        super.submit(renderState, matrices, renderTasks, cameraRenderState);
        matrices.popPose();
    }
}

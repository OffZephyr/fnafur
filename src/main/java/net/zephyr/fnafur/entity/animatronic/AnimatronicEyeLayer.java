package net.zephyr.fnafur.entity.animatronic;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.CustomRenderingPipelines;
import net.zephyr.fnafur.util.CustomDataTickets;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.*;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.base.PerBoneRender;
import software.bernie.geckolib.renderer.layer.CustomBoneTextureGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.List;
import java.util.function.BiConsumer;

public class AnimatronicEyeLayer<T extends AnimatronicEntity, O, R extends GeoRenderState> extends GeoRenderLayer<T, O, R>  {

    protected final List<String> boneNames;

    public AnimatronicEyeLayer(GeoRenderer<T, O, R> renderer, List<String> boneNames) {
        super(renderer);

        this.boneNames = boneNames;
    }

    /**
     * Get the texture resource path for the given {@link GeoRenderState}.
     */

    @Override
    protected Identifier getTextureResource(R renderState) {
        //return Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.USE_EYE_TEXTURE)) ? renderState.getGeckolibData(CustomDataTickets.EYE_TEXTURE) : renderState.getGeckolibData(CustomDataTickets.TEXTURE);
        return renderState.getGeckolibData(CustomDataTickets.EYE_TEXTURE);
    }
    /**
     * Get the render type for the render pass
     */
    protected RenderLayer getRenderType(R renderState, Identifier texture) {
        boolean hide = renderState.hasGeckolibData(CustomDataTickets.USE_EYE_TEXTURE) && Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.USE_EYE_TEXTURE));

        if(hide) return this.renderer.getRenderType(renderState, texture);
        return this.renderer.getRenderType(renderState, texture);
        //return CustomRenderingPipelines.getAnimatronic(getTextureResource(renderState), renderState.getGeckolibData(CustomDataTickets.EYE_MAP_TEXTURE));
    }

    @Override
    public void submitRenderTask(R renderState, MatrixStack poseStack, BakedGeoModel bakedModel, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraState, int packedLight, int packedOverlay, int renderColor, boolean didRenderModel) {
        if (!didRenderModel) return;

        Identifier eyeTexture = getTextureResource(renderState);
        RenderLayer renderType = getRenderType(renderState, eyeTexture);
        renderTasks.submitCustom(poseStack, renderType, (pose, vertexConsumer) -> {
            final MatrixStack poseStack2 = new MatrixStack();
            final boolean skipBoneTasks = this.renderer.getPerBoneTasks(renderState).isEmpty();

            poseStack2.peek().copy(pose);

            for (GeoBone bone : bakedModel.topLevelBones()) {
                renderState.addGeckolibData(CustomDataTickets.USE_EYE_TEXTURE, true);
                this.renderer.renderBone(renderState, poseStack2, bone, vertexConsumer, cameraState, packedLight, packedOverlay, renderColor);
                renderState.addGeckolibData(CustomDataTickets.USE_EYE_TEXTURE, false);
            }
        });
    }

    @Override
    public void addPerBoneRender(R renderState, BakedGeoModel model, boolean didRenderModel, BiConsumer<GeoBone, PerBoneRender<R>> consumer) {
//        if (!didRenderModel) return;
//        for (String boneName : this.boneNames){
//            for (String name : model.boneMap().get().keySet()){
//                if(name.contains(boneName)){
//                    model.getBone(name).ifPresent(bone -> consumer.accept(bone, this::renderBone));
//                }
//                //System.out.println("BONE NAME: " + boneName + " ?= " + bone.getName());
//            }
//        }

    }
    /**
     * Render the bone with the replacement texture
     */
    protected void renderBone(R renderState, MatrixStack poseStack, GeoBone bone, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraState,
                              int packedLight, int packedOverlay, int renderColor) {

        Identifier boneTexture = getTextureResource(renderState);

        renderTasks.submitCustom(poseStack, getRenderType(renderState, boneTexture), (pose, buffer) -> {
            MatrixStack poseStack2 = new MatrixStack();

            poseStack2.peek().copy(pose);
            bone.setHidden(false);
            bone.setChildrenHidden(true);

            for (GeoCube cube : bone.getCubes()) {
                poseStack2.push();
                this.renderer.renderCube(renderState, cube, poseStack2, buffer, cameraState, packedLight, packedOverlay, renderColor);
                poseStack2.pop();
            }

            bone.setHidden(false);
        });
    }

    /**
     * This method is called by the {@link GeoRenderer} before rendering, immediately after {@link GeoRenderer#preRender} has been called
     * <p>
     * This allows for RenderLayers to perform pre-render manipulations such as hiding or showing bones.
     * <p>
     * <b><u>NOTE:</u></b> Changing VertexConsumers or RenderTypes must not be performed here<br>
     * <b><u>NOTE:</u></b> If the passed {@link VertexConsumer buffer} is null, then the animatable was not actually rendered (invisible, etc)
     * and you may need to factor this in to your design
     */
    @ApiStatus.Internal
    @Override
    public void preRender(R renderState, MatrixStack poseStack, BakedGeoModel bakedModel, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraState,
                          int packedLight, int packedOverlay, int renderColor, boolean didRenderModel) {
        if (didRenderModel) {
            for(GeoBone bone : bakedModel.topLevelBones()){
//                bone.setHidden(true);
//                bone.setChildrenHidden(true);
            }
        }
    }


    //    @Override
//    public void render(R renderState, MatrixStack poseStack, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int renderColor) {
//
//        NbtCompound altNbt = renderState.getGeckolibData(CustomDataTickets.ENTITY_DATA).getCompound("alt").orElse(new NbtCompound());
//
//        if(altNbt.isEmpty()) return;
//
//        String texture = altNbt.getString("eyes_texture").orElse("");
//
//        if(texture.isEmpty()) return;
//
//        RenderLayer translucentRenderType = RenderLayer.getEntityTranslucent(Identifier.of(FnafUniverseRebuilt.MOD_ID, texture));
//
//        getRenderer().reRender(renderState, poseStack, getDefaultBakedModel(renderState), bufferSource, translucentRenderType, bufferSource.getBuffer(translucentRenderType), packedLight, packedOverlay, 0xFFFFFFFF);
//    }
}

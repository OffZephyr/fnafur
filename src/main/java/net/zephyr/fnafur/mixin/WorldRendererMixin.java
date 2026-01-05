package net.zephyr.fnafur.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.OutlineRenderState;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrame;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.client.ClientHook;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IWorldRendererAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin<R extends BlockEntityRenderState & GeoRenderState> implements IWorldRendererAccessor<R> {
    @Shadow
    private ClientWorld world;

    @Shadow @Final private BlockEntityRenderManager blockEntityRenderManager;
    @Unique
    Map<R, BlockEntityRenderer<?, R>> entityRenderStates = new HashMap();

//    @Redirect(
//            method = "getEntitiesToRender",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/render/Camera;isThirdPerson()Z"
//            )
//    )
//
//    public boolean Goopy_player_isThirdPerson(Camera camera) {
//        return camera.isThirdPerson()|| /*CameraRenderer.isDrawing() ||*/ MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen;
//    }

    @Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
    public void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, double x, double y, double z, OutlineRenderState state, int color, float lineWidth, CallbackInfo ci){
        BlockState blockState = world.getBlockState(state.pos());
        BlockPos pos = state.pos();
        if(blockState.getBlock() instanceof PropBlock) {
            PropBlock.drawBlockOutlineHook(world, blockState, pos, matrices, vertexConsumer, x, y, z, state, color, lineWidth);
            ci.cancel();
        }
    }

    @Inject(method = "renderBlockEntities", at = @At("TAIL"))
    void renderBlockEntities(MatrixStack matrices, WorldRenderState renderStates, OrderedRenderCommandQueueImpl queue, CallbackInfo ci){

        if(!getEntityRenderStates().isEmpty()) {
            for (R renderState : getEntityRenderStates().keySet()) {
                ClientHook.renderWorldBlockEntity(getBlockEntityRenderer(renderState), renderState, matrices, renderStates, queue);
            }
        }
        clearStates();

    }


    @Override
    public Map<R, BlockEntityRenderer<?, R>> getEntityRenderStates() {
        return entityRenderStates;
    }
    @Override
    public BlockEntityRenderer<?, R> getBlockEntityRenderer(R renderState) {
        return entityRenderStates.get(renderState);
    }

    @Override
    public void addEntityRenderState(R renderState, BlockEntityRenderer<?, R> entity) {
        entityRenderStates.put(renderState, entity);
    }

    @Override
    public void clearStates() {
        entityRenderStates.clear();
    }
}

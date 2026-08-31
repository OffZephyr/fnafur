package net.zephyr.fnafur.mixin;

import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.client.ClientHook;
import net.zephyr.fnafur.util.mixinAccessing.IWorldRendererAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.geckolib.renderer.base.GeoRenderState;

import java.util.HashMap;
import java.util.Map;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin<R extends BlockEntityRenderState & GeoRenderState> implements IWorldRendererAccessor<R> {
    @Shadow
    private ClientLevel level;

    @Shadow @Final private BlockEntityRenderDispatcher blockEntityRenderDispatcher;
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
//        return camera.isThirdPerson()|| /*CameraRenderer.isDrawing() ||*/ Minecraft.getInstance().currentScreen instanceof CameraTabletScreen;
//    }

    @Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
    public void drawBlockOutline(PoseStack matrices, VertexConsumer vertexConsumer, double x, double y, double z, BlockOutlineRenderState state, int color, float lineWidth, CallbackInfo ci){
        BlockState blockState = level.getBlockState(state.pos());
        BlockPos pos = state.pos();
        if(blockState.getBlock() instanceof PropBlock) {
            PropBlock.drawBlockOutlineHook(level, blockState, pos, matrices, vertexConsumer, x, y, z, state, color, lineWidth);
            ci.cancel();
        }
    }

    @Inject(method = "submitBlockEntities", at = @At("TAIL"))
    void renderBlockEntities(final PoseStack poseStack, final LevelRenderState levelRenderState, final SubmitNodeStorage submitNodeStorage, CallbackInfo ci){

        if(!getEntityRenderStates().isEmpty()) {
            for (R renderState : getEntityRenderStates().keySet()) {
                ClientHook.renderWorldBlockEntity(getBlockEntityRenderer(renderState), renderState, poseStack, levelRenderState, submitNodeStorage);
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

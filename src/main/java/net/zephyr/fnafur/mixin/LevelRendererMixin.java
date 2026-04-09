package net.zephyr.fnafur.mixin;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.LevelRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
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
    void renderBlockEntities(PoseStack matrices, LevelRenderState renderStates, SubmitNodeStorage queue, CallbackInfo ci){

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

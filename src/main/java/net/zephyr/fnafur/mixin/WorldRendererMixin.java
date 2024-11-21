package net.zephyr.fnafur.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.shape.VoxelShape;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.camera_desk.CameraRenderer;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.client.gui.screens.CameraTabletScreen;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    private ClientWorld world;

    @Redirect(
            method = "getEntitiesToRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;isThirdPerson()Z"
            )
    )

    public boolean Goopy_player_isThirdPerson(Camera camera) {
        return camera.isThirdPerson()|| CameraRenderer.isDrawing() || MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen;
    }


    @Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
    public void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, int color, CallbackInfo ci){
        if(state.getBlock() instanceof PropBlock) {
            FloorPropBlock.drawingOutline = true;
            matrices.push();
            float rotation = ((IEntityDataSaver)this.world.getBlockEntity(pos)).getPersistentData().getFloat("Rotation") + 180;

            double offsetX = ((IEntityDataSaver)this.world.getBlockEntity(pos)).getPersistentData().getDouble("xOffset");
            double offsetY = ((IEntityDataSaver)this.world.getBlockEntity(pos)).getPersistentData().getDouble("yOffset");
            double offsetZ = ((IEntityDataSaver)this.world.getBlockEntity(pos)).getPersistentData().getDouble("zOffset");

            double posX = pos.getX();
            double posY = pos.getY();
            double posZ = pos.getZ();

            matrices.translate(offsetX + posX, offsetY + posY, offsetZ + posZ);

            matrices.translate(-cameraX,-cameraY,-cameraZ);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rotation));
            matrices.translate(-0.5f, 0, -0.5f);
            if(state.getBlock() instanceof WallPropBlock<?>) {
                matrices.translate(0, -0.5f, 0);
            }

            VertexRendering.drawOutline(
                    matrices,
                    vertexConsumer,
                    state.getOutlineShape(this.world, pos, ShapeContext.of(entity)),
                    0,
                    0,
                    0,
                    color

            );

            matrices.pop();
            FloorPropBlock.drawingOutline = false;
            ci.cancel();
        }
    }


}

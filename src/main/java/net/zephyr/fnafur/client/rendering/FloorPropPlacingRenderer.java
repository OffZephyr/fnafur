package net.zephyr.fnafur.client.rendering;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;

public class FloorPropPlacingRenderer {
        public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {

            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            if (player.getMainHandStack() != null && player.getMainHandStack().getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof FloorPropBlock block) {
                    FloorPropBlock.drawingOutline = true;
                    HitResult blockHit = client.crosshairTarget;
                    if (blockHit.getType() == HitResult.Type.BLOCK) {
                        BlockPos pos = ((BlockHitResult) blockHit).getBlockPos();
                        Vec3d hitPos = blockHit.getPos().add(0, 1, 0);
                        if (((BlockHitResult) blockHit).getSide() == Direction.UP) pos = pos.up();
                        if (((BlockHitResult) blockHit).getSide() == Direction.DOWN) pos = pos.down();

                        float rotation = -MinecraftClient.getInstance().gameRenderer.getCamera().getYaw();

                        double x = hitPos.getX() - pos.getX();
                        double y = pos.getY();
                        double z = hitPos.getZ() - pos.getZ();

                        x = Math.clamp(x, 0, 1);
                        z = Math.clamp(z, 0, 1);

                        if(player.isSneaking()){
                            x = Math.round(x / PropBlock.gridSnap) * PropBlock.gridSnap;
                            z = Math.round(z / PropBlock.gridSnap) * PropBlock.gridSnap;
                            pos = pos.offset(((BlockHitResult) blockHit).getSide());

                            rotation = Math.round(rotation / PropBlock.angleSnap) * PropBlock.angleSnap;
                        }
                        else if(!client.world.getBlockState(pos).isOf(Blocks.AIR)){
                            if (((BlockHitResult) blockHit).getSide() == Direction.NORTH) z = 0;
                            if (((BlockHitResult) blockHit).getSide() == Direction.EAST) x = 1;
                            if (((BlockHitResult) blockHit).getSide() == Direction.SOUTH) z = 1;
                            if (((BlockHitResult) blockHit).getSide() == Direction.WEST) x = 0;
                        }

                        BlockState state = player.getMainHandStack().getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT).applyToState(block.getDefaultState());

                        VoxelShape shape = state.getOutlineShape(client.world, pos, ShapeContext.absent());

                        matrices.push();
                        matrices.translate(-cameraX + 0.5f, -cameraY, -cameraZ + 0.5f);

                        matrices.translate(-0.5f, 0, -0.5f);
                        matrices.translate(x + pos.getX() - 0.5f, y, z + pos.getZ() - 0.5f);

                        matrices.translate(0.5f, 0, 0.5f);
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
                        matrices.translate(-0.5f, 0, -0.5f);
                        if(block instanceof GeoPropBlock){

                        }
                        else {
                            BakedModel model = client.getBakedModelManager().getBlockModels().getModel(state);
                            client.getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)), state, model, 1, 1, 1, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
                        }

                        VertexRendering.drawOutline(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), shape, 0, 0, 0, 0x88FFFFFF);
                        matrices.pop();
                        FloorPropBlock.drawingOutline = false;
                    }
                }
            }
        }
}

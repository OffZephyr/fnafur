package net.zephyr.fnafur.client.rendering;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.WallHalfProperty;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.blocks.utility_blocks.cosmo_gift.CosmoGift;
import net.zephyr.fnafur.entity.animatronic.block.AnimatronicBlock;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class FloorPropPlacingRenderer {
        public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {

            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;

            if(!player.getAbilities().allowModifyWorld) return;

            if (player.getMainHandStack() != null && player.getMainHandStack().getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof PropBlock<?> block) {
                    PropBlock.drawingOutline = true;
                    HitResult blockHit = client.crosshairTarget;
                    if (blockHit.getType() == HitResult.Type.BLOCK && blockHit instanceof BlockHitResult blockHitResult) {
                        BlockPos pos = blockHitResult.getBlockPos();
                        Vec3d hitPos = blockHitResult.getPos();

                        if(block instanceof CosmoGift && player.getWorld().getBlockState(pos).isOf(BlockInit.ANIMATRONIC_BLOCK)) return;

                        float rotation = -MinecraftClient.getInstance().gameRenderer.getCamera().getYaw();
                        float offsetRotation = !block.rotates() ? 0 : block.getDefaultState().get(FloorPropBlock.FACING).getOpposite().getPositiveHorizontalDegrees();

                        if(block.snapsVertically()) hitPos = new Vec3d(hitPos.getX(), 0, hitPos.getZ());

                        double x = hitPos.getX() - pos.getX();
                        double y = hitPos.getY() - pos.getY();
                        double z = hitPos.getZ() - pos.getZ();

                        x = Math.clamp(x, 0, 1);
                        y = Math.clamp(y, 0, 1);
                        z = Math.clamp(z, 0, 1);

                        if (block.snapsVertically() && pos.getY() >= 0) pos = pos.up();
                        if (block.snapsVertically() && blockHitResult.getSide() == Direction.UP) pos = pos.up();
                        if (block.snapsVertically() && blockHitResult.getSide() == Direction.DOWN) pos = pos.down();

                        if (!(block instanceof WallPropBlock<?>) && !block.snapsVertically()) pos = pos.up();

                        if (block instanceof WallPropBlock<?>) pos = pos.offset(blockHitResult.getSide());

                        if(player.isSneaking()){
                            x = Math.round(x / PropBlock.gridSnap) * PropBlock.gridSnap;
                            y = Math.round(y / PropBlock.gridSnap) * PropBlock.gridSnap;
                            z = Math.round(z / PropBlock.gridSnap) * PropBlock.gridSnap;

                            rotation = Math.round(rotation / PropBlock.angleSnap) * PropBlock.angleSnap;
                        }

                        BlockState state = player.getMainHandStack().getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT).applyToState(block.getDefaultState());

                        if(block instanceof WallPropBlock<?> wallBlock){

                            WallHalfProperty half;
                            Direction facing;
                            if(blockHitResult.getSide().getAxis() == Direction.Axis.Y){
                                half = blockHitResult.getSide() == Direction.UP ? WallHalfProperty.FLOOR : WallHalfProperty.CEILING;
                                facing = player.getHorizontalFacing().getOpposite();
                                y = 0.5f;
                                if(wallBlock.lockY(state)) {
                                    x = 0.5f;
                                    z = 0.5f;
                                }
                            }
                            else {
                                half = WallHalfProperty.WALL;
                                facing = blockHitResult.getSide();
                                x = facing.getAxis() == Direction.Axis.X ? 0.5f : x;
                                z = facing.getAxis() == Direction.Axis.Z ? 0.5f : z;
                            }

                            state = state
                                    .with(WallPropBlock.FACING, facing)
                                    .with(WallPropBlock.HALF, half);
                        }


                        VoxelShape shape = state.getOutlineShape(client.world, pos, ShapeContext.absent());

                        matrices.push();
                        if(block instanceof WallPropBlock){
                            matrices.translate(-cameraX, -cameraY, -cameraZ);
                            matrices.translate(-0.5f, -0.5f, -0.5f);

                            matrices.translate(x + pos.getX(), y + pos.getY(), z + pos.getZ());
                        }
                        else {
                            matrices.translate(-cameraX, -cameraY, -cameraZ);

                            matrices.translate(0, -1, 0);
                            matrices.translate(x + pos.getX(), y + pos.getY(), z + pos.getZ());
                            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
                            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(offsetRotation));
                            matrices.translate(-0.5f, 0, -0.5f);
                        }

                        if(block instanceof GeoPropBlock){

                            matrices.push();

                            if(block instanceof WallPropBlock) {
                                float offsetRotation2 = state.get(WallPropBlock.FACING).getOpposite().getPositiveHorizontalDegrees();
                                matrices.translate(0.5f, 0, 0.5f);
                                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-offsetRotation2));
                                matrices.translate(-0.5f, 0, -0.5f);
                            }
                            GeoPropBlockEntity entity = (GeoPropBlockEntity) block.createBlockEntity(pos, block.getDefaultState());

                            entity.setWorld(MinecraftClient.getInstance().world);
                            if(MinecraftClient.getInstance().getBlockEntityRenderDispatcher().get(entity) instanceof GeoPropRenderer<GeoPropBlockEntity> geo){
                                geo.render(entity, matrices, vertexConsumers, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
                            }
                            matrices.pop();
                        }
                        else if(block instanceof AnimatronicBlock){
                            GeoPropBlockEntity entity = (GeoPropBlockEntity) block.createBlockEntity(pos, block.getDefaultState());
                            NbtCompound nbt = ItemNbtUtil.getNbt(player.getMainHandStack());

                            entity.setWorld(MinecraftClient.getInstance().world);
                            if(MinecraftClient.getInstance().getBlockEntityRenderDispatcher().get(entity) instanceof GeoPropRenderer<GeoPropBlockEntity> geo){
                                matrices.push();
                                geo.render(entity, matrices, vertexConsumers, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
                                matrices.pop();
                            }
                        }
                        else {
                            BakedModel model = client.getBakedModelManager().getBlockModels().getModel(state);
                            client.getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)), state, model, 1, 1, 1, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
                        }

                        matrices.translate(0.5f, 0, 0.5f);
                        float boxRot = block instanceof WallPropBlock ? 180 : -offsetRotation;
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                        matrices.translate(-0.5f, 0, -0.5f);
                        VertexRendering.drawOutline(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), shape, 0, 0, 0, 0x88FFFFFF);
                        matrices.pop();
                        FloorPropBlock.drawingOutline = false;
                    }
                }
            }
        }
}

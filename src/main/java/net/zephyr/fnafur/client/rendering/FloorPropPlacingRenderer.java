package net.zephyr.fnafur.client.rendering;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrame;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrameModel;
import net.zephyr.fnafur.blocks.props.base.*;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift.CosmoGift;
import net.zephyr.fnafur.entity.animatronic.block.AnimationList;
import net.zephyr.fnafur.entity.animatronic.block.AnimatronicBlock;
import net.zephyr.fnafur.entity.animatronic.block.AnimatronicBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.util.mixinAccessing.IGetClientManagers;

import java.util.List;

public class FloorPropPlacingRenderer {

    private static final Direction[] DIRECTIONS = Direction.values();
    GeoPropBlockEntity placementEntity = null;
    BlockEntityRenderer<GeoPropBlockEntity> renderer = null;
        public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {

            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;

            if(!player.getAbilities().allowModifyWorld) return;

            if (player.getMainHandStack() != null && player.getMainHandStack().getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof PropBlock<?> block) {
                    PropBlock.drawingOutline = true;
                    DiagonalMimicFrame.drawingOutline = true;
                    HitResult blockHit = client.crosshairTarget;
                    if (blockHit.getType() == HitResult.Type.BLOCK && blockHit instanceof BlockHitResult blockHitResult) {
                        BlockPos pos = blockHitResult.getBlockPos();
                        Vec3d hitPos = blockHitResult.getPos();

                        BlockPos hitBlockPos = blockHitResult.getBlockPos();
                        BlockState hitBlockState = client.world.getBlockState(blockHitResult.getBlockPos());
                        Block hitBlock = hitBlockState.getBlock();

                        if (block instanceof CosmoGift && player.getWorld().getBlockState(pos).isOf(BlockInit.ANIMATRONIC_BLOCK))
                            return;
                        float rotation = -MinecraftClient.getInstance().gameRenderer.getCamera().getYaw();
                        float offsetRotation = !block.rotates() ? 0 : block.getDefaultState().get(FloorPropBlock.FACING).getOpposite().getPositiveHorizontalDegrees();
                        float wallOffsetRotation = 0;

                        if (block.snapsVertically()) hitPos = new Vec3d(hitPos.getX(), 0, hitPos.getZ());

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

                        if (player.isSneaking()) {
                            x = Math.round(x / PropBlock.gridSnap) * PropBlock.gridSnap;
                            y = Math.round(y / PropBlock.gridSnap) * PropBlock.gridSnap;
                            z = Math.round(z / PropBlock.gridSnap) * PropBlock.gridSnap;

                            rotation = Math.round(rotation / PropBlock.angleSnap) * PropBlock.angleSnap;
                        }

                        BlockState state = player.getMainHandStack().getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT).applyToState(block.getDefaultState());

                        if (block instanceof WallPropBlock<?> wallBlock) {

                            WallHalfProperty half;
                            Direction facing;
                            if (blockHitResult.getSide().getAxis() == Direction.Axis.Y) {
                                half = blockHitResult.getSide() == Direction.UP ? WallHalfProperty.FLOOR : WallHalfProperty.CEILING;
                                facing = player.getHorizontalFacing().getOpposite();
                                y = 0.5f;
                                if (wallBlock.lockY(state)) {
                                    x = 0.5f;
                                    z = 0.5f;
                                }
                            } else {
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
                        matrices.translate(-cameraX, -cameraY, -cameraZ);

                        if (block instanceof WallPropBlock) {
                            Direction d = Direction.UP;
                            if (blockHitResult.getSide().getAxis() != Direction.Axis.Y) {
                                if (hitBlockState.getBlock() instanceof DiagonalMimicFrame f) {
                                    if (f.isDiagonal(hitBlockState)) {
                                        BooleanProperty p = f.getDiagonalDirection(hitBlockState);
                                        if (p == DiagonalMimicFrame.NEXT_MAP.get(blockHitResult.getSide())) {
                                            d = blockHitResult.getSide();
                                            wallOffsetRotation = -45f;
                                        }
                                        if (p == DiagonalMimicFrame.NEXT_MAP.get(blockHitResult.getSide().rotateYCounterclockwise())) {
                                            d = blockHitResult.getSide().rotateYCounterclockwise();
                                            wallOffsetRotation = 45f;
                                        }
                                    }

                                }
                            }
//
                            if (hitBlockState.getBlock() instanceof DiagonalMimicFrame && d != Direction.UP) {
                                Vec3d editPos = new Vec3d(x, y, z);

                                matrices.translate(0.25f, 0, 0.25f);
                                if(d.getAxis() == Direction.Axis.Z) {
                                    if (d == ((BlockHitResult) blockHit).getSide()){
                                        matrices.translate((editPos.getX()/2f) + pos.getX(), editPos.getY() + pos.getY(), (editPos.getX()/2f) + pos.getZ());
                                    } else {
                                        matrices.translate((editPos.getZ()/2f) + pos.getX(), editPos.getY() + pos.getY(), (editPos.getZ()/2f) + pos.getZ());
                                    }
                                } else {

                                    matrices.translate(0.5f * Math.abs(blockHitResult.getSide().getVector().getX()), 0, 0.5f * Math.abs(blockHitResult.getSide().getVector().getZ()));
                                    if (d == ((BlockHitResult) blockHit).getSide()){
                                        matrices.translate(((-editPos.getZ())/2f) + pos.getX(), editPos.getY() + pos.getY(), ((editPos.getZ())/2f) + pos.getZ());
                                    } else {
                                        matrices.translate(((editPos.getX())/2f) + pos.getX(), editPos.getY() + pos.getY(), ((-editPos.getX())/2f) + pos.getZ());
                                    }
                                }
                                matrices.translate(-0.5f * blockHitResult.getSide().getVector().getX(), 0, -0.5f * blockHitResult.getSide().getVector().getZ());
                                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(wallOffsetRotation));
                                matrices.translate(0.15f * blockHitResult.getSide().getVector().getX(), 0, 0.15f * blockHitResult.getSide().getVector().getZ());
                                matrices.translate(-0.5f, -0.5f, -0.5f);
                            }
                            else{
                                matrices.translate(x + pos.getX(), y + pos.getY(), z + pos.getZ());
                                matrices.translate(-0.5f, -0.5f, -0.5f);
                            }
                        } else {
                            matrices.translate(0, -1, 0);
                            matrices.translate(x + pos.getX(), y + pos.getY(), z + pos.getZ());
                            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
                            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(offsetRotation));
                            matrices.translate(-0.5f, 0, -0.5f);
                        }

                        if (block instanceof GeoPropBlock || block instanceof AnimatronicBlock) {

                            matrices.push();

                            if (block instanceof WallPropBlock) {

                                float offsetRotation2;
                                offsetRotation2 = state.get(WallPropBlock.FACING).getOpposite().getPositiveHorizontalDegrees();
                                matrices.translate(0.5f, 0, 0.5f);
                                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-offsetRotation2));
                                matrices.translate(-0.5f, 0, -0.5f);
                            }
                            if (!(placementEntity instanceof GeoPropBlockEntity) || !placementEntity.getCachedState().equals(block.getDefaultState())) {

                                if (block instanceof AnimatronicBlock b) {
                                    placementEntity = (GeoPropBlockEntity) b.createBlockEntity(pos, block.getDefaultState(), false);
                                } else {
                                    placementEntity = (GeoPropBlockEntity) block.createBlockEntity(pos, block.getDefaultState());
                                }
                                placementEntity.setWorld(MinecraftClient.getInstance().world);

                                renderer = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().get(placementEntity);
                            }

                            if (renderer instanceof GeoPropRenderer<GeoPropBlockEntity> geo) {

                                if (block instanceof AnimatronicBlock) {
                                    AnimationList pose = AnimatronicBlock.getPose(client.world, pos, player.getHorizontalFacing().getOpposite());
                                    ((AnimatronicBlockEntity) placementEntity).previewState = state.with(AnimatronicBlock.POSES, pose);
                                }

                                geo.render(placementEntity, matrices, vertexConsumers, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
                            }
                            matrices.pop();
                        } else {

                            BlockStateModel model = client.getBakedModelManager().getBlockModels().getModel(state);
                            if (model instanceof PropBlockModel pModel) {
                                model = pModel.ORIGINAL_MODEL;
                            }
                            renderBakedModel(matrices.peek(), vertexConsumers.getBuffer(RenderLayer.getTranslucentMovingBlock()), model, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);

                        }

                        matrices.translate(0.5f, 0, 0.5f);
                        float boxRot = block instanceof WallPropBlock ? 180 : -offsetRotation;
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                        matrices.translate(-0.5f, 0, -0.5f);
                        //VertexRendering.drawOutline(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), shape, 0, 0, 0, 0x88FFFFFF);
                        matrices.pop();
                        PropBlock.drawingOutline = false;
                        DiagonalMimicFrame.drawingOutline = false;
                    }
                }
            }
            else{
                renderer = null;
                placementEntity = null;
            }
        }


    public static void renderBakedModel(
            MatrixStack.Entry entry, VertexConsumer vertexConsumer, BlockStateModel model, int light, int overlay
    ) {
        for (BlockModelPart blockModelPart : model.getParts(Random.create(42L))) {
            for (Direction direction : DIRECTIONS) {
                renderQuads(entry, vertexConsumer, blockModelPart.getQuads(direction), light, overlay);
            }

            renderQuads(entry, vertexConsumer, blockModelPart.getQuads(null), light, overlay);
        }
    }

    private static void renderQuads(
            MatrixStack.Entry entry, VertexConsumer vertexConsumer, List<BakedQuad> quads, int light, int overlay
    ) {
        for (BakedQuad bakedQuad : quads) {
            double time = (System.currentTimeMillis() - ((IGetClientManagers)MinecraftClient.getInstance()).getStartTime()) / 200.0;
            double index = Math.sin(time);
            float alpha = 0.5f + (0.25f * (float)index);
            vertexConsumer.quad(entry, bakedQuad, 1, 1, 1, alpha, light, overlay);
        }
    }

    public static Vec2f GetClosestPointOnLineSegment(Vec2f A, Vec2f B, Vec2f P)
    {
        Vec2f AP = P.add(A.multiply(-1));
        Vec2f AB = B.add(A.multiply(-1));

        float magnitudeAB = AB.lengthSquared();
        float ABAPproduct = AP.dot(AB);
        float distance = ABAPproduct / magnitudeAB;

        if (distance < 0) {
            return A;

        }
        else if (distance > 1) {
            return B;
        }
        else {
            return A.add(AB.multiply(distance));
        }
    }
}

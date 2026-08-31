package net.zephyr.fnafur.client.rendering;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrame;
import net.zephyr.fnafur.blocks.props.base.*;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.mixinAccessing.IGetClientManagers;
import net.zephyr.fnafur.util.mixinAccessing.IWorldRendererAccessor;
import org.joml.Vector2f;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.List;

public class FloorPropPlacingRenderer<R extends BlockEntityRenderState & GeoRenderState> {

    private static final Direction[] DIRECTIONS = Direction.values();
    GeoPropBlockEntity placementEntity = null;
    BlockEntityRenderer<GeoPropBlockEntity, R> renderer = null;
        public void render(PoseStack matrices, MultiBufferSource.BufferSource vertexConsumers, double cameraX, double cameraY, double cameraZ) {

            Minecraft client = Minecraft.getInstance();
            AbstractClientPlayer player = client.player;

            if(!player.getAbilities().mayBuild) return;

            if (player.getMainHandItem() != null && player.getMainHandItem().getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof PropBlock<?> block) {
                    PropBlock.drawingOutline = true;
                    DiagonalMimicFrame.drawingOutline = true;
                    HitResult blockHit = client.hitResult;
                    if (blockHit.getType() == HitResult.Type.BLOCK && blockHit instanceof BlockHitResult blockHitResult) {
                        BlockPos pos = blockHitResult.getBlockPos();
                        Vec3 hitPos = blockHitResult.getLocation();

                        BlockPos hitBlockPos = blockHitResult.getBlockPos();
                        BlockState hitBlockState = client.level.getBlockState(blockHitResult.getBlockPos());
                        Block hitBlock = hitBlockState.getBlock();

                        float rotation = -Minecraft.getInstance().gameRenderer.getMainCamera().yaw();
                        float offsetRotation = !block.rotates() ? 0 : block.defaultBlockState().getValue(FloorPropBlock.FACING).getOpposite().toYRot();
                        float wallOffsetRotation = 0;

                        if (block.snapsVertically()) hitPos = new Vec3(hitPos.x(), 0, hitPos.z());

                        double x = hitPos.x() - pos.getX();
                        double y = hitPos.y() - pos.getY();
                        double z = hitPos.z() - pos.getZ();

                        x = Math.clamp(x, 0, 1);
                        y = Math.clamp(y, 0, 1);
                        z = Math.clamp(z, 0, 1);

                        if (block.snapsVertically() && pos.getY() >= 0) pos = pos.above();
                        if (block.snapsVertically() && blockHitResult.getDirection() == Direction.UP) pos = pos.above();
                        if (block.snapsVertically() && blockHitResult.getDirection() == Direction.DOWN) pos = pos.below();

                        if (!(block instanceof WallPropBlock<?>) && !block.snapsVertically()) pos = pos.above();

                        if (block instanceof WallPropBlock<?>) pos = pos.relative(blockHitResult.getDirection());

                        if (player.isCrouching()) {
                            x = Math.round(x / PropBlock.gridSnap) * PropBlock.gridSnap;
                            y = Math.round(y / PropBlock.gridSnap) * PropBlock.gridSnap;
                            z = Math.round(z / PropBlock.gridSnap) * PropBlock.gridSnap;

                            rotation = Math.round(rotation / PropBlock.angleSnap) * PropBlock.angleSnap;
                        }

                        BlockState state = player.getMainHandItem().getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY).apply(block.defaultBlockState());

                        if (block instanceof WallPropBlock<?> wallBlock) {

                            WallHalfProperty half;
                            Direction facing;
                            if (blockHitResult.getDirection().getAxis() == Direction.Axis.Y) {
                                half = blockHitResult.getDirection() == Direction.UP ? WallHalfProperty.FLOOR : WallHalfProperty.CEILING;
                                facing = player.getDirection().getOpposite();
                                y = 0.5f;
                                if (wallBlock.lockY(state)) {
                                    x = 0.5f;
                                    z = 0.5f;
                                }
                            } else {
                                half = WallHalfProperty.WALL;
                                facing = blockHitResult.getDirection();
                                x = facing.getAxis() == Direction.Axis.X ? 0.5f : x;
                                z = facing.getAxis() == Direction.Axis.Z ? 0.5f : z;
                            }

                            state = state
                                    .setValue(WallPropBlock.FACING, facing)
                                    .setValue(WallPropBlock.HALF, half);
                        }


                        VoxelShape shape = state.getShape(client.level, pos, CollisionContext.empty());

                        matrices.pushPose();
                        matrices.translate(-cameraX, -cameraY, -cameraZ);

                        double xDiagOffset = 0;
                        double zDiagOffset = 0;
                        if (block instanceof WallPropBlock) {
                            Direction d = Direction.UP;
                            if (blockHitResult.getDirection().getAxis() != Direction.Axis.Y) {
                                if (hitBlockState.getBlock() instanceof DiagonalMimicFrame f) {
                                    if (f.isDiagonal(hitBlockState)) {
                                        BooleanProperty p = f.getDiagonalDirection(hitBlockState);
                                        if (p == DiagonalMimicFrame.NEXT_MAP.get(blockHitResult.getDirection())) {
                                            d = blockHitResult.getDirection();
                                            wallOffsetRotation = -45f;
                                        }
                                        if (p == DiagonalMimicFrame.NEXT_MAP.get(blockHitResult.getDirection().getCounterClockWise())) {
                                            d = blockHitResult.getDirection().getCounterClockWise();
                                            wallOffsetRotation = 45f;
                                        }
                                    }

                                }
                            }

                            if (hitBlockState.getBlock() instanceof DiagonalMimicFrame && d != Direction.UP) {
                                Vec3 editPos = new Vec3(x, y, z);

                                xDiagOffset = 0.25f;
                                zDiagOffset = 0.25f;
                                matrices.translate(0.25f, 0, 0.25f);
                                if(d.getAxis() == Direction.Axis.Z) {
                                    if (d == ((BlockHitResult) blockHit).getDirection()){
                                        xDiagOffset += (editPos.x()/2f);
                                        zDiagOffset += (editPos.x()/2f);
                                        matrices.translate((editPos.x()/2f) + pos.getX(), editPos.y() + pos.getY(), (editPos.x()/2f) + pos.getZ());
                                    } else {
                                        xDiagOffset += (editPos.z()/2f);
                                        zDiagOffset += (editPos.z()/2f);
                                        matrices.translate((editPos.z()/2f) + pos.getX(), editPos.y() + pos.getY(), (editPos.z()/2f) + pos.getZ());
                                    }
                                } else {

                                    xDiagOffset += 0.5f * Math.abs(blockHitResult.getDirection().getUnitVec3i().getX());
                                    zDiagOffset += 0.5f * Math.abs(blockHitResult.getDirection().getUnitVec3i().getZ());
                                    matrices.translate(0.5f * Math.abs(blockHitResult.getDirection().getUnitVec3i().getX()), 0, 0.5f * Math.abs(blockHitResult.getDirection().getUnitVec3i().getZ()));
                                    if (d == ((BlockHitResult) blockHit).getDirection()){
                                        xDiagOffset += (-editPos.z()/2f);
                                        zDiagOffset += (editPos.z()/2f);
                                        matrices.translate(((-editPos.z())/2f) + pos.getX(), editPos.y() + pos.getY(), ((editPos.z())/2f) + pos.getZ());
                                    } else {
                                        xDiagOffset += (editPos.z()/2f);
                                        zDiagOffset += (-editPos.z()/2f);
                                        matrices.translate(((editPos.x())/2f) + pos.getX(), editPos.y() + pos.getY(), ((-editPos.x())/2f) + pos.getZ());
                                    }
                                }
                                matrices.translate(-0.5f * blockHitResult.getDirection().getUnitVec3i().getX(), 0, -0.5f * blockHitResult.getDirection().getUnitVec3i().getZ());
                                matrices.mulPose(Axis.YP.rotationDegrees(wallOffsetRotation));
                                matrices.translate(0.15f * blockHitResult.getDirection().getUnitVec3i().getX(), 0, 0.15f * blockHitResult.getDirection().getUnitVec3i().getZ());
                                matrices.translate(-0.5f, -0.5f, -0.5f);
                                xDiagOffset += -0.5f;
                                zDiagOffset += -0.5f;
                            }
                            else{
                                matrices.translate(x + pos.getX(), y + pos.getY(), z + pos.getZ());
                                matrices.translate(-0.5f, -0.5f, -0.5f);
                            }
                        } else {
                            matrices.translate(0, -1, 0);
                            matrices.translate(x + pos.getX(), y + pos.getY(), z + pos.getZ());
                            matrices.mulPose(Axis.YP.rotationDegrees(rotation));
                            matrices.mulPose(Axis.YP.rotationDegrees(offsetRotation));
                            matrices.translate(-0.5f, 0, -0.5f);
                        }

                        if (block instanceof GeoPropBlock) {

                            matrices.pushPose();

                            if (block instanceof WallPropBlock) {

                                float offsetRotation2;
                                offsetRotation2 = state.getValue(WallPropBlock.FACING).getOpposite().toYRot();
                                matrices.translate(0.5f, 0, 0.5f);
                                matrices.mulPose(Axis.YP.rotationDegrees(-offsetRotation2));
                                matrices.translate(-0.5f, 0, -0.5f);
                            }
                            if (!(placementEntity instanceof GeoPropBlockEntity) || !placementEntity.getBlockState().equals(state)) {

                                placementEntity = (GeoPropBlockEntity) block.newBlockEntity(pos, state);
                                placementEntity.setLevel(Minecraft.getInstance().level);

                                renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(placementEntity);
                            }
                            if (renderer instanceof GeoPropRenderer<GeoPropBlockEntity,R> geo) {

                                placementEntity.item = true;
                                R renderState = geo.fillRenderState(placementEntity, null, geo.createRenderState(), Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false));

                                renderState.lightCoords = LightTexture.FULL_BRIGHT;

                                renderState.addGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW, true);
                                renderState.addGeckolibData(CustomDataTickets.ENTITY_RENDER_MATRIX_ENTRY, matrices.last());
                                renderState.addGeckolibData(CustomDataTickets.X_OFFSET, x + pos.getX() + xDiagOffset);
                                renderState.addGeckolibData(CustomDataTickets.Y_OFFSET, y + pos.getY());
                                renderState.addGeckolibData(CustomDataTickets.Z_OFFSET, z + pos.getZ() + zDiagOffset);
                                if(!(block instanceof WallPropBlock<?>)){
                                    renderState.addGeckolibData(CustomDataTickets.ROTATION, rotation + offsetRotation);
                                }
                                else{
                                    renderState.addGeckolibData(CustomDataTickets.Y_OFFSET, y + pos.getY() + 0.5f);
                                    renderState.addGeckolibData(CustomDataTickets.ROTATION, 180 - state.getValue(WallPropBlock.FACING).toYRot() + wallOffsetRotation);
                                }
                                ((IWorldRendererAccessor)Minecraft.getInstance().levelRenderer).addEntityRenderState(renderState, geo);
                                placementEntity.item = false;
                            }
                            matrices.popPose();
                        } else {

                            BlockStateModel model = client.getModelManager().getBlockModelShaper().getBlockModel(state);
                            if (model instanceof PropBlockModel pModel) {
                                model = pModel.ORIGINAL_MODEL;
                            }
                            renderBakedModel(matrices.last(), vertexConsumers.getBuffer(RenderTypes.translucentMovingBlock()), model, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

                        }

                        matrices.translate(0.5f, 0, 0.5f);
                        float boxRot = block instanceof WallPropBlock ? 180 : -offsetRotation;
                        matrices.mulPose(Axis.YP.rotationDegrees(180));
                        matrices.translate(-0.5f, 0, -0.5f);
                        //VertexRendering.drawOutline(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), shape, 0, 0, 0, 0x88FFFFFF);
                        matrices.popPose();
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
            PoseStack.Pose entry, VertexConsumer vertexConsumer, BlockStateModel model, int light, int overlay
    ) {
        for (BlockModelPart blockModelPart : model.collectParts(RandomSource.create(42L))) {
            for (Direction direction : DIRECTIONS) {
                renderQuads(entry, vertexConsumer, blockModelPart.getQuads(direction), light, overlay);
            }

            renderQuads(entry, vertexConsumer, blockModelPart.getQuads(null), light, overlay);
        }
    }

    private static void renderQuads(
            PoseStack.Pose entry, VertexConsumer vertexConsumer, List<BakedQuad> quads, int light, int overlay
    ) {
        for (BakedQuad bakedQuad : quads) {
            double time = (System.currentTimeMillis() - ((IGetClientManagers)Minecraft.getInstance()).getStartTime()) / 200.0;
            double index = Math.sin(time);
            float alpha = 0.5f + (0.25f * (float)index);
            vertexConsumer.putBulkData(entry, bakedQuad, 1, 1, 1, alpha, light, overlay);
        }
    }

    public static Vector2f GetClosestPointOnLineSegment(Vector2f A, Vector2f B, Vector2f P)
    {
        Vector2f AP = P.add(A.mul(-1));
        Vector2f AB = B.add(A.mul(-1));

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
            return A.add(AB.mul(distance));
        }
    }
}

package net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors;

import net.fabricmc.fabric.api.client.renderer.v1.render.ChunkSectionLayerHelper;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.common_block_entity.CommonBlockEntityRenderState;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TileDoorBlockEntityRenderer implements BlockEntityRenderer<TileDoorBlockEntity, CommonBlockEntityRenderState> {

    public TileDoorBlockEntityRenderer(BlockEntityRendererProvider.Context context){

    }

    Vec3 getOffset(TileDoorDirection direction, float delta, float height, float width, boolean invertX){
        switch (direction){
            default: {
                float translateHeight = Mth.lerp(delta, 0, height);
                return new Vec3(0, translateHeight, 0);
            }
            case DOWN: {
                float translateHeight = Mth.lerp(delta, 0, -height);
                return new Vec3(0, translateHeight, 0);
            }
            case LEFT: {
                float translateWidth = Mth.lerp(delta, 0, invertX ? -width : width);
                return new Vec3(translateWidth, 0, 0);
            }
            case RIGHT: {
                float translateWidth = Mth.lerp(delta, 0, invertX ? width : -width);
                return new Vec3(translateWidth, 0, 0);
            }
        }
    }

    @Override
    public CommonBlockEntityRenderState createRenderState() {
        return new CommonBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(TileDoorBlockEntity blockEntity, CommonBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

        if(((IEntityDataSaver)blockEntity).getPersistentData().isEmpty()){
            GoopyNetworkingUtils.getNbtFromServer(blockEntity.getBlockPos());
        }
        state.nbt = ((IEntityDataSaver) blockEntity).getPersistentData();
    }

    @Override
    public void submit(CommonBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {

        BlockState blockState = state.blockState;

        if(blockState.getBlock() instanceof TileDoorBlock b && blockState.getValue(TileDoorBlock.MAIN)){
            int width = state.nbt.getInt("width").orElse(0);
            int height = state.nbt.getInt("height").orElse(0);

            float openDelta = state.nbt.getFloat("openDelta").orElse(0f);
            float speed = Math.clamp(state.nbt.getFloat("speed").orElse(0f), 1, 5);
            if(blockState.getValue(TileDoorBlock.OPEN) && openDelta != 1) {
                state.nbt.putFloat("openDelta", Math.clamp(openDelta + (Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks()/20f) * speed, 0, 1));
            }
            else if(!blockState.getValue(TileDoorBlock.OPEN) && openDelta != 0){
                state.nbt.putFloat("openDelta", Math.clamp(openDelta - (Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks()/20f) * speed, 0, 1));
            }

            float translateMaxHeight = height > 1 ? height + 0.5f : height + 0.8f;
            float translateMaxWidth = width > 1 ? width + 0.5f : width + 0.8f;

            BlockPos testPos = state.blockPos.relative(blockState.getValue(TileDoorBlock.FACING).getCounterClockWise());
            Direction direction = Minecraft.getInstance().level.getBlockState(testPos).getBlock() instanceof TileDoorBlock ? blockState.getValue(TileDoorBlock.FACING).getCounterClockWise() : blockState.getValue(TileDoorBlock.FACING).getClockWise();

            matrices.pushPose();
            TileDoorDirection doorDirection = TileDoorDirection.getDirection(state.nbt.getString("direction").orElse(b.doorDirection.getSerializedName()));
            Vec3 offset = getOffset(doorDirection, openDelta, translateMaxHeight, translateMaxWidth, direction.getAxis() == Direction.Axis.Z);

            double xO = direction.getAxis() == Direction.Axis.X ? offset.x() : offset.z();
            double zO = direction.getAxis() == Direction.Axis.X ? offset.z() : offset.x();
            matrices.translate(xO, offset.y(), zO);

            for (int x = 0; x <= width; x++) {
                boolean bl1 = x - offset.x() < - 1 || x - offset.x() > width + 1;
                boolean bl2 = x + offset.x() < - 1 || x + offset.x() > width + 1;
                boolean check = direction == Direction.NORTH || direction == Direction.WEST ? bl1 : bl2;
                if(check) continue;

                for (int y = 0; y <= height; y++) {
                    if(y + offset.y() < - 1 || y + offset.y() > height + 1) continue;

                    BlockPos updatePos = state.blockPos.above(y).relative(direction, x);

                    BlockState posState = Minecraft.getInstance().level.getBlockState(updatePos);
                    BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(posState);

                    matrices.pushPose();
                    matrices.translate(updatePos.getX() - state.blockPos.getX(),updatePos.getY() - state.blockPos.getY(),updatePos.getZ() - state.blockPos.getZ());

                    //dfmatrices.translate(Minecraft.getInstance().gameRenderer.getCamera().pos.multiply(-1));

                    //queue.submitCustomGeometry(matrices, ItemBlockRenderTypes.getMovingBlockRenderType(posState), (stack, layer) ->{
                    //    ModelBlockRenderer.renderModel(stack, layer, model, 1, 1, 1,  getLightLevel(Minecraft.getInstance().level, state.blockPos), OverlayTexture.NO_OVERLAY);
                    //});

                    List<BlockStateModelPart> parts = new ArrayList<>();
                    model.collectParts(RandomSource.create(), parts);
                    queue.submitBlockModel(matrices, _ -> ChunkSectionLayerHelper.getMovingBlockRenderType(ChunkSectionLayer.SOLID), false, parts, null, new int[0], getLightLevel(Minecraft.getInstance().level, state.blockPos), OverlayTexture.NO_OVERLAY, 0xFF000000);
                    matrices.popPose();

                }
            }
            matrices.popPose();
        }
    }

    private int getLightLevel(Level world, BlockPos pos){
        int bLight = world.getBrightness(LightLayer.BLOCK, pos);
        int sLight = world.getBrightness(LightLayer.SKY, pos);
        return LightCoordsUtil.pack(bLight, sLight);
    }
}

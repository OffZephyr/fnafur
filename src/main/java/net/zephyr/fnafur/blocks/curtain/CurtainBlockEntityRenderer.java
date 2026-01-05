package net.zephyr.fnafur.blocks.curtain;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.*;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.util.EasingMathUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.function.Function;

public class CurtainBlockEntityRenderer implements BlockEntityRenderer<CurtainBlockEntity, CurtainBlockRenderState> {

    static Identifier TEXTURE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/curtain_test.png");
    MinecraftClient client;
    int light = 0;
    int height = 0;

    public CurtainBlockEntityRenderer(BlockEntityRendererFactory.Context context){
        client = MinecraftClient.getInstance();
    }

    @Override
    public CurtainBlockRenderState createRenderState() {
        return new CurtainBlockRenderState();
    }

    @Override
    public void updateRenderState(CurtainBlockEntity blockEntity, CurtainBlockRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        NbtCompound nbt = ((IEntityDataSaver)blockEntity).getPersistentData();

        float openIndex = nbt.getFloat("openIndex").orElse(0f);
        float prevOpenIndex = nbt.getFloat("prevOpenIndex").orElse(0f);
        //openIndex = 0;

//        nbt.putFloat("openIndex", openIndex);
//        nbt.putFloat("prevOpenIndex", openIndex);

        state.height = nbt.getInt("height").orElse(1);
        state.previous = blockEntity.isFirst() ? null : BlockPos.fromLong(nbt.getLong("previous").orElse(0L));
        state.next = blockEntity.isLast() ? null :BlockPos.fromLong(nbt.getLong("next").orElse(0L));
        state.facing = blockEntity.getFacing();
        state.nextFacing = blockEntity.getNextFacing();
        state.isOpening = nbt.getBoolean("isOpenning", false) || nbt.getBoolean("isOpen", false);
        state.openIndex = MathHelper.lerp(MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false), prevOpenIndex, openIndex)/20f;

        state.isLast = blockEntity.isLast();
        state.front = blockEntity.frontCurtain;
        state.back = blockEntity.backCurtain;
        state.prev_front = blockEntity.prevFrontCurtain;
        state.prev_back = blockEntity.prevBackCurtain;
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    }

    @Override
    public void render(CurtainBlockRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {

        matrices.push();
        //matrices.translate(cameraState.pos.multiply(-1));
        //matrices.translate(0, 1.5f, 0);
        //matrices.multiply(MinecraftClient.getInstance().gameRenderer.getCamera().getRotation());

        queue.submitCustom(matrices, RenderLayers.entityCutoutNoCull(TEXTURE), (entry, vertexConsumer) -> {
            renderCurtain(entry, vertexConsumer, state.isLast, state.lightmapCoordinates, state.front, state.prev_front);
            renderCurtain(entry, vertexConsumer, state.isLast, state.lightmapCoordinates, state.back, state.prev_back);
        });
        matrices.pop();
    }

    void renderCurtain(MatrixStack.Entry entry, VertexConsumer vertexConsumer, boolean isLast, int light, CurtainData data, CurtainData prevData) {

//        float startX = 0.5f + backOffset * state.facing.getOffsetX();
//        float startZ = 0.5f + backOffset * state.facing.getOffsetZ();
//        float endX = 0.5f + backOffset * state.facing.getOffsetX();
//        float endZ = 0.5f + backOffset * state.facing.getOffsetZ();
//
//        //state.openIndex += MinecraftClient.getInstance().getRenderTickCounter().getDynamicDeltaTicks();
//        float yOffset = 0.25f;

        if (!isLast) {
//
//            Vec3d difference = state.next.toCenterPos().add(state.pos.toCenterPos().multiply(-1));
//
//            float startX2 = 0.5f + backOffset * state.nextFacing.getOffsetX();
//            float startZ2 = 0.5f + backOffset * state.nextFacing.getOffsetZ();
//            float endX2 = 0.5f + backOffset * state.nextFacing.getOffsetX();
//            float endZ2 = 0.5f + backOffset * state.nextFacing.getOffsetZ();
//
//            startX2 += (float) difference.getX();
//            endX2 += (float) difference.getX();
//            startZ2 += (float) difference.getZ();
//            endZ2 += (float) difference.getZ();
//
//            Vec3d length0 = new Vec3d(endX, 0, endZ);
//            Vec3d length1 = new Vec3d(startX2, 0, startZ2);
//            double length = length0.distanceTo(length1);
//            Vec3d length3 = new Vec3d(startX, 0, startZ);
//            Vec3d length4 = new Vec3d(endX2, 0, endZ2);
//            double length2 = length3.distanceTo(length4);
//
//            length = Math.min(length,length2);
//            boolean left = length == length2;
//
//            Pair<Function<Double, Double>, Function<Double, Double>> easing = getEasing(state);
//
//            length *= 1.5f;
//            double lengthOpen = (easing.getRight().apply((double)(state.openIndex)) * length);
//            if(!canOpen || !state.isOpening) lengthOpen = 0;
//            //lengthOpen = 0;
//
//            float prevUWidth = 0;
//            int intLength = (int) (length);
//            for(double l = lengthOpen; l <= intLength ; l++) {
//
//                float lerpIndex = (float) l /intLength;
//                float lerpIndex2 = (float) (l+1) /intLength;
//                lerpIndex = (float) easing.getLeft().apply((double) lerpIndex).doubleValue();
//                lerpIndex2 = (float) easing.getLeft().apply((double) lerpIndex2).doubleValue();
//                float lerpIndex3 = (float) easing.getRight().apply((double) lerpIndex).doubleValue();
//                float lerpIndex4 = (float) easing.getRight().apply((double) lerpIndex2).doubleValue();
//
//                float posX1 = left? startX : endX;
//                float posX2 = left? endX2 : startX2;
//                float posZ1 = left? startZ : endZ;
//                float posZ2 = left? endZ2 : startZ2;
//
//                float x1 = MathHelper.lerp(lerpIndex2, posX1, posX2);
//                float x2 = MathHelper.lerp(lerpIndex, posX1, posX2);
//                float z1 = MathHelper.lerp(lerpIndex4, posZ1, posZ2);
//                float z2 = MathHelper.lerp(lerpIndex3, posZ1, posZ2);
//                float y2 = (float) MathHelper.lerp((float) (l) /intLength, yOffset, yOffset + difference.getY());
//                float y1 = (float) MathHelper.lerp((float) (l+1) /intLength, yOffset, yOffset + difference.getY());
//
//                if(l >= intLength - 1){
//                    x1 = posX2;
//                    z1 = posZ2;
//                }
//                float uWidth = (float) new Vec3d(x1, y1, z1).distanceTo(new Vec3d(x2, y2, z2));
//                uWidth/=2f;
//
//                float u = prevUWidth;
//                if(canOpen && !state.isOpening){
//                   u -= state.openIndex;
//                }
//                prevUWidth += uWidth;
//
//
            float length = data.length;
            for (int l = 0; l < length; l++) {

                Vector3f pos1 = data.start[l];
                Vector3f pos2 = data.end[l];
                Vector3f prev_pos1 = pos1;
                Vector3f prev_pos2 = pos2;
                if(prevData.length == data.length){
                    prev_pos1 = prevData.start[l];
                    prev_pos2 = prevData.end[l];
                }
                if (pos1 != null && prev_pos1 != null) {

                    float x1 = pos1.x();
                    float y1 = pos1.y();
                    float z1 = pos1.z();
                    float x2 = pos2.x();
                    float y2 = pos2.y();
                    float z2 = pos2.z();

                    float prev_x1 = prev_pos1.x();
                    float prev_y1 = prev_pos1.y();
                    float prev_z1 = prev_pos1.z();
                    float prev_x2 = prev_pos2.x();
                    float prev_y2 = prev_pos2.y();
                    float prev_z2 = prev_pos2.z();

                    int color = data.COLOR;

                    float u = data.u[l];
                    float uWidth = data.uWidth[l];
                    float prev_u = u;
                    float prev_uWidth = uWidth;
                    if(prevData.length == data.length){
                        prev_u = prevData.u[l];
                        prev_uWidth = prevData.uWidth[l];
                    }

                    int height = data.height;

                    double prog = MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false);
                    x1 = (float) (MathHelper.lerp(prog, prev_x1, x1));
                    x2 = (float) (MathHelper.lerp(prog, prev_x2, x2));
                    y1 = (float) (MathHelper.lerp(prog, prev_y1, y1));
                    y2 = (float) (MathHelper.lerp(prog, prev_y2, y2));
                    z1 = (float) (MathHelper.lerp(prog, prev_z1, z1));
                    z2 = (float) (MathHelper.lerp(prog, prev_z2, z2));

                    u = (float) (MathHelper.lerp(prog, prev_u, u));
                    uWidth = (float) (MathHelper.lerp(prog, prev_uWidth, uWidth));

                    for (int i = 0; i < height; i++) {
                        float v = i == height - 1 ? 0.5f : 0;

                        Vec3d vert1 = new Vec3d(x1, -i + y1, z1);
                        Vec3d vert2 = new Vec3d(x1, -i + 1 + y1, z1);
                        Vec3d vert3 = new Vec3d(x2, -i + 1 + y2, z2);
                        Vec3d dir = vert2.add(vert1.multiply(-1)).crossProduct(vert3.add(vert1.multiply(-1)));
                        Vec3d normal = dir.normalize();

                        vertexConsumer
                                .vertex(entry.getPositionMatrix(), x1, -i + y1, z1)
                                .texture(u + uWidth, 0.5f + v)
                                .light(light)
                                .overlay(OverlayTexture.DEFAULT_UV)
                                .normal((float) normal.x, (float) normal.y, (float) normal.z)
                                .color(color);
                        vertexConsumer
                                .vertex(entry.getPositionMatrix(), x1, -i + 1 + y1, z1)
                                .texture(u + uWidth, 0 + v)
                                .light(light)
                                .overlay(OverlayTexture.DEFAULT_UV)
                                .normal((float) normal.x, (float) normal.y, (float) normal.z)
                                .color(color);
                        vertexConsumer
                                .vertex(entry.getPositionMatrix(), x2, -i + 1 + y2, z2)
                                .texture(u, 0 + v)
                                .light(light)
                                .overlay(OverlayTexture.DEFAULT_UV)
                                .normal((float) normal.x, (float) normal.y, (float) normal.z)
                                .color(color);
                        vertexConsumer
                                .vertex(entry.getPositionMatrix(), x2, -i + y2, z2)
                                .texture(u, 0.5f + v)
                                .light(light)
                                .overlay(OverlayTexture.DEFAULT_UV)
                                .normal((float) normal.x, (float) normal.y, (float) normal.z)
                                .color(color);
                    }

                }
            }
        }
    }

    Pair<Function<Double, Double>, Function<Double, Double>> getEasing(CurtainBlockRenderState state){
        Direction direction = state.facing;
        Direction nextDirection = state.nextFacing;

        Pair<Function<Double, Double>, Function<Double, Double>> pair = new Pair<>((index) -> index, (index) -> index);


        if(direction == nextDirection) {
            if((direction.getAxis() == Direction.Axis.X && state.pos.getX() == state.next.getX()) || (direction.getAxis() == Direction.Axis.Z && state.pos.getZ() == state.next.getZ())){
                return pair;
            }
            return new Pair<>(EasingMathUtil::easeOutCirc, EasingMathUtil::easeInCirc);
        }
        return switch (direction.getAxis()){
            default -> pair;
            case X -> new Pair<>(EasingMathUtil::easeInCirc, EasingMathUtil::easeOutCirc);
            case Z -> new Pair<>(EasingMathUtil::easeOutCirc, EasingMathUtil::easeInCirc);
        };
    }
}

package net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class TileDoorBlockEntityRenderer implements BlockEntityRenderer<TileDoorBlockEntity> {

    public TileDoorBlockEntityRenderer(BlockEntityRendererFactory.Context context){

    }
    @Override
    public void render(TileDoorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {

        if(((IEntityDataSaver)entity).getPersistentData().isEmpty()){
            GoopyNetworkingUtils.getNbtFromServer(entity.getPos());
        }

        BlockState state = entity.getWorld().getBlockState(entity.getPos());

        if(state.getBlock() instanceof TileDoorBlock b && state.get(TileDoorBlock.MAIN)){
            int width = ((IEntityDataSaver) entity).getPersistentData().getInt("width").orElse(0);
            int height = ((IEntityDataSaver) entity).getPersistentData().getInt("height").orElse(0);

            float openDelta = ((IEntityDataSaver) entity).getPersistentData().getFloat("openDelta").orElse(0f);
            float speed = Math.clamp(((IEntityDataSaver) entity).getPersistentData().getFloat("speed").orElse(0f), 1, 5);
            if(state.get(TileDoorBlock.OPEN) && openDelta != 1) {
                ((IEntityDataSaver) entity).getPersistentData().putFloat("openDelta", Math.clamp(openDelta + (MinecraftClient.getInstance().getRenderTickCounter().getDynamicDeltaTicks()/20f) * speed, 0, 1));
            }
            else if(!state.get(TileDoorBlock.OPEN) && openDelta != 0){
                ((IEntityDataSaver) entity).getPersistentData().putFloat("openDelta", Math.clamp(openDelta - (MinecraftClient.getInstance().getRenderTickCounter().getDynamicDeltaTicks()/20f) * speed, 0, 1));
            }

            float translateMaxHeight = height > 1 ? height + 0.5f : height + 0.8f;
            float translateMaxWidth = width > 1 ? width + 0.5f : width + 0.8f;

            BlockPos testPos = entity.getPos().offset(state.get(TileDoorBlock.FACING).rotateYCounterclockwise());
            Direction direction = entity.getWorld().getBlockState(testPos).getBlock() instanceof TileDoorBlock ? state.get(TileDoorBlock.FACING).rotateYCounterclockwise() : state.get(TileDoorBlock.FACING).rotateYClockwise();

            matrices.push();
            TileDoorDirection doorDirection = TileDoorDirection.getDirection(((IEntityDataSaver) entity).getPersistentData().getString("direction").orElse(b.doorDirection.asString()));
            Vec3d offset = getOffset(doorDirection, openDelta, translateMaxHeight, translateMaxWidth, direction.getAxis() == Direction.Axis.Z);

            double xO = direction.getAxis() == Direction.Axis.X ? offset.getX() : offset.getZ();
            double zO = direction.getAxis() == Direction.Axis.X ? offset.getZ() : offset.getX();
            matrices.translate(xO, offset.getY(), zO);

            for (int x = 0; x <= width; x++) {
                boolean bl1 = x - offset.getX() < - 1 || x - offset.getX() > width + 1;
                boolean bl2 = x + offset.getX() < - 1 || x + offset.getX() > width + 1;
                boolean check = direction == Direction.NORTH || direction == Direction.WEST ? bl1 : bl2;
                if(check) continue;

                for (int y = 0; y <= height; y++) {
                    if(y + offset.getY() < - 1 || y + offset.getY() > height + 1) continue;

                    BlockPos updatePos = entity.getPos().up(y).offset(direction, x);

                    BlockState posState = entity.getWorld().getBlockState(updatePos);
                    BlockStateModel model = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(posState);

                    matrices.push();
                    matrices.translate(updatePos.getX() - entity.getPos().getX(),updatePos.getY() - entity.getPos().getY(),updatePos.getZ() - entity.getPos().getZ());
                    BlockModelRenderer.render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(state)), model, 1, 1, 1, light, overlay);
                    matrices.pop();
                }
            }
            matrices.pop();
        }

    }

    Vec3d getOffset(TileDoorDirection direction, float delta, float height, float width, boolean invertX){
        switch (direction){
            default: {
                float translateHeight = MathHelper.lerp(delta, 0, height);
                return new Vec3d(0, translateHeight, 0);
            }
            case DOWN: {
                float translateHeight = MathHelper.lerp(delta, 0, -height);
                return new Vec3d(0, translateHeight, 0);
            }
            case LEFT: {
                float translateWidth = MathHelper.lerp(delta, 0, invertX ? -width : width);
                return new Vec3d(translateWidth, 0, 0);
            }
            case RIGHT: {
                float translateWidth = MathHelper.lerp(delta, 0, invertX ? width : -width);
                return new Vec3d(translateWidth, 0, 0);
            }
        }
    }
}

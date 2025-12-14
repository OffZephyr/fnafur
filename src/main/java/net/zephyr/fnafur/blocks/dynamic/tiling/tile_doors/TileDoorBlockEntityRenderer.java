package net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.common_block_entity.CommonBlockEntityRenderState;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class TileDoorBlockEntityRenderer implements BlockEntityRenderer<TileDoorBlockEntity, CommonBlockEntityRenderState> {

    public TileDoorBlockEntityRenderer(BlockEntityRendererFactory.Context context){

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

    @Override
    public CommonBlockEntityRenderState createRenderState() {
        return new CommonBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(TileDoorBlockEntity blockEntity, CommonBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

        if(((IEntityDataSaver)blockEntity).getPersistentData().isEmpty()){
            GoopyNetworkingUtils.getNbtFromServer(blockEntity.getPos());
        }
        state.nbt = ((IEntityDataSaver) blockEntity).getPersistentData();
    }

    @Override
    public void render(CommonBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {

        BlockState blockState = state.blockState;

        if(blockState.getBlock() instanceof TileDoorBlock b && blockState.get(TileDoorBlock.MAIN)){
            int width = state.nbt.getInt("width").orElse(0);
            int height = state.nbt.getInt("height").orElse(0);

            float openDelta = state.nbt.getFloat("openDelta").orElse(0f);
            float speed = Math.clamp(state.nbt.getFloat("speed").orElse(0f), 1, 5);
            if(blockState.get(TileDoorBlock.OPEN) && openDelta != 1) {
                state.nbt.putFloat("openDelta", Math.clamp(openDelta + (MinecraftClient.getInstance().getRenderTickCounter().getDynamicDeltaTicks()/20f) * speed, 0, 1));
            }
            else if(!blockState.get(TileDoorBlock.OPEN) && openDelta != 0){
                state.nbt.putFloat("openDelta", Math.clamp(openDelta - (MinecraftClient.getInstance().getRenderTickCounter().getDynamicDeltaTicks()/20f) * speed, 0, 1));
            }

            float translateMaxHeight = height > 1 ? height + 0.5f : height + 0.8f;
            float translateMaxWidth = width > 1 ? width + 0.5f : width + 0.8f;

            BlockPos testPos = state.pos.offset(blockState.get(TileDoorBlock.FACING).rotateYCounterclockwise());
            Direction direction = MinecraftClient.getInstance().world.getBlockState(testPos).getBlock() instanceof TileDoorBlock ? blockState.get(TileDoorBlock.FACING).rotateYCounterclockwise() : blockState.get(TileDoorBlock.FACING).rotateYClockwise();

            matrices.push();
            TileDoorDirection doorDirection = TileDoorDirection.getDirection(state.nbt.getString("direction").orElse(b.doorDirection.asString()));
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

                    BlockPos updatePos = state.pos.up(y).offset(direction, x);

                    BlockState posState = MinecraftClient.getInstance().world.getBlockState(updatePos);
                    BlockStateModel model = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(posState);

                    matrices.push();
                    matrices.translate(updatePos.getX() - state.pos.getX(),updatePos.getY() - state.pos.getY(),updatePos.getZ() - state.pos.getZ());

                    //dfmatrices.translate(MinecraftClient.getInstance().gameRenderer.getCamera().pos.multiply(-1));
                    queue.submitCustom(matrices, RenderLayers.getMovingBlockLayer(posState), (stack, layer) ->{
                        BlockModelRenderer.render(stack, layer, model, 1, 1, 1,  getLightLevel(MinecraftClient.getInstance().world, state.pos), OverlayTexture.DEFAULT_UV);
                    });
                    matrices.pop();

                }
            }
            matrices.pop();
        }
    }

    private int getLightLevel(World world, BlockPos pos){
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}

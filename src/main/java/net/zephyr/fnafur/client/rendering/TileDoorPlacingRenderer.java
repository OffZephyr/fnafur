package net.zephyr.fnafur.client.rendering;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.zephyr.fnafur.blocks.tile_doors.TileDoorBlock;
import net.zephyr.fnafur.blocks.tile_doors.TileDoorItem;
import net.zephyr.fnafur.util.ItemNbtUtil;

public class TileDoorPlacingRenderer {
    public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {

        matrices.push();
        matrices.translate(-cameraX, -cameraY, -cameraZ);
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        ItemStack stack = player.getMainHandStack();
        if(stack.getItem() instanceof TileDoorItem item){
            NbtCompound nbt = ItemNbtUtil.getNbt(stack);
            if(nbt.contains("pos1")){
                BlockState state = item.getBlock().getDefaultState();

                state = state.with(TileDoorBlock.FACING, player.getHorizontalFacing().getOpposite());

                BakedModel model = client.getBakedModelManager().getBlockModels().getModel(state);

                BlockPos pos1 = BlockPos.fromLong(nbt.getLong("pos1"));
                if(client.crosshairTarget instanceof BlockHitResult hitResult){
                    BlockPos pos2 = hitResult.getBlockPos().offset(hitResult.getSide());

                    Direction dir = player.getHorizontalFacing().getOpposite();
                    Vec3i distance = getDistance(pos1, pos2, dir);

                    int h = Math.clamp(Math.max(distance.getX(), distance.getZ()), 0, 16);
                    int v = Math.clamp(distance.getY(), 0, 16);

                    Vec3i minPos = getMin(pos1, pos2);

                    for(int x = 0; x <= h; x++){
                        for(int y = 0; y <= v; y++){

                            Vec3i pos = new Vec3i(
                                    h == distance.getZ() ? pos1.getX() : minPos.getX() + (Math.abs(dir.getVector().getZ()) * x),
                                    minPos.getY() + y,
                                    h == distance.getX() ? pos1.getZ() :minPos.getZ() + (Math.abs(dir.getVector().getX()) * x)
                            );

                            matrices.push();
                            matrices.translate(pos.getX(), pos.getY(), pos.getZ());

                            client.getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)), state, model, 1, 1, 1, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);

                            matrices.pop();
                        }
                    }
                }
            }
        }
        matrices.pop();
    }

    public static Vec3i getMin(Vec3i pos1, Vec3i pos2){
        return new Vec3i(
                Math.min(pos1.getX(), pos2.getX()),
                Math.min(pos1.getY(), pos2.getY()),
                Math.min(pos1.getZ(), pos2.getZ())
        );
    }

    public static Vec3i getMax(Vec3i pos1, Vec3i pos2){
        return new Vec3i(
                Math.max(pos1.getX(), pos2.getX()),
                Math.max(pos1.getY(), pos2.getY()),
                Math.max(pos1.getZ(), pos2.getZ())
        );
    }
    public static Vec3i getDistance(Vec3i pos1, Vec3i pos2, Direction facing){
        Vec3i direction = facing.getVector();

        Vec3i minPos = getMin(pos1, pos2);
        Vec3i maxPos = getMax(pos1, pos2);

        Vec3i distance =
                new Vec3i(
                        maxPos.getX() - minPos.getX(),
                        maxPos.getY() - minPos.getY(),
                        maxPos.getZ() - minPos.getZ()
                );

        return new Vec3i(
                distance.getX() * Math.abs(direction.getZ()),
                distance.getY(),
                distance.getZ() * Math.abs(direction.getX())
        );
    }
}

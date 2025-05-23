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
import net.zephyr.fnafur.blocks.props.tiling.VerticalTileStates;
import net.zephyr.fnafur.blocks.props.tiling.tile_doors.TileDoorBlock;
import net.zephyr.fnafur.blocks.props.tiling.tile_doors.TileDoorItem;
import net.zephyr.fnafur.util.ItemNbtUtil;

public class TileDoorPlacingRenderer {
    public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {

        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if(!player.getAbilities().allowModifyWorld) return;

        matrices.push();
        matrices.translate(-cameraX, -cameraY, -cameraZ);
        ItemStack stack = player.getMainHandStack();
        if(stack.getItem() instanceof TileDoorItem item){
            NbtCompound nbt = ItemNbtUtil.getNbt(stack);
            if(nbt.contains("pos1")){
                BlockState state = item.getBlock().getDefaultState();

                state = state.with(TileDoorBlock.FACING, player.getHorizontalFacing().getAxis().getNegativeDirection());


                BlockPos pos1 = BlockPos.fromLong(nbt.getLong("pos1"));
                if(client.crosshairTarget instanceof BlockHitResult hitResult){
                    BlockPos pos2 = hitResult.getBlockPos().offset(hitResult.getSide());

                    Direction dir = player.getHorizontalFacing().getOpposite();
                    Vec3i distance = TileDoorItem.getDistance(pos1, pos2, dir);

                    int h = Math.clamp(Math.max(distance.getX(), distance.getZ()), 0, 16);
                    int v = Math.clamp(distance.getY(), 0, 16);

                    Vec3i minPos = TileDoorItem.getMin(pos1, pos2);
                    if(minPos.getY() < pos1.getY()) v = 0;

                    for(int x = 0; x <= h; x++){
                        for(int y = 0; y <= v; y++){

                            boolean right = state.get(TileDoorBlock.FACING).getAxis() == Direction.Axis.Z ? x != h : x != 0;
                            boolean left = state.get(TileDoorBlock.FACING).getAxis() == Direction.Axis.Z ? x != 0 : x != h;
                            state = state.with(TileDoorBlock.TYPE, VerticalTileStates.get(y != v, right, y != 0, left));

                            BakedModel model = client.getBakedModelManager().getBlockModels().getModel(state);

                            Vec3i pos = new Vec3i(
                                    h == distance.getZ() ? pos1.getX() : minPos.getX() + (Math.abs(dir.getVector().getZ()) * x),
                                    pos1.getY() + y,
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
}

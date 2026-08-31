package net.zephyr.fnafur.client.rendering;

import net.minecraft.util.LightCoordsUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.zephyr.fnafur.blocks.dynamic.tiling.VerticalTileStates;
import net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors.TileDoorBlock;
import net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors.TileDoorItem;
import net.zephyr.fnafur.util.ItemUtil;

public class TileDoorPlacingRenderer {
    public void render(PoseStack matrices, MultiBufferSource.BufferSource vertexConsumers, double cameraX, double cameraY, double cameraZ) {

        Minecraft client = Minecraft.getInstance();
        Player player = client.player;

        if(!player.getAbilities().mayBuild) return;

        matrices.pushPose();
        matrices.translate(-cameraX, -cameraY, -cameraZ);
        ItemStack stack = player.getMainHandItem();
        if(stack.getItem() instanceof TileDoorItem item){
            CompoundTag nbt = ItemUtil.getNbt(stack);
            if(nbt.contains("pos1")){
                BlockState state = item.getBlock().defaultBlockState();

                state = state.setValue(TileDoorBlock.FACING, player.getDirection().getAxis().getNegative());


                BlockPos pos1 = BlockPos.of(nbt.getLong("pos1").get());
                if(client.hitResult instanceof BlockHitResult hitResult){
                    BlockPos pos2 = hitResult.getBlockPos().relative(hitResult.getDirection());

                    Direction dir = player.getDirection().getOpposite();
                    Vec3i distance = TileDoorItem.getDistance(pos1, pos2, dir);

                    int h = Math.clamp(Math.max(distance.getX(), distance.getZ()), 0, 16);
                    int v = Math.clamp(distance.getY(), 0, 16);

                    Vec3i minPos = TileDoorItem.getMin(pos1, pos2);
                    if(minPos.getY() < pos1.getY()) v = 0;

                    for(int x = 0; x <= h; x++){
                        for(int y = 0; y <= v; y++){

                            boolean right = state.getValue(TileDoorBlock.FACING).getAxis() == Direction.Axis.Z ? x != h : x != 0;
                            boolean left = state.getValue(TileDoorBlock.FACING).getAxis() == Direction.Axis.Z ? x != 0 : x != h;
                            state = state.setValue(TileDoorBlock.TYPE, VerticalTileStates.get(y != v, right, y != 0, left));

                            BlockStateModel model = client.getModelManager().getBlockStateModelSet().get(state);

                            Vec3i pos = new Vec3i(
                                    h == distance.getZ() ? pos1.getX() : minPos.getX() + (Math.abs(dir.getUnitVec3i().getZ()) * x),
                                    pos1.getY() + y,
                                    h == distance.getX() ? pos1.getZ() :minPos.getZ() + (Math.abs(dir.getUnitVec3i().getX()) * x)
                            );

                            matrices.pushPose();
                            matrices.translate(pos.getX(), pos.getY(), pos.getZ());

                            ModelBlockRenderer.renderModel(matrices.last(), vertexConsumers.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(state)), model, 1, 1, 1, LightCoordsUtil.FULL_BLOCK, OverlayTexture.NO_OVERLAY);

                            matrices.popPose();
                        }
                    }
                }
            }
        }
        matrices.popPose();
    }
}

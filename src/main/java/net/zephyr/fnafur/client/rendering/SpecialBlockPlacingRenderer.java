package net.zephyr.fnafur.client.rendering;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrames;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.blocks.stickers_blocks.BlockWithSticker;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockModel;
import net.zephyr.fnafur.blocks.utility_blocks.cosmo_gift.CosmoGift;
import net.zephyr.fnafur.entity.animatronic.block.AnimatronicBlock;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class SpecialBlockPlacingRenderer {
        public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {

            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;

            if(!player.getAbilities().allowModifyWorld) return;

            if (player.getMainHandStack() != null && player.getMainHandStack().getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof BlockWithSticker block) {
                    FloorPropBlock.drawingOutline = true;
                    HitResult blockHit = client.crosshairTarget;
                    if (blockHit.getType() == HitResult.Type.BLOCK) {
                        BlockPos pos = ((BlockHitResult) blockHit).getBlockPos().offset(((BlockHitResult) blockHit).getSide());

                        BlockState state = block.getDefaultState();
                        VoxelShape shape = state.getOutlineShape(client.world, pos, ShapeContext.absent());

                        BakedModel model = client.getBakedModelManager().getBlockModels().getModel(state);

                        if(!client.world.canPlace(state, pos, ShapeContext.of(client.player)) || !client.world.getBlockState(pos).isReplaceable()) return;

                        if(ItemNbtUtil.getNbt(client.player.getMainHandStack()).isEmpty()) return;
                        matrices.push();
                        matrices.translate(-cameraX, -cameraY, -cameraZ);
                        matrices.translate(pos.getX(), pos.getY(), pos.getZ());

                        if(model instanceof StickerBlockModel m){
                            BlockEntity entity = block.createBlockEntity(BlockPos.ORIGIN, state);

                            ((IEntityDataSaver)entity).getPersistentData().copyFrom(ItemNbtUtil.getNbt(client.player.getMainHandStack()));

                            m.forceEnt = entity;
                            client.getBlockRenderManager().getModelRenderer().render(client.world, m, state, pos, matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), false, Random.create(), 0, OverlayTexture.DEFAULT_UV);
                            m.forceEnt = null;


                            VertexRendering.drawOutline(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), shape, 0, 0, 0, 0x88FFFFFF);
                        }

                        matrices.pop();
                    }
                }
            }
        }
}

package net.zephyr.fnafur.blocks.fog;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.client.gui.screens.CameraTabletScreen;
import net.zephyr.fnafur.init.block_init.BlockInit;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FogBlockRenderer implements BlockEntityRenderer<FogBlockEntity> {
    MinecraftClient client;
    BlockRenderManager manager;

    public FogBlockRenderer(BlockEntityRendererFactory.Context context){
        client = MinecraftClient.getInstance();
        manager = context.getRenderManager();
    }
    @Override
    public void render(FogBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockPos pos = entity.getPos();
        BlockState state = client.world.getBlockState(pos);

        if(state.getBlock() instanceof FogBlock block) {
            if(entity.visible) {
                renderModel(pos, state, matrices, vertexConsumers, client.world, false, overlay);

                /*for (Direction direction : Direction.values()) {
                    matrices.push();
                    matrices.translate(0.5f, 0.5f, 0.5f);
                    matrices.multiply(direction.getRotationQuaternion());
                    matrices.translate(0, 0.5, 0);
                    BlockPos right;
                    BlockPos left;
                    BlockPos top;
                    BlockPos bottom;
                    if(direction.getAxis() != Direction.Axis.Y) {
                        right = pos.offset(direction).offset(direction.rotateYClockwise());
                        left = pos.offset(direction).offset(direction.rotateYCounterclockwise());
                        top = pos.offset(direction).up();
                        bottom = pos.offset(direction).down();
                    }
                    else {
                        right = direction == Direction.UP ? pos.offset(direction).west() : pos.offset(direction).east();
                        left = direction == Direction.UP ? pos.offset(direction).east() : pos.offset(direction).west();
                        top = direction == Direction.UP ? pos.offset(direction).north() : pos.offset(direction).south();
                        bottom = direction == Direction.UP ? pos.offset(direction).south() : pos.offset(direction).north();
                    }

                    boolean rightBl = entity.getWorld().getBlockState(right).isFullCube(entity.getWorld(), right);
                    boolean leftBl = entity.getWorld().getBlockState(left).isFullCube(entity.getWorld(), left);
                    boolean topBl = entity.getWorld().getBlockState(top).isFullCube(entity.getWorld(), top);
                    boolean bottomBl = entity.getWorld().getBlockState(bottom).isFullCube(entity.getWorld(), bottom);

                    for(int i = 0; i < 4; i++){

                        if(!bottomBl && i == 0) continue;
                        if(!leftBl && i == 1) continue;
                        if(!topBl && i == 2) continue;
                        if(!rightBl && i == 3) continue;

                        matrices.push();
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * 90));
                        matrices.translate(0, 0, 0.495f);

                        renderSquare(matrices.peek().getPositionMatrix(), 0, 0, 0, 1, 1);
                        matrices.pop();
                    }
                    matrices.pop();
                }*/

            }
        }
    }
    private void renderModel(BlockPos pos, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, boolean cull, int overlay) {
        RenderLayer renderLayer = RenderLayers.getBlockLayer(state);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
        this.manager
                .getModelRenderer()
                .render(world, this.manager.getModel(state), state, pos, matrices, vertexConsumer, cull, Random.create(), state.getRenderingSeed(pos), overlay);
    }
    private void renderSquare(Matrix4f positionMatrix, float x, float y, float z, float width, float height){

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getRenderTypeTranslucentProgram);

        var buffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(positionMatrix, x - (width / 2f), y, z)
                .color(0xFF000000)
        ;
        buffer.vertex(positionMatrix, x - (width / 2f), y + (height), z)
                .color(0)
        ;
        buffer.vertex(positionMatrix, x + (width / 2f), y + (height), z)
                .color(0)
        ;
        buffer.vertex(positionMatrix, x + (width / 2f), y, z)
                .color(0xFF000000)
        ;
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}

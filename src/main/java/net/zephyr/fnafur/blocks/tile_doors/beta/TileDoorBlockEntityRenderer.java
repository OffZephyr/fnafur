package net.zephyr.fnafur.blocks.tile_doors.beta;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.zephyr.fnafur.blocks.tile_doors.TileDoorBlockEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TileDoorBlockEntityRenderer implements BlockEntityRenderer<TileDoorBlockEntity> {

    public TileDoorBlockEntityRenderer(BlockEntityRendererFactory.Context context){

    }
    @Override
    public void render(TileDoorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        BlockState state = entity.getWorld().getBlockState(entity.getPos());
        matrices.push();
        if(entity.getWorld().getBlockState(entity.getPos()).contains(TileDoorBlock.POWERED)) {

            BlockPos lowPos = BlockPos.fromLong(((IEntityDataSaver) entity).getPersistentData().getLong("lowest"));
            BlockPos highPos = BlockPos.fromLong(((IEntityDataSaver) entity).getPersistentData().getLong("highest"));

            if(entity.getWorld().getBlockEntity(lowPos) instanceof TileDoorBlockEntity ent) {

                double goal = (highPos.getY() - lowPos.getY());

                ent.delta = entity.getWorld().getBlockState(lowPos).get(TileDoorBlock.POWERED) ?
                        ent.delta + ((0.15f / ((goal + 1)*2))  * tickDelta ):
                        ent.delta - ((0.15f / ((goal + 1)*2))  * tickDelta );

                ent.delta = Math.clamp(ent.delta, 0, 1);

                double lerp = MathHelper.lerp(ent.delta, 0, 0.75f + goal);

                matrices.translate(0, lerp, 0);
            }
        }

        if (state.getBlock() instanceof TileDoorBlock block) {

            Direction facing = state.get(TileDoorBlock.FACING);

            Identifier texture = block.blockTexture();
            for (Direction direction : Direction.values()) {
                Vector3f normal = new Vector3f();
                Vector3f normal2 = matrices.peek().transformNormal(direction.getUnitVector(), normal);

                if (skipSide(state.get(TileDoorBlock.TILE), facing, direction)) continue;
                MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager().enable();
                matrices.push();
                matrices.translate(0.5f, 0.5f, 0.5f);
                matrices.multiply(direction.getRotationQuaternion());
                /*matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-facing.asRotation()));*/
                //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction.asRotation()));

                if (direction == facing || direction == facing.getOpposite()) {
                    renderSquare(
                            matrices.peek().getPositionMatrix(),
                            texture,
                            0,
                            0.1875f,
                            0,
                            1,
                            1,
                            getTexture(state.get(TileDoorBlock.TILE)).x,
                            getTexture(state.get(TileDoorBlock.TILE)).y,
                            light,
                            overlay,
                            normal2,
                            direction == facing,
                            false
                    );
                } else if (direction == facing.rotateYClockwise() || direction == facing.rotateYCounterclockwise()) {

                    renderSquare(
                            matrices.peek().getPositionMatrix(),
                            texture,
                            0,
                            0.5f,
                            0,
                            0.375f,
                            1,
                            getSideTexture(state.get(TileDoorBlock.TILE)).x,
                            getSideTexture(state.get(TileDoorBlock.TILE)).y,
                            light,
                            overlay,
                            normal2,
                            direction == facing.rotateYCounterclockwise(),
                            false
                    );
                } else if (direction == Direction.UP || direction == Direction.DOWN) {

                    matrices.push();
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(facing.getPositiveHorizontalDegrees()));
                    renderSquare(
                            matrices.peek().getPositionMatrix(),
                            texture,
                            0,
                            0.5f,
                            0,
                            0.375f,
                            1,
                            getTopTexture(state.get(TileDoorBlock.TILE)).x,
                            getTopTexture(state.get(TileDoorBlock.TILE)).y,
                            light,
                            overlay,
                            normal2,
                            direction == Direction.DOWN,
                            true
                    );
                    matrices.pop();
                }

                matrices.pop();
            }
        }
        MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager().disable();
        matrices.pop();
    }

    Vec2f getTexture(DoorTileProperty property){
        return switch (property){
            case DoorTileProperty.SINGLE -> new Vec2f(0.75f, 0.75f);

            case DoorTileProperty.SINGLE_BOTTOM -> new Vec2f(0.75f, 0.5f);
            case DoorTileProperty.SINGLE_CENTER -> new Vec2f(0.75f, 0.25f);
            case DoorTileProperty.SINGLE_TOP -> new Vec2f(0.75f, 0.0f);

            case DoorTileProperty.CORNER_TOP_LEFT -> new Vec2f(0f, 0f);
            case DoorTileProperty.TOP -> new Vec2f(0.25f, 0f);
            case DoorTileProperty.CORNER_TOP_RIGHT -> new Vec2f(0.5f, 0f);

            case DoorTileProperty.SIDE_LEFT -> new Vec2f(0f, 0.25f);
            case DoorTileProperty.CENTER -> new Vec2f(0.25f, 0.25f);
            case DoorTileProperty.SIDE_RIGHT -> new Vec2f(0.5f, 0.25f);

            case DoorTileProperty.CORNER_BOTTOM_LEFT -> new Vec2f(0f, 0.5f);
            case DoorTileProperty.BOTTOM -> new Vec2f(0.25f, 0.5f);
            case DoorTileProperty.CORNER_BOTTOM_RIGHT -> new Vec2f(0.5f, 0.5f);
        };
    }
    Vec2f getSideTexture(DoorTileProperty property){
        return switch (property){
            default -> new Vec2f(0, 0.75f);
            case DoorTileProperty.SINGLE -> new Vec2f(0.28125f, 0.75f);

            case DoorTileProperty.SINGLE_BOTTOM, CORNER_BOTTOM_LEFT, CORNER_BOTTOM_RIGHT -> new Vec2f(0.1875f, 0.75f);
            case DoorTileProperty.SINGLE_CENTER, SIDE_LEFT, SIDE_RIGHT -> new Vec2f(0.09375f, 0.75f);
        };
    }
    Vec2f getTopTexture(DoorTileProperty property){
        return switch (property){
            default -> new Vec2f(0, 0.75f);
            case DoorTileProperty.SINGLE, SINGLE_TOP, SINGLE_BOTTOM -> new Vec2f(0.65625f, 0.75f);
            case DoorTileProperty.CORNER_TOP_RIGHT, CORNER_BOTTOM_RIGHT-> new Vec2f(0.5625f, 0.75f);
            case DoorTileProperty.CORNER_TOP_LEFT, CORNER_BOTTOM_LEFT-> new Vec2f(0.375f, 0.75f);
            case DoorTileProperty.TOP, BOTTOM-> new Vec2f(0.46875f, 0.75f);
        };
    }

    boolean skipSide(DoorTileProperty property, Direction facing, Direction direction){
        return switch (property) {
            default -> false;
            case DoorTileProperty.SINGLE_BOTTOM -> direction == Direction.UP;

            case DoorTileProperty.SINGLE_CENTER -> direction == Direction.UP || direction == Direction.DOWN;
            case DoorTileProperty.SINGLE_TOP -> direction == Direction.DOWN;

            case DoorTileProperty.CORNER_BOTTOM_LEFT -> direction == Direction.UP || direction == facing.rotateYCounterclockwise();
            case DoorTileProperty.BOTTOM -> direction == Direction.UP || direction == facing.rotateYClockwise() || direction == facing.rotateYCounterclockwise();
            case DoorTileProperty.CORNER_BOTTOM_RIGHT -> direction == Direction.UP || direction == facing.rotateYClockwise();

            case DoorTileProperty.SIDE_LEFT -> direction == Direction.UP || direction == Direction.DOWN || direction == facing.rotateYCounterclockwise();
            case DoorTileProperty.CENTER -> direction == Direction.UP || direction == Direction.DOWN || direction == facing.rotateYClockwise() || direction == facing.rotateYCounterclockwise();
            case DoorTileProperty.SIDE_RIGHT -> direction == Direction.UP || direction == Direction.DOWN || direction == facing.rotateYClockwise();

            case DoorTileProperty.CORNER_TOP_LEFT -> direction == Direction.DOWN || direction == facing.rotateYCounterclockwise();
            case DoorTileProperty.TOP -> direction == Direction.DOWN || direction == facing.rotateYClockwise() || direction == facing.rotateYCounterclockwise();
            case DoorTileProperty.CORNER_TOP_RIGHT -> direction == Direction.DOWN || direction == facing.rotateYClockwise();
        };
    }

    private void renderSquare(Matrix4f positionMatrix, Identifier texture, float x, float y, float z, float width, float height, float u, float v, int light, int overlay, Vector3f normal, boolean flipU, boolean flipV){

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_CUTOUT);

        var buffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);

        RenderSystem.setShaderTexture(0, texture);

        float u1 = flipU ? u : u + (width / 4f);
        float u2 = flipU ? u + (width / 4f) : u;

        float v1 = flipV ? v : v + (height / 4f);
        float v2 = flipV ? v + (height / 4f) : v;

        buffer.vertex(positionMatrix, x - (width / 2f), y, z - (height / 2f))
                .color(0xFFFFFFFF)
                .texture(u1, v2)
                .overlay(overlay)
                .light(light)
                .normal(normal.x, normal.y, normal.z)
        ;
        buffer.vertex(positionMatrix, x - (width / 2f), y, z + (height / 2f))
                .color(0xFFFFFFFF)
                .texture(u1, v1)
                .overlay(overlay)
                .light(light)
                .normal(normal.x, normal.y, normal.z)
        ;
        buffer.vertex(positionMatrix, x + (width / 2f), y, z + (height / 2f))
                .color(0xFFFFFFFF)
                .texture(u2, v1)
                .overlay(overlay)
                .light(light)
                .normal(normal.x, normal.y, normal.z)
        ;
        buffer.vertex(positionMatrix, x + (width / 2f), y, z - (height / 2f))
                .color(0xFFFFFFFF)
                .texture(u2, v2)
                .overlay(overlay)
                .light(light)
                .normal(normal.x, normal.y, normal.z)
        ;
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }
}

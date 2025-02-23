package net.zephyr.fnafur.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.ItemNbtUtil;

public class CameraMapRenderer {
    public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ){

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        NbtCompound MainData = ItemNbtUtil.getNbt(player.getMainHandStack());
        NbtCompound OffData = ItemNbtUtil.getNbt(player.getOffHandStack());

        matrices.push();
        matrices.translate(-cameraX + 0.5f, -cameraY, -cameraZ + 0.5f);
        if((player.getMainHandStack().isOf(ItemInit.TAPEMEASURE) || (player.getMainHandStack().isOf(ItemInit.PAINTBRUSH)) && player.getOffHandStack().isOf(ItemInit.TABLET))){
            if(MainData.getBoolean("hasCorner")){
                BlockPos pos1 = BlockPos.fromLong(MainData.getLong("setupCorner1"));
                BlockPos pos2 = BlockPos.fromLong(MainData.getLong("setupCorner2"));

                long[] line = new long[] {pos1.asLong(), pos2.asLong()};
                NbtLongArray lineNbt = new NbtLongArray(line);
                NbtList list = new NbtList();
                list.add(lineNbt);
                renderLine(matrices, pos1, pos2, 0.75f, 1.0f, 0.5f, 1f, true, list);
            }

            if(!OffData.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).isEmpty()){
                NbtList mapNbt = OffData.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).copy();

                HitResult blockHit = MinecraftClient.getInstance().player.raycast(20.0, 0.0f, false);
                BlockPos pos = ((BlockHitResult)blockHit).getBlockPos();

                for(int i = 0; i < mapNbt.size(); i++){
                    if(mapNbt.getLongArray(i).length > 0){
                        BlockPos pos1 = BlockPos.fromLong(mapNbt.getLongArray(i)[0]);
                        BlockPos pos2 = BlockPos.fromLong(mapNbt.getLongArray(i)[1]);
                        Box line = new Box(pos1.toCenterPos(), pos2.toCenterPos()).expand(0.5f);

                        if(MinecraftClient.getInstance().player.getMainHandStack().isOf(ItemInit.TAPEMEASURE) && (!MainData.getBoolean("hasCorner") && line.contains(pos.toCenterPos()))){
                            long[] line2 = new long[] {pos1.asLong(), pos2.asLong()};
                            NbtLongArray lineNbt = new NbtLongArray(line2);
                            NbtList list = new NbtList();
                            list.add(lineNbt);

                            if(MinecraftClient.getInstance().player.isSneaking()){
                                renderLine(matrices, pos1, pos2, 1.0f, 0.5f, 0.5f, 1f, true, list);
                            }
                            else {
                                renderLine(matrices, pos1, pos2, 0.5f, 0.75f, 1.0f, 1f, true, list);
                            }
                        }
                        else {
                            long color = mapNbt.getLongArray(i).length >= 3 ? mapNbt.getLongArray(i)[2] : 0xFFFFFFFFL;
                            float red = ColorHelper.getRed((int)color) / 255f;
                            float green = ColorHelper.getGreen((int)color) / 255f;
                            float blue = ColorHelper.getBlue((int)color) / 255f;
                            float alpha = ColorHelper.getAlpha((int)color) / 255f;

                            long[] line2 = new long[] {pos1.asLong(), pos2.asLong()};
                            NbtLongArray lineNbt = new NbtLongArray(line2);
                            NbtList list = new NbtList();
                            list.add(lineNbt);

                            NbtList mapList = line.contains(pos.toCenterPos()) ? list : mapNbt;
                            renderLine(matrices, pos1, pos2, red, green, blue, alpha, false, mapList);
                        }
                    }
                }
            }


        }
        matrices.pop();
    }


    void renderLine(MatrixStack matrices, BlockPos pos1, BlockPos pos2, float red, float green, float blue, float alpha, boolean selected, NbtList mapNbt){
        int color = ColorHelper.fromFloats(alpha, red, green, blue);
        boolean onXAxis = pos1.getX() != pos2.getX();
        boolean onXPos = pos1.getX() < pos2.getX();
        boolean onZPos = pos1.getZ() < pos2.getZ();

        Direction lineDirection = onXAxis ? onXPos ? Direction.EAST : Direction.WEST : onZPos ? Direction.SOUTH : Direction.NORTH;

        float xStart = pos1.getX();
        float zStart = pos1.getZ();
        float xEnd = pos2.getX();
        float zEnd = pos2.getZ();
        float distance = MathHelper.abs(MathHelper.sqrt(((xEnd - xStart) * (xEnd - xStart)) + ((zEnd - zStart) * (zEnd - zStart))));

        float y = pos1.getY() + 0.5f;

        matrices.push();
        //matrices.translate(0, -0.5f, 0);
        BlockPos pos = pos1;
        for(int i = 0; i <= distance; i++) {
            matrices.push();
            float x = pos.getX();
            float z = pos.getZ();
            matrices.translate(x, y, z);
            for (Direction direction : Direction.values()) {
                ClientWorld world = MinecraftClient.getInstance().world;
                BlockPos offsetPos = pos.offset(direction);
                if(world.getBlockState(offsetPos).isSideSolidFullSquare(world, offsetPos, direction.getOpposite())) continue;
                matrices.push();
                matrices.multiply(direction.getRotationQuaternion());
                matrices.translate(0, 0.5015, 0);

                int shapeIndex = getLinePartShape(pos, pos1, pos2, direction, lineDirection, mapNbt);
                Identifier texture = getLineTexture(shapeIndex);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(textureRot(shapeIndex, direction, lineDirection)));

                float tWidth = 0.5f;
                float tHeight = 0.5f;
                float vWidth = 0.5f;
                float vHeight = 0.5f;

                RenderSystem.enableBlend();
                RenderSystem.enableDepthTest();
                RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);

                var buffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

                RenderSystem.setShaderTexture(0, texture);
                RenderSystem.setShaderColor(red, green, blue, alpha);

                buffer.vertex(matrices.peek().getPositionMatrix(), -vWidth, 0.0f, -vHeight).texture(0.5f - tWidth, 0.5f + tHeight).color(color);
                buffer.vertex(matrices.peek().getPositionMatrix(), -vWidth, 0.0f, vHeight).texture(0.5f - tWidth, 0.5f - tHeight).color(color);
                buffer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, vHeight).texture(0.5f + tWidth, 0.5f - tHeight).color(color);
                buffer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, -vHeight).texture(0.5f + tWidth, 0.5f + tHeight).color(color);
                BufferRenderer.drawWithGlobalProgram(buffer.end());
                RenderSystem.setShaderColor(1, 1, 1, 1);
                matrices.pop();
            }
            pos = pos.offset(lineDirection);
            matrices.pop();
        }
        matrices.pop();
    }

    Identifier getLineTexture(int index){
        return switch(index){
            default -> Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/camera_map/line.png");
            case 1, 2 -> Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/camera_map/end.png");
            case 3 -> Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/camera_map/square.png");
            case 4, 5, 6, 7 -> Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/camera_map/t_split.png");
            case 8 -> Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/camera_map/x_split.png");
            case 9, 10, 11, 12 -> Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/camera_map/corner.png");
        };
    }
    int getLinePartShape(BlockPos pos, BlockPos pos1, BlockPos pos2, Direction direction, Direction lineDirection, NbtList mapNbt){
        boolean oneLong = pos1.getX() == pos2.getX() && pos1.getZ() == pos2.getZ();

        boolean lineZ = lineDirection == Direction.NORTH || lineDirection == Direction.SOUTH;
        boolean z = direction == Direction.NORTH || direction == Direction.SOUTH;

        final int line = 0;
        final int start = 1;
        final int end = 2;
        final int square = 3;
        final int t_split_left = 4;
        final int t_split_right = 5;
        final int t_split_both_front = 6;
        final int t_split_both_back = 7;
        final int x_split = 8;
        final int corner_left_front = 9;
        final int corner_right_front = 10;
        final int corner_left_back = 11;
        final int corner_right_back = 12;

        boolean left = isPartOfLine(pos.offset(lineDirection.rotateYCounterclockwise()), mapNbt);
        boolean right = isPartOfLine(pos.offset(lineDirection.rotateYClockwise()), mapNbt);
        boolean next = isPartOfLine(pos.offset(lineDirection), mapNbt);
        boolean prev = isPartOfLine(pos.offset(lineDirection.getOpposite()), mapNbt);

        if(direction == Direction.UP || direction == Direction.DOWN) {

            if(left || right){
                if(left && right) {
                    if(next && prev) return x_split;
                    else if(next) return t_split_both_front;
                    else if(prev) return t_split_both_back;
                }
                else {
                    if(left) {
                        if(next && prev) return t_split_left;
                        else if(next) return corner_left_front;
                        else if(prev) return corner_left_back;
                    }
                    else {
                        if(next && prev) return t_split_right;
                        else if(next) return corner_right_front;
                        else if(prev) return corner_right_back;
                    }
                }
            }
            else {
                if(next && prev) return line;
                else if(next) return start;
                else if(prev) return end;
            }
        } else {
            boolean edge = !lineZ && !z || lineZ && z;
            if(edge) {
                if (left && right) return line;
                if(next){
                    if (right) return end;
                    else if (left) return start;
                }
                if(prev){
                    if (right) return start;
                    else if (left) return end;
                }
            }
            else {
                if (next && prev) return line;
                else if (next) return start;
                else if (prev) return end;
            }
        }
        return square;
    }

    float textureRot(int index, Direction direction, Direction lineDirection) {
        boolean opposite = direction == lineDirection.rotateYCounterclockwise();

        final int start = 1;
        final int end = 2;
        final int t_split_left = 4;
        final int t_split_right = 5;
        final int t_split_both_front = 6;
        final int t_split_both_back = 7;
        final int corner_left_front = 9;
        final int corner_right_front = 10;
        final int corner_left_back = 11;
        final int corner_right_back = 12;

        boolean z = lineDirection == Direction.NORTH || lineDirection == Direction.SOUTH;
        boolean z2 = direction == Direction.NORTH || direction == Direction.SOUTH;

        if(direction == Direction.UP) {
            float rot = switch (index) {
                default -> 0;
                case start -> z ? 180 : 0;
                case end -> z ? 0 : 180;
                case t_split_right -> z ? 90 : -90;
                case t_split_left -> z ? -90 : 90;
                case t_split_both_front -> z ? 180 : 0;
                case t_split_both_back -> z ? 0 : 180;
                case corner_left_front -> z ? -90 : 90;
                case corner_left_back -> z ? 0 : 180;
                case corner_right_front -> z ? 180 : 0;
                case corner_right_back -> z ? 90 : -90;
            };
            return lineDirection.getPositiveHorizontalDegrees() + rot;
        } else if(direction == Direction.DOWN) {
            float rot = switch (index) {
                default -> 0;
                case start -> 0;
                case end -> 180;
                case t_split_right -> z ? 90 : -90;
                case t_split_left -> z ? -90 : 90;
                case t_split_both_front -> z ? 180 : 0;
                case t_split_both_back -> z ? 0 : 180;
                case corner_left_front -> 0;
                case corner_left_back -> -90;
                case corner_right_front -> 90;
                case corner_right_back -> 180;
            };
            return lineDirection.getPositiveHorizontalDegrees() + rot;
        }
        else {
            float rot;
            boolean edge = !z2 && !z || z2 && z;
            if(edge) {
                rot = switch (index) {
                    default -> 90;
                    case start -> 90;
                    case end -> -90;
                };
            }
            else {
                rot = switch (index) {
                    default -> 90;
                    case start -> opposite ? 90 : -90;
                    case end -> opposite ? -90 : 90;
                };
            }
            return rot;
        }
    }

    boolean isPartOfLine(BlockPos pos, NbtList mapNbt){
        for(int i = 0; i < mapNbt.size(); i++) {
            if (mapNbt.getLongArray(i).length > 0) {
                BlockPos pos1 = BlockPos.fromLong(mapNbt.getLongArray(i)[0]);
                BlockPos pos2 = BlockPos.fromLong(mapNbt.getLongArray(i)[1]);
                Box box = new Box(pos1.toCenterPos(), pos2.toCenterPos()).expand(0.25);
                if(!box.contains(pos.toCenterPos())) continue;
                return true;
            }
        }
        return false;
    }
}

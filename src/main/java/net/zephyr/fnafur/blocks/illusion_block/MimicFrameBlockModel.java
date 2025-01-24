package net.zephyr.fnafur.blocks.illusion_block;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlock;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockModel;
import net.zephyr.fnafur.init.item_init.StickerInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.Arrays;
import java.util.List;

public class MimicFrameBlockModel extends StickerBlockModel {

    @Override
    public Sprite getParticleSprite() {
        return new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/mimic_frame_1")).getSprite();
    }

    @Override
    public void emitBaseCube(BlockState state, BlockPos pos, QuadEmitter emitter, NbtCompound nbt) {
        World world = MinecraftClient.getInstance().world;
        if(state.getBlock() instanceof MimicFrames block) {
            int matrixSize = block.getMatrixSize();
            byte[] cubeArray = nbt.getByteArray("cubeMatrix");
            if (cubeArray.length == 0) {
                cubeArray = new byte[matrixSize * matrixSize * matrixSize];
                cubeArray[0] = 1;
            }
            boolean[][][] cubeMatrix = MimicFrames.arrayToMatrix(cubeArray, matrixSize);

            int index = 0;
            for (int x = 0; x < matrixSize; x++) {
                index++;
                for (int y = 0; y < matrixSize; y++) {
                    index++;
                    for (int z = 0; z < matrixSize; z++) {
                        index++;

                        if (!cubeMatrix[x][y][z]) continue;

                        for (Direction direction : Direction.values()) {

                            Block sideBlock = block.getCurrentBlock(nbt, world, direction, new Vec3i(x, y, z));

                            if (direction == Direction.EAST && x + 1 < matrixSize && cubeMatrix[x + 1][y][z]) continue;
                            if (direction == Direction.WEST && x - 1 >= 0 && cubeMatrix[x - 1][y][z]) continue;
                            if (direction == Direction.SOUTH && z + 1 < matrixSize && cubeMatrix[x][y][z + 1]) continue;
                            if (direction == Direction.NORTH && z - 1 >= 0 && cubeMatrix[x][y][z - 1]) continue;
                            if (direction == Direction.UP && y + 1 < matrixSize && cubeMatrix[x][y + 1][z]) continue;
                            if (direction == Direction.DOWN && y - 1 >= 0 && cubeMatrix[x][y - 1][z]) continue;

                            emitSide(emitter, nbt, direction, sideBlock, pos, sideBlock == null, matrixSize, x, y, z, index);
                        }
                    }
                }
            }
        }
    }
    public void emitSide(QuadEmitter emitter, NbtCompound nbt, Direction direction, Block sideBlock, BlockPos pos, boolean reColor, int matrixSize, int x, int y, int z, int colorIndex) {

        int frameSize = matrixSize * matrixSize;
        Sprite frame = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/mimic_frame_" + frameSize)).getSprite();

        if (frame == null) return;

        float part = 1.0f / matrixSize;

        float x0 = 0 + (x * part);
        float x1 = part + (x * part);
        float z0 = 0 + (y * part);
        float z1 = part + (y * part);
        float depth = 0;

        if (part == 1) {
            emitter.square(direction, 0.0f, 0.0f, 1, 1, 0);
        } else {
            switch (direction) {
                case NORTH, SOUTH: {
                    x0 = direction == Direction.SOUTH ? 0 + (x * part) : (1 - part) - (x * part);
                    x1 = direction == Direction.SOUTH ? part + (x * part) : 1 - (x * part);
                    depth = direction == Direction.SOUTH ? (1 - part) - (z * part) : 0 + (z * part);
                    break;
                }
                case WEST, EAST: {
                    x0 = direction == Direction.WEST ? 0 + (z * part) : (1 - part) - (z * part);
                    x1 = direction == Direction.WEST ? part + (z * part) : 1 - (z * part);
                    depth = direction == Direction.EAST ? (1 - part) - (x * part) : 0 + (x * part);
                    break;
                }
                case UP, DOWN: {
                    z0 = direction == Direction.DOWN ? 0 + (z * part) : (1 - part) - (z * part);
                    z1 = direction == Direction.DOWN ? part + (z * part) : 1 - (z * part);
                    depth = direction == Direction.UP ? (1 - part) - (y * part) : 0 + (y * part);
                    break;
                }
            }
        }
        emitter.square(direction, x0, z0, x1, z1, depth);

        Sprite sprite = sideBlock == null ? frame : MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(sideBlock.getDefaultState()).getQuads(sideBlock.getDefaultState(), direction, Random.create()).get(0).getSprite();
        emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
        int color = reColor ? getColor(direction, colorIndex) : 0xFFFFFFFF;
        emitter.color(color, color, color, color);
        emitter.emit();

        emitStickers(direction, emitter, nbt, pos, x0, x1, z0, z1, depth, matrixSize);
    }
    int getColor(Direction direction, int offset){
        Direction side = offset % 2 == 0 ? direction.getOpposite() : direction;
        int base = switch(side){
            case NORTH -> 0xFFBBBBFF;
            case EAST -> 0xFFBBFFBB;
            case SOUTH -> 0xFFFFBB99;
            case WEST -> 0xFFFF9999;
            case UP -> 0xFFFFFFFF;
            case DOWN -> 0xFF808080;
        };
        return base;
    }

    @Override
    public void emitStickers(BlockPos pos, QuadEmitter emitter, NbtCompound nbt) {
    }

    public void emitStickers(Direction direction, QuadEmitter emitter, NbtCompound nbt, BlockPos pos, float x0, float x1, float z0, float z1, float depth, int matrixSize) {
        if(!nbt.isEmpty()) {

            MinecraftClient client = MinecraftClient.getInstance();
            NbtList list = nbt.getList(direction.name(), NbtElement.STRING_TYPE);
            NbtList offset_list = nbt.getList(direction.name() + "_offset", NbtElement.FLOAT_TYPE);

            for (int i = 0; i < list.size(); i++) {
                String name = list.getString(i);
                StickerInit.Sticker sticker = StickerInit.getSticker(name);
                if (name.isEmpty() || sticker == null) continue;
                int dirPos = direction.getAxis() == Direction.Axis.Z ? Math.abs(pos.getX()) :
                        direction.getAxis() == Direction.Axis.X ? Math.abs(pos.getZ()) :
                                0;
                int num = dirPos % sticker.getTextures().length;
                Identifier identifier = sticker.getTextures()[num];
                Sprite sprite = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, identifier).getSprite();

                float Offset = offset_list.getFloat(i);
                float xOffset = sticker.getDirection() == StickerInit.Movable.HORIZONTAL ? Offset : 0;
                float yOffset = sticker.getDirection() == StickerInit.Movable.VERTICAL ? Offset : 0;

                boolean snapBelow = matrixSize == 1 && client.world.getBlockState(pos.down()).isSideSolidFullSquare(client.world, pos.down(), direction);
                boolean snapAbove = matrixSize == 1 && client.world.getBlockState(pos.up()).isSideSolidFullSquare(client.world, pos.up(), direction);

                float textureSize = sticker.getPixelDensity() - sticker.getSize();
                float scaledSpace = (float) sticker.getSize() / sticker.getPixelDensity();
                float textureSizeY = sticker.getDirection() == StickerInit.Movable.VERTICAL ? (float) textureSize / sticker.getPixelDensity() : 1.0f;

                float bottom = snapBelow ? yOffset : yOffset > 0 ? yOffset : 0.0f;
                float top = snapAbove ? 1.0f + yOffset : 1.0f + yOffset < 1 ? 1.0f + yOffset : 1.0f;
                float v = snapBelow ? 16 : yOffset > 0 ? 16 : 16 + yOffset * 16;
                float v2 = snapAbove ? 0 : 1.0f + yOffset < 1 ? 0 : yOffset * 16;

                bottom = matrixSize == 1 ? bottom : z0;
                top = matrixSize == 1 ? top : z1;

                float part = z1 - z0;
                float z = z0 / part;

                if(matrixSize != 1){
                    v2 = 16 - z1 *16;
                    v = 16 - z0 *16;
                }

                float left = Math.clamp(0.0f + xOffset, x0, 1);
                float right = Math.clamp(1.0f + xOffset, 0, x1);
                float u = x0 * 16;
                float u2 = x1 * 16;
                float offset = -0.0002f;

                float stickerDepth = depth + (offset + offset * i);

                emitter.square(direction,
                                left,
                                bottom,
                                right,
                                top,
                                stickerDepth)
                        .uv(0, u, v2)
                        .uv(1, u, v)
                        .uv(2, u2, v)
                        .uv(3, u2, v2)
                        .spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE)
                        .color(-1, -1, -1, -1)
                        .emit();
            }
        }
    }
}

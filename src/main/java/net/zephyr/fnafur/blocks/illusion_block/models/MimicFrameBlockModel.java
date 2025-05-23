package net.zephyr.fnafur.blocks.illusion_block.models;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrames;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockModel;
import net.zephyr.fnafur.init.DecalInit;

import java.util.function.Supplier;

public class MimicFrameBlockModel extends StickerBlockModel {
    public final BlockState defaultState;

    public MimicFrameBlockModel(UnbakedModel model, BlockState state){
        super(model);
        this.defaultState = state;
    }

    @Override
    public void emitItemQuads(QuadEmitter emitter, Supplier<Random> randomSupplier) {

        int matrixSize = ((MimicFrames)defaultState.getBlock()).getMatrixSize();
        byte[] cubeArray = new byte[matrixSize * matrixSize * matrixSize];
        for(int i = 0; i < matrixSize * matrixSize * matrixSize; i++){
            cubeArray[i] = 1;
        }
        NbtCompound nbt = new NbtCompound();
        nbt.putByteArray("cubeMatrix", cubeArray);

        emitQuads(defaultState, BlockPos.ORIGIN, nbt, emitter);
    }

    @Override
    public void emitBaseCube(BlockState baseState, BlockState state, BlockPos pos, QuadEmitter emitter, NbtCompound nbt) {
        if(!(baseState.getBlock() instanceof MimicFrames)) {
            super.emitBaseCube(baseState, state, pos, emitter, nbt);
            return;
        }

        this.particlesprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/mimic_frame_1")).getSprite();

        World world = MinecraftClient.getInstance().world;
        if (state.getBlock() instanceof MimicFrames block) {
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

                            if((z == 0 && direction == Direction.NORTH) ||
                                            (z == matrixSize - 1 && direction == Direction.SOUTH) ||
                                            (x == 0 && direction == Direction.WEST) ||
                                            (x == matrixSize - 1 && direction == Direction.EAST) ||
                                            (y == matrixSize - 1 && direction == Direction.UP) ||
                                            (y == 0 && direction == Direction.DOWN)
                            ) {
                                if (world != null && world.getBlockState(pos.offset(direction)).getBlock() instanceof MimicFrames frame && MimicFrames.isSideFull(direction.getOpposite(), MinecraftClient.getInstance().world, pos.offset(direction), pos, frame.getMatrixSize(), matrixSize))
                                    continue;
                            }
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
        Sprite frame = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/mimic_frame_" + frameSize)).getSprite();

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

    int getColor(Direction direction, int offset) {
        Direction side = offset % 2 == 0 ? direction.getOpposite() : direction;
        int base = switch (side) {
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
    public void emitStickers(BlockState baseState, BlockPos pos, QuadEmitter emitter, NbtCompound nbt) {

        if(!(baseState.getBlock() instanceof MimicFrames)) {
            super.emitStickers(baseState, pos, emitter, nbt);
        }

    }

    public void emitStickers(Direction direction, QuadEmitter emitter, NbtCompound nbt, BlockPos pos, float x0, float x1, float z0, float z1, float depth, int matrixSize) {
        if (!nbt.isEmpty()) {

            MinecraftClient client = MinecraftClient.getInstance();
            NbtList list = nbt.getList(direction.name(), NbtElement.STRING_TYPE);
            NbtList offset_list = nbt.getList(direction.name() + "_offset", NbtElement.FLOAT_TYPE);

            for (int i = 0; i < list.size(); i++) {
                String name = list.getString(i);
                DecalInit.Decal decal = DecalInit.getDecal(name);
                if (name.isEmpty() || decal == null) continue;
                int dirPos = direction.getAxis() == Direction.Axis.Z ? Math.abs(pos.getX()) :
                        direction.getAxis() == Direction.Axis.X ? Math.abs(pos.getZ()) :
                                0;
                int num = dirPos % decal.getTextures().length;
                Identifier identifier = decal.getTextures()[num];
                Sprite sprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, identifier).getSprite();

                float Offset = offset_list.getFloat(i);
                float xOffset = decal.getDirection() == DecalInit.Movable.HORIZONTAL ? Offset : 0;
                float yOffset = decal.getDirection() == DecalInit.Movable.VERTICAL ? Offset : 0;

                boolean snapBelow = client.world.getBlockState(pos.down()).isSideSolidFullSquare(client.world, pos.down(), direction);
                boolean snapAbove = client.world.getBlockState(pos.up()).isSideSolidFullSquare(client.world, pos.up(), direction);

                float textureSize = decal.getPixelDensity() - decal.getSize();
                float scaledSpace = (float) decal.getSize() / decal.getPixelDensity();
                float textureSizeY = decal.getDirection() == DecalInit.Movable.VERTICAL ? (float) textureSize / decal.getPixelDensity() : 1.0f;

                float bottom = snapBelow ? yOffset : yOffset > 0 ? yOffset : 0.0f;
                float top = snapAbove ? 1.0f + yOffset : 1.0f + yOffset < 1 ? 1.0f + yOffset : 1.0f;
                float v = snapBelow ? 16 : yOffset > 0 ? 16 : 16 + yOffset * 16;
                float v2 = snapAbove ? 0 : 1.0f + yOffset < 1 ? 0 : yOffset * 16;

                float part = z1 - z0;
                int z = (int) (matrixSize - 1 - (z0 / part));
                int offsetZ = (int) (Math.abs(yOffset) / part);
                if (matrixSize == 4) {
                    int l = 0;
                }
                if(z < offsetZ) continue;

                if (matrixSize != 1) {

                    top = z == offsetZ || z <= 0 && offsetZ <= 0 ? top : z1;
                    bottom = z == matrixSize - 1 ? bottom : z0;

                    v2 = z == offsetZ || z <= 0 && offsetZ <= 0 ? v2 : (16 - z1 * 16) + (yOffset * 16);
                    v = z == matrixSize - 1 ? v : (16 - z0 * 16) + (yOffset * 16);
                }

                float left = Math.clamp(0.0f + xOffset, x0, 1);
                float right = Math.clamp(1.0f + xOffset, 0, x1);
                float u = x0 * 16;
                float u2 = x1 * 16;

                float stickerDepth = depth + (STICKER_OFFSET + STICKER_OFFSET * i);

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

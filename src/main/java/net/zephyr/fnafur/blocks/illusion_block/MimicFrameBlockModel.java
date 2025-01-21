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
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlock;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockModel;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.Arrays;
import java.util.List;

public class MimicFrameBlockModel extends StickerBlockModel {

    @Override
    public Sprite getParticleSprite() {
        return new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/mimic_frame_block")).getSprite();
    }

    @Override
    public void emitBaseCube(BlockState state, BlockPos pos, QuadEmitter emitter, NbtCompound nbt) {
        World world = MinecraftClient.getInstance().world;
        MimicFrames block = ((MimicFrames) state.getBlock());
        int matrixSize = block.getMatrixSize();
        int frameSize = matrixSize * matrixSize;
        Sprite frame = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/mimic_frame_" + frameSize)).getSprite();
        Sprite center = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/mimic_frame_block_center")).getSprite();

        byte[] cubeArray = nbt.getByteArray("cubeMatrix");
        if(cubeArray.length == 0) {
            cubeArray = new byte[matrixSize * matrixSize * matrixSize];
            cubeArray[0] = 1;
        }
        boolean[][][] cubeMatrix = MimicFrames.arrayToMatrix(cubeArray, matrixSize);

        for (int x = 0; x < matrixSize; x++) {
            for (int y = 0; y < matrixSize; y++) {
                for (int z = 0; z < matrixSize; z++) {

                    if(!cubeMatrix[x][y][z]) continue;

                    for (Direction direction : Direction.values()) {

                        Block sideBlock = block.getBlockFromNbt(nbt.getCompound(direction.getName()), world);

                        if (sideBlock == null) {

                            if (frame == null) return;

                            float part = 1.0f / matrixSize;
                            if(part == 1){
                                emitter.square(direction, 0.0f, 0.0f, 1, 1, 0);
                            }
                            else {
                                switch (direction) {
                                    case NORTH, SOUTH: {
                                        float x0 = direction == Direction.SOUTH ? 0 + (x * part) : part - (x * part);
                                        float x1 = direction == Direction.SOUTH ? part + (x * part) : 1 - (x * part);
                                        float depth = direction == Direction.SOUTH ? part - (z * part) : 0 + (z * part);
                                        emitter.square(direction, x0, 0 + (y * part), x1, part + (y * part), depth);
                                        break;
                                    }
                                    case WEST, EAST: {
                                        float x0 = direction == Direction.WEST ? 0 + (z * part) : part - (z * part);
                                        float x1 = direction == Direction.WEST ? part + (z * part) : 1 - (z * part);
                                        float depth = direction == Direction.EAST ? part - (x * part) : 0 + (x * part);
                                        emitter.square(direction, x0, 0 + (y * part), x1, part + (y * part), depth);
                                        break;
                                    }
                                    case UP, DOWN: {
                                        float z0 = direction == Direction.DOWN ? 0 + (z * part) : part - (z * part);
                                        float z1 = direction == Direction.DOWN ? part + (z * part) : 1 - (z * part);
                                        float depth = direction == Direction.UP ? part - (y * part) : 0 + (y * part);
                                        emitter.square(direction, 0 + (x * part), z0, part + (x * part), z1, depth);
                                        break;
                                    }
                                }
                            }
                            emitter.spriteBake(frame, MutableQuadView.BAKE_LOCK_UV);
                            emitter.color(-1, -1, -1, -1);
                            emitter.emit();
                        } else {
                            if (sideBlock instanceof StickerBlock stickerBlock) {
                                Sprite sprite = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, stickerBlock.sprites().get(direction)).getSprite();
                                emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                                emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
                                emitter.color(-1, -1, -1, -1);
                                emitter.emit();
                            } else {
                                List<BakedQuad> quadList = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(sideBlock.getDefaultState()).getQuads(sideBlock.getDefaultState(), direction, Random.create());

                                for (BakedQuad quad : quadList) {
                                    Sprite sprite = quad.getSprite();

                                    emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                                    emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
                                    emitter.color(-1, -1, -1, -1);
                                    emitter.emit();
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

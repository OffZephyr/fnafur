package net.zephyr.fnafur.blocks.basic_blocks.illusion_block;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedQuad;
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

import java.util.List;

public class MimicFrameBlockSlabModel extends MimicFrameBlockModel {

    @Override
    public void emitBaseCube(BlockState state, BlockPos pos, QuadEmitter emitter, NbtCompound nbt) {
        World world = MinecraftClient.getInstance().world;
        Sprite frame = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/mimic_frame_block")).getSprite();
        Sprite center = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/mimic_frame_block_center")).getSprite();

        Sprite frame_slab = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/mimic_frame_slab")).getSprite();

        for (Direction direction: Direction.values()) {
            Block bottomSideBlock = ((MimicFrames) state.getBlock()).getBlockFromNbt(nbt.getCompound("bottom").getCompound(direction.getName()), world);
            Block topSideBlock = ((MimicFrames) state.getBlock()).getBlockFromNbt(nbt.getCompound("top").getCompound(direction.getName()), world);

            switch (state.get(MimicFramesSlab.TYPE)) {
                case BOTTOM: {
                    if (bottomSideBlock == null) {
                        if (direction.getAxis() != Direction.Axis.Y) {
                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 0.5f, 0.0f)
                                    .uv(0, 0, 0)
                                    .uv(1, 0, 8)
                                    .uv(2, 16, 8)
                                    .uv(3, 16, 0)
                                    .spriteBake(frame_slab, MutableQuadView.BAKE_ROTATE_NONE)
                                    .color(-1, -1, -1, -1);
                            emitter.emit();

                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 0.5f, 0.05f)
                                    .uv(0, 0, 8)
                                    .uv(1, 0, 16)
                                    .uv(2, 16, 16)
                                    .uv(3, 16, 8)
                                    .spriteBake(frame_slab, MutableQuadView.BAKE_ROTATE_NONE)
                                    .color(-1, -1, -1, -1);
                            emitter.emit();
                        } else {
                            float depth = direction == Direction.UP ? 0.5f : 0.0f;
                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, depth);
                            emitter.spriteBake(frame, MutableQuadView.BAKE_LOCK_UV);
                            emitter.color(-1, -1, -1, -1);
                            emitter.emit();

                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, depth + 0.05f);
                            emitter.spriteBake(center, MutableQuadView.BAKE_LOCK_UV);
                            emitter.color(-1, -1, -1, -1);
                            emitter.emit();
                        }

                    }
                    break;
                }
                case TOP: {
                    if (topSideBlock == null) {
                        if (direction.getAxis() != Direction.Axis.Y) {
                            emitter.square(direction, 0.0f, 0.5f, 1.0f, 1.0f, 0.0f)
                                    .uv(0, 0, 0)
                                    .uv(1, 0, 8)
                                    .uv(2, 16, 8)
                                    .uv(3, 16, 0)
                                    .spriteBake(frame_slab, MutableQuadView.BAKE_ROTATE_NONE)
                                    .color(-1, -1, -1, -1);
                            emitter.emit();

                            emitter.square(direction, 0.0f, 0.5f, 1.0f, 1.0f, 0.05f)
                                    .uv(0, 0, 8)
                                    .uv(1, 0, 16)
                                    .uv(2, 16, 16)
                                    .uv(3, 16, 8)
                                    .spriteBake(frame_slab, MutableQuadView.BAKE_ROTATE_NONE)
                                    .color(-1, -1, -1, -1);
                            emitter.emit();
                        } else {
                            float depth = direction == Direction.UP ? 0.0f : 0.5f;
                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, depth);
                            emitter.spriteBake(frame, MutableQuadView.BAKE_LOCK_UV);
                            emitter.color(-1, -1, -1, -1);
                            emitter.emit();

                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, depth + 0.05f);
                            emitter.spriteBake(center, MutableQuadView.BAKE_LOCK_UV);
                            emitter.color(-1, -1, -1, -1);
                            emitter.emit();
                        }
                    }
                    break;
                }
                case DOUBLE: {
                    if (direction.getAxis() != Direction.Axis.Y) {

                        if (bottomSideBlock == null) {
                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 0.5f, 0.0f)
                                    .uv(0, 0, 0)
                                    .uv(1, 0, 8)
                                    .uv(2, 16, 8)
                                    .uv(3, 16, 0)
                                    .spriteBake(frame_slab, MutableQuadView.BAKE_ROTATE_NONE)
                                    .color(-1, -1, -1, -1);
                            emitter.emit();

                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 0.5f, 0.05f)
                                    .uv(0, 0, 8)
                                    .uv(1, 0, 16)
                                    .uv(2, 16, 16)
                                    .uv(3, 16, 8)
                                    .spriteBake(frame_slab, MutableQuadView.BAKE_ROTATE_NONE)
                                    .color(-1, -1, -1, -1);
                            emitter.emit();
                        }

                        if (topSideBlock == null) {
                            emitter.square(direction, 0.0f, 0.5f, 1.0f, 1.0f, 0.0f)
                                    .uv(0, 0, 0)
                                    .uv(1, 0, 8)
                                    .uv(2, 16, 8)
                                    .uv(3, 16, 0)
                                    .spriteBake(frame_slab, MutableQuadView.BAKE_ROTATE_NONE)
                                    .color(-1, -1, -1, -1);
                            emitter.emit();

                            emitter.square(direction, 0.0f, 0.5f, 1.0f, 1.0f, 0.05f)
                                    .uv(0, 0, 8)
                                    .uv(1, 0, 16)
                                    .uv(2, 16, 16)
                                    .uv(3, 16, 8)
                                    .spriteBake(frame_slab, MutableQuadView.BAKE_ROTATE_NONE)
                                    .color(-1, -1, -1, -1);
                            emitter.emit();

                        }
                    } else {
                        if ((direction == Direction.UP && topSideBlock == null) || (direction == Direction.DOWN && bottomSideBlock == null)) {
                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                            emitter.spriteBake(frame, MutableQuadView.BAKE_LOCK_UV);
                            emitter.color(-1, -1, -1, -1);
                            emitter.emit();

                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.05f);
                            emitter.spriteBake(center, MutableQuadView.BAKE_LOCK_UV);
                            emitter.color(-1, -1, -1, -1);
                            emitter.emit();
                        }
                    }
                    break;
                }
            }
            if (bottomSideBlock != null) {
                if (bottomSideBlock instanceof StickerBlock stickerBlock) {
                    Sprite sprite = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, stickerBlock.sprites().get(direction)).getSprite();


                    if(direction.getAxis() == Direction.Axis.Y){
                        float depth = direction == Direction.UP ? 0.5f : 0.0f;
                        emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, depth)
                                .uv(0, 0, 0)
                                .uv(1, 0, 16)
                                .uv(2, 16, 16)
                                .uv(3, 16, 0)
                                .spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE)
                                .color(-1, -1, -1, -1);
                        emitter.emit();
                    }
                    else {
                        emitter.square(direction, 0.0f, 0.0f, 1.0f, 0.5f, 0.0f)
                                .uv(0, 0, 0)
                                .uv(1, 0, 8)
                                .uv(2, 16, 8)
                                .uv(3, 16, 0)
                                .spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE)
                                .color(-1, -1, -1, -1);
                        emitter.emit();
                    }
                } else {
                    List<BakedQuad> quadList = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(bottomSideBlock.getDefaultState()).getQuads(bottomSideBlock.getDefaultState(), direction, Random.create());

                    for (BakedQuad quad : quadList) {
                        Sprite sprite = quad.getSprite();

                        if(direction.getAxis() == Direction.Axis.Y){
                            float depth = direction == Direction.UP ? 0.5f : 0.0f;
                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, depth)
                                    .uv(0, 0, 0)
                                    .uv(1, 0, 16)
                                    .uv(2, 16, 16)
                                    .uv(3, 16, 0)
                                    .spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE)
                                    .color(-1, -1, -1, -1);
                            emitter.emit();
                        }
                        else {
                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 0.5f, 0.0f)
                                    .uv(0, 0, 0)
                                    .uv(1, 0, 8)
                                    .uv(2, 16, 8)
                                    .uv(3, 16, 0)
                                    .spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE)
                                    .color(-1, -1, -1, -1);
                            emitter.emit();
                        }
                    }
                }
            }
            if (topSideBlock != null) {
                if (topSideBlock instanceof StickerBlock stickerBlock) {
                    Sprite sprite = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, stickerBlock.sprites().get(direction)).getSprite();

                    if(direction.getAxis() == Direction.Axis.Y){
                        float depth = direction == Direction.UP ? 0.0f : 0.5f;
                        emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, depth)
                                .uv(0, 0, 0)
                                .uv(1, 0, 16)
                                .uv(2, 16, 16)
                                .uv(3, 16, 0)
                                .spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE)
                                .color(-1, -1, -1, -1);
                        emitter.emit();
                    }
                    else {
                        emitter.square(direction, 0.0f, 0.5f, 1.0f, 1.0f, 0.0f)
                                .uv(0, 0, 8)
                                .uv(1, 0, 16)
                                .uv(2, 16, 16)
                                .uv(3, 16, 8)
                                .spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE)
                                .color(-1, -1, -1, -1);
                        emitter.emit();
                    }
                } else {
                    List<BakedQuad> quadList = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(topSideBlock.getDefaultState()).getQuads(topSideBlock.getDefaultState(), direction, Random.create());

                    for (BakedQuad quad : quadList) {
                        Sprite sprite = quad.getSprite();

                        if(direction.getAxis() == Direction.Axis.Y){
                            float depth = direction == Direction.UP ? 0.0f : 0.5f;
                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, depth)
                                    .uv(0, 0, 0)
                                    .uv(1, 0, 16)
                                    .uv(2, 16, 16)
                                    .uv(3, 16, 0)
                                    .spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE)
                                    .color(-1, -1, -1, -1);
                            emitter.emit();
                        }
                        else {
                            emitter.square(direction, 0.0f, 0.5f, 1.0f, 1.0f, 0.0f)
                                    .uv(0, 0, 8)
                                    .uv(1, 0, 16)
                                    .uv(2, 16, 16)
                                    .uv(3, 16, 8)
                                    .spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE)
                                    .color(-1, -1, -1, -1);
                            emitter.emit();
                        }
                    }
                }
            }
        }
    }
}

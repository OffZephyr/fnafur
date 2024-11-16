package net.zephyr.fnafur.blocks.basic_blocks.illusion_block;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.stickers.base.Sticker;
import net.zephyr.fnafur.blocks.stickers.base.StickerBlock;
import net.zephyr.fnafur.blocks.stickers.base.StickerBlockEntity;
import net.zephyr.fnafur.blocks.stickers.base.StickerBlockModel;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class MimicFrameBlockModel extends StickerBlockModel {

    @Override
    public Sprite getParticleSprite() {
        return new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/mimic_frame_block")).getSprite();
    }

    @Override
    public void emitBaseCube(BlockState state, BlockPos pos, QuadEmitter emitter, NbtCompound nbt) {
        World world = MinecraftClient.getInstance().world;
        Sprite frame = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/mimic_frame_block")).getSprite();
        Sprite center = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/mimic_frame_block_center")).getSprite();

        for (Direction direction: Direction.values()) {
            Block sideBlock = ((MimicFrames)state.getBlock()).getBlockFromNbt(nbt.getCompound(direction.getName()), world);

            if(sideBlock == null) {

                if (frame == null || center == null) return;

                emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                emitter.spriteBake(frame, MutableQuadView.BAKE_LOCK_UV);
                emitter.color(-1, -1, -1, -1);
                emitter.emit();

                emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.05f);
                emitter.spriteBake(center, MutableQuadView.BAKE_LOCK_UV);
                emitter.color(-1, -1, -1, -1);
                emitter.emit();
            }
            else {
                if(sideBlock instanceof StickerBlock stickerBlock){
                    Sprite sprite = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, stickerBlock.sprites().get(direction)).getSprite();
                    emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                    emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
                    emitter.color(-1, -1, -1, -1);
                    emitter.emit();
                }
                else {
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

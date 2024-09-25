package net.zephyr.fnafur.blocks.stickers.base;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
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
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class StickerBlockModel  implements UnbakedModel, BakedModel, FabricBakedModel {
    Sprite particlesprite;
    private Mesh mesh;
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return List.of();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return true;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return particlesprite;
    }

    @Override
    public ModelTransformation getTransformation() {
        return ModelHelper.MODEL_TRANSFORM_BLOCK;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of();
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

    }

    @Nullable
    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer) {

        return this;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }
    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {

        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();
        emitQuads(state, pos, ((IEntityDataSaver)blockView.getBlockEntity(pos)).getPersistentData(), emitter, builder, context);

    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

        BlockState state = ((BlockItem)stack.getItem()).getBlock().getDefaultState();
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT).copyNbt().getCompound("fnafur.persistent");;
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();
        emitQuads(state, BlockPos.ORIGIN, nbt, emitter, builder, context);
    }

    public void emitQuads(BlockState state, BlockPos pos, NbtCompound nbt, QuadEmitter emitter, MeshBuilder builder, RenderContext context){

        particlesprite = ((StickerBlock)state.getBlock()).particleSprite().getSprite();

        for(Direction direction : Direction.values()) {
            Sprite sprite = ((StickerBlock)state.getBlock()).sprites().get(direction).getSprite();

            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
            emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        for(Direction direction : Direction.values()) {
            NbtList list = nbt.getList(direction.name(), NbtElement.STRING_TYPE);
            NbtList offset_list = nbt.getList(direction.name() + "_offset", NbtElement.FLOAT_TYPE);
            for(int i = 0; i < list.size(); i++) {
                String name = list.getString(i);
                Sticker sticker = Sticker.getSticker(name);
                if(sticker != null) {
                    int dirPos = direction == Direction.NORTH || direction == Direction.SOUTH ? Math.abs(pos.getX()) :
                            direction == Direction.WEST || direction == Direction.EAST ? Math.abs(pos.getZ()) :
                                    0;
                    int num = dirPos % sticker.getTextures().length;
                    Identifier identifier = sticker.getTextures()[num];
                    Sprite sprite = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, identifier).getSprite();

                    float Offset = offset_list.getFloat(i);
                    float xOffset = sticker.getDirection() == Sticker.Movable.HORIZONTAL ? Offset : 0;
                    float yOffset = sticker.getDirection() == Sticker.Movable.VERTICAL ? Offset : 0;

                    int textureSize = sticker.getPixelDensity() - sticker.getSize();
                    float scaledSpace = (float) sticker.getSize() / sticker.getPixelDensity();
                    float textureSizeY = sticker.getDirection() == Sticker.Movable.VERTICAL ? (float) textureSize / sticker.getPixelDensity() : 1.0f;


                    emitter.square(direction, 0.0f + xOffset, 0.0f + yOffset, 1.0f + xOffset, textureSizeY + yOffset, -0.0011f + -0.0011f * i)
                            .uv(0, 0, scaledSpace * 16)
                            .uv(1, 0, 16)
                            .uv(2, 16, 16)
                            .uv(3, 16, scaledSpace * 16)
                            .spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE)
                            .color(-1, -1, -1, -1)
                            .emit();
                }
            }
        }

        mesh = builder.build();
        mesh.outputTo(context.getEmitter());
    }
}

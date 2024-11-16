package net.zephyr.fnafur.blocks.basic_blocks.layered_block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.block_init.ModelLoading;
import net.zephyr.fnafur.util.jsonReaders.layered_block.LayeredBlockLayer;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IGetClientManagers;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
@Environment(EnvType.CLIENT)
public class LayeredBlockModel implements UnbakedModel, BakedModel, FabricBakedModel {
    private Mesh mesh;
    private Sprite particleSprite;
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
        return particleSprite;
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
        Identifier texture = ModelLoading.BASE_LAYERED_TEXTURE;
        Sprite sprite = textureGetter.apply(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture));
        particleSprite = sprite;
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
        Sprite baseSprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ModelLoading.BASE_LAYERED_TEXTURE).getSprite();

        int rotationAmount = getDirectionId(state.get(LayeredBlock.FACING));

        if (state.isOf(BlockInit.LAYERED_BLOCK_BASE)) {
            if (blockView.getBlockEntity(pos) instanceof LayeredBlockEntity entity) {
                NbtCompound data = ((IEntityDataSaver)entity).getPersistentData();
                LayeredBlockLayer[][] Layers = getLayerInfo(data);

                for (int i = 0; i < 3; i++) {
                    for (Direction direction : Direction.values()) {
                        Direction textureRotation = direction;
                        if(direction != Direction.UP && direction != Direction.DOWN) {
                            for (int j = 0; j < rotationAmount; j++) {
                                textureRotation = textureRotation.rotateYClockwise();
                            }
                        }
                        int directionId = getDirectionId(textureRotation);

                        if(Layers[i][directionId] == null || Layers[i][directionId].getName().isEmpty()) {
                            if(i != 0) continue;

                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                            emitter.spriteBake(baseSprite, MutableQuadView.BAKE_LOCK_UV);
                            emitter.color(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF);
                            emitter.emit();

                            continue;
                        }
                        LayeredBlockLayer thisLayer = Layers[i][directionId];

                        int comparator = thisLayer.cantRecolorLayer() || thisLayer.getRgbCount() <= 0 ? 1 : thisLayer.getRgbCount();
                        for(int k = 0; k < comparator; k++) {

                            Identifier texture = thisLayer.cantRecolorLayer() ? thisLayer.getTexture() : thisLayer.getRgbTexture(k);
                            Sprite sprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture).getSprite();

                            int colorValue = data.getCompound("layer" + i).getInt(directionId + "_" + k + "_color");
                            float r = ColorHelper.Argb.getRed(colorValue);
                            float g = ColorHelper.Argb.getGreen(colorValue);
                            float b = ColorHelper.Argb.getBlue(colorValue);
                            int color = thisLayer.cantRecolorLayer() ? 0xFFFFFFFF : ColorHelper.Argb.getArgb(255, (int) r, (int) g, (int) b);

                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                            emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
                            emitter.color(color, color, color, color);
                            emitter.emit();
                        }

                        if(!thisLayer.getOverlay().getPath().isEmpty()) {
                            Identifier overlay = thisLayer.getOverlay();
                            Sprite sprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, overlay).getSprite();

                            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                            emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
                            emitter.color(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF);
                            emitter.emit();
                        }
                        if(particleSprite == null) particleSprite = baseSprite;

                    }
                }
            }
        }
        mesh = builder.build();
        mesh.outputTo(context.getEmitter());
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();
        Sprite baseSprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ModelLoading.BASE_LAYERED_TEXTURE).getSprite();


        if (stack.isOf(BlockInit.LAYERED_BLOCK_BASE.asItem())) {
            NbtCompound data = stack.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT).copyNbt().getCompound("fnafur.persistent");
            LayeredBlockLayer[][] Layers = getLayerInfo(data);

            for (int i = 0; i < 3; i++) {
                for (Direction direction : Direction.values()) {
                    int directionId = getDirectionId(direction);
                    if (Layers[i][directionId] == null || Layers[i][directionId].getName().isEmpty()) {
                        if (i != 0) continue;

                        emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                        emitter.spriteBake(baseSprite, MutableQuadView.BAKE_LOCK_UV);
                        emitter.color(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF);
                        emitter.emit();

                        continue;
                    }
                    LayeredBlockLayer thisLayer = Layers[i][directionId];

                    int comparator = thisLayer.cantRecolorLayer() || thisLayer.getRgbCount() <= 0 ? 1 : thisLayer.getRgbCount();
                    for (int k = 0; k < comparator; k++) {

                        Identifier texture = thisLayer.cantRecolorLayer() ? thisLayer.getTexture() : thisLayer.getRgbTexture(k);
                        Sprite sprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture).getSprite();

                        int colorValue = data.getCompound("layer" + i).getInt(directionId + "_" + k + "_color");
                        float r = ColorHelper.Argb.getRed(colorValue);
                        float g = ColorHelper.Argb.getGreen(colorValue);
                        float b = ColorHelper.Argb.getBlue(colorValue);
                        int color = thisLayer.cantRecolorLayer() ? 0xFFFFFFFF : ColorHelper.Argb.getArgb(255, (int) r, (int) g, (int) b);

                        emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                        emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
                        emitter.color(color, color, color, color);
                        emitter.emit();
                    }

                    if(!thisLayer.getOverlay().getPath().isEmpty()) {
                        Identifier overlay = thisLayer.getOverlay();
                        Sprite sprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, overlay).getSprite();

                        emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                        emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
                        emitter.color(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF);
                        emitter.emit();
                    }
                }
            }
        }
        mesh = builder.build();
        mesh.outputTo(context.getEmitter());
    }

    public static int getDirectionId(Direction direction) {
        switch (direction) {
            default:
                return 0;
            case WEST:
                return 1;
            case SOUTH:
                return 2;
            case EAST:
                return 3;
            case UP:
                return 4;
            case DOWN:
                return 5;
        }
    }

    private LayeredBlockLayer[][] getLayerInfo(NbtCompound data) {

        LayeredBlockLayer[][] array = new LayeredBlockLayer[3][6];
        for (int j = 0; j < 3; j++) {

            NbtCompound layerData = data.getCompound("layer" + j);

            for (int i = 0; i < 6; i++) {

                array[j][i] = Objects.equals(layerData.getString("" + i), "") || layerData.isEmpty() ?
                        new LayeredBlockLayer("", false, Identifier.of("")) :
                        ((IGetClientManagers) MinecraftClient.getInstance()).getLayerManager().getLayer(layerData.getString("" + i));
            }

        }
        return array;
    }
}

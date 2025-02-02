package net.zephyr.fnafur.blocks.stickers_blocks;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
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
import net.zephyr.fnafur.blocks.illusion_block.MimicFrames;
import net.zephyr.fnafur.init.item_init.StickerInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class StickerBlockModel implements UnbakedModel, BakedModel, FabricBakedModel {
    Sprite particlesprite;
    public static float STICKER_OFFSET = -0.002f;
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
    public void resolve(Resolver resolver) {

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
        BlockEntity entity = blockView.getBlockEntity(pos);

        if (entity instanceof BlockEntity ent) {
            emitQuads(state, pos, ((IEntityDataSaver) ent).getPersistentData(), emitter, builder, context);
        }

    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

        BlockState state = ((BlockItem)stack.getItem()).getBlock().getDefaultState();
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT).copyNbt().getCompound("fnafur.persistent");
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();
        emitQuads(state, BlockPos.ORIGIN, nbt, emitter, builder, context);
    }

    public void emitQuads(BlockState state, BlockPos pos, NbtCompound nbt, QuadEmitter emitter, MeshBuilder builder, RenderContext context){

        ItemStack stack = ItemStack.fromNbtOrEmpty(MinecraftClient.getInstance().world.getRegistryManager(), nbt.getCompound("BlockState"));
        BlockState newState = state.getBlock() instanceof BlockWithSticker && !stack.isEmpty() ? stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT).applyToState(((BlockItem)stack.getItem()).getBlock().getDefaultState()) : state;

        emitBaseCube(newState, pos, emitter, nbt);
        emitStickers(pos, emitter, nbt);

        mesh = builder.build();
        mesh.outputTo(context.getEmitter());
    }
    public void emitBaseCube(BlockState state, BlockPos pos, QuadEmitter emitter, NbtCompound nbt){
        MinecraftClient client = MinecraftClient.getInstance();
        BakedModel model = client.getBakedModelManager().getBlockModels().getModel(state);
        particlesprite = model.getParticleSprite();

        for(Direction direction : Direction.values()) {

            BlockState sideState = client.world.getBlockState(pos.offset(direction));
            if (pos != BlockPos.ORIGIN && !(sideState.getBlock() instanceof MimicFrames) && sideState.isOpaque() && client.world.getBlockState(pos.offset(direction)).isSideSolidFullSquare(client.world, pos.offset(direction), direction.getOpposite())) continue;
            if (pos != BlockPos.ORIGIN && sideState.getBlock() instanceof MimicFrames frame && MimicFrames.isSideFull(direction.getOpposite(), client.world, pos.offset(direction), frame.getMatrixSize())) continue;

            List<BakedQuad> quadList = model.getQuads(state, direction, Random.create());

            for (BakedQuad quad : quadList) {
                Sprite sprite = quad.getSprite();

                emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
                emitter.color(-1, -1, -1, -1);
                emitter.emit();
            }
        }
    }
    public void emitStickers(BlockPos pos, QuadEmitter emitter, NbtCompound nbt){

        if(!nbt.isEmpty()) {
            MinecraftClient client = MinecraftClient.getInstance();
            for (Direction direction : Direction.values()) {
                BlockState sideState = client.world.getBlockState(pos.offset(direction));
                if (pos != BlockPos.ORIGIN && !(sideState.getBlock() instanceof MimicFrames) && sideState.isOpaque() && client.world.getBlockState(pos.offset(direction)).isSideSolidFullSquare(client.world, pos.offset(direction), direction.getOpposite())) continue;
                if (pos != BlockPos.ORIGIN && sideState.getBlock() instanceof MimicFrames frame && MimicFrames.isSideFull(direction.getOpposite(), client.world, pos.offset(direction), frame.getMatrixSize())) continue;

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

                    boolean snapBelow = client.world.getBlockState(pos.down()).isSideSolidFullSquare(client.world, pos.down(), direction);
                    boolean snapAbove = client.world.getBlockState(pos.up()).isSideSolidFullSquare(client.world, pos.up(), direction);

                    float textureSize = sticker.getPixelDensity() - sticker.getSize();
                    float scaledSpace = (float) sticker.getSize() / sticker.getPixelDensity();
                    float textureSizeY = sticker.getDirection() == StickerInit.Movable.VERTICAL ? (float) textureSize / sticker.getPixelDensity() : 1.0f;

                    float bottom = snapBelow ? yOffset : yOffset > 0 ? yOffset : 0.0f;
                    float top = snapAbove ? 1.0f + yOffset : 1.0f + yOffset < 1 ? 1.0f + yOffset : 1.0f;
                    float v = snapBelow ? 16 : yOffset > 0 ? 16 : 16 + yOffset * 16;
                    float v2 = snapAbove ? 0 : 1.0f + yOffset < 1 ? 0 : yOffset * 16;

                    emitter.square(direction,
                                    0.0f + xOffset,
                                    bottom,
                                    1.0f + xOffset,
                                    top,
                                    STICKER_OFFSET + STICKER_OFFSET * i)
                            .uv(0, 0, v2)
                            .uv(1, 0, v)
                            .uv(2, 16, v)
                            .uv(3, 16, v2)
                            .spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE)
                            .color(-1, -1, -1, -1)
                            .emit();
                }
            }
        }
    }
}

package net.zephyr.fnafur.blocks.stickers_blocks;

import net.fabricmc.fabric.api.client.model.loading.v1.WrapperGroupableModel;
import net.fabricmc.fabric.api.client.model.loading.v1.WrapperUnbakedModel;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.material.ShadeMode;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrames;
import net.zephyr.fnafur.init.item_init.StickerInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class StickerBlockModel extends WrapperUnbakedModel implements BakedModel, FabricBakedModel {

    public StickerBlockModel(UnbakedModel model){
        super(model);
    }

    Sprite particlesprite;

    Block block;
    public static float STICKER_OFFSET = -0.002f;

    Random random = Random.create();
    private static final RenderMaterial STANDARD_MATERIAL = Renderer.get().materialFinder().shadeMode(ShadeMode.VANILLA).find();
    private static final RenderMaterial NO_AO_MATERIAL = Renderer.get().materialFinder().shadeMode(ShadeMode.VANILLA).ambientOcclusion(TriState.FALSE).find();
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return List.of();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public void resolve(Resolver resolver) {

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
    public Sprite getParticleSprite() {
        return particlesprite;
    }

    @Override
    public BakedModel bake(ModelTextures textures, Baker baker, ModelBakeSettings settings, boolean ambientOcclusion, boolean isSideLit, ModelTransformation transformation) {

        FnafUniverseResuited.print("BAKE BAKE");
        /*Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();*/
        return this;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }


    @Override
    public void emitBlockQuads(QuadEmitter emitter, BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, Predicate<@Nullable Direction> cullTest) {

        block = state.getBlock();
        BlockEntity entity = blockView.getBlockEntity(pos);

        if (entity instanceof BlockEntity ent) {
            emitQuads(state, pos, ((IEntityDataSaver) ent).getPersistentData(), emitter);
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter emitter, Supplier<Random> randomSupplier) {

        if(block != null) {
            BlockState state = (block.getDefaultState());
            //NbtCompound nbt = item.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT).copyNbt().getCompound("fnafur.persistent");

            emitQuads(state, BlockPos.ORIGIN, new NbtCompound(), emitter);
        }
    }

    public void emitQuads(BlockState state, BlockPos pos, NbtCompound nbt, QuadEmitter emitter){

        ItemStack stack = ItemStack.fromNbtOrEmpty(MinecraftClient.getInstance().world.getRegistryManager(), nbt.getCompound("BlockState"));

        BlockState newState = state.getBlock() instanceof BlockWithSticker && !stack.isEmpty() ? stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT).applyToState(((BlockItem)stack.getItem()).getBlock().getDefaultState()) : state;

        emitBaseCube(state, newState, pos, emitter, nbt);
        emitStickers(state, pos, emitter, nbt);
    }
    public void emitBaseCube(BlockState baseState, BlockState state, BlockPos pos, QuadEmitter emitter, NbtCompound nbt){
        MinecraftClient client = MinecraftClient.getInstance();
        BakedModel model = client.getBakedModelManager().getBlockModels().getModel(state);
        particlesprite = model.getParticleSprite();

        for(Direction direction : Direction.values()) {

            BlockState sideState = client.world.getBlockState(pos.offset(direction));
            if (pos != BlockPos.ORIGIN && !(sideState.getBlock() instanceof MimicFrames) && sideState.isOpaque() && client.world.getBlockState(pos.offset(direction)).isSideSolidFullSquare(client.world, pos.offset(direction), direction.getOpposite())) continue;
            if (pos != BlockPos.ORIGIN && sideState.getBlock() instanceof MimicFrames frame && MimicFrames.isSideFull(direction.getOpposite(), client.world, pos.offset(direction), frame.getMatrixSize())) continue;

            List<BakedQuad> quadList = model.getQuads(state, direction, random);
            final RenderMaterial defaultMaterial = model.useAmbientOcclusion() ? STANDARD_MATERIAL : NO_AO_MATERIAL;

            for (BakedQuad quad : quadList) {
                emitter.fromVanilla(quad, defaultMaterial, direction);
                emitter.emit();
            }
        }
    }
    public void emitStickers(BlockState baseState, BlockPos pos, QuadEmitter emitter, NbtCompound nbt){

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

                    Sprite sprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, identifier).getSprite();

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

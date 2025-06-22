package net.zephyr.fnafur.blocks.stickers_blocks;

import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperUnbakedModel;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.material.ShadeMode;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrames;
import net.zephyr.fnafur.init.DecalInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class StickerBlockModel extends WrapperUnbakedModel implements BlockStateModel {

    public StickerBlockModel(UnbakedModel model){
        super(model);
    }

    public Sprite particlesprite;

    Block block;
    public BlockEntity forceEnt = null;
    public static float STICKER_OFFSET = -0.002f;

    Random random = Random.create();
    private static final RenderMaterial STANDARD_MATERIAL = Renderer.get().materialFinder().shadeMode(ShadeMode.VANILLA).find();
    private static final RenderMaterial NO_AO_MATERIAL = Renderer.get().materialFinder().shadeMode(ShadeMode.VANILLA).ambientOcclusion(TriState.FALSE).find();

    @Override
    public void addParts(Random random, List<BlockModelPart> parts) {

    }

    @Override
    public List<BlockModelPart> getParts(Random random) {
        return BlockStateModel.super.getParts(random);
    }

    @Override
    public Sprite particleSprite() {
        return particlesprite;
    }

    @Override
    public void emitQuads(QuadEmitter emitter, BlockRenderView blockView, BlockPos pos, BlockState state, Random random, Predicate<@Nullable Direction> cullTest) {
        BlockStateModel.super.emitQuads(emitter, blockView, pos, state, random, cullTest);
        block = state.getBlock();
        System.out.println("WORKS");

        BlockEntity entity = forceEnt == null ? blockView.getBlockEntity(pos) : forceEnt;

        if (entity instanceof BlockEntity ent) {

            NbtCompound nbt = ((IEntityDataSaver) ent).getPersistentData();

            //if(!nbt.getBoolean("synced")){
            //    ClientPlayNetworking.send(new UpdateBlockNbtC2SGetFromServerPayload(pos.asLong()));
            //}

            emitQuads(state, pos, nbt, emitter);
        }
    }

    @Override
    public Sprite particleSprite(BlockRenderView blockView, BlockPos pos, BlockState state) {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockStateModel model = client.getBakedModelManager().getBlockModels().getModel(state);
        return model.particleSprite();
    }

    public void emitQuads(BlockState state, BlockPos pos, NbtCompound nbt, QuadEmitter emitter){
        ItemStack stack = ItemStack.fromNbt(MinecraftClient.getInstance().world.getRegistryManager(), nbt.getCompound("BlockState").get()).get();

        BlockState newState = state.getBlock() instanceof BlockWithSticker && !stack.isEmpty() ? stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT).applyToState(((BlockItem)stack.getItem()).getBlock().getDefaultState()) : state;

        emitBaseCube(state, newState, pos, emitter, nbt);
        emitStickers(state, pos, emitter, nbt);
    }
    public void emitBaseCube(BlockState baseState, BlockState state, BlockPos pos, QuadEmitter emitter, NbtCompound nbt){
        MinecraftClient client = MinecraftClient.getInstance();
        BlockStateModel model = client.getBakedModelManager().getBlockModels().getModel(state);

        for(Direction direction : Direction.values()) {

            BlockState sideState = client.world.getBlockState(pos.offset(direction));
            if (pos != BlockPos.ORIGIN && !(sideState.getBlock() instanceof MimicFrames) && sideState.isOpaque() && client.world.getBlockState(pos.offset(direction)).isSideSolidFullSquare(client.world, pos.offset(direction), direction.getOpposite())) continue;
            if (pos != BlockPos.ORIGIN && sideState.getBlock() instanceof MimicFrames frame && MimicFrames.isSideFull(direction.getOpposite(), client.world, pos.offset(direction), frame.getMatrixSize())) continue;

            List<BakedQuad> quadList = model.getParts(random).get(0).getQuads(direction);
            final RenderMaterial defaultMaterial = model.getParts(random).get(0).useAmbientOcclusion() ? STANDARD_MATERIAL : NO_AO_MATERIAL;

            for (BakedQuad quad : quadList) {
                if(direction == Direction.UP) particlesprite = quad.sprite();
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

                NbtList list = nbt.getList(direction.name()).get();
                NbtList offset_list = nbt.getList(direction.name() + "_offset").get();

                for (int i = 0; i < list.size(); i++) {
                    String name = list.getString(i).get();

                    DecalInit.Decal decal = DecalInit.getDecal(name);

                    if (name.isEmpty() || decal == null) continue;
                    int dirPos = direction.getAxis() == Direction.Axis.Z ? Math.abs(pos.getX()) :
                            direction.getAxis() == Direction.Axis.X ? Math.abs(pos.getZ()) :
                                    0;
                    int num = dirPos % decal.getTextures().length;
                    Identifier identifier = decal.getTextures()[num];

                    Sprite sprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, identifier).getSprite();

                    float Offset = offset_list.getFloat(i).get();
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

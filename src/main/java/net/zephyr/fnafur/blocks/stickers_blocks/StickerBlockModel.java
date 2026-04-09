package net.zephyr.fnafur.blocks.stickers_blocks;

import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperUnbakedGroupedBlockStateModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.MimicFrames;
import net.zephyr.fnafur.init.decal_init.DecalInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class StickerBlockModel extends WrapperUnbakedGroupedBlockStateModel implements BlockStateModel {

    public StickerBlockModel(BlockStateModel.UnbakedRoot wrapped){
        super(wrapped);
    }

    public TextureAtlasSprite particlesprite;

    Block block;
    public BlockEntity forceEnt = null;
    public static float STICKER_OFFSET = -0.002f;

    RandomSource random = RandomSource.create();

    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> parts) {

    }

    @Override
    public List<BlockModelPart> collectParts(RandomSource random) {
        return BlockStateModel.super.collectParts(random);
    }

    @Override
    public TextureAtlasSprite particleIcon() {
        return Minecraft.getInstance().getBlockRenderer().materials.get(new Material(TextureAtlas.LOCATION_BLOCKS, Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/mimic_frame_1")));
    }

    @Override
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {

        BlockStateModel.super.emitQuads(emitter, blockView, pos, state, random, cullTest);
        block = state.getBlock();

        BlockEntity entity = forceEnt == null ? blockView.getBlockEntity(pos) : forceEnt;

        if (entity instanceof BlockEntity ent) {

            CompoundTag nbt = ((IEntityDataSaver) ent).getPersistentData();

            //if(!nbt.getBoolean("synced")){
            //    ClientPlayNetworking.send(new UpdateBlockNbtC2SGetFromServerPayload(pos.asLong()));
            //}

            emitQuads(state, pos, nbt, emitter);
        }
    }

    @Override
    public TextureAtlasSprite particleSprite(BlockAndTintGetter blockView, BlockPos pos, BlockState state) {
        Minecraft client = Minecraft.getInstance();
        BlockStateModel model = client.getModelManager().getBlockModelShaper().getBlockModel(state);
        if(model != null && model.particleIcon() != null){
            particlesprite = model.particleIcon();
        }
        return particlesprite;
    }

    public void emitQuads(BlockState state, BlockPos pos, CompoundTag nbt, QuadEmitter emitter){
        ItemStack stack = nbt.read("BlockState", ItemStack.CODEC).orElse(ItemStack.EMPTY);

        BlockState newState = state.getBlock() instanceof BlockWithSticker && !stack.isEmpty() ? stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY).apply(((BlockItem)stack.getItem()).getBlock().defaultBlockState()) : state;

        emitBaseCube(state, newState, pos, emitter, nbt);
        emitStickers(state, pos, emitter, nbt);
    }
    public void emitBaseCube(BlockState baseState, BlockState state, BlockPos pos, QuadEmitter emitter, CompoundTag nbt){
        Minecraft client = Minecraft.getInstance();
        BlockStateModel model = client.getModelManager().getBlockModelShaper().getBlockModel(state);

        for(Direction direction : Direction.values()) {

            BlockState sideState = client.level.getBlockState(pos.relative(direction));
            if (pos != BlockPos.ZERO && !(sideState.getBlock() instanceof MimicFrames) && sideState.isSolidRender() && client.level.getBlockState(pos.relative(direction)).isFaceSturdy(client.level, pos.relative(direction), direction.getOpposite())) continue;
            if (pos != BlockPos.ZERO && sideState.getBlock() instanceof MimicFrames frame && MimicFrames.isSideFull(direction.getOpposite(), client.level, pos.relative(direction), frame.getMatrixSize())) continue;

            List<BlockModelPart> parts = model.collectParts(RandomSource.create());
            if(!parts.isEmpty()){
                List<BakedQuad> quadList = parts.get(0).getQuads(direction);

                for (BakedQuad quad : quadList) {
                    //if(direction == Direction.UP) particlesprite = quad.sprite();
                    emitter.fromBakedQuad(quad);
                    emitter.emit();
                }
            }
        }
    }
    public void emitStickers(BlockState baseState, BlockPos pos, QuadEmitter emitter, CompoundTag nbt){

        if(!nbt.isEmpty()) {
            Minecraft client = Minecraft.getInstance();
            for (Direction direction : Direction.values()) {
                BlockState sideState = client.level.getBlockState(pos.relative(direction));
                if (pos != BlockPos.ZERO && !(sideState.getBlock() instanceof MimicFrames) && sideState.isSolidRender() && client.level.getBlockState(pos.relative(direction)).isFaceSturdy(client.level, pos.relative(direction), direction.getOpposite())) continue;
                if (pos != BlockPos.ZERO && sideState.getBlock() instanceof MimicFrames frame && MimicFrames.isSideFull(direction.getOpposite(), client.level, pos.relative(direction), frame.getMatrixSize())) continue;

                ListTag list = nbt.getList(direction.name()).orElse(new ListTag());
                ListTag offset_list = nbt.getList(direction.name() + "_offset").orElse(new ListTag());

                for (int i = 0; i < list.size(); i++) {
                    String name = list.getString(i).orElse("");

                    DecalInit.Decal decal = DecalInit.getDecal(name);

                    if (name.isEmpty() || decal == null) continue;
                    int dirPos = direction.getAxis() == Direction.Axis.Z ? Math.abs(pos.getX()) :
                            direction.getAxis() == Direction.Axis.X ? Math.abs(pos.getZ()) :
                                    0;
                    int num = dirPos % decal.getTextures().length;
                    Identifier identifier = decal.getTextures()[num];

                    TextureAtlasSprite sprite = Minecraft.getInstance().getBlockRenderer().materials.get(new Material(TextureAtlas.LOCATION_BLOCKS, identifier));

                    float Offset = offset_list.getFloat(i).orElse(0f);
                    float xOffset = decal.getDirection() == DecalInit.Movable.HORIZONTAL ? Offset : 0;
                    float yOffset = decal.getDirection() == DecalInit.Movable.VERTICAL ? Offset : 0;

                    boolean snapBelow = client.level.getBlockState(pos.below()).isFaceSturdy(client.level, pos.below(), direction);
                    boolean snapAbove = client.level.getBlockState(pos.above()).isFaceSturdy(client.level, pos.above(), direction);

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

    @Override
    public BlockStateModel bake(BlockState state, ModelBaker baker) {
        return this;
    }
}

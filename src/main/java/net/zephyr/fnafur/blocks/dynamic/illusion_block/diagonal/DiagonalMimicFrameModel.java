package net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal;

import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperUnbakedRootBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.MimicFrames;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockEntity;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockModel;
import net.zephyr.fnafur.init.decal_init.DecalInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DiagonalMimicFrameModel extends WrapperUnbakedRootBlockStateModel implements BlockStateModel {
    public static TextureAtlasSprite FRAME;
    public DiagonalMimicFrameModel(BlockStateModel.UnbakedRoot wrapped){
        super(wrapped);
    }
    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {

    }

    @Override
    public BlockStateModel bake(BlockState state, ModelBaker baker) {
        return this;
    }

    @Override
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {


        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(TextureAtlas.LOCATION_BLOCKS).getSprite(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/mimic_frame_1"));

        if(blockView.getBlockEntity(pos) instanceof StickerBlockEntity ent) {
            CompoundTag nbt = ((IEntityDataSaver) ent).getPersistentData();
            if (nbt.contains("BlockData")) {
                ItemStack blockStack = nbt.read("BlockData", ItemStack.CODEC).orElse(ItemStack.EMPTY);
                if (blockStack.getItem() instanceof BlockItem blockItem) {
                    Block block = blockItem.getBlock();

                    BlockState textureState = block.defaultBlockState();
                    BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(textureState);
                    List<BlockStateModelPart> parts = new ArrayList();
                    model.collectParts(RandomSource.create(), parts);
                    sprite = parts.get(0).getQuads(Direction.UP).get(0).materialInfo().sprite();
                }
            }

            emitStickers(state, pos, emitter, nbt);
        }

        boolean freeUp = !blockView.getBlockState(pos.above()).isCollisionShapeFullBlock(blockView, pos);
        boolean freeDown = !blockView.getBlockState(pos.below()).isCollisionShapeFullBlock(blockView, pos);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis() == Direction.Axis.Y) continue;

            Direction nextDirection = direction.getClockWise();
            Direction prevDirection = direction.getCounterClockWise();
            Direction oppositeDirection = direction.getOpposite();

            boolean bl = state.getValue(DiagonalMimicFrame.DIRECTION_MAP.get(prevDirection)) && state.getValue(DiagonalMimicFrame.DIRECTION_MAP.get(oppositeDirection)) && !state.getValue(DiagonalMimicFrame.DIRECTION_MAP.get(nextDirection));
            boolean bl2 = state.getValue(DiagonalMimicFrame.DIRECTION_MAP.get(nextDirection)) && state.getValue(DiagonalMimicFrame.DIRECTION_MAP.get(oppositeDirection)) && !state.getValue(DiagonalMimicFrame.DIRECTION_MAP.get(prevDirection));
            boolean bl3 = state.getValue(DiagonalMimicFrame.DIRECTION_MAP.get(nextDirection)) && state.getValue(DiagonalMimicFrame.DIRECTION_MAP.get(oppositeDirection)) && state.getValue(DiagonalMimicFrame.DIRECTION_MAP.get(prevDirection)) && state.getValue(DiagonalMimicFrame.DIRECTION_MAP.get(direction));

            if (!state.getValue(DiagonalMimicFrame.DIRECTION_MAP.get(direction)) && bl) {
                Vector2f[] vertices = new Vector2f[4];
                Vector2f[] uvs = new Vector2f[4];
                for (int i = 0; i < 4; i++) {

                    float x, y, z;

                    x = i < 2 ? prevDirection.getUnitVec3i().getX() * 0.5f : nextDirection.getUnitVec3i().getX() * 0.5f;
                    y = i == 1 || i == 2 ? 1 : 0;
                    z = i < 2 ? direction.getUnitVec3i().getZ() * 0.5f : oppositeDirection.getUnitVec3i().getZ() * 0.5f;

                    if (direction.getAxis() == Direction.Axis.X) {
                        x = i < 2 ? direction.getUnitVec3i().getX() * 0.5f : oppositeDirection.getUnitVec3i().getX() * 0.5f;
                        y = i == 1 || i == 2 ? 1 : 0;
                        z = i < 2 ? prevDirection.getUnitVec3i().getZ() * 0.5f : nextDirection.getUnitVec3i().getZ() * 0.5f;
                    }

                    x += 0.5f;
                    z += 0.5f;
                    emitter.pos(i, x, y, z);


                    int u = i < 2 ? 0 : 1;
                    int v = i == 1 || i == 2 ? 0 : 1;

                    vertices[i] = new Vector2f(x, z);
                    uvs[i] = new Vector2f(u, v);

                    emitter.uv(i, u * 16, v * 16);
                }
                emitter.materialBake(new Material.Baked(sprite, false), MutableQuadView.BAKE_ROTATE_NONE);
                emitter.emit();

                if(freeUp) {
                    emitter.pos(0, vertices[0].x(), 1, vertices[0].y());
                    emitter.pos(1, vertices[2].x(), 1, vertices[0].y());
                    if (direction.getAxis() == Direction.Axis.Z)
                        emitter.pos(1, vertices[0].x(), 1, vertices[2].y());
                    emitter.pos(2, vertices[2].x(), 1, vertices[2].y());
                    emitter.pos(3, vertices[2].x(), 1, vertices[2].y());


                    for (int i = 0; i < 3; i++) {
                        emitter.uv(i, emitter.x(i) * 16, emitter.z(i) * 16);
                    }
                    emitter.materialBake(new Material.Baked(sprite, false), MutableQuadView.BAKE_ROTATE_NONE);
                    emitter.emit();
                }

                if(freeDown) {
                    emitter.pos(0, vertices[0].x(), 0, vertices[0].y());
                    emitter.pos(3, vertices[0].x(), 0, vertices[2].y());
                    if (direction.getAxis() == Direction.Axis.X)
                        emitter.pos(3, vertices[2].x(), 0, vertices[0].y());
                    emitter.pos(2, vertices[2].x(), 0, vertices[2].y());
                    emitter.pos(1, vertices[2].x(), 0, vertices[2].y());

                    for (int i = 0; i < 4; i++) {
                        emitter.uv(i, emitter.x(i) * 16, 16 - (emitter.z(i) * 16));
                    }
                    emitter.materialBake(new Material.Baked(sprite, false), MutableQuadView.BAKE_ROTATE_NONE);
                    emitter.emit();
                }

                continue;
            }
            else if(!state.getValue(DiagonalMimicFrame.DIRECTION_MAP.get(direction)) && !bl2) {
                for (int i = 0; i < 4; i++) {
                    emitter.square(direction, 0, 0, 1, 1, 0);


                    int u = i < 2 ? 0 : 1;
                    int v = i == 1 || i == 2 ? 1 : 0;
                    emitter.uv(i, u * 16, v * 16);
                }
                emitter.materialBake(new Material.Baked(sprite, false), MutableQuadView.BAKE_ROTATE_NONE);
                emitter.emit();

                if (freeUp) {
                    emitter.square(Direction.UP, 0, 0, 1, 1, 0);
                    for (int i = 0; i < 4; i++) {
                        emitter.uv(i, emitter.x(i) * 16, (emitter.z(i) * 16));
                    }

                    emitter.materialBake(new Material.Baked(sprite, false), MutableQuadView.BAKE_ROTATE_NONE);
                    emitter.emit();
                }

                if (freeDown) {
                    emitter.square(Direction.DOWN, 0, 0, 1, 1, 0);
                    for (int i = 0; i < 4; i++) {
                        emitter.uv(i, emitter.x(i) * 16, 16 - (emitter.z(i) * 16));
                    }

                    emitter.materialBake(new Material.Baked(sprite, false), MutableQuadView.BAKE_ROTATE_NONE);
                    emitter.emit();
                }
            }
            if(bl3){

                if (freeUp) {
                    emitter.square(Direction.UP, 0, 0, 1, 1, 0);
                    for (int i = 0; i < 4; i++) {
                        emitter.uv(i, emitter.x(i) * 16, (emitter.z(i) * 16));
                    }

                    emitter.materialBake(new Material.Baked(sprite, false), MutableQuadView.BAKE_ROTATE_NONE);
                    emitter.emit();
                }

                if (freeDown) {
                    emitter.square(Direction.DOWN, 0, 0, 1, 1, 0);
                    for (int i = 0; i < 4; i++) {
                        emitter.uv(i, emitter.x(i) * 16, 16 - (emitter.z(i) * 16));
                    }

                    emitter.materialBake(new Material.Baked(sprite, false), MutableQuadView.BAKE_ROTATE_NONE);
                    emitter.emit();
                }
            }
        }

        BlockStateModel.super.emitQuads(emitter, blockView, pos, state, random, cullTest);
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

                    TextureAtlasSprite sprite = client.getAtlasManager().getAtlasOrThrow(TextureAtlas.LOCATION_BLOCKS).getSprite(identifier);

                    float Offset = offset_list.getFloat(i).orElse(0f);
                    float xOffset = decal.getDirection() == DecalInit.Movable.HORIZONTAL ? Offset : 0;
                    float yOffset = decal.getDirection() == DecalInit.Movable.VERTICAL ? Offset : 0;

                    Level world = Minecraft.getInstance().level;
                    boolean snapBelow = world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), direction) || (world.getBlockState(pos).getBlock() instanceof DiagonalMimicFrame && world.getBlockState(pos.below()).getBlock() instanceof DiagonalMimicFrame);
                    boolean snapAbove = world.getBlockState(pos.above()).isFaceSturdy(world, pos.above(), direction) || (world.getBlockState(pos).getBlock() instanceof DiagonalMimicFrame && world.getBlockState(pos.above()).getBlock() instanceof DiagonalMimicFrame);

                    float textureSize = decal.getPixelDensity() - decal.getSize();
                    float scaledSpace = (float) decal.getSize() / decal.getPixelDensity();
                    float textureSizeY = decal.getDirection() == DecalInit.Movable.VERTICAL ? (float) textureSize / decal.getPixelDensity() : 1.0f;


                    float v1 = snapBelow ? 16 : yOffset > 0 ? 16 : 16 + yOffset * 16;
                    float v2 = snapAbove ? 0 : 1.0f + yOffset < 1 ? 0 : yOffset * 16;

                    float bottom = snapBelow ? yOffset : yOffset > 0 ? yOffset : 0.0f;
                    float top = snapAbove ? 1.0f + yOffset : 1.0f + yOffset < 1 ? 1.0f + yOffset : 1.0f;

                    if(baseState.getBlock() instanceof DiagonalMimicFrame f){
                        if(f.isDiagonal(baseState)){
                            Direction usedD = direction;
                            if(DiagonalMimicFrame.NEXT_MAP.get(direction) == f.getDiagonalDirection(baseState)){

                                Direction nextDirection = usedD.getClockWise();
                                Direction prevDirection = usedD.getCounterClockWise();
                                Direction oppositeDirection = usedD.getOpposite();
                                for (int j = 0; j < 4; j++) {

                                    float x, y, z;

                                    x = j < 2 ? prevDirection.getUnitVec3i().getX() * 0.5f : nextDirection.getUnitVec3i().getX() * 0.5f;
                                    y = j == 1 || j == 2 ? top : bottom;
                                    z = j < 2 ? direction.getUnitVec3i().getZ() * 0.5f : oppositeDirection.getUnitVec3i().getZ() * 0.5f;

                                    if (direction.getAxis() == Direction.Axis.X) {
                                        x = j < 2 ? direction.getUnitVec3i().getX() * 0.5f : oppositeDirection.getUnitVec3i().getX() * 0.5f;
                                        z = j < 2 ? prevDirection.getUnitVec3i().getZ() * 0.5f : nextDirection.getUnitVec3i().getZ() * 0.5f;
                                    }

                                    x += 0.5f;
                                    z += 0.5f;
                                    x -= (i + 1) * (((StickerBlockModel.STICKER_OFFSET/2) * usedD.getUnitVec3i().getX()) + ((StickerBlockModel.STICKER_OFFSET/2) * nextDirection.getUnitVec3i().getX()));
                                    z -= (i + 1) * (((StickerBlockModel.STICKER_OFFSET/2) * usedD.getUnitVec3i().getZ()) + ((StickerBlockModel.STICKER_OFFSET/2) * nextDirection.getUnitVec3i().getZ()));
                                    emitter.pos(j, x, y, z);

                                    int u = j < 2 ? 1 : 0;

                                    float v = j == 1 || j == 2 ? v2 : v1;

                                    emitter.uv(j, u * 16, v);
                                }
                                emitter.materialBake(new Material.Baked(sprite, false), MutableQuadView.BAKE_ROTATE_NONE)
                                        .color(-1, -1, -1, -1)
                                        .emit();
                                continue;
                            }
                        }
                    }

                    emitter.square(direction,
                                    0.0f + xOffset,
                                    bottom,
                                    1.0f + xOffset,
                                    top,
                                    StickerBlockModel.STICKER_OFFSET + StickerBlockModel.STICKER_OFFSET * i)
                            .uv(0, 0, v2)
                            .uv(1, 0, v1)
                            .uv(2, 16, v1)
                            .uv(3, 16, v2)
                            .materialBake(new Material.Baked(sprite, false), MutableQuadView.BAKE_ROTATE_NONE)
                            .color(-1, -1, -1, -1)
                            .emit();
                }
            }
        }
    }

    @Override
    public Material.Baked particleMaterial() {
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(TextureAtlas.LOCATION_BLOCKS).getSprite(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/mimic_frame_1"));
        return  new Material.Baked(sprite, false);
    }

    @Override
    public @BakedQuad.MaterialFlags int materialFlags() {
        return 0;
    }

    @Override
    public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(TextureAtlas.LOCATION_BLOCKS).getSprite(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/mimic_frame_1"));
        return  new Material.Baked(sprite, false);
    }
}

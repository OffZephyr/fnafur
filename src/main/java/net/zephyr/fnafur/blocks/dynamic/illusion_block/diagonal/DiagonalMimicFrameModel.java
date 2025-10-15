package net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal;

import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperUnbakedGroupedBlockStateModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.MimicFrames;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockEntity;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockModel;
import net.zephyr.fnafur.init.DecalInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class DiagonalMimicFrameModel extends WrapperUnbakedGroupedBlockStateModel implements BlockStateModel {
    public static Sprite FRAME;
    public DiagonalMimicFrameModel(BlockStateModel.UnbakedGrouped wrapped){
        super(wrapped);
    }
    @Override
    public void addParts(Random random, List<BlockModelPart> parts) {

    }

    @Override
    public Sprite particleSprite() {
        return FRAME;
    }

    @Override
    public BlockStateModel bake(BlockState state, Baker baker) {
        return this;
    }

    @Override
    public void emitQuads(QuadEmitter emitter, BlockRenderView blockView, BlockPos pos, BlockState state, Random random, Predicate<@Nullable Direction> cullTest) {

        FRAME = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/mimic_frame_1")).getSprite();

        Sprite sprite = FRAME;

        if(blockView.getBlockEntity(pos) instanceof StickerBlockEntity ent) {
            NbtCompound nbt = ((IEntityDataSaver) ent).getPersistentData();
            if (nbt.contains("BlockData")) {
                ItemStack blockStack = nbt.get("BlockData", ItemStack.CODEC).orElse(ItemStack.EMPTY);
                if (blockStack.getItem() instanceof BlockItem blockItem) {
                    Block block = blockItem.getBlock();

                    BlockState textureState = block.getDefaultState();
                    BlockStateModel model = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(textureState);
                    sprite = model.getParts(Random.create()).get(0).getQuads(Direction.UP).get(0).sprite();
                }
            }

            emitStickers(state, pos, emitter, nbt);
        }

        boolean freeUp = !blockView.getBlockState(pos.up()).isFullCube(blockView, pos);
        boolean freeDown = !blockView.getBlockState(pos.down()).isFullCube(blockView, pos);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis() == Direction.Axis.Y) continue;

            Direction nextDirection = direction.rotateYClockwise();
            Direction prevDirection = direction.rotateYCounterclockwise();
            Direction oppositeDirection = direction.getOpposite();

            boolean bl = state.get(DiagonalMimicFrame.DIRECTION_MAP.get(prevDirection)) && state.get(DiagonalMimicFrame.DIRECTION_MAP.get(oppositeDirection)) && !state.get(DiagonalMimicFrame.DIRECTION_MAP.get(nextDirection));
            boolean bl2 = state.get(DiagonalMimicFrame.DIRECTION_MAP.get(nextDirection)) && state.get(DiagonalMimicFrame.DIRECTION_MAP.get(oppositeDirection)) && !state.get(DiagonalMimicFrame.DIRECTION_MAP.get(prevDirection));
            boolean bl3 = state.get(DiagonalMimicFrame.DIRECTION_MAP.get(nextDirection)) && state.get(DiagonalMimicFrame.DIRECTION_MAP.get(oppositeDirection)) && state.get(DiagonalMimicFrame.DIRECTION_MAP.get(prevDirection)) && state.get(DiagonalMimicFrame.DIRECTION_MAP.get(direction));

            if (!state.get(DiagonalMimicFrame.DIRECTION_MAP.get(direction)) && bl) {
                Vector2f[] vertices = new Vector2f[4];
                Vector2f[] uvs = new Vector2f[4];
                for (int i = 0; i < 4; i++) {

                    float x, y, z;

                    x = i < 2 ? prevDirection.getVector().getX() * 0.5f : nextDirection.getVector().getX() * 0.5f;
                    y = i == 1 || i == 2 ? 1 : 0;
                    z = i < 2 ? direction.getVector().getZ() * 0.5f : oppositeDirection.getVector().getZ() * 0.5f;

                    if (direction.getAxis() == Direction.Axis.X) {
                        x = i < 2 ? direction.getVector().getX() * 0.5f : oppositeDirection.getVector().getX() * 0.5f;
                        y = i == 1 || i == 2 ? 1 : 0;
                        z = i < 2 ? prevDirection.getVector().getZ() * 0.5f : nextDirection.getVector().getZ() * 0.5f;
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
                emitter.spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE);
                emitter.emit();

                if(freeUp) {
                    emitter.pos(0, vertices[0].getX(), 1, vertices[0].getY());
                    emitter.pos(1, vertices[2].getX(), 1, vertices[0].getY());
                    if (direction.getAxis() == Direction.Axis.Z)
                        emitter.pos(1, vertices[0].getX(), 1, vertices[2].getY());
                    emitter.pos(2, vertices[2].getX(), 1, vertices[2].getY());
                    emitter.pos(3, vertices[2].getX(), 1, vertices[2].getY());


                    for (int i = 0; i < 3; i++) {
                        emitter.uv(i, emitter.x(i) * 16, emitter.z(i) * 16);
                    }
                    emitter.spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE);
                    emitter.emit();
                }

                if(freeDown) {
                    emitter.pos(0, vertices[0].getX(), 0, vertices[0].getY());
                    emitter.pos(3, vertices[0].getX(), 0, vertices[2].getY());
                    if (direction.getAxis() == Direction.Axis.X)
                        emitter.pos(3, vertices[2].getX(), 0, vertices[0].getY());
                    emitter.pos(2, vertices[2].getX(), 0, vertices[2].getY());
                    emitter.pos(1, vertices[2].getX(), 0, vertices[2].getY());

                    for (int i = 0; i < 4; i++) {
                        emitter.uv(i, emitter.x(i) * 16, 16 - (emitter.z(i) * 16));
                    }
                    emitter.spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE);
                    emitter.emit();
                }

                continue;
            }
            else if(!state.get(DiagonalMimicFrame.DIRECTION_MAP.get(direction)) && !bl2) {
                for (int i = 0; i < 4; i++) {
                    emitter.square(direction, 0, 0, 1, 1, 0);


                    int u = i < 2 ? 0 : 1;
                    int v = i == 1 || i == 2 ? 1 : 0;
                    emitter.uv(i, u * 16, v * 16);
                }
                emitter.spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE);
                emitter.emit();

                if (freeUp) {
                    emitter.square(Direction.UP, 0, 0, 1, 1, 0);
                    for (int i = 0; i < 4; i++) {
                        emitter.uv(i, emitter.x(i) * 16, (emitter.z(i) * 16));
                    }

                    emitter.spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE);
                    emitter.emit();
                }

                if (freeDown) {
                    emitter.square(Direction.DOWN, 0, 0, 1, 1, 0);
                    for (int i = 0; i < 4; i++) {
                        emitter.uv(i, emitter.x(i) * 16, 16 - (emitter.z(i) * 16));
                    }

                    emitter.spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE);
                    emitter.emit();
                }
            }
            if(bl3){

                if (freeUp) {
                    emitter.square(Direction.UP, 0, 0, 1, 1, 0);
                    for (int i = 0; i < 4; i++) {
                        emitter.uv(i, emitter.x(i) * 16, (emitter.z(i) * 16));
                    }

                    emitter.spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE);
                    emitter.emit();
                }

                if (freeDown) {
                    emitter.square(Direction.DOWN, 0, 0, 1, 1, 0);
                    for (int i = 0; i < 4; i++) {
                        emitter.uv(i, emitter.x(i) * 16, 16 - (emitter.z(i) * 16));
                    }

                    emitter.spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE);
                    emitter.emit();
                }
            }
        }

        BlockStateModel.super.emitQuads(emitter, blockView, pos, state, random, cullTest);
    }

    public void emitStickers(BlockState baseState, BlockPos pos, QuadEmitter emitter, NbtCompound nbt){

        if(!nbt.isEmpty()) {
            MinecraftClient client = MinecraftClient.getInstance();
            for (Direction direction : Direction.values()) {
                BlockState sideState = client.world.getBlockState(pos.offset(direction));
                if (pos != BlockPos.ORIGIN && !(sideState.getBlock() instanceof MimicFrames) && sideState.isOpaque() && client.world.getBlockState(pos.offset(direction)).isSideSolidFullSquare(client.world, pos.offset(direction), direction.getOpposite())) continue;
                if (pos != BlockPos.ORIGIN && sideState.getBlock() instanceof MimicFrames frame && MimicFrames.isSideFull(direction.getOpposite(), client.world, pos.offset(direction), frame.getMatrixSize())) continue;

                NbtList list = nbt.getList(direction.name()).orElse(new NbtList());
                NbtList offset_list = nbt.getList(direction.name() + "_offset").orElse(new NbtList());

                for (int i = 0; i < list.size(); i++) {
                    String name = list.getString(i).orElse("");

                    DecalInit.Decal decal = DecalInit.getDecal(name);

                    if (name.isEmpty() || decal == null) continue;
                    int dirPos = direction.getAxis() == Direction.Axis.Z ? Math.abs(pos.getX()) :
                            direction.getAxis() == Direction.Axis.X ? Math.abs(pos.getZ()) :
                                    0;
                    int num = dirPos % decal.getTextures().length;
                    Identifier identifier = decal.getTextures()[num];

                    Sprite sprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, identifier).getSprite();

                    float Offset = offset_list.getFloat(i).orElse(0f);
                    float xOffset = decal.getDirection() == DecalInit.Movable.HORIZONTAL ? Offset : 0;
                    float yOffset = decal.getDirection() == DecalInit.Movable.VERTICAL ? Offset : 0;

                    World world = MinecraftClient.getInstance().world;
                    boolean snapBelow = world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), direction) || (world.getBlockState(pos).getBlock() instanceof DiagonalMimicFrame && world.getBlockState(pos.down()).getBlock() instanceof DiagonalMimicFrame);
                    boolean snapAbove = world.getBlockState(pos.up()).isSideSolidFullSquare(world, pos.up(), direction) || (world.getBlockState(pos).getBlock() instanceof DiagonalMimicFrame && world.getBlockState(pos.up()).getBlock() instanceof DiagonalMimicFrame);

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

                                Direction nextDirection = usedD.rotateYClockwise();
                                Direction prevDirection = usedD.rotateYCounterclockwise();
                                Direction oppositeDirection = usedD.getOpposite();
                                for (int j = 0; j < 4; j++) {

                                    float x, y, z;

                                    x = j < 2 ? prevDirection.getVector().getX() * 0.5f : nextDirection.getVector().getX() * 0.5f;
                                    y = j == 1 || j == 2 ? top : bottom;
                                    z = j < 2 ? direction.getVector().getZ() * 0.5f : oppositeDirection.getVector().getZ() * 0.5f;

                                    if (direction.getAxis() == Direction.Axis.X) {
                                        x = j < 2 ? direction.getVector().getX() * 0.5f : oppositeDirection.getVector().getX() * 0.5f;
                                        z = j < 2 ? prevDirection.getVector().getZ() * 0.5f : nextDirection.getVector().getZ() * 0.5f;
                                    }

                                    x += 0.5f;
                                    z += 0.5f;
                                    x -= (i + 1) * (((StickerBlockModel.STICKER_OFFSET/2) * usedD.getVector().getX()) + ((StickerBlockModel.STICKER_OFFSET/2) * nextDirection.getVector().getX()));
                                    z -= (i + 1) * (((StickerBlockModel.STICKER_OFFSET/2) * usedD.getVector().getZ()) + ((StickerBlockModel.STICKER_OFFSET/2) * nextDirection.getVector().getZ()));
                                    emitter.pos(j, x, y, z);

                                    int u = j < 2 ? 1 : 0;

                                    float v = j == 1 || j == 2 ? v2 : v1;

                                    emitter.uv(j, u * 16, v);
                                }
                                emitter.spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE)
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
                            .spriteBake(sprite, MutableQuadView.BAKE_ROTATE_NONE)
                            .color(-1, -1, -1, -1)
                            .emit();
                }
            }
        }
    }
    @Override
    public Sprite particleSprite(BlockRenderView blockView, BlockPos pos, BlockState state) {
        //NbtCompound nbt = ((IEntityDataSaver) blockView.getBlockEntity(pos)).getPersistentData();
        //if (nbt.contains("BlockData")) {
        //    ItemStack blockStack = nbt.get("BlockData", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        //    if (blockStack.getItem() instanceof BlockItem blockItem) {
        //        Block block = blockItem.getBlock();
//
        //        BlockState textureState = block.getDefaultState();
        //        BlockStateModel model = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(textureState);
        //        return model.getParts(Random.create()).get(0).getQuads(Direction.UP).get(0).sprite();
        //    }
        //}
        return FRAME;
    }
}

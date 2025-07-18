package net.zephyr.fnafur.blocks.props.base;

import io.netty.util.internal.SuppressJava6Requirement;
import jdk.jshell.Diag;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperUnbakedGroupedBlockStateModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.mesh.ShadeMode;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrame;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrameModel;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Predicate;

public class PropBlockModel extends WrapperUnbakedGroupedBlockStateModel implements BlockStateModel {

    public BlockStateModel ORIGINAL_MODEL;
    public PropBlockModel(BlockStateModel.UnbakedGrouped wrapped){
        super(wrapped);
    }

    @Override
    public void addParts(Random random, List<BlockModelPart> parts) {

    }

    @Override
    public BlockStateModel bake(BlockState state, Baker baker) {
        ORIGINAL_MODEL = this.wrapped.bake(state, baker);
        return this;
    }

    @Override
    public Sprite particleSprite() {
        return ORIGINAL_MODEL.particleSprite();

    }

    @Override
    public void emitQuads(QuadEmitter emitter, BlockRenderView blockView, BlockPos pos, BlockState state, Random random, Predicate<@Nullable Direction> cullTest) {

        if(ORIGINAL_MODEL != null) {

            if (MinecraftClient.getInstance().world.getBlockEntity(pos) instanceof PropBlockEntity ent) {
                NbtCompound nbt = ((IEntityDataSaver) ent).getPersistentData();

                if (state.getBlock() instanceof PropBlock<?> block) {
                    float rotation = nbt.getFloat("Rotation").orElse(0.0f);
                    rotation += 0.01f;

                    double offsetX = nbt.getDouble("xOffset").orElse(0.0);
                    double offsetY = nbt.getDouble("yOffset").orElse(0.0);
                    double offsetZ = nbt.getDouble("zOffset").orElse(0.0);

                    Vec3d offsetPos = new Vec3d(offsetX, offsetY, offsetZ);

                    for (BlockModelPart part : ORIGINAL_MODEL.getParts(Random.create())) {

                        final TriState ao = part.useAmbientOcclusion() ? TriState.DEFAULT : TriState.FALSE;
                        for (int i = 0; i <= ModelHelper.NULL_FACE_ID; i++) {
                            final Direction cullFace = ModelHelper.faceFromIndex(i);

                            if (cullTest.test(cullFace)) {
                                // Skip entire quad list if possible.
                                continue;
                            }

                            final List<BakedQuad> quads = part.getQuads(cullFace);
                            final int quadCount = quads.size();

                            for (int j = 0; j < quadCount; j++) {
                                final BakedQuad q = quads.get(j);
                                emitter.cullFace(cullFace);

                                emitter.fromBakedQuad(q);


                                float newRot = rotation;
                                if(block instanceof WallPropBlock<?>) newRot = -newRot;
                                for (int k = 0; k < 4; k++) {

                                    float startX = emitter.x(k) - 0.5f;
                                    float startY = emitter.y(k) - 0.5f;
                                    float startZ = emitter.z(k) - 0.5f;

                                    if(block instanceof FloorPropBlock<?>){
                                        startY -= 0.5f;
                                    }
                                    else{
                                        startX = emitter.x(k) - 0.5f;
                                        startZ = emitter.z(k) - 0.5f;
                                    }

                                    float rot = newRot % 360;
                                    double radRot = Math.toRadians(rot);

                                    float newX = (float) ((startX * Math.cos(radRot)) - (startZ * Math.sin(radRot)));
                                    float newZ = (float) ((startX * Math.sin(radRot)) + (startZ * Math.cos(radRot)));

                                    if(block instanceof WallPropBlock<?> && newRot != 0){
                                        Direction d = state.get(WallPropBlock.FACING);
                                        float xO = 0, zO = 0;
                                        BlockState getBlockState = MinecraftClient.getInstance().world.getBlockState(pos.offset(d.getOpposite()));
                                        if(getBlockState.getBlock() instanceof DiagonalMimicFrame f && f.isDiagonal(getBlockState)){

                                            d = DiagonalMimicFrame.NEXT_MAP.get(d) == f.getDiagonalDirection(getBlockState) ? d : d.rotateYCounterclockwise();


                                            if(d.getAxis() == Direction.Axis.Z){
                                                if(d == state.get(WallPropBlock.FACING)){
                                                    xO = -0.1f * d.getVector().getZ();
                                                    zO = -0.375f * d.getVector().getZ();
                                                }
                                                else{
                                                    xO = 0.4f * d.getVector().getZ();
                                                    zO = 0.125f * d.getVector().getZ();
                                                }
                                            }
                                            else{
                                                if(d == state.get(WallPropBlock.FACING)){
                                                    xO = -0.4f * d.getVector().getX();
                                                    zO = 0.125f * d.getVector().getX();
                                                }
                                                else{
                                                    xO = 0.1f * d.getVector().getX();
                                                    zO = -0.375f * d.getVector().getX();
                                                }
                                            }
                                            newX += xO;
                                            newZ += zO;

                                            Vector3f normal = new Vector3f(newX, 0, newZ);
                                            emitter.normal(k, normal);
                                        }
                                    }

                                    emitter.pos(k, (float) (newX + offsetX), (float) (startY + offsetY), (float) (newZ + offsetZ));

                                }

                                emitter.ambientOcclusion(ao);
                                emitter.shadeMode(ShadeMode.VANILLA);
                                emitter.emit();
                            }
                        }
                    }
                    return;
                }
            }

            for (BlockModelPart part : ORIGINAL_MODEL.getParts(Random.create())) {
                final TriState ao = part.useAmbientOcclusion() ? TriState.DEFAULT : TriState.FALSE;
                for (int i = 0; i <= ModelHelper.NULL_FACE_ID; i++) {
                    final Direction cullFace = ModelHelper.faceFromIndex(i);

                    if (cullTest.test(cullFace)) {
                        // Skip entire quad list if possible.
                        continue;
                    }

                    final List<BakedQuad> quads = part.getQuads(cullFace);
                    final int quadCount = quads.size();

                    for (int j = 0; j < quadCount; j++) {
                        final BakedQuad q = quads.get(j);
                        emitter.cullFace(cullFace);

                        emitter.fromBakedQuad(q);
                        emitter.ambientOcclusion(ao);
                        emitter.shadeMode(ShadeMode.VANILLA);
                        emitter.emit();
                    }
                }
            }
        }
        //BlockStateModel.super.emitQuads(emitter, blockView, pos, state, random, cullTest);
    }

    @Override
    public Sprite particleSprite(BlockRenderView blockView, BlockPos pos, BlockState state) {
        return ORIGINAL_MODEL.particleSprite();
    }
}

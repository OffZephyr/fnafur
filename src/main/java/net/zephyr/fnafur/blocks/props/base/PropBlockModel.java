package net.zephyr.fnafur.blocks.props.base;

import io.netty.util.internal.SuppressJava6Requirement;
import jdk.jshell.Diag;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperUnbakedGroupedBlockStateModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.mesh.ShadeMode;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrame;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrameModel;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Predicate;

public class PropBlockModel extends WrapperUnbakedGroupedBlockStateModel implements BlockStateModel {

    public BlockStateModel ORIGINAL_MODEL;
    public PropBlockModel(BlockStateModel.UnbakedRoot wrapped){
        super(wrapped);
    }

    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> parts) {

    }

    @Override
    public BlockStateModel bake(BlockState state, ModelBaker baker) {
        ORIGINAL_MODEL = this.wrapped.bake(state, baker);
        return this;
    }

    @Override
    public TextureAtlasSprite particleIcon() {
        return ORIGINAL_MODEL.particleIcon();

    }

    @Override
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {

        if(ORIGINAL_MODEL != null) {

            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof PropBlockEntity ent) {
                CompoundTag nbt = ((IEntityDataSaver) ent).getPersistentData();

                if (state.getBlock() instanceof PropBlock<?> block) {
                    float rotation = nbt.getFloat("Rotation").orElse(0.0f);
                    rotation += 0.01f;

                    double offsetX = nbt.getDouble("xOffset").orElse(0.0);
                    double offsetY = nbt.getDouble("yOffset").orElse(0.0);
                    double offsetZ = nbt.getDouble("zOffset").orElse(0.0);

                    Vec3 offsetPos = new Vec3(offsetX, offsetY, offsetZ);

                    for (BlockModelPart part : ORIGINAL_MODEL.collectParts(RandomSource.create())) {

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
                                        Direction d = state.getValue(WallPropBlock.FACING);
                                        float xO = 0, zO = 0;
                                        BlockState getBlockState = Minecraft.getInstance().level.getBlockState(pos.relative(d.getOpposite()));
                                        if(getBlockState.getBlock() instanceof DiagonalMimicFrame f && f.isDiagonal(getBlockState)){

                                            d = DiagonalMimicFrame.NEXT_MAP.get(d) == f.getDiagonalDirection(getBlockState) ? d : d.getCounterClockWise();


                                            if(d.getAxis() == Direction.Axis.Z){
                                                if(d == state.getValue(WallPropBlock.FACING)){
                                                    xO = -0.1f * d.getUnitVec3i().getZ();
                                                    zO = -0.375f * d.getUnitVec3i().getZ();
                                                }
                                                else{
                                                    xO = 0.4f * d.getUnitVec3i().getZ();
                                                    zO = 0.125f * d.getUnitVec3i().getZ();
                                                }
                                            }
                                            else{
                                                if(d == state.getValue(WallPropBlock.FACING)){
                                                    xO = -0.4f * d.getUnitVec3i().getX();
                                                    zO = 0.125f * d.getUnitVec3i().getX();
                                                }
                                                else{
                                                    xO = 0.1f * d.getUnitVec3i().getX();
                                                    zO = -0.375f * d.getUnitVec3i().getX();
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

            for (BlockModelPart part : ORIGINAL_MODEL.collectParts(RandomSource.create())) {
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
    public TextureAtlasSprite particleSprite(BlockAndTintGetter blockView, BlockPos pos, BlockState state) {
        return ORIGINAL_MODEL.particleIcon();
    }
}

package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.List;
import java.util.Map;

public interface IWorldRendererAccessor<R extends BlockEntityRenderState & GeoRenderState> {
    Map<R, BlockEntityRenderer<?, R>> getEntityRenderStates();
    BlockEntityRenderer<?, R> getBlockEntityRenderer(R renderState);
    void addEntityRenderState(R renderState, BlockEntityRenderer<?, R> entity);
    void clearStates();
}

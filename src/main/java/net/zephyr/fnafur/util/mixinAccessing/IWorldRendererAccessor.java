package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.client.renderer.RenderBuffers;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.shapes.VoxelShape;
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

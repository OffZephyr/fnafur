package net.zephyr.fnafur.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.mixinAccessing.IWorldRendererAccessor;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class ClientHook {
    public static void openScreen(String index, NbtCompound nbt, long l){
        /*if (ScreenUtils.getScreens().containsKey(index)) {
            Screen screen = ScreenUtils.getScreens().get(index).create(Text.translatable("screen." + index + ".title"), nbt, l);
            if(MinecraftClient.getInstance().currentScreen == null || MinecraftClient.getInstance().currentScreen.getClass() != screen.getClass()) {
                MinecraftClient.getInstance().setScreen(screen);
            }
        }*/
    }

    public static<T extends BlockEntity, R extends BlockEntityRenderState & GeoRenderState> void renderWorldBlockEntity(BlockEntityRenderer<T, R> blockEntityRenderer, R state, MatrixStack matrices, WorldRenderState renderStates, OrderedRenderCommandQueueImpl queue){

        MatrixStack.Entry pos = state.getGeckolibData(CustomDataTickets.ENTITY_RENDER_MATRIX_ENTRY);
        matrices.push();
        //matrices.multiplyPositionMatrix(pos.getPositionMatrix());
        matrices.translate(MinecraftClient.getInstance().gameRenderer.getCamera().pos.multiply(-1));

        if(blockEntityRenderer != null){
            if(state.hasGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW) && Boolean.TRUE.equals(state.getGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW))){
                ((GeoPropRenderer<GeoPropBlockEntity, R>)blockEntityRenderer).renderPreview(state, matrices, queue, renderStates.cameraRenderState);
            }
        }
        matrices.pop();
    }

}

package net.zephyr.fnafur.item.animatronic.suit;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.client.CustomRenderingPipelines;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.jsonReaders.animatronics.AnimatronicDataHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.model.BakedGeoModel;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.RenderPassInfo;

public class SuitItemRenderer extends GeoItemRenderer<SuitItem> {


    public SuitItemRenderer() {
        super(new SuitItemModel());
    }

    @Override
    public GeoRenderState fillRenderState(SuitItem animatable, RenderData relatedObject, GeoRenderState renderState, float partialTick) {
        CompoundTag nbt = ItemUtil.getNbt(relatedObject.itemStack());

        nbt.getString("chara").ifPresent(chara -> {
            AnimatronicDataHandler.Chara c = AnimatronicDataHandler.CHARACTERS.get(chara);
            String alt = nbt.getString("alt").orElse(c.DEFAULT_ALT);
            String eyes = nbt.getString("eyes").orElse(c.ALTS.get(alt).default_eyes());

            renderState.addGeckolibData(CustomDataTickets.RENDER_SCALE, c.ALTS.get(alt).preview_scale());
            renderState.addGeckolibData(CustomDataTickets.TEXTURE, AnimatronicDataHandler.getAltTexture(chara, alt));
            renderState.addGeckolibData(CustomDataTickets.MODEL, AnimatronicDataHandler.getModel(chara, alt));
            renderState.addGeckolibData(CustomDataTickets.SUIT_MAP_TEXTURE, AnimatronicDataHandler.getEndoMask(chara, alt));
            renderState.addGeckolibData(CustomDataTickets.EYE_TEXTURE, AnimatronicDataHandler.getEyeTexture(chara, alt, eyes));
        });

        return super.fillRenderState(animatable, relatedObject, renderState, partialTick);
    }

    @Override
    public @Nullable RenderType getRenderType(GeoRenderState renderState, Identifier texture) {
        if(renderState.hasGeckolibData(CustomDataTickets.SUIT_MAP_TEXTURE)){
            return CustomRenderingPipelines.getAnimatronicSuit(texture, renderState.getGeckolibData(CustomDataTickets.SUIT_MAP_TEXTURE));
        }
        return CustomRenderingPipelines.getAnimatronicSuit(texture, AnimatronicDataHandler.getDefaultEndoMask());
    }

    @Override
    public void submitRenderTasks(RenderPassInfo<GeoRenderState> renderPassInfo, OrderedSubmitNodeCollector renderTasks, @org.jspecify.annotations.Nullable RenderType renderType) {
        if (renderType == null)
            return;

        final int packedLight = renderPassInfo.packedLight();
        final int packedOverlay = renderPassInfo.packedOverlay();
        final int renderColor = renderPassInfo.renderColor();

        if(renderPassInfo.model().getBone("head").isPresent()) {
            GeoRenderState renderState = renderPassInfo.renderState();
            PoseStack poseStack = renderPassInfo.poseStack();
            BakedGeoModel model = renderPassInfo.model();

            poseStack.pushPose();
            GeoBone bone = model.getBone("head").get();
            //poseStack.translate(-bone.getPosX(), -bone.getPosY(), -bone.getPosZ());
            float scale = 1;
            if (renderState.hasGeckolibData(CustomDataTickets.RENDER_SCALE)){
                scale = renderState.getGeckolibData(CustomDataTickets.RENDER_SCALE);
            }

            poseStack.scale(scale, scale, scale);
            poseStack.translate(0, -2.15f * (1f / scale), 0);
            renderTasks.submitCustomGeometry(renderPassInfo.poseStack(), renderType, (pose, vertexConsumer) -> {
                final PoseStack poseStack2 = renderPassInfo.poseStack();

                poseStack2.pushPose();
                poseStack2.last().set(pose);
                bone.positionAndRender(renderPassInfo, vertexConsumer, packedLight, packedOverlay, renderColor);
                poseStack2.popPose();
            });
            poseStack.popPose();
        }
    }
}

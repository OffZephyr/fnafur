package net.zephyr.fnafur.init.block_init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.zephyr.fnafur.blocks.illusion_block.models.MimicFrameBlockModel;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockModel;

@Environment(EnvType.CLIENT)
public class ModelLoading implements ModelLoadingPlugin {
    public static final String STICKER_BLOCK_ID = "sticker_block";
    public static final String MIMIC_BLOCK_ID = "mimic_frame";
    @Override
    public void initialize(Context pluginContext) {

        pluginContext.modifyModelOnLoad().register((original, context) -> {
            if(context.id() != null && original != null) {
                if (context.id().toString().contains(STICKER_BLOCK_ID)) {
                    return new StickerBlockModel(original);
                }
                else if (context.id().toString().contains(MIMIC_BLOCK_ID)) {
                    if(context.id().toString().contains("_2")) return new MimicFrameBlockModel(original, BlockInit.MIMIC_FRAME_2x2.getDefaultState());
                    else if(context.id().toString().contains("_4")) return new MimicFrameBlockModel(original, BlockInit.MIMIC_FRAME_4x4.getDefaultState());

                    return new MimicFrameBlockModel(original, BlockInit.MIMIC_FRAME.getDefaultState());
                }
            }
            return original;
        });
    }
}

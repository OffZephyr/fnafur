package net.zephyr.fnafur.init.block_init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrameBlockModel;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockModel;

@Environment(EnvType.CLIENT)
public class ModelLoading implements ModelLoadingPlugin {
    public static final String STICKER_BLOCK_ID = "sticker_block";
    public static final String MIMIC_BLOCK_ID = "mimic_frame";
    @Override
    public void initialize(Context pluginContext) {

        pluginContext.modifyModelOnLoad().register((original, context) -> {
            if(context.id() != null) {
                if (context.id().toString().contains(STICKER_BLOCK_ID)) {
                    return new StickerBlockModel(original);
                }
                else if (context.id().toString().contains(MIMIC_BLOCK_ID)) {
                    return new MimicFrameBlockModel(original);
                }
            }
            return original;
        });
    }
}

package net.zephyr.fnafur.init.block_init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrameBlockModel;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrameBlockSlabModel;
import net.zephyr.fnafur.blocks.basic_blocks.layered_block.LayeredBlockModel;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrames;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockModel;

@Environment(EnvType.CLIENT)
public class ModelLoading implements ModelLoadingPlugin {
    private static final Identifier LAYERED_BLOCK_ID = Identifier.of(FnafUniverseResuited.MOD_ID, "layered_block");
    public static final Identifier BASE_LAYERED_TEXTURE = Identifier.of(FnafUniverseResuited.MOD_ID, "block/layered_block");
    public static final Identifier MIMIC_FRAME_SLAB_ID = Identifier.of(FnafUniverseResuited.MOD_ID, "mimic_frame_slab");
    public static final Identifier STICKER_BLOCK_ID = Identifier.of(FnafUniverseResuited.MOD_ID, "sticker_block");
    @Override
    public void initialize(Context pluginContext) {

        pluginContext.modifyModelOnLoad().register((original, context) -> {
            if(context.topLevelId() != null) {
                if (MimicFrames.IDs.contains(context.topLevelId().id())) {
                    return new MimicFrameBlockModel();
                }
                if (context.topLevelId().id().equals(STICKER_BLOCK_ID)) {
                    return new StickerBlockModel();
                }
                if (context.topLevelId().id().equals(MIMIC_FRAME_SLAB_ID)) {
                    return new MimicFrameBlockSlabModel();
                }
                if (context.topLevelId().id().equals(LAYERED_BLOCK_ID)) {
                    return new LayeredBlockModel();
                }
            }
            return original;
        });
    }
}

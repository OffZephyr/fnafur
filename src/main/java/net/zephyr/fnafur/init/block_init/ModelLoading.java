package net.zephyr.fnafur.init.block_init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrames;
import net.zephyr.fnafur.blocks.illusion_block.models.MimicFrameBlockModel;
import net.zephyr.fnafur.blocks.stickers_blocks.BlockWithSticker;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockModel;

@Environment(EnvType.CLIENT)
public class ModelLoading implements ModelLoadingPlugin {
    public static final String STICKER_BLOCK_ID = "sticker_block";
    public static final String MIMIC_BLOCK_ID = "mimic_frame";
    @Override
    public void initialize(Context pluginContext) {


        pluginContext.modifyBlockModelOnLoad().register((original, context) -> {
            if(context.state().getBlock() instanceof BlockWithSticker && original != null) {
                System.out.println(context.state().getBlock());
                if (context.state().getBlock() instanceof MimicFrames) {
                    System.out.println("GUH but mimic");

                    if(context.state().isOf(BlockInit.MIMIC_FRAME_2x2)) return new MimicFrameBlockModel(original, BlockInit.MIMIC_FRAME_2x2.getDefaultState());
                    else if(context.state().isOf(BlockInit.MIMIC_FRAME_4x4)) return new MimicFrameBlockModel(original, BlockInit.MIMIC_FRAME_4x4.getDefaultState());
                    else return new MimicFrameBlockModel(original, BlockInit.MIMIC_FRAME.getDefaultState());
                }
                else if (context.state().getBlock() instanceof BlockWithSticker) {
                    System.out.println("GUH");
                    return new StickerBlockModel(original);
                }
            }
            return original;
        });
    }
}

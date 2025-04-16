package net.zephyr.fnafur.init;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickerInit {
    public static Map<String, Decal> STICKERS = new HashMap<>();
    public static Map<String, List<Decal>> CATEGORIES = new HashMap<>();

    static {
        registerCategory(
                "wall_tiles",
                registerSticker(
                        "b_w_r_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_red_wall_tiles")
                ),
                registerSticker(
                        "b_w_hr2_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_hotred2_wall_tiles")
                ),
                registerSticker(
                        "b_w_hr_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_hotred_wall_tiles")
                ),
                registerSticker(
                        "b_w_o_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_orange_wall_tiles")
                ),
                registerSticker(
                        "b_w_y_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_yellow_wall_tiles")
                ),
                registerSticker(
                        "b_w_l_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_lime_wall_tiles")
                ),
                registerSticker(
                        "b_w_lbl_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_light_blue_wall_tiles")
                ),
                registerSticker(
                        "b_w_bl_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_blue_wall_tiles")
                ),
                registerSticker(
                        "b_w_p_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_purple_wall_tiles")
                ),
                registerSticker(
                        "b_w_m_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_magenta_wall_tiles")
                ),
                registerSticker(
                        "b_w_pi_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_pink_wall_tiles")
                ),
                registerSticker(
                        "b_w_g_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_gray_wall_tiles")
                ),
                registerSticker(
                        "b_w_b_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_black_wall_tiles")
                ),
                registerSticker(
                        "b_w_b_dirty_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_black_dirty_wall_tiles")
                ),
                registerSticker(
                        "b_w_b2_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_black2_wall_tiles")
                ),
                registerSticker(
                        "b_p_b_wall_tiles",
                        5.5f,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/purple_black_wall_tile")
                ),
                registerSticker(
                        "c_w_wall_tiles",
                        9,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/colored_white_wall_tiles")
                ),
                registerSticker(
                        "c_w_long_wall_tiles",
                        9,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/colored_white_long_wall_tiles")
                )
        );
        registerCategory(
                "large_wall_tiles",
                registerSticker(
                        "b_w_r_wall_big_tiles",
                        1.75f,
                        16,
                        0.55f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_red_wall_big_tiles")
                )
        );
        registerCategory(
                "diamond_wall_tiles",
                registerSticker(
                        "b_r_wall_diamond_tiles",
                        9,
                        16,
                        0.75f,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_red_diamond_tiles")
                )
        );
        registerCategory(
                "wall_grunge",
                registerSticker(
                        "wall_grunge",
                        0,
                        16,
                        0,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_grunge_1"),
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_grunge_2"),
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_grunge_3"),
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_grunge_4")
                ),
                registerSticker(
                        "wall_grunge_2",
                        0,
                        16,
                        0,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_grunge_flat")
                )
        );
    }
    private static Decal registerSticker(String stickerName, float size, int pixelDensity, float mouseOffset, Movable direction, boolean isWallSticker, boolean isStackable, Identifier... textures) {
        Decal decal = new Decal(stickerName, direction, size, pixelDensity, mouseOffset, isWallSticker, isStackable, textures);
        STICKERS.put(stickerName, decal);

        return decal;
    }
    private static List<Decal> registerCategory(String name, Decal... decals) {
        CATEGORIES.put(name, List.of(decals));
        return List.of(decals);
    }

    public static Decal getSticker(String name){
        return STICKERS.get(name);
    }

    public static void registerStickers() {
        FnafUniverseRebuilt.LOGGER.info("Registering Sticker Items for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }

    public record Decal(String name, Movable move, float size, int pixelDensity, float mouseOffset, boolean isWallSticker, boolean isStackable, Identifier... ids) {
        public float getSize() {
            return size;
        }

        public int getPixelDensity() {
            return pixelDensity;
        }

        public Identifier[] getTextures() {
            return ids;
        }

        public Movable getDirection() {
            return move;
        }
    }

    public enum Movable {
        NONE,
        VERTICAL,
        HORIZONTAL;
    }
}

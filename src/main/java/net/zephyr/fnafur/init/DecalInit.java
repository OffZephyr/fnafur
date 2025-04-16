package net.zephyr.fnafur.init;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecalInit {
    /** <h2 id="format">HOW TO ADD A CATEGORY</h2>
     * Just call the {@link #registerCategory(String, Decal...)} method. You'll register the decals directly inside of that method.
     * Don't forget to add the name of the category to the LANG file, otherwise it'll bleed out of the page in the book.
     * <h2 id="format">HOW TO ADD A DECAL</h2>
     * Call the {@link #registerDecal(String, float, int, Movable, boolean, boolean, Identifier...)} method and fill up the parameters.
     * the SIZE relates to the amount of free space on a texture. Open it and count the dead pixels, then do a bit of math to get the value.
     * if the texture is 64x64 pixels and the Pixel Density is 16, since 16 is a 4th of 64, you will divide the amount of dead space by 4.
     * <h2 id="format">[DEAD SPACE] / 4 = [SIZE]</h2>
     *
     * NOTE: leave isWallSticker on TRUE for all stickers you may add, FLOOR STICKERS ARE NOT FUNCTIONAL
     */

    public static Map<String, Decal> DECALS = new HashMap<>();
    public static Map<String, List<Decal>> CATEGORIES = new HashMap<>();
    public static List<String> CATEGORIES_LIST = new ArrayList<>();

    static {
        registerCategory(
                "wall_tiles",
                registerDecal(
                        "b_w_r_wall_tiles",
                        5.5f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_red_wall_tiles")
                ),
                registerDecal(
                        "b_w_hr2_wall_tiles",
                        6f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_hotred2_wall_tiles")
                ),
                registerDecal(
                        "b_w_hr_wall_tiles",
                        5.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_hotred_wall_tiles")
                ),
                registerDecal(
                        "b_w_o_wall_tiles",
                        5.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_orange_wall_tiles")
                ),
                registerDecal(
                        "b_w_y_wall_tiles",
                        5.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_yellow_wall_tiles")
                ),
                registerDecal(
                        "b_w_l_wall_tiles",
                        5.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_lime_wall_tiles")
                ),
                registerDecal(
                        "b_w_lbl_wall_tiles",
                        5.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_light_blue_wall_tiles")
                ),
                registerDecal(
                        "b_w_bl_wall_tiles",
                        5.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_blue_wall_tiles")
                ),
                registerDecal(
                        "b_w_p_wall_tiles",
                        5.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_purple_wall_tiles")
                ),
                registerDecal(
                        "b_w_m_wall_tiles",
                        5.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_magenta_wall_tiles")
                ),
                registerDecal(
                        "b_w_pi_wall_tiles",
                        5.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_pink_wall_tiles")
                ),
                registerDecal(
                        "b_w_g_wall_tiles",
                        5.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_gray_wall_tiles")
                ),
                registerDecal(
                        "b_w_b_wall_tiles",
                        5.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_black_wall_tiles")
                ),
                registerDecal(
                        "b_w_b_dirty_wall_tiles",
                        5.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_black_dirty_wall_tiles")
                ),
                registerDecal(
                        "b_w_b2_wall_tiles",
                        5.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_black2_wall_tiles")
                ),
                registerDecal(
                        "b_p_b_wall_tiles",
                        5f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/purple_black_wall_tile")
                ),
                registerDecal(
                        "c_w_wall_tiles",
                        9,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/colored_white_wall_tiles")
                ),
                registerDecal(
                        "c_w_long_wall_tiles",
                        9,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/colored_white_long_wall_tiles")
                ),
                registerDecal(
                        "b_r_wall_diamond_tiles",
                        7.5f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_red_diamond_tiles")
                )
        );
        registerCategory(
                "large_wall_tiles",
                registerDecal(
                        "b_w_r_wall_big_tiles",
                        1.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/black_white_red_wall_big_tiles")
                )
        );
        registerCategory(
                "tiny_wall_tiles",
                registerDecal(
                        "b_w_r_wall_tiny_tiles",
                        12.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/small_wall_tiles_red")
                ),
                registerDecal(
                        "b_w_o_wall_tiny_tiles",
                        12.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/small_wall_tiles_orange")
                ),
                registerDecal(
                        "b_w_y_wall_tiny_tiles",
                        12.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/small_wall_tiles_yellow")
                ),
                registerDecal(
                        "b_w_g_wall_tiny_tiles",
                        12.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/small_wall_tiles_green")
                ),
                registerDecal(
                        "b_w_c_wall_tiny_tiles",
                        12.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/small_wall_tiles_cyan")
                ),
                registerDecal(
                        "b_w_bl_wall_tiny_tiles",
                        12.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/small_wall_tiles_blue")
                ),
                registerDecal(
                        "b_w_p_wall_tiny_tiles",
                        12.25f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_tiles/small_wall_tiles_purple")
                )
        );
        registerCategory(
                "horizontal_trims",
                registerDecal(
                        "star_trim",
                        9.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/star_trim")
                ),
                registerDecal(
                        "red_wall_trimming",
                        13.5f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/red_wall_trimming")
                ),
                registerDecal(
                        "orange_wall_trimming",
                        13.5f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/orange_wall_trimming")
                ),
                registerDecal(
                        "yellow_wall_trimming",
                        13.5f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/yellow_wall_trimming")
                ),
                registerDecal(
                        "green_wall_trimming",
                        13.5f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/green_wall_trimming")
                ),
                registerDecal(
                        "turquoise_wall_trimming",
                        13.5f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/turquoise_wall_trimming")
                ),
                registerDecal(
                        "cyan_wall_trimming",
                        13.5f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/cyan_wall_trimming")
                ),
                registerDecal(
                        "blue_trimming",
                        13.5f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/blue_trimming")
                ),
                registerDecal(
                        "purple_blue_trimming",
                        13.5f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/purple_blue_trimming")
                ),
                registerDecal(
                        "silver_wall_trimming",
                        13.5f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/silver_wall_trimming")
                ),
                registerDecal(
                        "red_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/red_tile_trim")
                ),
                registerDecal(
                        "orange_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/orange_tile_trim")
                ),
                registerDecal(
                        "yellow_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/yellow_tile_trim")
                ),
                registerDecal(
                        "green_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/green_tile_trim")
                ),
                registerDecal(
                        "mint_green_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/mint_green_tile_trim")
                ),
                registerDecal(
                        "turquoise_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/turquoise_tile_trim")
                ),
                registerDecal(
                        "cyan_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/cyan_tile_trim")
                ),
                registerDecal(
                        "blue_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/blue_tile_trim")
                ),
                registerDecal(
                        "purple_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/purple_tile_trim")
                ),
                registerDecal(
                        "purple_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/purple_tile_trim")
                ),
                registerDecal(
                        "magenta_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/magenta_tile_trim")
                ),
                registerDecal(
                        "pink_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/pink_tile_trim")
                ),
                registerDecal(
                        "silver_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/silver_tile_trim")
                ),
                registerDecal(
                        "white_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/white_tile_trim")
                ),
                registerDecal(
                        "black_tile_trim",
                        14.75f,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/trims/black_tile_trim")
                )
        );
        registerCategory(
                "wall_grunge",
                registerDecal(
                        "wall_grunge",
                        0,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_grunge_1"),
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_grunge_2"),
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_grunge_3"),
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_grunge_4")
                ),
                registerDecal(
                        "wall_grunge_2",
                        0,
                        16,
                        Movable.VERTICAL,
                        true,
                        true,
                        Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/stickers/wall_grunge_flat")
                )
        );
    }
    private static Decal registerDecal(String stickerName, float size, int pixelDensity, Movable direction, boolean isWallSticker, boolean isStackable, Identifier... textures) {

        float offset = 1 - ((pixelDensity - size)/2f / pixelDensity);

        Decal decal = new Decal(stickerName, direction, size, pixelDensity, offset, isWallSticker, isStackable, textures);
        DECALS.put(stickerName, decal);

        return decal;
    }
    private static List<Decal> registerCategory(String name, Decal... decals) {
        CATEGORIES.put(name, List.of(decals));
        CATEGORIES_LIST.add(name);
        return List.of(decals);
    }

    public static Decal getDecal(String name){
        return DECALS.get(name);
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

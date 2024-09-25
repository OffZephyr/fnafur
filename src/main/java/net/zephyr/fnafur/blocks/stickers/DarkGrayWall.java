package net.zephyr.fnafur.blocks.stickers;

import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.stickers.base.StickerBlock;

import java.util.HashMap;
import java.util.Map;

public class DarkGrayWall extends StickerBlock {
    public DarkGrayWall(Settings settings) {
        super(settings);
    }
    SpriteIdentifier DARK_GRAY_WALL = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/dark_gray_wall"));

    @Override
    public Map<Direction, SpriteIdentifier> sprites() {
        Map<Direction, SpriteIdentifier> map = new HashMap<>();
        map.put(Direction.NORTH, DARK_GRAY_WALL);
        map.put(Direction.EAST, DARK_GRAY_WALL);
        map.put(Direction.SOUTH, DARK_GRAY_WALL);
        map.put(Direction.WEST, DARK_GRAY_WALL);
        map.put(Direction.UP, DARK_GRAY_WALL);
        map.put(Direction.DOWN, DARK_GRAY_WALL);
        return map;
    }

    @Override
    public SpriteIdentifier particleSprite() {
        return DARK_GRAY_WALL;
    }
}

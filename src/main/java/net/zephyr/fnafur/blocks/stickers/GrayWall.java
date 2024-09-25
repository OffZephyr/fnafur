package net.zephyr.fnafur.blocks.stickers;

import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.stickers.base.StickerBlock;

import java.util.HashMap;
import java.util.Map;

public class GrayWall extends StickerBlock {
    public GrayWall(Settings settings) {
        super(settings);
    }
    SpriteIdentifier GRAY_WALL = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/gray_wall"));

    @Override
    public Map<Direction, SpriteIdentifier> sprites() {
        Map<Direction, SpriteIdentifier> map = new HashMap<>();
        map.put(Direction.NORTH, GRAY_WALL);
        map.put(Direction.EAST, GRAY_WALL);
        map.put(Direction.SOUTH, GRAY_WALL);
        map.put(Direction.WEST, GRAY_WALL);
        map.put(Direction.UP, GRAY_WALL);
        map.put(Direction.DOWN, GRAY_WALL);
        return map;
    }

    @Override
    public SpriteIdentifier particleSprite() {
        return GRAY_WALL;
    }
}

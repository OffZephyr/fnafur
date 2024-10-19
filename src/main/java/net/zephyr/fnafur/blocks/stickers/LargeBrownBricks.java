package net.zephyr.fnafur.blocks.stickers;

import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.stickers.base.StickerBlock;

import java.util.HashMap;
import java.util.Map;

public class LargeBrownBricks extends StickerBlock {
    public LargeBrownBricks(Settings settings) {
        super(settings);
    }
    SpriteIdentifier LARGE_BROWN_BRICKS = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseResuited.MOD_ID, "block/large_brown_bricks"));

    @Override
    public Map<Direction, SpriteIdentifier> sprites() {
        Map<Direction, SpriteIdentifier> map = new HashMap<>();
        map.put(Direction.NORTH, LARGE_BROWN_BRICKS);
        map.put(Direction.EAST, LARGE_BROWN_BRICKS);
        map.put(Direction.SOUTH, LARGE_BROWN_BRICKS);
        map.put(Direction.WEST, LARGE_BROWN_BRICKS);
        map.put(Direction.UP, LARGE_BROWN_BRICKS);
        map.put(Direction.DOWN, LARGE_BROWN_BRICKS);
        return map;
    }

    @Override
    public SpriteIdentifier particleSprite() {
        return LARGE_BROWN_BRICKS;
    }
}

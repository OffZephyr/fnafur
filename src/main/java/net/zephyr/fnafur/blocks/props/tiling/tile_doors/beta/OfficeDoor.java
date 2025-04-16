package net.zephyr.fnafur.blocks.props.tiling.tile_doors.beta;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

public class OfficeDoor extends TileDoorBlock{
    public OfficeDoor(Settings settings) {
        super(settings);
    }

    @Override
    public Identifier blockTexture() {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/office_door.png");
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
}

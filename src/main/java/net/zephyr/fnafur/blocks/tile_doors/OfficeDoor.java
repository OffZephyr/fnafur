package net.zephyr.fnafur.blocks.tile_doors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.Direction;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;

public class OfficeDoor extends TileDoorBlock{
    public OfficeDoor(Settings settings) {
        super(settings);
    }

    @Override
    public Identifier blockTexture() {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/office_door.png");
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
}

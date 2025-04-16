package net.zephyr.fnafur.blocks.props.tiling.tile_doors;

import net.minecraft.util.StringIdentifiable;

public enum TileDoorDirection implements StringIdentifiable {
    UP("up", 0),
    DOWN("down", 180),
    LEFT("left", 270),
    RIGHT("right", 90);

    final int ANGLE;
    final String NAME;
    TileDoorDirection(String name, int angle){
        ANGLE = angle;
        NAME = name;
    }
    public int getANGLE(){
        return ANGLE;
    }

    @Override
    public String asString() {
        return NAME;
    }
}

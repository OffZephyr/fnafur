package net.zephyr.fnafur.blocks.props.tiling.tile_doors.beta;

import net.minecraft.util.StringIdentifiable;

public enum DoorTileProperty implements StringIdentifiable {
    SINGLE("1x1"),
    SINGLE_BOTTOM("1x3_bottom"),
    SINGLE_CENTER("1x3_center"),
    SINGLE_TOP("1x3_top"),
    CORNER_BOTTOM_LEFT("corner_bottom_left"),
    CORNER_BOTTOM_RIGHT("corner_bottom_right"),
    CORNER_TOP_LEFT("corner_top_left"),
    CORNER_TOP_RIGHT("corner_top_right"),
    SIDE_LEFT("side_left"),
    SIDE_RIGHT("side_right"),
    TOP("top"),
    BOTTOM("bottom"),
    CENTER("center");

    final String name;
    DoorTileProperty(String name){
        this.name = name;
    }

    @Override
    public String asString() {
        return name;
    }
}

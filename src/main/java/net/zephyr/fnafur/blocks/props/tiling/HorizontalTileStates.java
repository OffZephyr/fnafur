package net.zephyr.fnafur.blocks.props.tiling;

import net.minecraft.util.StringIdentifiable;

public enum HorizontalTileStates implements StringIdentifiable {
    SINGLE("single", false, false, false, false),
    NORTH_WEST("north_west", false, true, true, false),
    NORTH("north", false, true, true, true),
    NORTH_EAST("north_east", false, false, true, true),

    WEST("west", true, true, true, false),
    CENTER("center", true, true, true, true),

    EAST("east", true, false, true, true),
    SOUTH_WEST("south_west", true, true, false, false),

    SOUTH("south", true, true, false, true),
    SOUTH_EAST("south_east", true, false, false, true),
    VERTICAL_NORTH("vertical_north", false, false, true, false),
    VERTICAL_CENTER("vertical_center", true, false, true, false),
    VERTICAL_SOUTH("vertical_south", true, false, false, false),
    HORIZONTAL_WEST("horizontal_west", false, true, false, false),
    HORIZONTAL_CENTER("horizontal_center", false, true, false, true),
    HORIZONTAL_EAST("horizontal_east", false, false, false, true),
    ;

    public final String NAME;
    public final boolean CONNECTS_NORTH;
    public final boolean CONNECTS_EAST;
    public final boolean CONNECTS_SOUTH;
    public final boolean CONNECTS_WEST;

    HorizontalTileStates(String name, boolean north, boolean east, boolean south, boolean west) {
        this.NAME = name;

        this.CONNECTS_NORTH = north;
        this.CONNECTS_EAST = east;
        this.CONNECTS_SOUTH = south;
        this.CONNECTS_WEST = west;
    }

    @Override
    public String asString() {
        return this.NAME;
    }

    public static HorizontalTileStates get(boolean north, boolean east, boolean south, boolean west){
        for (HorizontalTileStates state : HorizontalTileStates.values()) {
            if(north == state.CONNECTS_NORTH && east == state.CONNECTS_EAST && south == state.CONNECTS_SOUTH && west == state.CONNECTS_WEST){
                return state;
            }
        }
        return SINGLE;
    }
}

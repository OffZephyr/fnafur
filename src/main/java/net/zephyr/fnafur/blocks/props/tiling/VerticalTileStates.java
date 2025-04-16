package net.zephyr.fnafur.blocks.props.tiling;

import net.minecraft.util.StringIdentifiable;

public enum VerticalTileStates implements StringIdentifiable {
    SINGLE("single", false, false, false, false),
    TOP_LEFT("top_left", false, true, true, false),
    TOP("top", false, true, true, true),
    TOP_RIGHT("top_right", false, false, true, true),

    LEFT("left", true, true, true, false),
    CENTER("center", true, true, true, true),

    RIGHT("right", true, false, true, true),
    BOTTOM_LEFT("bottom_left", true, true, false, false),

    BOTTOM("bottom", true, true, false, true),
    BOTTOM_RIGHT("bottom_right", true, false, false, true),
    VERTICAL_TOP("vertical_top", false, false, true, false),
    VERTICAL_CENTER("vertical_center", true, false, true, false),
    VERTICAL_BOTTOM("vertical_bottom", true, false, false, false),
    HORIZONTAL_LEFT("horizontal_left", false, true, false, false),
    HORIZONTAL_CENTER("horizontal_center", false, true, false, true),
    HORIZONTAL_RIGHT("horizontal_right", false, false, false, true),
    ;

    public final String NAME;
    public final boolean CONNECTS_TOP;
    public final boolean CONNECTS_RIGHT;
    public final boolean CONNECTS_BOTTOM;
    public final boolean CONNECTS_LEFT;

    VerticalTileStates(String name, boolean top, boolean right, boolean bottom, boolean left) {
        this.NAME = name;

        this.CONNECTS_TOP = top;
        this.CONNECTS_RIGHT = right;
        this.CONNECTS_BOTTOM = bottom;
        this.CONNECTS_LEFT = left;
    }

    @Override
    public String asString() {
        return this.NAME;
    }

    public static VerticalTileStates get(boolean top, boolean right, boolean bottom, boolean left){
        for (VerticalTileStates state : VerticalTileStates.values()) {
            if(top == state.CONNECTS_TOP && left == state.CONNECTS_RIGHT && bottom == state.CONNECTS_BOTTOM && right == state.CONNECTS_LEFT){
                return state;
            }
        }
        return SINGLE;
    }
}

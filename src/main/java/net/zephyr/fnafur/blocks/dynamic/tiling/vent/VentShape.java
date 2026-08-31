package net.zephyr.fnafur.blocks.dynamic.tiling.vent;

import net.minecraft.util.StringRepresentable;

public enum VentShape implements StringRepresentable {
    STRAIGHT("straight", Type.STRAIGHT),
    TRANSITION("transition", Type.STRAIGHT),
    TRANSITION_END("transition_end", Type.STRAIGHT),
    CORNER("corner", Type.TURN),
    T("t", Type.TURN),
    PLUS("plus", Type.TURN),
    WALL("wall", Type.WALL)
    ;

    final String NAME;
    final Type TYPE;
    VentShape(String name, Type type){
        this.NAME = name;
        this.TYPE = type;
    }

    @Override
    public String getSerializedName() {
        return NAME;
    }
    public Type getType() {
        return TYPE;
    }

    public enum Type{
        STRAIGHT,
        TURN,
        WALL
    }
}

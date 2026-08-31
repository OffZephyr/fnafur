package net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors;

import net.minecraft.util.StringRepresentable;

public enum TileDoorDirection implements StringRepresentable {
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
    public String getSerializedName() {
        return NAME;
    }

    public static TileDoorDirection getDirection(String name){
        for(TileDoorDirection d : values()){
            if(d.getSerializedName().equals(name)) return d;
        }
        return null;
    }
}

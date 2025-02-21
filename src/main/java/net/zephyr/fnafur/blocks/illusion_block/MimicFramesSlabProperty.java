package net.zephyr.fnafur.blocks.illusion_block;

import net.minecraft.util.StringIdentifiable;

public enum MimicFramesSlabProperty implements StringIdentifiable {
    BOTTOM("bottom", 0),
    TOP("top", 1);
    private final String name;
    private final int index;
    MimicFramesSlabProperty(String name, int index){
        this.name = name;
        this.index = index;
    }
    @Override
    public String asString() {
        return this.name;
    }
    public int getIndex(){
        return this.index;
    }
}

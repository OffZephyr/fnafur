package net.zephyr.fnafur.blocks.energy.enums;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum EnergyNodeType implements StringIdentifiable, ColorEnumInterface {
    GENERATOR("generator", 0),
    SWITCH("switch", 1),
    OUTPUT("output", 2);

    private final String name;
    private final int index;

    EnergyNodeType(String name, int index){
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

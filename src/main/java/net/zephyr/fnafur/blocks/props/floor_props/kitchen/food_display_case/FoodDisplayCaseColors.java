package net.zephyr.fnafur.blocks.props.floor_props.kitchen.food_display_case;

import net.minecraft.util.StringRepresentable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum FoodDisplayCaseColors implements StringRepresentable, ColorEnumInterface {
    DEFAULT("default", 0),
    METAL("metal", 1),
    ;

    private final String name;
    private final int index;
    FoodDisplayCaseColors(String name, int index){
        this.name = name;
        this.index = index;
    }
    @Override
    public String getSerializedName() {
        return this.name;
    }
    public int getIndex(){
        return this.index;
    }
}

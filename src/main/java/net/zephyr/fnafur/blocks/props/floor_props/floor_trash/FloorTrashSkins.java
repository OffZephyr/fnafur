package net.zephyr.fnafur.blocks.props.floor_props.floor_trash;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum FloorTrashSkins implements StringIdentifiable, ColorEnumInterface {
    MODEL_O("model_0", 0),
    MODEL_1("model_1", 1),
    MODEL_2("model_2", 2),
    MODEL_3("model_3", 3),
    MODEL_4("model_4", 4),

    ;
    private final String name;
    private final int index;
    FloorTrashSkins(String name, int index){
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

package net.zephyr.fnafur.blocks.props.office_buttons;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.ColorEnumInterface;

public enum OfficeButtonsColors implements StringIdentifiable, ColorEnumInterface {
    DEFAULT("both", 0, true),
    DOOR("door", 1, true),
    LIGHT("light", 2, false),
    NAMELESS_DOOR("nameless_door", 3, true),
    NAMELESS_LIGHT("nameless_light", 4, false);
    private final String name;
    private final int index;
    private final boolean door;
    OfficeButtonsColors(final String name, final int index, final boolean isDoor){
        this.name = name;
        this.index = index;
        this.door = isDoor;
    }
    @Override
    public String asString() {
        return this.name;
    }
    public int getIndex(){
        return this.index;
    }
    public boolean isDoor(){
        return this.door;
    }
}

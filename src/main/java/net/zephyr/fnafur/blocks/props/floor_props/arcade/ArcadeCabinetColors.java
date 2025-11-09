package net.zephyr.fnafur.blocks.props.floor_props.arcade;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum ArcadeCabinetColors implements StringIdentifiable, ColorEnumInterface {
    GRAY("0", 0),
    BLUE("1", 1),
    TEAL("2", 2),
    PURPLE("3", 3),
    GREEN("4", 4),
    RED("5", 5),
    ORANGE("6", 6),
    YELLOW("7", 7),
    CHICA("8",8),

    ;
    private final String name;
    private final int index;
    ArcadeCabinetColors(String name, int index){
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

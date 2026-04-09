package net.zephyr.fnafur.blocks.props.floor_props.plushies;

import net.minecraft.util.StringRepresentable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum PlushieColorEnum implements StringRepresentable, ColorEnumInterface {
    FREDDY("freddy", 0),
    BONNIE("bonnie", 1),
    CHICA("chica", 1),
    FOXY("foxy", 1),
    GOLDEN_FREDDY("golden_freddy", 1),
    HAUNTED_FREDBEAR("haunted_fredbear", 1),
    FREDBEAR("fredbear", 1),
    SPRING_BONNIE("spring_bonnie", 1)
    ;
    private final String name;
    private final int index;
    PlushieColorEnum(String name, int index){
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

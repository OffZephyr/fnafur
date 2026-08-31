package net.zephyr.fnafur.blocks.props.floor_props.instruments.standing_piano;

import net.minecraft.util.StringRepresentable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum StandingPianoColors implements StringRepresentable, ColorEnumInterface {
    DEFAULT("default", 0),
    SHEET_MUSIC("sheet_music", 1),
    ;

    private final String name;
    private final int index;
    StandingPianoColors(String name, int index){
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

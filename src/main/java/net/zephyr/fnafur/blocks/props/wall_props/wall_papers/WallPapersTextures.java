package net.zephyr.fnafur.blocks.props.wall_props.wall_papers;

import net.minecraft.util.StringRepresentable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum WallPapersTextures implements ColorEnumInterface, StringRepresentable {
    MISSING_1_FEMALE("missing_1_female", 0),
    MISSING_1_MALE("missing_1_male", 1),
    MISSING_2("missing_2", 2),
    MISSING_3("missing_3", 3),
    MISSING_4("missing_4", 4),
    WALLPAPERS_1("wallpapers_1", 5),
    WALLPAPERS_2("wallpapers_2", 6),
    WALLPAPERS_3("wallpapers_3", 7),
    WALLPAPERS_4("wallpapers_4", 8)
    ;
    private final String name;
    private final int index;
    WallPapersTextures(final String name, final int index){
        this.name = name;
        this.index = index;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public int getIndex() {
        return index;
    }
}

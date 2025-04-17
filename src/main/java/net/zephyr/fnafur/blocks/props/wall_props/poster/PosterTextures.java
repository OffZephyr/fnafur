package net.zephyr.fnafur.blocks.props.wall_props.poster;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum PosterTextures implements ColorEnumInterface, StringIdentifiable {
    FREDDY_LETS_PARTY("freddy_lets_party", 0),
    FOXY_PLAY_TIME("foxy_play_time", 1)
    ;
    private final String name;
    private final int index;
    PosterTextures(final String name, final int index){
        this.name = name;
        this.index = index;
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    public int getIndex() {
        return index;
    }
}

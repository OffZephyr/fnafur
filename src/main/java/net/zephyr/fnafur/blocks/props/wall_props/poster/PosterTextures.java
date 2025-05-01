package net.zephyr.fnafur.blocks.props.wall_props.poster;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum PosterTextures implements ColorEnumInterface, StringIdentifiable {
    FNAFONE_CELEBRATE_SMALL("fnafone_celebrate_small", 0),
    FNAFONE_CELEBRATE("fnafone_celebrate", 1),
    FREDDY_FUN_TIME("freddy_fun_time", 2),
    BONNIE_PARTY_TIME("bonnie_party_time", 3),
    CHICA_EATING_TIME("chica_eating_time", 4),
    FOXY_PLAY_TIME("foxy_play_time", 5),
    FREDDY_LETS_PARTY("freddy_lets_party", 6)
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

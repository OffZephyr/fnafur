package net.zephyr.fnafur.entity.animatronic.block;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum DemoAnimationList implements StringIdentifiable, ColorEnumInterface {
    DEACTIVATED("deactivated","deactivated", false, "loweridle"),
    PLAYER_IDLE("player_idle","playeridle", true, "loweridle"),
    STAGE_IDLE("stage_idle","stageidle", true, "loweridle"),
    PERFORMANCE("performance","performance", true, "loweridle"),
    HAUNTED_IDLE("haunted_idle","hauntedidle", false, "loweridle"),
    FLOOR_LAY("floor_lay","floorlay", false, null),
    FLOOR_SIT("floor_sit","floorsit", false, null),
    OFF_SIT("off_sit","situpper", false, "sit"),
    DEACTIVATED_SIT("deactivated_sit","deactivated", false, "sit"),
    IDLE_SIT("idle_sit","stageidle", true, "sit"),
    PERFORMANCE_SIT("performance_sit","performance", true, "sit"),
    WALK("walk","walk_upper", true, "walk_lower"),
    RUN("run","run_upper", true, "run_lower"),
    NIGHT_WALK("night_walk","nightwalk_upper", true, "nightwalk_lower")
    ;

    final String NAME;
    final String MAIN;
    final String LOWER;
    final boolean BLINKS;
    DemoAnimationList(String name, String animation_main, boolean blinks, String animation_lower){
        this.NAME = name;
        this.MAIN = animation_main;
        this.BLINKS = blinks;
        this.LOWER = animation_lower;
    }
    @Override
    public String asString() {
        return NAME;
    }
    public String getMain() {
        return MAIN;
    }
    public String getLower() {
        return LOWER;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    public boolean blinks(){
        return BLINKS;
    }
}

package net.zephyr.fnafur.entity.animatronic.data;

import net.minecraft.util.StringIdentifiable;

public enum PartType implements StringIdentifiable {
    HEAD("head"),
    LEFT_EAR("left_ear"),
    RIGHT_EAR("right_ear"),
    FACE("face"),
    HEAD_ACCESSORY("head_accessory"),
    JAW("jaw"),
    TORSO("torso"),
    TORSO_ACCESSORY("torso_accessory"),
    PELVIS("pelvis"),
    LEFT_ARM("left_arm"),
    LEFT_HAND("left_hand"),
    LEFT_HAND_ACCESSORY("left_hand_accessory"),
    RIGHT_ARM("right_arm"),
    RIGHT_HAND("right_hand"),
    RIGHT_HAND_ACCESSORY("right_hand_accessory"),
    LEFT_LEG("left_leg"),
    LEFT_FOOT("left_foot"),
    RIGHT_LEG("right_leg"),
    RIGHT_FOOT("right_foot")
    ;
    public final String NAME;
    PartType(String name){
        this.NAME = name;
    }
    @Override
    public String asString() {
        return NAME;
    }
}

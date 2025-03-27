package net.zephyr.fnafur.entity.animatronic.data;

import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public enum PartType implements StringIdentifiable {
    ROOT("root", null),
    PELVIS("pelvis", ROOT),
    TORSO("torso", PELVIS),
    TORSO_ACCESSORY("torso_accessory", TORSO),
    GUITAR("guitar", TORSO),

    HEAD("head", TORSO),
    LEFT_EAR("left_ear", HEAD),
    RIGHT_EAR("right_ear", HEAD),
    LEFT_EYE("eyeleft", HEAD),
    LEFT_EYELID_TOP("lefteyelid_top", LEFT_EYE),
    LEFT_EYELID_BOTTOM("lefteyelid_bottom", LEFT_EYE),
    RIGHT_EYE("eyeright", HEAD),
    RIGHT_EYELID_TOP("righteyelid_top", RIGHT_EYE),
    RIGHT_EYELID_BOTTOM("righteyelid_bottom", RIGHT_EYE),
    HEAD_ACCESSORY("head_accessory", HEAD),
    JAW("jaw", HEAD),
    LEFT_ARM("left_arm", TORSO),
    LEFT_LOWER_ARM("left_lower_arm", TORSO),
    LEFT_HAND("left_hand", LEFT_ARM),
    LEFT_HAND_ACCESSORY("left_hand_accessory", LEFT_HAND),
    RIGHT_ARM("right_arm", TORSO),
    RIGHT_LOWER_ARM("right_lower_arm", TORSO),
    RIGHT_HAND("right_hand", RIGHT_ARM),
    RIGHT_HAND_ACCESSORY("right_hand_accessory", RIGHT_HAND),
    LEFT_LEG("left_leg", ROOT),
    LEFT_LOWER_LEG("left_lower_leg", ROOT),
    LEFT_FOOT("left_foot", LEFT_LEG),
    RIGHT_LEG("right_leg", ROOT),
    RIGHT_LOWER_LEG("right_lower_leg", ROOT),
    RIGHT_FOOT("right_foot", RIGHT_LEG)
    ;
    public final String NAME;
    public final PartType PREVIOUS;
    PartType(String name, @Nullable PartType previousPart){
        this.NAME = name;
        this.PREVIOUS = previousPart;
    }
    @Override
    public String asString() {
        return NAME;
    }

    public static PartType getFromString(String string){
        for(PartType type : PartType.values()){
            if(Objects.equals(type.NAME, string)) return type;
        }
        return null;
    }
}

package net.zephyr.fnafur.entity.animatronic.data;

import net.minecraft.nbt.NbtCompound;

import java.util.*;

public class CpuData {

    public interface CpuDataArgument{
        String getKey();
        String getName();
        CpuDataArgument cycleLeft();
        CpuDataArgument cycleRight();
        CpuDataArgument getFromName(String name);
    }
    public interface CpuDataRangeArgument extends CpuDataArgument{
        int getMin();
        int getMax();
        int getValue();
        void setValue(int val);
    }

    public enum OnReset implements CpuDataArgument {
        WALK("walk"),
        RUN("run"),
        TELEPORT("teleport")
        ;
        final String NAME;

        OnReset(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "on_reset";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static OnReset getDefault() {
            return WALK;
        }
    }
    public enum MovementMode implements CpuDataArgument {
        NONE("none"),
        WALK("walk"),
        RUN("run"),
        TELEPORT("teleport")
        ;
        final String NAME;

        MovementMode(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "movement_mode";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static MovementMode getDefault() {
            return WALK;
        }
    }
    public enum BehaviorWhenSeen implements CpuDataArgument {
        NONE("none"),
        FREEZE_ON_SIGHT("freeze_on_sight"),
        RESET_ON_SIGHT("reset_on_sight"),
        FREEZE_ON_CAMERA("freeze_on_camera"),
        RESET_ON_CAMERA("reset_on_camera")
        ;
        final String NAME;

        BehaviorWhenSeen(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "behavior_when_seen";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static BehaviorWhenSeen getDefault() {
            return FREEZE_ON_CAMERA;
        }
    }
    public enum VentBehavior implements CpuDataArgument {
        NONE("none"),
        CRAWL("crawl"),
        REACH_IN("reach_in")
        ;
        final String NAME;

        VentBehavior(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "vent_behavior";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static VentBehavior getDefault() {
            return REACH_IN;
        }
    }
    public enum LightBehavior implements CpuDataArgument {
        NONE("none"),
        FLICKER("flicker"),
        TURN_OFF("turn_off")
        ;
        final String NAME;

        LightBehavior(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "light_behavior";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static LightBehavior getDefault() {
            return NONE;
        }
    }
    public enum ReactionToLight implements CpuDataArgument {
        NONE("none"),
        STUNNED("stunned"),
        RESET("reset")
        ;
        final String NAME;

        ReactionToLight(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "reaction_to_light";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static ReactionToLight getDefault() {
            return NONE;
        }
    }
    public enum ReactionToShock implements CpuDataArgument {
        NONE("none"),
        STUNNED("stunned"),
        RESET("reset")
        ;
        final String NAME;

        ReactionToShock(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "reaction_to_shock";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static ReactionToShock getDefault() {
            return STUNNED;
        }
    }
    public enum ReactionToDoor implements CpuDataArgument {
        LEAVE("leave"),
        WAIT("wait"),
        RESET("reset"),
        BANG_AND_LEAVE("bang_and_leave"),
        BANG_AND_RESET("bang_and_reset")
        ;
        final String NAME;

        ReactionToDoor(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "reaction_to_door";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static ReactionToDoor getDefault() {
            return BANG_AND_LEAVE;
        }
    }
    public enum ReactionToMask implements CpuDataArgument {
        FOOLED("fooled"),
        RESET("reset"),
        NOT_FOOLED("not_fooled"),
        ;
        final String NAME;

        ReactionToMask(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "reaction_to_mask";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static ReactionToMask getDefault() {
            return FOOLED;
        }
    }
    public enum VisionMode implements CpuDataArgument {
        NORMAL("normal"),
        BLIND("blind"),
        DEAF("deaf"),
        BLIND_AND_DEAF("blind_and_deaf")
        ;
        final String NAME;

        VisionMode(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "vision_mode";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static VisionMode getDefault() {
            return NORMAL;
        }
    }
    public enum AggressionMode implements CpuDataArgument {
        PASSIVE("passive"),
        STALK("stalk"),
        CHASE_WALK("chase_walk"),
        CHASE_RUN("chase_run")
        ;
        final String NAME;

        AggressionMode(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "aggression_mode";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static AggressionMode getDefault() {
            return CHASE_WALK;
        }
    }
    public enum OnSpotTarget implements CpuDataArgument {
        NOTHING("nothing"),
        TAUNT("taunt"),
        SOUND_ALONE("sound_alone")
        ;
        final String NAME;

        OnSpotTarget(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "on_spot_target";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static OnSpotTarget getDefault() {
            return TAUNT;
        }
    }
    public enum OnLoseTarget implements CpuDataArgument {
        NOTHING("nothing"),
        RAGE("rage"),
        SEEK("seek"),
        NEVER_LOSE_TARGET("never_lose_target")
        ;
        final String NAME;

        OnLoseTarget(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "on_lose_target";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static OnLoseTarget getDefault() {
            return SEEK;
        }
    }
    public enum HidingSpots implements CpuDataArgument {
        IGNORE("ignore"),
        CHECK("check")
        ;
        final String NAME;

        HidingSpots(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "hiding_spots";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static HidingSpots getDefault() {
            return CHECK;
        }
    }
    public enum OnContactWithTarget implements CpuDataArgument {
        PASSIVE("passive"),
        MAKE_NOISE("make_noise"),
        JUMPSCARE("jumpscare"),
        KILL("kill"),
        JUMPSCARE_AND_KILL("jumpscare_and_kill")
        ;
        final String NAME;

        OnContactWithTarget(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "on_contact";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static OnContactWithTarget getDefault() {
            return JUMPSCARE_AND_KILL;
        }
    }
    public enum KillStyle implements CpuDataArgument {
        NONE("none"),
        SHREDDY("shreddy"),
        BLOW_UP("blow_up")
        ;
        final String NAME;

        KillStyle(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "kill_style";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static KillStyle getDefault() {
            return NONE;
        }
    }
    public enum GlowingEyesMode implements CpuDataArgument {
        DOTS("dots"),
        BLACK_EYED_DOTS("black_eyed_dots"),
        IRISES("irises"),
        BLACK_EYED_IRISES("black_eyed_irises"),
        FULL_EYES("full_eyes"),
        FULL_EYES_DOTS("full_eyes_dots")
        ;
        final String NAME;

        GlowingEyesMode(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "glowing_eyes_mode";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static GlowingEyesMode getDefault() {
            return DOTS;
        }
    }
    public enum GlowingEyesColor implements CpuDataArgument {

        RED("red"),
        ORANGE("orange"),
        YELLOW("yellow"),
        GREEN("green"),
        BLUE("blue"),
        PURPLE("purple"),
        PINK("pink"),
        WHITE("white"),
        ;
        final String NAME;

        GlowingEyesColor(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "glowing_eyes_color";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static GlowingEyesColor getDefault() {
            return WHITE;
        }
    }
    public enum GlowingEyesTrigger implements CpuDataArgument {
        NEVER("never"),
        CHASING("chasing"),
        FLICKER("flickering"),
        ALWAYS("always")
        ;
        final String NAME;

        GlowingEyesTrigger(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "glowing_eyes_trigger";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static GlowingEyesTrigger getDefault() {
            return CHASING;
        }
    }
    public enum SingingRole implements CpuDataArgument {
        NONE("none"),
        LEAD("lead"),
        BACKUP1("backup1"),
        BACKUP2("backup2"),
        ADDITIONAL("additional"),
        EXTRA1("extra1"),
        EXTRA2("extra2"),
        EXTRA3("extra3"),
        EXTRA4("extra4"),
        EXTRA5("extra5")
        ;
        final String NAME;

        SingingRole(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "singing_role";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static SingingRole getDefault() {
            return LEAD;
        }
    }
    public enum CameraJamming implements CpuDataArgument {
        NONE("none"),
        ON_MOVEMENT("on_movement"),
        ON_SEEN("on_seen"),
        BREAK_AFTER_TIME_SPOTTED("break_after_time_spotted")
        ;
        final String NAME;

        CameraJamming(String name){
            NAME = name;
        }

        @Override
        public String getKey() {
            return "camera_jamming";
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public CpuDataArgument cycleLeft() {
            int id = this.ordinal() - 1;
            id = id < 0 ? values().length - 1 : id;
            return values()[id];
        }

        @Override
        public CpuDataArgument cycleRight() {
            int id = this.ordinal() + 1;
            id = id >= values().length ? 0 : id;
            return values()[id];
        }
        @Override
        public CpuDataArgument getFromName(String name) {
            for(CpuDataArgument arg : values()){
                if(Objects.equals(arg.getName(), name)){
                    return arg;
                }
            }
            return values()[0];
        }

        public static CameraJamming getDefault() {
            return ON_MOVEMENT;
        }
    }

    public static class MovementSpeed implements CpuDataRangeArgument {

        private int value = 0;

        @Override
        public int getMin() {
            return 0;
        }

        @Override
        public int getMax() {
            return 10;
        }

        public static int getDefaultValue() {
            return 4;
        }

        public int getValue(){
            return value;
        }

        public void setValue(int val){
            value = val;
        }

        @Override
        public String getKey() {
            return "movement_speed";
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public CpuDataArgument cycleLeft() {
            value = Math.max(getMin(), value - 1);
            return this;
        }

        @Override
        public CpuDataArgument cycleRight() {

            value = Math.min(getMax(), value + 1);
            return this;
        }

        @Override
        public CpuDataArgument getFromName(String name) {
            return this;
        }

        public static MovementSpeed getDefault() {
            MovementSpeed attribute = new MovementSpeed();
            attribute.setValue(MovementSpeed.getDefaultValue());
            return attribute;
        }
    }
    public static class SightRange implements CpuDataRangeArgument {

        private int value = 0;

        @Override
        public int getMin() {
            return 3;
        }

        @Override
        public int getMax() {
            return 50;
        }

        public static int getDefaultValue() {
            return 20;
        }

        public int getValue(){
            return value;
        }

        public void setValue(int val){
            value = val;
        }

        @Override
        public String getKey() {
            return "sight_range";
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public CpuDataArgument cycleLeft() {
            value = Math.max(getMin(), value - 1);
            return this;
        }

        @Override
        public CpuDataArgument cycleRight() {

            value = Math.min(getMax(), value + 1);
            return this;
        }

        @Override
        public CpuDataArgument getFromName(String name) {
            return this;
        }

        public static SightRange getDefault() {
            SightRange attribute = new SightRange();
            attribute.setValue(SightRange.getDefaultValue());
            return attribute;
        }
    }
    public static class ServoSoundsVolume implements CpuDataRangeArgument {

        private int value = 0;

        @Override
        public int getMin() {
            return 0;
        }

        @Override
        public int getMax() {
            return 10;
        }

        public static int getDefaultValue() {
            return 5;
        }

        public int getValue(){
            return value;
        }

        public void setValue(int val){
            value = val;
        }

        @Override
        public String getKey() {
            return "servo_sounds_volume";
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public CpuDataArgument cycleLeft() {
            value = Math.max(getMin(), value - 1);
            return this;
        }

        @Override
        public CpuDataArgument cycleRight() {

            value = Math.min(getMax(), value + 1);
            return this;
        }

        @Override
        public CpuDataArgument getFromName(String name) {
            return this;
        }

        public static ServoSoundsVolume getDefault() {
            ServoSoundsVolume attribute = new ServoSoundsVolume();
            attribute.setValue(ServoSoundsVolume.getDefaultValue());
            return attribute;
        }
    }
    public static class AmbientSoundsVolume implements CpuDataRangeArgument {

        private int value = 0;

        @Override
        public int getMin() {
            return 0;
        }

        @Override
        public int getMax() {
            return 10;
        }

        public static int getDefaultValue() {
            return 8;
        }

        public int getValue(){
            return value;
        }

        public void setValue(int val){
            value = val;
        }

        @Override
        public String getKey() {
            return "ambient_sounds_volume";
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public CpuDataArgument cycleLeft() {
            value = Math.max(getMin(), value - 1);
            return this;
        }

        @Override
        public CpuDataArgument cycleRight() {

            value = Math.min(getMax(), value + 1);
            return this;
        }

        @Override
        public CpuDataArgument getFromName(String name) {
            return this;
        }

        public static AmbientSoundsVolume getDefault() {
            AmbientSoundsVolume attribute = new AmbientSoundsVolume();
            attribute.setValue(AmbientSoundsVolume.getDefaultValue());
            return attribute;
        }
    }
    public static class AIMovementLevel implements CpuDataRangeArgument {

        private int value = 0;

        @Override
        public int getMin() {
            return 0;
        }

        @Override
        public int getMax() {
            return 20;
        }

        public static int getDefaultValue() {
            return 12;
        }

        public int getValue(){
            return value;
        }

        public void setValue(int val){
            value = val;
        }

        @Override
        public String getKey() {
            return "ai_movement_level";
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public CpuDataArgument cycleLeft() {
            value = Math.max(getMin(), value - 1);
            return this;
        }

        @Override
        public CpuDataArgument cycleRight() {

            value = Math.min(getMax(), value + 1);
            return this;
        }

        @Override
        public CpuDataArgument getFromName(String name) {
            return this;
        }

        public static AIMovementLevel getDefault() {
            AIMovementLevel attribute = new AIMovementLevel();
            attribute.setValue(AIMovementLevel.getDefaultValue());
            return attribute;
        }
    }

    public String Animation = "default";
    public String AmbientSound = "";
    OnReset ON_RESET = OnReset.getDefault();
    MovementMode MOVEMENT_MODE = MovementMode.getDefault();
    BehaviorWhenSeen BEHAVIOR_WHEN_SEEN = BehaviorWhenSeen.getDefault();
    VentBehavior VENT_BEHAVIOR = VentBehavior.getDefault();
    LightBehavior LIGHT_BEHAVIOR = LightBehavior.getDefault();
    ReactionToDoor REACTION_TO_DOOR = ReactionToDoor.getDefault();
    ReactionToLight REACTION_TO_LIGHT = ReactionToLight.getDefault();
    ReactionToShock REACTION_TO_SHOCK = ReactionToShock.getDefault();
    ReactionToMask REACTION_TO_MASK = ReactionToMask.getDefault();
    VisionMode VISION_MODE = VisionMode.getDefault();
    AggressionMode AGGRESSION_MODE = AggressionMode.getDefault();
    OnSpotTarget ON_SPOT_TARGET = OnSpotTarget.getDefault();
    OnLoseTarget ON_LOSE_TARGET = OnLoseTarget.getDefault();
    HidingSpots HIDING_SPOTS = HidingSpots.getDefault();
    OnContactWithTarget ON_CONTACT_WITH_TARGET = OnContactWithTarget.getDefault();
    KillStyle KILL_STYLE = KillStyle.getDefault();
    GlowingEyesMode GLOWING_EYES_MODE = GlowingEyesMode.getDefault();
    GlowingEyesColor GLOWING_EYES_COLOR = GlowingEyesColor.getDefault();
    GlowingEyesTrigger GLOWING_EYES_TRIGGER = GlowingEyesTrigger.getDefault();
    SingingRole SINGING_ROLE = SingingRole.getDefault();
    CameraJamming CAMERA_JAMMING = CameraJamming.getDefault();
    MovementSpeed MOVEMENT_SPEED = MovementSpeed.getDefault();
    SightRange SIGHT_RANGE = SightRange.getDefault();
    ServoSoundsVolume SERVO_SOUND_VOLUME = ServoSoundsVolume.getDefault();
    AmbientSoundsVolume AMBIENT_SOUND_VOLUME = AmbientSoundsVolume.getDefault();
    AIMovementLevel AI_MOVEMENT_LEVEL = AIMovementLevel.getDefault();

    List<? extends CpuDataArgument> DefaultList = List.of(
            // BASE
            SINGING_ROLE,
            ON_RESET,
            MOVEMENT_MODE,
            BEHAVIOR_WHEN_SEEN,
            VENT_BEHAVIOR,
            LIGHT_BEHAVIOR,
            GLOWING_EYES_TRIGGER,
            GLOWING_EYES_MODE,
            GLOWING_EYES_COLOR,

            // INTERACTIONS
            REACTION_TO_LIGHT,
            REACTION_TO_DOOR,
            REACTION_TO_SHOCK,
            REACTION_TO_MASK,
            CAMERA_JAMMING,
            HIDING_SPOTS,

            // AGGRESSIVITY
            AGGRESSION_MODE,
            ON_SPOT_TARGET,
            ON_LOSE_TARGET,
            ON_CONTACT_WITH_TARGET,
            KILL_STYLE,
            VISION_MODE,

            // STATS
            MOVEMENT_SPEED,
            SIGHT_RANGE,
            AMBIENT_SOUND_VOLUME,
            SERVO_SOUND_VOLUME,
            AI_MOVEMENT_LEVEL
    );

    public List<String> KeyList = new ArrayList<>();

    public Map<String, CpuDataArgument> DATA_LIST = new HashMap<>();

    public CpuData(){
        for(CpuDataArgument arg : DefaultList){
            KeyList.add(arg.getKey());
            DATA_LIST.put(arg.getKey(), arg);
        }
    }

    public CpuData with(CpuDataArgument argument){
        DATA_LIST.put(argument.getKey(), argument);
        return this;
    }

    public CpuData with(CpuDataRangeArgument argument, int value){
        argument.setValue(value);
        DATA_LIST.put(argument.getKey(), argument);
        return this;
    }

    public NbtCompound toNbt(){
        NbtCompound nbt = new NbtCompound();
        for(CpuDataArgument argument : DefaultList){
            CpuDataArgument arg = DATA_LIST.get(argument.getKey());
            if(arg != null){
                if(arg instanceof CpuDataRangeArgument range){
                    nbt.putInt(range.getKey(), range.getValue());
                }
                else{
                    nbt.putString(arg.getKey(), arg.getName());
                }
            }
        }
        nbt.putString("animation", Animation);
        nbt.putString("ambient_sound", AmbientSound);
        return nbt;
    }

    public static CpuData fromNbt(NbtCompound nbt){

        CpuData data = new CpuData();

        for(CpuDataArgument argument : data.DefaultList){
            if(argument instanceof CpuDataRangeArgument range){
                int value = nbt.getInt(range.getKey(), range.getValue());
                range.setValue(value);
                data.DATA_LIST.put(range.getKey(), range);
            }
            else{
                String name = nbt.getString(argument.getKey(), argument.getName());
                data.DATA_LIST.put(argument.getKey(), argument.getFromName(name));
            }
        }
        data.Animation = nbt.getString("animation", data.Animation);
        data.AmbientSound = nbt.getString("ambient_sound", data.AmbientSound);
        return data;
    }

}

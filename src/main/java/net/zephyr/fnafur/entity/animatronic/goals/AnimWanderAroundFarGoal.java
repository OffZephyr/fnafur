package net.zephyr.fnafur.entity.animatronic.goals;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.entity.animatronic.data.CpuData;
import net.zephyr.fnafur.entity.animatronic.voice.EntityVoiceSoundInstance;

import java.util.EnumSet;

public class AnimWanderAroundFarGoal extends Goal {

    public static final int DEFAULT_CHANCE = 120;
    protected final AnimatronicEntity mob;
    protected double targetX;
    protected double targetY;
    protected double targetZ;
    protected int chance;
    protected boolean ignoringChance;
    private final boolean canDespawn;

    public static final float CHANCE = 0.001F;

    public AnimWanderAroundFarGoal(AnimatronicEntity mob) {
        this(mob, 120);
    }

    public AnimWanderAroundFarGoal(AnimatronicEntity mob, int chance) {
        this(mob, chance, true);
    }

    public AnimWanderAroundFarGoal(AnimatronicEntity entity, int chance, boolean canDespawn) {
        this.mob = entity;
        this.chance = chance;
        this.canDespawn = canDespawn;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if(mob.isRetreating || mob.lastHeardPosition != null || mob.getTarget() != null) return false;

        if (this.mob.hasControllingPassenger()) {
            return false;
        } else {
            if (!this.ignoringChance) {
                if (this.canDespawn && this.mob.getDespawnCounter() >= 100) {
                    return false;
                }

                if (this.mob.getRandom().nextInt(toGoalTicks(this.chance)) != 0) {
                    return false;
                }
            }

            Vec3d vec3d = this.getWanderTarget();
            if (vec3d == null) {
                return false;
            } else {
                this.targetX = vec3d.x;
                this.targetY = vec3d.y;
                this.targetZ = vec3d.z;
                this.ignoringChance = false;
                return true;
            }
        }
    }


    protected Vec3d getWanderTarget() {
        if (this.mob.isTouchingWater()) {
            Vec3d vec3d = FuzzyTargeting.find(this.mob, 15, 7);
            return vec3d == null ? NoPenaltyTargeting.find(this.mob, 10, 7) : vec3d;
        } else {
            return FuzzyTargeting.find(this.mob, Random.create().nextBetween(10, 20), 7);
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle() && !this.mob.hasControllingPassenger() && !mob.isRetreating;
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingTo(this.targetX, this.targetY, this.targetZ, getSpeed());
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        String name = this.mob.getAnimatronicAmbientSoundName();
        this.mob.playVoiceSound(name, this.mob.getAnimatronicAmbientSound(name), this.mob.ambientSoundVolume());

        super.stop();
    }

    public void ignoreChanceOnce() {
        this.ignoringChance = true;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }


    float getSpeed(){

        int speed = mob.runningSpeed();
        int maxSpeed = CpuData.MovementSpeed.getDefaultValue() + CpuData.RunSpeed.getDefaultValue();

        return MathHelper.lerp(((float) speed / maxSpeed), 0f, 2.5f)/1.5f;
    }
}

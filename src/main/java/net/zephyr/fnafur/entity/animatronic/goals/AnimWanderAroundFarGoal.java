package net.zephyr.fnafur.entity.animatronic.goals;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.RandomSource;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.entity.animatronic.data.CpuData;
import net.zephyr.fnafur.entity.animatronic.voice.EntityVoiceSoundInstance;

import java.util.EnumSet;
import java.util.List;

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
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if(!(mob.getWanderBehavior() == CpuData.WanderBehavior.WANDER || mob.getWanderBehavior() == CpuData.WanderBehavior.WANDER_TOWARDS_PLAYERS)  || mob.isRetreating || mob.lastHeardPosition != null || mob.getTarget() != null) return false;

        if (this.mob.hasControllingPassenger()) {
            return false;
        } else {
            if (!this.ignoringChance) {
                if (this.canDespawn && this.mob.getNoActionTime() >= 100) {
                    return false;
                }

                if (this.mob.getRandom().nextInt(reducedTickDelay(this.chance)) != 0) {
                    return false;
                }
            }

            Vec3 vec3d = this.getWanderTarget();
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


    protected Vec3 getWanderTarget() {
        List<Player> players = this.mob.level().getEntitiesOfClass(Player.class, this.mob.getBoundingBox().inflate(10), (entity) -> (entity instanceof Player && !entity.isSpectator() && !entity.isCreative()));

        if(mob.getWanderBehavior() == CpuData.WanderBehavior.WANDER || RandomSource.create().nextInt(0, 100) < 25 || players.isEmpty()) {
            if (this.mob.isInWater()) {
                Vec3 vec3d = LandRandomPos.getPos(this.mob, 15, 7);
                return vec3d == null ? DefaultRandomPos.getPos(this.mob, 10, 7) : vec3d;
            } else {
                return LandRandomPos.getPos(this.mob, RandomSource.create().nextIntBetweenInclusive(10, 20), 7);
            }
        }
        else{

            return LandRandomPos.getPos(this.mob, RandomSource.create().nextIntBetweenInclusive(10, 20), 7);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return (mob.getWanderBehavior() == CpuData.WanderBehavior.WANDER || mob.getWanderBehavior() == CpuData.WanderBehavior.WANDER_TOWARDS_PLAYERS) && !this.mob.getNavigation().isDone() && !this.mob.hasControllingPassenger() && !mob.isRetreating;
    }

    @Override
    public void start() {
        if(this.mob.getMovementMode() == CpuData.MovementMode.TELEPORT){
            this.mob.setPos(this.targetX, this.targetY, this.targetZ);
            this.mob.setYRot(RandomSource.create().nextFloat() * 360f);
        }
        else{
            this.mob.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, getSpeed());
        }
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

        return Mth.lerp(((float) speed / maxSpeed), 0f, 2.5f)/1.5f;
    }
}

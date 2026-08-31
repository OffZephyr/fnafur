package net.zephyr.fnafur.entity.animatronic.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.entity.animatronic.data.CpuData;

import java.util.EnumSet;

public class AnimMeleeAttackGoal extends Goal {

    protected final AnimatronicEntity mob;
    private final boolean pauseWhenMobIdle;
    private Path path;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int updateCountdownTicks;
    private int cooldown;
    private final int attackIntervalTicks = 20;
    private long lastUpdateTime;
    private static final long MAX_ATTACK_TIME = 20L;

    public AnimMeleeAttackGoal(AnimatronicEntity mob, boolean pauseWhenMobIdle) {
        this.mob = mob;
        this.pauseWhenMobIdle = pauseWhenMobIdle;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if(!mob.isAggressive()) return false;
        long l = this.mob.level().getGameTime();
        if (l - this.lastUpdateTime < 20L) {
            return false;
        } else {
            this.lastUpdateTime = l;
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            } else if (!livingEntity.isAlive()) {
                return false;
            } else {
                this.path = getPath(livingEntity, 0);
                return this.path != null ? true : this.mob.isWithinMeleeAttackRange(livingEntity);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if(!mob.isAggressive()) return false;

        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null) {
            return false;
        } else if (!livingEntity.isAlive()) {
            return false;
        } else if (!this.pauseWhenMobIdle) {
            return !this.mob.getNavigation().isDone();
        } else {
            return !this.mob.isWithinHome(livingEntity.blockPosition())
                    ? false
                    : !(livingEntity instanceof Player playerEntity && (playerEntity.isSpectator() || playerEntity.isCreative()));
        }
    }

    @Override
    public void start() {
        if(this.mob.canSee() && !mob.isFrozen){
            this.mob.getNavigation().moveTo(this.path, getSpeed());
        }
        this.mob.setAggressive(true);
        this.updateCountdownTicks = 0;
        this.cooldown = 0;
    }

    @Override
    public void stop() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
            this.mob.setTarget(null);
        }

        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity != null) {
            if(this.mob.canSee()) { //  && !this.mob.isFrozen
                if(this.mob.lastSeenPosition == null || this.mob.getSensing().hasLineOfSight(livingEntity)){
                    this.mob.getLookControl().setLookAt(livingEntity, 45.0F, 30.0F);
                }
                else{
                    this.mob.getLookControl().setLookAt(this.mob.lastSeenPosition.getX(), this.mob.getEyeY(), this.mob.lastSeenPosition.getZ(), 30.0F, 30.0F);

                }
            }
            this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
            if ((this.pauseWhenMobIdle || this.mob.getSensing().hasLineOfSight(livingEntity))
                    && this.updateCountdownTicks <= 0
                    && (
                    this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0
                            || livingEntity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0
                            || this.mob.getRandom().nextFloat() < 0.05F
            )) {
                this.targetX = livingEntity.getX();
                this.targetY = livingEntity.getY();
                this.targetZ = livingEntity.getZ();
                this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
                double d = this.mob.distanceToSqr(livingEntity);
                if (d > 1024.0) {
                    this.updateCountdownTicks += 10;
                } else if (d > 256.0) {
                    this.updateCountdownTicks += 5;
                }

                if (!mob.isFrozen && !(this.mob.canSee() && this.mob.getNavigation().moveTo(getPath(livingEntity, 0), getSpeed()))) {
                    this.updateCountdownTicks += 15;
                }

                this.updateCountdownTicks = this.adjustedTickDelay(this.updateCountdownTicks);
            }

            this.cooldown = Math.max(this.cooldown - 1, 0);
            this.attack(livingEntity);
        }
    }

    protected void attack(LivingEntity target) {
        if (this.canAttack(target)) {
            this.resetCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(getServerLevel(this.mob), target);
        }
    }

    protected void resetCooldown() {
        this.cooldown = this.adjustedTickDelay(20);
    }

    protected boolean isCooledDown() {
        return this.cooldown <= 0;
    }

    protected boolean canAttack(LivingEntity target) {
        return this.isCooledDown() && this.mob.isWithinMeleeAttackRange(target) && this.mob.getSensing().hasLineOfSight(target);
    }

    protected int getCooldown() {
        return this.cooldown;
    }

    protected int getMaxCooldown() {
        return this.adjustedTickDelay(20);
    }

    Path getPath(LivingEntity livingEntity, int distance){
        Path path = this.mob.getNavigation().createPath(livingEntity, 0);
        if(this.mob.lastSeenPosition != null && !this.mob.getSensing().hasLineOfSight(livingEntity)){
            path = this.mob.getNavigation().createPath(this.mob.lastSeenPosition, distance);
        }
        return path;
    }

    float getSpeed(){

        int speed = mob.runningSpeed();
        int maxSpeed = CpuData.MovementSpeed.getDefaultValue() + CpuData.RunSpeed.getDefaultValue();

        return Mth.lerp(((float) speed / maxSpeed), 0f, 2.5f)/1.5f;
    }
}

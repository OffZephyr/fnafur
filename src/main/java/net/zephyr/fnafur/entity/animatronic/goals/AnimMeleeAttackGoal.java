package net.zephyr.fnafur.entity.animatronic.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
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
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if(!mob.isAggressive()) return false;
        long l = this.mob.getEntityWorld().getTime();
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
                return this.path != null ? true : this.mob.isInAttackRange(livingEntity);
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        if(!mob.isAggressive()) return false;

        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null) {
            return false;
        } else if (!livingEntity.isAlive()) {
            return false;
        } else if (!this.pauseWhenMobIdle) {
            return !this.mob.getNavigation().isIdle();
        } else {
            return !this.mob.isInPositionTargetRange(livingEntity.getBlockPos())
                    ? false
                    : !(livingEntity instanceof PlayerEntity playerEntity && (playerEntity.isSpectator() || playerEntity.isCreative()));
        }
    }

    @Override
    public void start() {
        if(this.mob.canSee()){
            this.mob.getNavigation().startMovingAlong(this.path, getSpeed());
        }
        this.mob.setAttacking(true);
        this.updateCountdownTicks = 0;
        this.cooldown = 0;
    }

    @Override
    public void stop() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
            this.mob.setTarget(null);
        }

        this.mob.setAttacking(false);
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity != null) {
            if(this.mob.canSee()) {
                if(this.mob.lastSeenPosition != null && !this.mob.getVisibilityCache().canSee(livingEntity)){

                    this.mob.getLookControl().lookAt(this.mob.lastSeenPosition.getX(), this.mob.getEyeY(), this.mob.lastSeenPosition.getZ(), 30.0F, 30.0F);
                }
                else{
                    this.mob.getLookControl().lookAt(livingEntity, 30.0F, 30.0F);
                }
                this.mob.getLookControl().lookAt(livingEntity, 30.0F, 30.0F);
            }
            this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
            if ((this.pauseWhenMobIdle || this.mob.getVisibilityCache().canSee(livingEntity))
                    && this.updateCountdownTicks <= 0
                    && (
                    this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0
                            || livingEntity.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0
                            || this.mob.getRandom().nextFloat() < 0.05F
            )) {
                this.targetX = livingEntity.getX();
                this.targetY = livingEntity.getY();
                this.targetZ = livingEntity.getZ();
                this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
                double d = this.mob.squaredDistanceTo(livingEntity);
                if (d > 1024.0) {
                    this.updateCountdownTicks += 10;
                } else if (d > 256.0) {
                    this.updateCountdownTicks += 5;
                }

                if (!(this.mob.canSee() && this.mob.getNavigation().startMovingAlong(getPath(livingEntity, 0), getSpeed()))) {
                    this.updateCountdownTicks += 15;
                }

                this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
            }

            this.cooldown = Math.max(this.cooldown - 1, 0);
            this.attack(livingEntity);
        }
    }

    protected void attack(LivingEntity target) {
        if (this.canAttack(target)) {
            this.resetCooldown();
            this.mob.swingHand(Hand.MAIN_HAND);
            this.mob.tryAttack(getServerWorld(this.mob), target);
        }
    }

    protected void resetCooldown() {
        this.cooldown = this.getTickCount(20);
    }

    protected boolean isCooledDown() {
        return this.cooldown <= 0;
    }

    protected boolean canAttack(LivingEntity target) {
        return this.isCooledDown() && this.mob.isInAttackRange(target) && this.mob.getVisibilityCache().canSee(target);
    }

    protected int getCooldown() {
        return this.cooldown;
    }

    protected int getMaxCooldown() {
        return this.getTickCount(20);
    }

    Path getPath(LivingEntity livingEntity, int distance){
        Path path = this.mob.getNavigation().findPathTo(livingEntity, 0);
        if(this.mob.lastSeenPosition != null && !this.mob.getVisibilityCache().canSee(livingEntity)){
            path = this.mob.getNavigation().findPathTo(this.mob.lastSeenPosition, distance);
        }
        return path;
    }

    float getSpeed(){

        int speed = mob.runningSpeed();
        int maxSpeed = CpuData.MovementSpeed.getDefaultValue() + CpuData.RunSpeed.getDefaultValue();

        return MathHelper.lerp(((float) speed / maxSpeed), 0f, 2.5f)/1.5f;
    }
}

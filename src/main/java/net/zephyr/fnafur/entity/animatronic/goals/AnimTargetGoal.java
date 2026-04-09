package net.zephyr.fnafur.entity.animatronic.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Team;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;
import java.util.Map;

public class AnimTargetGoal<T extends LivingEntity> extends TargetGoal {
    private static final int DEFAULT_RECIPROCAL_CHANCE = 10;
    protected final Class<T> targetClass;
    protected final int reciprocalChance;
    @Nullable
    protected LivingEntity targetEntity;
    protected TargetingConditions targetPredicate;

    private int timeWithoutVisibility;

    final AnimatronicEntity a;

    BlockPos lastSeenPosition;

    public AnimTargetGoal(AnimatronicEntity mob, Class<T> targetClass, boolean checkVisibility) {
        this(mob, targetClass, 10, checkVisibility, false, null);
    }

    public AnimTargetGoal(AnimatronicEntity mob, Class<T> targetClass, boolean checkVisibility, TargetingConditions.Selector predicate) {
        this(mob, targetClass, 10, checkVisibility, false, predicate);
    }

    public AnimTargetGoal(AnimatronicEntity mob, Class<T> targetClass, boolean checkVisibility, boolean checkCanNavigate) {
        this(mob, targetClass, 10, checkVisibility, checkCanNavigate, null);
    }

    public AnimTargetGoal(
            AnimatronicEntity mob,
            Class<T> targetClass,
            int reciprocalChance,
            boolean checkVisibility,
            boolean checkCanNavigate,
            TargetingConditions.Selector targetPredicate
    ) {
        super(mob, checkVisibility, checkCanNavigate);
        this.a = mob;
        this.targetClass = targetClass;
        this.reciprocalChance = reducedTickDelay(reciprocalChance);
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetPredicate = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(targetPredicate);
    }

    @Override
    public boolean canUse() {
        if(!a.isAggressive() || a.isRetreating) return false;
        if (this.reciprocalChance > 0 && this.mob.getRandom().nextInt(this.reciprocalChance) != 0) {
            return false;
        } else {
            this.findClosestTarget();
            if(this.targetEntity != null){
                Vec3 difference = this.targetEntity.position().add(mob.position().scale(-1));
                float angle = mob.getYHeadRot() - difference.rotation().y;
                while(angle < 0) angle += 360;
                angle %= 360;
                if(difference.length() > a.sightRange()) return false;
                if(angle <= a.sightConeAngle() || angle >= 360 - a.sightConeAngle()){
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    @Override
    public void stop() {
        this.a.lastSeenPosition = null;
        super.stop();
    }

    protected AABB getSearchBox(double distance) {
        return this.mob.getBoundingBox().inflate(distance, distance, distance);
    }

    protected void findClosestTarget() {
        ServerLevel serverWorld = getServerLevel(this.mob);
        if (this.targetClass != Player.class && this.targetClass != ServerPlayer.class) {
            this.targetEntity = serverWorld.getNearestEntity(
                    this.mob.level().getEntitiesOfClass(this.targetClass, this.getSearchBox(this.getFollowDistance()), livingEntity -> true),
                    this.getAndUpdateTargetPredicate(),
                    this.mob,
                    this.mob.getX(),
                    this.mob.getEyeY(),
                    this.mob.getZ()
            );
        } else {
            this.targetEntity = serverWorld.getNearestPlayer(this.getAndUpdateTargetPredicate(), this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }

    public void setTargetEntity(@Nullable LivingEntity targetEntity) {
        this.targetEntity = targetEntity;
    }

    private TargetingConditions getAndUpdateTargetPredicate() {
        return this.targetPredicate.range(this.getFollowDistance());
    }

    @Override
    public boolean canContinueToUse() {
        if(!a.isAggressive() || a.isRetreating) return false;
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null) {
            livingEntity = this.targetMob;
        }

        if (livingEntity == null) {
            return false;
        } else if (!this.mob.canAttack(livingEntity)) {
            return false;
        } else {
            Team abstractTeam = this.mob.getTeam();
            Team abstractTeam2 = livingEntity.getTeam();
            if (abstractTeam != null && abstractTeam2 == abstractTeam) {
                return false;
            } else {
                double d = this.getFollowDistance();
                if (this.mob.distanceToSqr(livingEntity) > d * d) {
                    return false;
                } else {
                    if (this.mustSee) {
                        if (this.mob.getSensing().hasLineOfSight(livingEntity)) {
                            this.a.lastSeenPosition = livingEntity.blockPosition();
                            this.timeWithoutVisibility = 0;
                        }
                        else if(a.lastSeenPosition != null && this.mob.blockPosition().closerThan(this.a.lastSeenPosition, 0.5f)) {
                            unseenMemoryTicks = 20;
                            a.lastSeenPosition = null;
                        }
                        else if(a.lastSeenPosition == null){
                            if (++this.timeWithoutVisibility > reducedTickDelay(this.unseenMemoryTicks)){
                                unseenMemoryTicks = 5;
                                return false;
                            }
                        }
                    }

                    this.mob.setTarget(livingEntity);
                    return true;
                }
            }
        }
    }
}

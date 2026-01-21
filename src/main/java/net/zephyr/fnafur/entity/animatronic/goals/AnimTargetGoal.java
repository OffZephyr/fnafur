package net.zephyr.fnafur.entity.animatronic.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;
import java.util.Map;

public class AnimTargetGoal<T extends LivingEntity> extends TrackTargetGoal {
    private static final int DEFAULT_RECIPROCAL_CHANCE = 10;
    protected final Class<T> targetClass;
    protected final int reciprocalChance;
    @Nullable
    protected LivingEntity targetEntity;
    protected TargetPredicate targetPredicate;

    private int timeWithoutVisibility;

    final AnimatronicEntity a;

    BlockPos lastSeenPosition;

    public AnimTargetGoal(AnimatronicEntity mob, Class<T> targetClass, boolean checkVisibility) {
        this(mob, targetClass, 10, checkVisibility, false, null);
    }

    public AnimTargetGoal(AnimatronicEntity mob, Class<T> targetClass, boolean checkVisibility, TargetPredicate.EntityPredicate predicate) {
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
            TargetPredicate.EntityPredicate targetPredicate
    ) {
        super(mob, checkVisibility, checkCanNavigate);
        this.a = mob;
        this.targetClass = targetClass;
        this.reciprocalChance = toGoalTicks(reciprocalChance);
        this.setControls(EnumSet.of(Goal.Control.TARGET));
        this.targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(this.getFollowRange()).setPredicate(targetPredicate);
    }

    @Override
    public boolean canStart() {
        if(!a.isAggressive()) return false;
        if (this.reciprocalChance > 0 && this.mob.getRandom().nextInt(this.reciprocalChance) != 0) {
            return false;
        } else {
            this.findClosestTarget();
            return this.targetEntity != null;
        }
    }

    @Override
    public void stop() {
        this.a.lastSeenPosition = null;
        super.stop();
    }

    protected Box getSearchBox(double distance) {
        return this.mob.getBoundingBox().expand(distance, distance, distance);
    }

    protected void findClosestTarget() {
        ServerWorld serverWorld = getServerWorld(this.mob);
        if (this.targetClass != PlayerEntity.class && this.targetClass != ServerPlayerEntity.class) {
            this.targetEntity = serverWorld.getClosestEntity(
                    this.mob.getEntityWorld().getEntitiesByClass(this.targetClass, this.getSearchBox(this.getFollowRange()), livingEntity -> true),
                    this.getAndUpdateTargetPredicate(),
                    this.mob,
                    this.mob.getX(),
                    this.mob.getEyeY(),
                    this.mob.getZ()
            );
        } else {
            this.targetEntity = serverWorld.getClosestPlayer(this.getAndUpdateTargetPredicate(), this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
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

    private TargetPredicate getAndUpdateTargetPredicate() {
        return this.targetPredicate.setBaseMaxDistance(this.getFollowRange());
    }

    @Override
    public boolean shouldContinue() {
        if(!a.isAggressive()) return false;
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null) {
            livingEntity = this.target;
        }

        if (livingEntity == null) {
            return false;
        } else if (!this.mob.canTarget(livingEntity)) {
            return false;
        } else {
            AbstractTeam abstractTeam = this.mob.getScoreboardTeam();
            AbstractTeam abstractTeam2 = livingEntity.getScoreboardTeam();
            if (abstractTeam != null && abstractTeam2 == abstractTeam) {
                return false;
            } else {
                double d = this.getFollowRange();
                if (this.mob.squaredDistanceTo(livingEntity) > d * d) {
                    return false;
                } else {
                    if (this.checkVisibility) {
                        if (this.mob.getVisibilityCache().canSee(livingEntity)) {
                            this.a.lastSeenPosition = livingEntity.getBlockPos();
                            this.timeWithoutVisibility = 0;
                        }
                        else if(a.lastSeenPosition != null && this.mob.getBlockPos().isWithinDistance(this.a.lastSeenPosition, 0.5f)) {
                            maxTimeWithoutVisibility = 20;
                            a.lastSeenPosition = null;
                        }
                        else if(a.lastSeenPosition == null){
                            if (++this.timeWithoutVisibility > toGoalTicks(this.maxTimeWithoutVisibility)){
                                maxTimeWithoutVisibility = 5;
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

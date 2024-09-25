package net.zephyr.fnafur.entity.goals;

import net.minecraft.entity.ai.FuzzyPositions;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ShouldWanderGoal extends WanderAroundGoal {
    public static final float CHANCE = 0.001f;
    protected final float probability;

    public ShouldWanderGoal(PathAwareEntity pathAwareEntity, double d) {
        this(pathAwareEntity, d, 0.001f);
    }

    public ShouldWanderGoal(PathAwareEntity mob, double speed, float probability) {
        super(mob, speed);
        this.probability = probability;
    }

    @Override
    @Nullable
    protected Vec3d getWanderTarget() {
        if (this.mob.isInsideWaterOrBubbleColumn()) {
            Vec3d vec3d = find(this.mob, 15, 7);
            return vec3d == null ? super.getWanderTarget() : vec3d;
        }
        if (this.mob.getRandom().nextFloat() >= this.probability) {
            return find(this.mob, 10, 7);
        }
        return super.getWanderTarget();
    }

    @Override
    public boolean canStart() {
        if(mob instanceof DefaultEntity entity) {
            boolean bl = Objects.equals(entity.getBehavior(), "moving");
            return super.canStart() && bl;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        if(mob instanceof DefaultEntity entity) {
            boolean bl = Objects.equals(entity.getBehavior(), "moving");
            return super.shouldContinue() && bl;
        }
        return false;
    }

    @Nullable
    public static Vec3d find(PathAwareEntity entity, int horizontalRange, int verticalRange) {
        boolean bl = NavigationConditions.isPositionTargetInRange(entity, horizontalRange);
        return FuzzyPositions.guessBestPathTarget(entity, () -> {
            BlockPos blockPos = FuzzyPositions.localFuzz(entity.getRandom(), horizontalRange, verticalRange);
            return tryMake(entity, horizontalRange, bl, blockPos);
        });
    }


    @Nullable
    private static BlockPos tryMake(PathAwareEntity entity, int horizontalRange, boolean posTargetInRange, BlockPos fuzz) {
        BlockPos blockPos = FuzzyPositions.towardTarget(entity, horizontalRange, entity.getRandom(), fuzz);
        if (isHeightInvalid(blockPos, entity) || NavigationConditions.isPositionTargetOutOfWalkRange(posTargetInRange, entity, blockPos) || NavigationConditions.isInvalidPosition(entity.getNavigation(), blockPos) || NavigationConditions.hasPathfindingPenalty(entity, blockPos)) {
            return null;
        }
        return blockPos;
    }

    public static boolean isHeightInvalid(BlockPos pos, PathAwareEntity entity) {
        return pos.getY() < entity.getWorld().getBottomY();
    }
}

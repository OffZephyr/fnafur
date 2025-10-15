package net.zephyr.fnafur.entity.animatronic.goals;

import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public class AnimWanderAroundFarGoal extends WanderAroundFarGoal {
    public AnimWanderAroundFarGoal(AnimatronicEntity pathAwareEntity, double d) {
        super(pathAwareEntity, d);
    }
}

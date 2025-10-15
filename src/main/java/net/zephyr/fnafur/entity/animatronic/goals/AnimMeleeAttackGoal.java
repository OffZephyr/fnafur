package net.zephyr.fnafur.entity.animatronic.goals;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public class AnimMeleeAttackGoal extends MeleeAttackGoal {
    public AnimMeleeAttackGoal(AnimatronicEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
    }
}

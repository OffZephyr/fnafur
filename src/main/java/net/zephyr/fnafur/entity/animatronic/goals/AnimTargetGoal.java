package net.zephyr.fnafur.entity.animatronic.goals;

import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public class AnimTargetGoal extends ActiveTargetGoal {
    public AnimTargetGoal(AnimatronicEntity mob, Class targetClass, boolean checkVisibility, boolean checkCanNavigate) {
        super(mob, targetClass, checkVisibility, checkCanNavigate);
    }
}

package net.zephyr.fnafur.entity.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import net.zephyr.fnafur.entity.base.DefaultEntity;

public class ShouldLookAtEntityGoal extends LookAtEntityGoal {
    public ShouldLookAtEntityGoal(MobEntity mob, Class<? extends LivingEntity> targetType, float range) {
        super(mob, targetType, range);
    }

    @Override
    public boolean canStart() {
        if(mob instanceof DefaultEntity entity) {
            boolean bl = entity.boolData(entity.getBehavior(), "look_nearby_entities", entity);
            return super.canStart() && bl && !entity.isCrawling();
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        if(mob instanceof DefaultEntity entity) {
            boolean bl = entity.boolData(entity.getBehavior(), "look_nearby_entities", entity);
            return super.shouldContinue() && bl && !entity.isCrawling();
        }
        return false;
    }
}

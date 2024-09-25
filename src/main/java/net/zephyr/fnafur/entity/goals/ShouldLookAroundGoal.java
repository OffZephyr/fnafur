package net.zephyr.fnafur.entity.goals;

import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.mob.MobEntity;
import net.zephyr.fnafur.entity.base.DefaultEntity;

public class ShouldLookAroundGoal extends LookAroundGoal {
    private final MobEntity mob;

    public ShouldLookAroundGoal(MobEntity mob) {
        super(mob);
        this.mob = mob;
    }


    @Override
    public boolean canStart() {
        if(mob instanceof DefaultEntity entity) {
            boolean bl = entity.boolData(entity.getBehavior(), "look_around", entity);
            return super.canStart() && bl && !entity.isCrawling();
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        if(mob instanceof DefaultEntity entity) {
            boolean bl = entity.boolData(entity.getBehavior(), "look_around", entity);
            return super.shouldContinue() && bl && !entity.isCrawling();
        }
        return false;
    }
}

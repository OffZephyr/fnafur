package net.zephyr.fnafur.entity.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.init.item_init.ItemInit;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ShouldActiveTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
    protected final Class<T> targetClass;
    protected final int reciprocalChance;
    public ShouldActiveTargetGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility, boolean checkCanNavigate) {
        super(mob, targetClass, checkVisibility, checkCanNavigate);
        this.targetClass = targetClass;
        this.reciprocalChance = ActiveTargetGoal.toGoalTicks(10);
        this.setControls(EnumSet.of(Goal.Control.TARGET));
        this.targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(this.getFollowRange()).setPredicate(null);
    }


    @Override
    public boolean canStart() {
        if(mob instanceof DefaultEntity entity) {
            boolean bl = entity.boolData(entity.getBehavior(), "aggressive", entity);

            boolean bl2 = true;
            if(targetEntity instanceof PlayerEntity player) {
                ItemStack stack = player.getInventory().armor.get(2);
                if(stack.isOf(ItemInit.ILLUSIONDISC)){
                    bl2 = false;
                }
            }

            return super.canStart() && bl && bl2;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        if(mob instanceof DefaultEntity entity) {
            boolean bl = entity.boolData(entity.getBehavior(), "aggressive", entity);

            boolean bl2 = true;
            if(targetEntity instanceof PlayerEntity player) {
                ItemStack stack = player.getInventory().armor.get(2);
                if(stack.isOf(ItemInit.ILLUSIONDISC)){
                    bl2 = false;
                }
            }

            return super.shouldContinue() && bl && bl2;
        }
        return false;
    }

    protected Box getSearchBox(double distance) {
        return this.mob.getBoundingBox().expand(distance, distance, distance);
    }

    protected void findClosestTarget() {
        ServerWorld serverWorld = getServerWorld(this.mob);
        if (this.targetClass != PlayerEntity.class && this.targetClass != ServerPlayerEntity.class) {
            this.targetEntity = serverWorld.getClosestEntity(
                    this.mob.getWorld().getEntitiesByClass(this.targetClass, this.getSearchBox(this.getFollowRange()), livingEntity -> true),
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

    private TargetPredicate getAndUpdateTargetPredicate() {
        return this.targetPredicate.setBaseMaxDistance(this.getFollowRange());
    }
    @Override
    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }

    @Override
    protected boolean canTrack(@Nullable LivingEntity target, TargetPredicate targetPredicate) {
        return true;
    }
}

package net.zephyr.fnafur.entity.classic.cl_chica;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.entity.goals.ShouldActiveTargetGoal;
import net.zephyr.fnafur.entity.goals.ShouldLookAroundGoal;
import net.zephyr.fnafur.entity.goals.ShouldLookAtEntityGoal;
import net.zephyr.fnafur.entity.goals.ShouldWanderGoal;
import net.zephyr.fnafur.init.ScreensInit;
import net.zephyr.fnafur.init.SoundsInit;

import java.util.ArrayList;
import java.util.List;

public class cl_chica extends DefaultEntity {

    private final EntitySkin DEFAULT =
            new EntitySkin("entity.fnafur.cl_chica.default");

    public cl_chica(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    @Override
    public SoundEvent walkSound() {
        return SoundsInit.FNAF1_FOOTSTEPS;
    }
    public SoundEvent hurtSound() {
        return SoundsInit.ANIMATRONIC_HURT;
    }
    public SoundEvent deathSound() {
        return SoundsInit.ANIMATRONIC_DEATH;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {

        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 50f)
                .add(EntityAttributes.ATTACK_DAMAGE, 9999f)
                .add(EntityAttributes.ATTACK_SPEED, 1f)
                .add(EntityAttributes.ATTACK_KNOCKBACK, 0f)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 9999f)
                .add(EntityAttributes.FOLLOW_RANGE, 16D);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void initGoals() {

        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2, false));
        this.targetSelector.add(1, new ShouldActiveTargetGoal<>(this, PlayerEntity.class, true, true));
        this.goalSelector.add(2, new ShouldWanderGoal(this, 1));
        this.goalSelector.add(3, new ShouldLookAtEntityGoal(this, PlayerEntity.class, 6f));
        this.goalSelector.add(3, new ShouldLookAtEntityGoal(this, VillagerEntity.class, 6f));
        this.goalSelector.add(3, new ShouldLookAtEntityGoal(this, DefaultEntity.class, 6f));
        this.goalSelector.add(4, new ShouldLookAroundGoal(this));
    }

    @Override
    public boolean isBoopable(){
        return true;
    }

    @Override
    public Vec3d boopSize() {
        return new Vec3d(0.5f, 0.5f, 0.5f);
    }

    @Override
    public Vec3d boopOffset() {
        return new Vec3d(0f, -0.5f, 0f);
    }

    @Override
    public String demoAnim() {
        return performingAnim();
    }

    @Override
    public float demo_scale() {
        return 1.05f;
    }

    @Override
    protected String defaultIdleAnim() {
        return "animation.cl_chica.dayidle";
    }

    @Override
    protected String defaultWalkingAnim() {
        return "animation.cl_chica.daywalk";
    }

    @Override
    protected String defaultRunningAnim() {
        return "animation.cl_chica.dayrun";
    }

    @Override
    protected String performingAnim() {
        return "animation.cl_chica.performance";
    }

    @Override
    protected String deactivatingAnim() {
        return "animation.cl_chica.deactivating";
    }

    @Override
    protected int deactivatingLength() {
        return 20;
    }

    @Override
    protected String deactivatedAnim() {
        return "animation.cl_chica.deactivated";
    }

    @Override
    protected String activatingAnim() {
        return "animation.cl_chica.activating";
    }

    @Override
    protected int activatingLength() {
        return 20;
    }

    @Override
    protected String aggressiveIdleAnim() {
        return "animation.cl_chica.nightidle";
    }

    @Override
    protected String aggressiveWalkingAnim() {
        return "animation.cl_chica.nightwalk";
    }

    @Override
    protected String aggressiveRunningAnim() {
        return "animation.cl_chica.nightrun";
    }

    @Override
    protected String crawlingIdleAnim() {
        return "";
    }

    @Override
    protected String crawlingWalkingAnim() {
        return "";
    }

    @Override
    public boolean hasJumpScare() {
        return true;
    }

    @Override
    protected String JumpScareAnim() {
        return "animation.cl_chica.jumpscare";
    }

    @Override
    public int JumpScareLength() {
        return 20;
    }

    @Override
    protected String attackAnim() {
        return "animation.cl_chica.attack";
    }

    @Override
    protected int attackLength() {
        return 0;
    }

    @Override
    protected String dyingAnim() {
        return "animation.cl_chica.death";
    }

    @Override
    protected int deathLength() {
        return 62;
    }

    @Override
    protected float walkAnimationSpeed() {
        return 1;
    }

    @Override
    protected float runAnimationSpeed() {
        return 1;
    }

    @Override
    protected boolean isAbleToCrawl() {
        return false;
    }

    @Override
    protected float getCrawlHeight() {
        return 0;
    }

    @Override
    public List<EntitySkin> getSkins() {
        List<EntitySkin> list = new ArrayList<>();
        list.add(DEFAULT);
        return list;
    }
    @Override
    public Identifier getDefaultTexture() {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_chica/cl_chica.png");
    }

    @Override
    public Identifier getDefaultGeoModel() {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "geo/entity/classic/cl_chica/cl_chica.geo.json");
    }

    @Override
    public Identifier getDefaultAnimations() {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "animations/entity/classic/cl_chica/cl_chica.animation.json");
    }

    @Override
    public String getDefaultKillScreenID() {
        return ScreensInit.DEFAULT_KILLSCREEN;
    }

    @Override
    public List<String> getIdleAnimations() {
        List<String> list = new ArrayList<>();
        list.add(defaultIdleAnim());
        list.add(aggressiveIdleAnim());
        list.add(deactivatedAnim());
        list.add(performingAnim());
        return list;
    }

}

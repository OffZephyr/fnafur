package net.zephyr.fnafur.entity.zephyr;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.entity.goals.ShouldActiveTargetGoal;
import net.zephyr.fnafur.entity.goals.ShouldLookAroundGoal;
import net.zephyr.fnafur.entity.goals.ShouldLookAtEntityGoal;
import net.zephyr.fnafur.entity.goals.ShouldWanderGoal;
import net.zephyr.fnafur.init.ScreensInit;

import java.util.ArrayList;
import java.util.List;

public class ZephyrEntity extends DefaultEntity {
    public ZephyrEntity(EntityType<ZephyrEntity> type, World world) {
        super(type, world);
    }
    private final EntitySkin DEFAULT =
            new EntitySkin("entity.fnafur.zephyr.default");

    private final EntitySkin NEON_SKIN =
            new EntitySkin("entity.fnafur.zephyr.neon")
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/zephyr/icons/neon.png"))
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/zephyr/zephyr_neon.png"));

    public static DefaultAttributeContainer.Builder setAttributes() {

        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 15f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.25f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 12D);
    }

    @Override
    protected void initGoals() {

        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2, false));
        this.targetSelector.add(1, new ShouldActiveTargetGoal<>(this, PlayerEntity.class, false, true));
        this.goalSelector.add(2, new ShouldWanderGoal(this, 1));
        this.goalSelector.add(3, new ShouldLookAtEntityGoal(this, PlayerEntity.class, 6f));
        this.goalSelector.add(3, new ShouldLookAtEntityGoal(this, DefaultEntity.class, 6f));
        this.goalSelector.add(4, new ShouldLookAroundGoal(this));
    }

    @Override
    public SoundEvent walkSound() {
        return null;
    }

    @Override
    public SoundEvent hurtSound() {
        return null;
    }

    @Override
    public SoundEvent deathSound() {
        return null;
    }

    @Override
    public String demoAnim() {
        return aggressiveIdleAnim();
    }

    @Override
    public float demo_scale() {
        return 1.2f;
    }

    public String defaultIdleAnim() {
        return "animation.zephyr.dayidle";
    }

    public String defaultWalkingAnim() {
        return "animation.zephyr.daywalk";
    }

    public String defaultRunningAnim() {
        return "animation.zephyr.dayrun";
    }

    public String performingAnim() {
        return "animation.zephyr.performance";
    }

    public String deactivatingAnim() {
        return "animation.zephyr.deactivating";
    }

    public String deactivatedAnim() {
        return "animation.zephyr.deactivated";
    }

    @Override
    protected int deactivatingLength() {
        return 50;
    }

    public String activatingAnim() {
        return "animation.zephyr.activating";
    }

    @Override
    protected int activatingLength() {
        return 90;
    }

    public String aggressiveIdleAnim() {
        return "animation.zephyr.nightidle";
    }

    public String aggressiveWalkingAnim() {
        return "animation.zephyr.nightwalk";
    }

    public String aggressiveRunningAnim() {
        return "animation.zephyr.nightrun";
    }

    public String crawlingIdleAnim() {
        return "animation.zephyr.crawlidle";
    }

    public String crawlingWalkingAnim() {
        return "animation.zephyr.crawling";
    }

    @Override
    public boolean hasJumpScare() {
        return true;
    }

    public String JumpScareAnim() {
        return "animation.zephyr.jumpscare";
    }

    public int JumpScareLength() {
        return 40;
    }

    public String attackAnim() {
        return "animation.zephyr.attack";
    }

    public int attackLength() {
        return 10;
    }

    public String dyingAnim() {
        return "animation.zephyr.death";
    }

    public int deathLength() {
        return 60;
    }

    @Override
    protected float walkAnimationSpeed() {
        return 1.5f;
    }

    @Override
    protected float runAnimationSpeed() {
        return 1.25f;
    }

    @Override
    protected boolean isAbleToCrawl() {
        return true;
    }

    @Override
    protected float getCrawlHeight() {
        return 0.65f;
    }

    @Override
    public List<EntitySkin> getSkins() {
        List<EntitySkin> list = new ArrayList<>();
        list.add(DEFAULT);
        list.add(NEON_SKIN);
        return list;
    }

    @Override
    public Identifier getDefaultTexture() {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/zephyr/zephyr.png");
    }

    @Override
    public Identifier getDefaultGeoModel() {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "geo/entity/zephyr/zephyr.geo.json");
    }

    @Override
    public Identifier getDefaultAnimations() {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "animations/entity/zephyr/zephyr.animation.json");
    }

    @Override
    public Identifier getDefaultIcon() {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/zephyr/icons/default.png");
    }

    @Override
    public String getDefaultKillScreenID() {
        return ScreensInit.ZEPHYR_KILLSCREEN;
    }
}

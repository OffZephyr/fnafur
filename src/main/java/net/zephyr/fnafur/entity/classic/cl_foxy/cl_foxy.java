package net.zephyr.fnafur.entity.classic.cl_foxy;

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
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.entity.goals.ShouldActiveTargetGoal;
import net.zephyr.fnafur.entity.goals.ShouldLookAroundGoal;
import net.zephyr.fnafur.entity.goals.ShouldLookAtEntityGoal;
import net.zephyr.fnafur.entity.goals.ShouldWanderGoal;
import net.zephyr.fnafur.init.ScreensInit;
import net.zephyr.fnafur.init.SoundsInit;

import java.util.ArrayList;
import java.util.List;

public class cl_foxy extends DefaultEntity {

    private final EntitySkin DEFAULT =
            new EntitySkin("entity.fnafur.cl_foxy.default")
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_foxy/icon/normalfoxyicon.png"));
    private final EntitySkin FIXED =
            new EntitySkin("entity.fnafur.cl_foxy.fixed")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_foxy/cl_foxy_fixed.png"))
                    .geo(Identifier.of(FnafUniverseRebuilt.MOD_ID, "geo/entity/classic/cl_foxy/cl_foxy_fixed.geo.json"));
    private final EntitySkin FIXED_TAIL =
            new EntitySkin("entity.fnafur.cl_foxy.fixed_tail")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_foxy/cl_foxy_fixed_tail.png"))
                    .geo(Identifier.of(FnafUniverseRebuilt.MOD_ID, "geo/entity/classic/cl_foxy/cl_foxy_fixed_tail.geo.json"))
                    .animations(Identifier.of(FnafUniverseRebuilt.MOD_ID, "animations/entity/classic/cl_foxy/cl_foxy_tail.animation.json"));
    private final EntitySkin DIRTY =
            new EntitySkin("entity.fnafur.cl_foxy.dirty")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_foxy/cl_foxy_dirty.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_foxy/icon/dirtyfoxyicon.png"));
    private final EntitySkin STAINED_FABRIC =
            new EntitySkin("entity.fnafur.cl_foxy.stained_fabric")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_foxy/cl_foxy_stained_fabric.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_foxy/icon/stainedfabricfoxyicon.png"));
    private final EntitySkin OLD =
            new EntitySkin("entity.fnafur.cl_foxy.old")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_foxy/cl_foxy_old.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_foxy/icon/oldfoxyicon.png"));
    private final EntitySkin BLACKLIGHT =
            new EntitySkin("entity.fnafur.cl_foxy.blacklight")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_foxy/cl_foxy_blacklight.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_foxy/icon/blacklightfoxyicon.png"));
    private final EntitySkin BLACKLIGHT_PBG =
            new EntitySkin("entity.fnafur.cl_foxy.blacklight_pbg")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_foxy/cl_foxy_blacklight_pbg.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_foxy/icon/pbgblacklightfoxyicon.png"));
    private final EntitySkin INVERTED =
            new EntitySkin("entity.fnafur.cl_foxy.inverted")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_foxy/cl_foxy_inverted.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_foxy/icon/invertedfoxyicon.png"));
    private final EntitySkin FIXED_INVERTED =
            new EntitySkin("entity.fnafur.cl_foxy.fixed_inverted")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_foxy/cl_foxy_fixed_inverted.png"))
                    .geo(Identifier.of(FnafUniverseRebuilt.MOD_ID, "geo/entity/classic/cl_foxy/cl_foxy_fixed.geo.json"));
    private final EntitySkin SHADOW =
            new EntitySkin("entity.fnafur.cl_foxy.shadow")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_foxy/cl_foxy_shadow.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_foxy/icon/shadowfoxyicon.png"));
    private final EntitySkin SHADOW_WHITE_EYES =
            new EntitySkin("entity.fnafur.cl_foxy.shadow_white_eyes")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_foxy/cl_foxy_shadow_white_eyes.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_foxy/icon/shadowfoxywhiteeyesicon.png"));
    private final EntitySkin SHADOW_NORMAL_EYES =
            new EntitySkin("entity.fnafur.cl_foxy.shadow_normal_eyes")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_foxy/cl_foxy_shadow_normal_eyes.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_foxy/icon/shadowfoxynormaleyesicon.png"));

    public cl_foxy(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    @Override
    public SoundEvent walkSound() {
        return SoundsInit.FOXY_FOOTSTEPS;
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
        this.goalSelector.add(1, new MeleeAttackGoal(this, 2f, false));
        this.targetSelector.add(1, new ShouldActiveTargetGoal<>(this, PlayerEntity.class, true, true));
        this.goalSelector.add(2, new ShouldWanderGoal(this, 0.85f));
        this.goalSelector.add(3, new ShouldLookAtEntityGoal(this, PlayerEntity.class, 6f));
        this.goalSelector.add(3, new ShouldLookAtEntityGoal(this, VillagerEntity.class, 6f));
        this.goalSelector.add(3, new ShouldLookAtEntityGoal(this, DefaultEntity.class, 6f));
        this.goalSelector.add(4, new ShouldLookAroundGoal(this));
    }

    @Override
    public float demo_scale() {
        return 1.05f;
    }

    @Override
    protected String defaultIdleAnim() {
        return "animation.cl_foxy.dayidle";
    }

    @Override
    protected String defaultWalkingAnim() {
        return "animation.cl_foxy.daywalk";
    }

    @Override
    protected String defaultRunningAnim() {
        return "animation.cl_foxy.dayrun";
    }

    @Override
    protected String performingAnim() {
        return "animation.cl_foxy.performance";
    }

    @Override
    protected String deactivatingAnim() {
        return "animation.cl_foxy.deactivating";
    }

    @Override
    protected int deactivatingLength() {
        return 20;
    }

    @Override
    protected String deactivatedAnim() {
        return "animation.cl_foxy.deactivated";
    }

    @Override
    protected String activatingAnim() {
        return "animation.cl_foxy.activating";
    }

    @Override
    protected int activatingLength() {
        return 20;
    }

    @Override
    protected String aggressiveIdleAnim() {
        return "animation.cl_foxy.nightidle";
    }

    @Override
    protected String aggressiveWalkingAnim() {
        return "animation.cl_foxy.nightwalk";
    }

    @Override
    protected String aggressiveRunningAnim() {
        return "animation.cl_foxy.nightrun";
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
        return "animation.cl_foxy.jumpscare";
    }

    @Override
    public int JumpScareLength() {
        return 20;
    }

    @Override
    protected String attackAnim() {
        return "animation.cl_foxy.attack";
    }

    @Override
    protected int attackLength() {
        return 0;
    }

    @Override
    protected String dyingAnim() {
        return "animation.cl_foxy.death";
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
        list.add(FIXED);
        list.add(FIXED_TAIL);
        list.add(DIRTY);
        list.add(STAINED_FABRIC);
        list.add(OLD);
        list.add(BLACKLIGHT);
        list.add(BLACKLIGHT_PBG);
        list.add(SHADOW);
        list.add(SHADOW_WHITE_EYES);
        list.add(SHADOW_NORMAL_EYES);
        list.add(INVERTED);
        list.add(FIXED_INVERTED);
        return list;
    }
    @Override
    public Identifier getDefaultTexture() {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_foxy/cl_foxy.png");
    }

    @Override
    public Identifier getDefaultGeoModel() {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geo/entity/classic/cl_foxy/cl_foxy.geo.json");
    }

    @Override
    public Identifier getDefaultAnimations() {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "animations/entity/classic/cl_foxy/cl_foxy.animation.json");
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

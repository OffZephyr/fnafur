package net.zephyr.fnafur.entity.classic.cl_fred;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
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
import net.zephyr.fnafur.util.SoundUtils;

import java.util.ArrayList;
import java.util.List;

public class cl_fred extends DefaultEntity {

    private final EntitySkin DEFAULT =
            new EntitySkin("entity.fnafur.cl_fred.default")
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/normalfreddyicon.png"));
    private final EntitySkin CLEAN =
            new EntitySkin("entity.fnafur.cl_fred.default_clean")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/cl_fred_clean.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/cleanfreddyicon.png"));
    private final EntitySkin DIRTY =
            new EntitySkin("entity.fnafur.cl_fred.default_dirty")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/cl_fred_dirty.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/dirtyfreddyicon.png"));
    private final EntitySkin AR =
            new EntitySkin("entity.fnafur.cl_fred.ar")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/ar_cl_fred.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/arfreddyicon.png"));
    private final EntitySkin YELLOW_BEAR_SKIN =
            new EntitySkin("entity.fnafur.cl_fred.yellow_bear")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/yellow_bear.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/yellowbearnormalicon.png"));
    private final EntitySkin YELLOW_BEAR_SKIN_CLEAN =
            new EntitySkin("entity.fnafur.cl_fred.yellow_bear_clean")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/yellow_bear_clean.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/yellowbearicon.png"));
    private final EntitySkin SHADOW_FREDDY_LIGHT_SKIN =
            new EntitySkin("entity.fnafur.cl_fred.shadow_freddy_light")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/shadow_freddy_light.png"))
                    .glow_texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/white_glow.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/shadowfreddylighticon.png"));
    private final EntitySkin SHADOW_FREDDY_NORMAL =
            new EntitySkin("entity.fnafur.cl_fred.shadow_freddy_normal")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/shadow_freddy_normal.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/shadowfreddyicon.png"));
    private final EntitySkin SHADOW_FREDDY_SUIT =
            new EntitySkin("entity.fnafur.cl_fred.shadow_freddy_suit")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/shadow_freddy_suit.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/shadowfreddynormaleyesicon.png"));
    private final EntitySkin SHADOW_FREDDY_WHITE =
            new EntitySkin("entity.fnafur.cl_fred.shadow_freddy_white")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/shadow_freddy_white.png"))
                    .glow_texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/white_glow.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/shadowfreddywhiteeyesicon.png"));
    private final EntitySkin BLANK =
            new EntitySkin("entity.fnafur.cl_fred.default_blank")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/cl_fred_blank.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/blankfreddyicon.png"));
    private final EntitySkin BLACKLIGHT =
            new EntitySkin("entity.fnafur.cl_fred.blacklight")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/blacklight_freddy.png"))
                    .glow_texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/blacklight_freddy_glow.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/blacklightfreddyicon.png"));
    private final EntitySkin BLACKLIGHT_YELLOW =
            new EntitySkin("entity.fnafur.cl_fred.blacklight_yellow")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/blacklight_freddy_yellow.png"))
                    .glow_texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/blacklight_freddy_yellow_glow.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/blacklightfreddyyellowearsicon.png"));
    private final EntitySkin BLACK =
            new EntitySkin("entity.fnafur.cl_fred.black")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/black.png"))
                    .glow_texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/black_glow.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/blackfreddyicon.png"));
    private final EntitySkin BLACKLIGHT_BGM =
            new EntitySkin("entity.fnafur.cl_fred.blacklight_bgm")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/blacklight_freddy_bgm.png"))
                    .glow_texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/blacklight_freddy_bgm_glow.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/blacklightfreddybgmicon.png"));
    private final EntitySkin BLACKLIGHT_BGB =
            new EntitySkin("entity.fnafur.cl_fred.blacklight_bgb")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/blacklight_freddy_bgb.png"))
                    .glow_texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/blacklight_freddy_bgb_glow.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/blacklightfreddybgbicon.png"));
    private final EntitySkin BLACKLIGHT_MBG =
            new EntitySkin("entity.fnafur.cl_fred.blacklight_mbg")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/blacklight_freddy_mbg.png"))
                    .glow_texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/blacklight_freddy_mbg_glow.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/blacklightfreddymbgicon.png"));
    private final EntitySkin INVERTED =
            new EntitySkin("entity.fnafur.cl_fred.inverted")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/cl_fred_inverted.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/invertedfreddyicon.png"));
    private final EntitySkin GOLDEN_FREDDY =
            new EntitySkin("entity.fnafur.cl_fred.golden_freddy")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/golden_freddy.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/goldenfreddyicon.png"));
    private final EntitySkin GOLDEN_FREDDY_CLEAN =
            new EntitySkin("entity.fnafur.cl_fred.golden_freddy_clean")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/golden_freddy_clean.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/goldenfreddycleanicon.png"));
    private final EntitySkin AR_GOLDEN_FREDDY =
            new EntitySkin("entity.fnafur.cl_fred.ar_golden_freddy")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/ar_golden_freddy.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/goldfreddyicon.png"));
    private final EntitySkin AR_GOLDEN_FREDDY_GOLD =
            new EntitySkin("entity.fnafur.cl_fred.ar_golden_freddy_gold")
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/ar_golden_freddy_gold.png"))
                    .icon(Identifier.of(FnafUniverseResuited.MOD_ID,"textures/entity/classic/cl_fred/icon/argoldenfreddyicon.png"));

    public cl_fred(EntityType<? extends PathAwareEntity> type, World world) {
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
        return new Vec3d(0.25f, 0.15f, 0.25f);
    }

    @Override
    public Vec3d boopOffset() {
        return new Vec3d(0, -1, 0);
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
        return "animation.cl_fred.dayidle";
    }

    @Override
    protected String defaultWalkingAnim() {
        return "animation.cl_fred.daywalk";
    }

    @Override
    protected String defaultRunningAnim() {
        return "animation.cl_fred.dayrun";
    }

    @Override
    protected String performingAnim() {
        return "animation.cl_fred.performance";
    }

    @Override
    protected String deactivatingAnim() {
        return "animation.cl_fred.deactivating";
    }

    @Override
    protected int deactivatingLength() {
        return 20;
    }

    @Override
    protected String deactivatedAnim() {
        return "animation.cl_fred.deactivated";
    }

    @Override
    protected String activatingAnim() {
        return "animation.cl_fred.activating";
    }

    @Override
    protected int activatingLength() {
        return 20;
    }

    @Override
    protected String aggressiveIdleAnim() {
        return "animation.cl_fred.nightidle";
    }

    @Override
    protected String aggressiveWalkingAnim() {
        return "animation.cl_fred.nightwalk";
    }

    @Override
    protected String aggressiveRunningAnim() {
        return "animation.cl_fred.nightrun";
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
        return "animation.cl_fred.jumpscare";
    }

    @Override
    public int JumpScareLength() {
        return 20;
    }

    @Override
    protected String attackAnim() {
        return "animation.cl_fred.attack";
    }

    @Override
    protected int attackLength() {
        return 0;
    }

    @Override
    protected String dyingAnim() {
        return "animation.cl_fred.death";
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
        list.add(CLEAN);
        list.add(DIRTY);
        list.add(AR);
        list.add(YELLOW_BEAR_SKIN);
        list.add(YELLOW_BEAR_SKIN_CLEAN);
        list.add(GOLDEN_FREDDY);
        list.add(GOLDEN_FREDDY_CLEAN);
        list.add(AR_GOLDEN_FREDDY);
        list.add(AR_GOLDEN_FREDDY_GOLD);
        list.add(SHADOW_FREDDY_SUIT);
        list.add(SHADOW_FREDDY_NORMAL);
        list.add(SHADOW_FREDDY_WHITE);
        list.add(SHADOW_FREDDY_LIGHT_SKIN);
        list.add(BLACK);
        list.add(BLACKLIGHT);
        list.add(BLACKLIGHT_BGM);
        list.add(BLACKLIGHT_BGB);
        list.add(BLACKLIGHT_MBG);
        list.add(BLACKLIGHT_YELLOW);
        list.add(INVERTED);
        list.add(BLANK);
        return list;
    }
    @Override
    public Identifier getDefaultTexture() {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_fred/cl_fred.png");
    }

    @Override
    public Identifier getDefaultGeoModel() {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "geo/entity/classic/cl_fred/cl_fred.geo.json");
    }

    @Override
    public Identifier getDefaultAnimations() {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "animations/entity/classic/cl_fred/cl_fred.animation.json");
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
        list.add("animation.cl_fred.pose_stagecamstare");
        list.add("animation.cl_fred.pose_restroompeekleft");
        list.add("animation.cl_fred.pose_diningareastance");
        list.add("animation.cl_fred.pose_restroompeekright");
        list.add("animation.cl_fred.pose_hallstance");
        list.add("animation.cl_fred.pose_closecamstare");
        list.add("animation.cl_fred.pose_ucnhallstance");
        list.add("animation.cl_fred.pose_ucnhallstanceleft");
        list.add("animation.cl_fred.pose_ucnhallstare");
        return list;
    }
}

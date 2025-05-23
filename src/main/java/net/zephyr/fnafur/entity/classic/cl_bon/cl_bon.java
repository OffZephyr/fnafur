package net.zephyr.fnafur.entity.classic.cl_bon;

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

public class cl_bon extends DefaultEntity {

    private final EntitySkin DEFAULT =
            new EntitySkin("entity.fnafur.cl_bon.default")
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_bon/icon/normalbonnieicon.png"));
    private final EntitySkin CLEAN =
            new EntitySkin("entity.fnafur.cl_bon.default_clean")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_bon/cl_bon_clean.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_bon/icon/bonniecleanicon.png"));
    private final EntitySkin BLACKLIGHT =
            new EntitySkin("entity.fnafur.cl_bon.blacklight")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_bon/cl_bon_blacklight.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_bon/icon/blacklightbonnieicon.png"));
    private final EntitySkin BLACKLIGHT_MYB =
            new EntitySkin("entity.fnafur.cl_bon.blacklight_myb")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_bon/cl_bon_blacklight_myb.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_bon/icon/blacklightbonniemybicon.png"));
    private final EntitySkin SHADOW =
            new EntitySkin("entity.fnafur.cl_bon.shadow")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_bon/cl_bon_shadow.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_bon/icon/shadowclassicbonnieicon.png"));
    private final EntitySkin SHADOW_SUIT =
            new EntitySkin("entity.fnafur.cl_bon.shadow_suit")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_bon/cl_bon_shadow_suit.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_bon/icon/shadowclassicbonniesuiticon.png"));
    private final EntitySkin SHADOW_SILVER =
            new EntitySkin("entity.fnafur.cl_bon.shadow_silver")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_bon/cl_bon_shadow_silver.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_bon/icon/shadowclassicbonniesilvericon.png"));
    private final EntitySkin SHADOW_WHITE =
            new EntitySkin("entity.fnafur.cl_bon.shadow_white")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_bon/cl_bon_shadow_white.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_bon/icon/shadowclassicbonniewhiteicon.png"));
    private final EntitySkin INVERTED =
            new EntitySkin("entity.fnafur.cl_bon.inverted")
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_bon/cl_bon_inverted.png"))
                    .icon(Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/entity/classic/cl_bon/icon/bonnieinvertedicon.png"));

    public cl_bon(EntityType<? extends PathAwareEntity> type, World world) {
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
        return "animation.cl_bon.dayidle";
    }

    @Override
    protected String defaultWalkingAnim() {
        return "animation.cl_bon.daywalk";
    }

    @Override
    protected String defaultRunningAnim() {
        return "animation.cl_bon.dayrun";
    }

    @Override
    protected String performingAnim() {
        return "animation.cl_bon.performance";
    }

    @Override
    protected String deactivatingAnim() {
        return "animation.cl_bon.deactivating";
    }

    @Override
    protected int deactivatingLength() {
        return 20;
    }

    @Override
    protected String deactivatedAnim() {
        return "animation.cl_bon.deactivated";
    }

    @Override
    protected String activatingAnim() {
        return "animation.cl_bon.activating";
    }

    @Override
    protected int activatingLength() {
        return 20;
    }

    @Override
    protected String aggressiveIdleAnim() {
        return "animation.cl_bon.nightidle";
    }

    @Override
    protected String aggressiveWalkingAnim() {
        return "animation.cl_bon.nightwalk";
    }

    @Override
    protected String aggressiveRunningAnim() {
        return "animation.cl_bon.nightrun";
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
        return "animation.cl_bon.jumpscare";
    }

    @Override
    public int JumpScareLength() {
        return 20;
    }

    @Override
    protected String attackAnim() {
        return "animation.cl_bon.attack";
    }

    @Override
    protected int attackLength() {
        return 0;
    }

    @Override
    protected String dyingAnim() {
        return "animation.cl_bon.death";
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
        list.add(SHADOW);
        list.add(SHADOW_SILVER);
        list.add(SHADOW_WHITE);
        list.add(SHADOW_SUIT);
        list.add(BLACKLIGHT);
        list.add(BLACKLIGHT_MYB);
        list.add(INVERTED);
        return list;
    }
    @Override
    public Identifier getDefaultTexture() {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/classic/cl_bon/cl_bon.png");
    }

    @Override
    public Identifier getDefaultGeoModel() {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geo/entity/classic/cl_bon/cl_bon.geo.json");
    }

    @Override
    public Identifier getDefaultAnimations() {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "animations/entity/classic/cl_bon/cl_bon.animation.json");
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

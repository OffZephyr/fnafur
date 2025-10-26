package net.zephyr.fnafur.entity.animatronic;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader.ChipReaderBlockEntity;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.server_monitor.ServerMonitorBlockEntity;
import net.zephyr.fnafur.entity.animatronic.block.AnimatronicBlock;
import net.zephyr.fnafur.entity.animatronic.block.AnimatronicBlockEntity;
import net.zephyr.fnafur.entity.animatronic.data.CharacterData;
import net.zephyr.fnafur.entity.animatronic.goals.AnimTargetGoal;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.entity_init.CharacterInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateEntityNbtC2SGetFromServerPayload;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.jsonReaders.animatronics.AnimatronicDataHandler;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.keyframe.event.KeyFrameEvent;
import software.bernie.geckolib.animation.keyframe.event.data.CustomInstructionKeyframeData;
import software.bernie.geckolib.cache.GeckoLibResources;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AnimatronicEntity extends PathAwareEntity implements GeoEntity {

    int updateDepth = 0;
    public List<IEntityDataSaver> sources = new ArrayList<>();
    private CharacterData character;
    public double force_age = 0;
    public boolean isMenu = false;
    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    List<String> blinkList = List.of(
           "performance",
           "walk_upper",
           "nightwalk_upper",
           "backwards_walk_upper",
           "run_upper",
           "idle",
           "playeridle",
           "stageidle",
           "hauntedidle",
           "death"
    );
    String currentAnim = "";
    int blinkDelay;
    public AnimatronicEntity(EntityType<? extends PathAwareEntity> entityType, World world){
        super(entityType, world);
        if(!LinkTarget.allTargets.contains((IEntityDataSaver) this)){
            LinkTarget.allTargets.add((IEntityDataSaver) this);
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }


    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        return super.interactAt(player, hitPos, hand);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("Lower", 3, this::lowerAnimController));
        controllers.add(new AnimationController<>("Upper", 3, this::upperAnimController));
        controllers.add(new AnimationController<>("Blink", 0, this::blinkAnimController)
                .setCustomInstructionKeyframeHandler(this::instructionHandler));
    }

    private void instructionHandler(KeyFrameEvent<GeoAnimatable, CustomInstructionKeyframeData> handler) {
        
    }

    private PlayState blinkAnimController(AnimationTest<GeoAnimatable> geoAnimatableAnimationTest) {


        if(true){
            if (blinkDelay == 0) {
                geoAnimatableAnimationTest.resetCurrentAnimation();
                Random random = new Random();
                blinkDelay = random.nextInt(100, 200);
            }

            RawAnimation anim = RawAnimation.begin().thenPlayAndHold(AnimatronicDataHandler.getAnimationFullName("blink", getAnimPrefix()));
            geoAnimatableAnimationTest.setAnimation(anim);

            return PlayState.CONTINUE;
        }
        blinkDelay = 0;
        return PlayState.STOP;
    }

    private PlayState lowerAnimController(AnimationTest<AnimatronicEntity> animatronicEntityAnimationState) {
        animatronicEntityAnimationState.controller().transitionLength(3);
        animatronicEntityAnimationState.controller().setAnimationSpeed(1);
        if(isMenu) {
            animatronicEntityAnimationState.controller().transitionLength(0);
            return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(AnimatronicDataHandler.getAnimationFullName("loweridle", getAnimPrefix())));
        }

        String getupAnim = ((IEntityDataSaver)this).getPersistentData().getString("getupAnim", "");
        if(!getupAnim.isEmpty()){
            animatronicEntityAnimationState.controller().transitionLength(0);
            return PlayState.STOP;
        }

        double speed = getMovement().horizontalLength() * 15;
        if(speed > 0){
            animatronicEntityAnimationState.controller().setAnimationSpeed(speed);
            return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(AnimatronicDataHandler.getAnimationFullName("walk_lower", getAnimPrefix())));
        }
        return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(AnimatronicDataHandler.getAnimationFullName("loweridle", getAnimPrefix())));
    }

    private PlayState upperAnimController(AnimationTest<AnimatronicEntity> animatronicEntityAnimationState) {
        animatronicEntityAnimationState.controller().transitionLength(3);
        animatronicEntityAnimationState.controller().setAnimationSpeed(1);
        if(isMenu) {
            animatronicEntityAnimationState.controller().transitionLength(0);
            return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(AnimatronicDataHandler.getAnimationFullName("menuidle", getAnimPrefix())));
        }

        //TODO ADD POWERED
        if(true){
            double speed = getMovement().horizontalLength() * 15;
            if(speed > 0){
                animatronicEntityAnimationState.controller().setAnimationSpeed(speed);
                return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(AnimatronicDataHandler.getAnimationFullName("walk_upper", getAnimPrefix())));
            }

            return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(AnimatronicDataHandler.getAnimationFullName("stageidle", getAnimPrefix())));
        }
        return animatronicEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(AnimatronicDataHandler.getAnimationFullName("deactivated", getAnimPrefix())));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void tick() {
        blinkDelay = Math.max(0, blinkDelay - 1);
        if(isMenu){
            age++;
        }

        if(!getEntityWorld().isClient()) {
            ((IEntityDataSaver) this).getPersistentData().putString("getupAnim", "");
        }
        else{
            if (!((IEntityDataSaver) this).getPersistentData().contains("synced")) {
                ClientPlayNetworking.send(new UpdateEntityNbtC2SGetFromServerPayload(getId()));
            }

            //if (((IEntityDataSaver) this).getServerUpdateStatus()) {
            //    //MinecraftClient.getInstance().player.sendMessage(Text.literal("SYNCING PROP"), false);
            //    ClientPlayNetworking.send(new UpdateBlockNbtC2SPayload(getPos().asLong(), ((IEntityDataSaver) this).getPersistentData()));
            //}
        }
        super.tick();
    }

    @Override
    public double getTick(Object entity) {
        if(isMenu){
            return MinecraftClient.getInstance().world.getTime();
        }

        return age;
    }

    public CharacterData getCharacter(){
        if(character == null) return CharacterInit.ENDO_01;
        return character;
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
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.8));
        this.targetSelector.add(1, new AnimTargetGoal(this, PlayerEntity.class, true, true));
        this.targetSelector.add(2, new AnimTargetGoal(this, VillagerEntity.class, true, true));
        this.targetSelector.add(3, new RevengeGoal(this));
    }

    public void setChara(String chara, @Nullable String alt, @Nullable String eyes){
        AnimatronicDataHandler.Chara chara2 = AnimatronicDataHandler.CHARACTERS.get(chara);
        String alt2 = alt == null || alt.isEmpty() ? chara2.DEFAULT_ALT : alt;
        AnimatronicDataHandler.Alt alt3 = chara2.ALTS.get(alt2);
        String eyes2 = eyes == null || alt.isEmpty() ? alt3.default_eyes() : eyes;
        ((IEntityDataSaver)this).getPersistentData().putString("chara", chara);
        ((IEntityDataSaver)this).getPersistentData().putString("suit", alt2);
        ((IEntityDataSaver)this).getPersistentData().putString("eyes", eyes2);
    }
    public String getChara(){
        return ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
    }
    public String getAlt(){
        return ((IEntityDataSaver)this).getPersistentData().getString("suit").orElse("");
    }
    public String getEyes(){
        return ((IEntityDataSaver)this).getPersistentData().getString("eyes").orElse("");
    }

    public Identifier getTexture(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("chara")) {
            String chara = ((IEntityDataSaver) this).getPersistentData().getString("chara").orElse("");
            if (!chara.isEmpty()) {
                String alt = ((IEntityDataSaver) this).getPersistentData().getString("suit").orElse("");
                if (!alt.isEmpty()) {
                    return AnimatronicDataHandler.getAltTexture(chara, alt);
                }
            }
        }

        return AnimatronicDataHandler.getDefaultAltTexture();
    }
    public boolean isEmptyEye(){

        if(((IEntityDataSaver)this).getPersistentData().contains("chara")){
            String chara = ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
            if(!chara.isEmpty()){
                String alt = ((IEntityDataSaver)this).getPersistentData().getString("suit").orElse("");
                if(!alt.isEmpty()) {
                    String eyes = ((IEntityDataSaver)this).getPersistentData().getString("eyes").orElse("");
                    if(!eyes.isEmpty()) {
                        return eyes.equals("none");
                    }
                }
            }
        }

        return false;
    }
    public Identifier getEyeTexture(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("chara")){
            String chara = ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
            if(!chara.isEmpty()){
                String alt = ((IEntityDataSaver)this).getPersistentData().getString("suit").orElse("");
                if(!alt.isEmpty()) {
                    String eyes = ((IEntityDataSaver)this).getPersistentData().getString("eyes").orElse("");
                    if(!eyes.isEmpty()) {
                    return AnimatronicDataHandler.getEyeTexture(chara, alt, eyes);
                    }
                }
            }
        }

        return AnimatronicDataHandler.getDefaultEyeTexture();
    }
    public Identifier getEyeMapTexture(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("chara")){
            String chara = ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
            if(!chara.isEmpty()){
                String alt = ((IEntityDataSaver)this).getPersistentData().getString("suit").orElse("");
                if(!alt.isEmpty()) {
                    String eyes = ((IEntityDataSaver)this).getPersistentData().getString("eyes").orElse("");
                    if(!eyes.isEmpty()) {
                        return AnimatronicDataHandler.getEyeMapTexture(chara, alt, eyes);
                    }
                }
            }
        }

        return AnimatronicDataHandler.getDefaultEyeMapTexture();
    }

    public Identifier getReRenderTexture(World world){
        return getTexture(world);
    }
    public Identifier getModel(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("chara")){
            String chara = ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
            if(!chara.isEmpty()){
                String alt = ((IEntityDataSaver)this).getPersistentData().getString("suit").orElse("");
                if(!alt.isEmpty()) {
                    return AnimatronicDataHandler.getModel(chara, alt);
                }
            }
        }

        return AnimatronicDataHandler.getDefaultModel();
    }

    public Identifier getReRenderModel(World world){
        return getModel(world);
    }

    public String getAnimPrefix(){
        if(((IEntityDataSaver)this).getPersistentData().contains("chara")) {
            String chara = ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
            if(!chara.isEmpty()) {
                String alt = ((IEntityDataSaver) this).getPersistentData().getString("suit").orElse("");
            AnimatronicDataHandler.Chara chara1 = AnimatronicDataHandler.CHARACTERS.get(chara);
            AnimatronicDataHandler.Alt alt1 = chara1.ALTS.get(alt);

            return alt1.preview_anim();
            }
        }
        return "default";
    }

    public String getAnimationsName(){
        if(((IEntityDataSaver)this).getPersistentData().contains("chara")){
            String chara = ((IEntityDataSaver)this).getPersistentData().getString("chara").orElse("");
            if(!chara.isEmpty()) {
                String alt = ((IEntityDataSaver) this).getPersistentData().getString("suit").orElse("");

                AnimatronicDataHandler.Chara chara1 = AnimatronicDataHandler.CHARACTERS.get(chara);
                AnimatronicDataHandler.Alt alt1 = chara1.ALTS.get(alt);

                String animString = alt1.preview_anim();
                String anim = AnimatronicDataHandler.ALL_ANIMATIONS.get(animString);

                if(isMenu) currentAnim = "menuidle";
                String animations = AnimatronicDataHandler.getAnimationFilePath(currentAnim, animString);
                if(!animations.isEmpty()){
                    return animations;
                }
            }
        }
        return "";
    }
    public Identifier getAnimations(){

        String animation = getAnimationsName();
        if(!animation.isEmpty()){
            return Identifier.of(FnafUniverseRebuilt.MOD_ID, animation);
        }

        return Identifier.of(FnafUniverseRebuilt.MOD_ID, AnimatronicDataHandler.getAnimationFilePath("default"));
    }

    public String prefixAnim(String animation){
        currentAnim = animation;
        Identifier location = Identifier.of(FnafUniverseRebuilt.MOD_ID, getAnimationsName());
        Map<Identifier, BakedAnimations> animations = GeckoLibResources.getBakedAnimations();
        BakedAnimations bakedAnimations = animations.get(location);

        NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt").orElse(new NbtCompound());
        String name = nbt.getString("chara").orElse("");
        if(name.isEmpty()) name = "default";
        String anim = "animation." + name + "." + animation;
        if(bakedAnimations != null && bakedAnimations.animations().containsKey(anim)) {
            return anim;
        }

        return "animation.default." + animation;
    }


    public RenderLayer getRenderType(Identifier texture){
        return RenderLayer.getEntityTranslucent(texture);
    }
}

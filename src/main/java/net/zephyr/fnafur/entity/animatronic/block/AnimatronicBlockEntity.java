package net.zephyr.fnafur.entity.animatronic.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.cache.GeckoLibResources;
import software.bernie.geckolib.loading.object.BakedAnimations;

import java.util.Map;
import java.util.Random;

public class AnimatronicBlockEntity extends GeoPropBlockEntity{
    int blinkDelay;
    public BlockState previewState = null;
    public AnimatronicBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.ANIMATRONIC_BLOCK, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("main", 3, this::mainController));
        controllers.add(new AnimationController<>("lower", 3, this::lowerController));
        controllers.add(new AnimationController<>("blink", 3, this::blinkController));
    }

    private PlayState mainController(AnimationTest<AnimatronicBlockEntity> geoPropBlockEntityAnimationState) {

        geoPropBlockEntityAnimationState.controller().transitionLength(3);
        if(item) {
            geoPropBlockEntityAnimationState.controller().transitionLength(0);
        }

        BlockState state = getWorld().getBlockState(getPos()).isOf(BlockInit.ANIMATRONIC_BLOCK) ? getWorld().getBlockState(getPos()) : BlockInit.ANIMATRONIC_BLOCK.getDefaultState();
        if(previewState != null) state = previewState;
        String anim = state.get(AnimatronicBlock.POSES).getMain();
        if(anim != null){
            return geoPropBlockEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(prefixAnim(anim)));
        }
        return PlayState.STOP;
    }

    private PlayState lowerController(AnimationTest<AnimatronicBlockEntity> animatronicBlockEntityAnimationState) {

        animatronicBlockEntityAnimationState.controller().transitionLength(3);
        if(item) {
            animatronicBlockEntityAnimationState.controller().transitionLength(0);
        }

        BlockState state = getWorld().getBlockState(getPos()).isOf(BlockInit.ANIMATRONIC_BLOCK) ? getWorld().getBlockState(getPos()) : BlockInit.ANIMATRONIC_BLOCK.getDefaultState();
        if(previewState != null) state = previewState;
        String anim = state.get(AnimatronicBlock.POSES).getLower();

        if(anim != null){
            return animatronicBlockEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(prefixAnim(anim)));
        }
        return PlayState.STOP;
    }

    private PlayState blinkController(AnimationTest<AnimatronicBlockEntity> animatronicBlockEntityAnimationState) {

        BlockState state = getWorld().getBlockState(getPos()).isOf(BlockInit.ANIMATRONIC_BLOCK) ? getWorld().getBlockState(getPos()) : BlockInit.ANIMATRONIC_BLOCK.getDefaultState();

        if(false) {

            if (blinkDelay == 0) {
                animatronicBlockEntityAnimationState.resetCurrentAnimation();
                Random random = new Random();
                blinkDelay = random.nextInt(100, 200);
            }

            RawAnimation anim = RawAnimation.begin().thenPlayAndHold(prefixAnim("blink"));
            animatronicBlockEntityAnimationState.setAnimation(anim);

            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void tick(World world, BlockPos blockPos, BlockState state, PropBlockEntity entity) {
        blinkDelay = Math.max(0, blinkDelay - 1);
        super.tick(world, blockPos, state, entity);
    }
    public Identifier getTexture(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("alt")){
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt").orElse(new NbtCompound());
            String texture = nbt.getString("texture").orElse("");
            if(!texture.isEmpty()){
                return Identifier.of(FnafUniverseRebuilt.MOD_ID, texture);
            }
        }

        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/default/endo_01/endo_01.png");
    }
    public Identifier getReRenderTexture(World world){
        return getTexture(world);
    }
    public Identifier getModel(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("alt")){
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt").orElse(new NbtCompound());
            String model = nbt.getString("model").orElse("");
            if(!model.isEmpty()){
                return Identifier.of(FnafUniverseRebuilt.MOD_ID, model);
            }
        }

        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/models/entity/default/endo_01/endo_01.geo.json");
    }

    public Identifier getReRenderModel(World world){
        return getModel(world);
    }
    public String getAnimationsPath(World world){
        if(((IEntityDataSaver)this).getPersistentData().contains("alt")){
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt").orElse(new NbtCompound());
            String animations = nbt.getString("animations").orElse("");
            if(!animations.isEmpty()){
                return animations;
            }
        }
        return "";
    }
    public Identifier getAnimations(World world){

        String animation = getAnimationsPath(world);
        if(!animation.isEmpty()){
            return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/animations/" + animation + ".animation.json");
        }

        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/animations/entity/default.animation.json");
    }

    public String prefixAnim(String animation){
        Identifier location = Identifier.of(FnafUniverseRebuilt.MOD_ID, getAnimationsPath(getWorld()));
        Map<Identifier, BakedAnimations> animations = GeckoLibResources.getBakedAnimations();
        BakedAnimations bakedAnimations = animations.get(location);

        NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt").orElse(new NbtCompound());
        String name = nbt.getString("chara").orElse("");
        String anim = "animation." + name + "." + animation;
        if(bakedAnimations != null && bakedAnimations.animations().containsKey(anim)) {
            return anim;
        }

        return "animation.default." + animation;
    }

    public RenderLayer getRenderType(){
        return RenderLayer.getEntityTranslucent(getTexture(getWorld()));
    }
}

package net.zephyr.fnafur.entity.animatronic.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;
import java.util.Random;

public class AnimatronicBlockEntity extends GeoPropBlockEntity{
    int blinkDelay;
    public AnimatronicBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.ANIMATRONIC_BLOCK, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 3, this::mainController));
        controllers.add(new AnimationController<>(this, "lower", 3, this::lowerController));
        controllers.add(new AnimationController<>(this, "blink", 0, this::blinkController));
    }

    private PlayState mainController(AnimationState<AnimatronicBlockEntity> geoPropBlockEntityAnimationState) {
        BlockState state = getWorld().getBlockState(getPos()).isOf(BlockInit.ANIMATRONIC_BLOCK) ? getWorld().getBlockState(getPos()) : BlockInit.ANIMATRONIC_BLOCK.getDefaultState();
        String anim = ((DemoAnimationList)state.get(((PropBlock)state.getBlock()).COLOR_PROPERTY())).getMain();
        return geoPropBlockEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(prefixAnim(anim)));
    }

    private PlayState lowerController(AnimationState<AnimatronicBlockEntity> animatronicBlockEntityAnimationState) {
        BlockState state = getWorld().getBlockState(getPos()).isOf(BlockInit.ANIMATRONIC_BLOCK) ? getWorld().getBlockState(getPos()) : BlockInit.ANIMATRONIC_BLOCK.getDefaultState();
        String anim = ((DemoAnimationList)state.get(((PropBlock)state.getBlock()).COLOR_PROPERTY())).getLower();
        if(anim != null){
            return animatronicBlockEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop(prefixAnim(anim)));
        }
        return PlayState.STOP;
    }

    private PlayState blinkController(AnimationState<AnimatronicBlockEntity> animatronicBlockEntityAnimationState) {

        BlockState state = getWorld().getBlockState(getPos()).isOf(BlockInit.ANIMATRONIC_BLOCK) ? getWorld().getBlockState(getPos()) : BlockInit.ANIMATRONIC_BLOCK.getDefaultState();

        if(((DemoAnimationList)state.get(((PropBlock)state.getBlock()).COLOR_PROPERTY())).blinks()) {

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
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt");
            String texture = nbt.getString("texture");
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
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt");
            String model = nbt.getString("model");
            if(!model.isEmpty()){
                return Identifier.of(FnafUniverseRebuilt.MOD_ID, model);
            }
        }

        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geo/entity/default/endo_01/endo_01.geo.json");
    }

    public Identifier getReRenderModel(World world){
        return getModel(world);
    }
    public Identifier getAnimations(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("alt")){
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt");
            String animations = nbt.getString("animations");
            if(!animations.isEmpty()){
                return Identifier.of(FnafUniverseRebuilt.MOD_ID, animations);
            }
        }

        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "animations/entity/classic/cl_fred/cl_fred.animation.json");
    }

    public String prefixAnim(String animation){
        Identifier location = getAnimations(getWorld());
        BakedAnimations bakedAnimations = GeckoLibCache.getBakedAnimations().get(location);

        NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt");
        String name = nbt.getString("chara");
        String anim = "animation." + name + "." + animation;
        if(bakedAnimations != null && bakedAnimations.animations().containsKey(anim)) {

            return anim;
        }

        return "animation.cl_fred." + animation;
    }

    public RenderLayer getRenderType(){
        return RenderLayer.getEntityTranslucent(getTexture(getWorld()));
    }
}

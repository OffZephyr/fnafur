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
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AnimatronicBlockEntity extends GeoPropBlockEntity{
    public AnimatronicBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.ANIMATRONIC_BLOCK, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 0, this::mainController));
    }

    private PlayState mainController(AnimationState<AnimatronicBlockEntity> geoPropBlockEntityAnimationState) {
        BlockState state = getWorld().getBlockState(getPos());
        return PlayState.CONTINUE;
    }
    public Identifier getTexture(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("alt")){
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt");
            String texture = nbt.getString("texture");
            if(!texture.isEmpty()){
                return Identifier.of(FnafUniverseResuited.MOD_ID, texture);
            }
        }

        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/default/endo_01/endo_01.png");
    }
    public Identifier getReRenderTexture(World world){
        return getTexture(world);
    }
    public Identifier getModel(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("alt")){
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt");
            String model = nbt.getString("model");
            if(!model.isEmpty()){
                return Identifier.of(FnafUniverseResuited.MOD_ID, model);
            }
        }

        return Identifier.of(FnafUniverseResuited.MOD_ID, "geo/entity/default/endo_01/endo_01.geo.json");
    }

    public Identifier getReRenderModel(World world){
        return getModel(world);
    }
    public Identifier getAnimations(World world){

        if(((IEntityDataSaver)this).getPersistentData().contains("alt")){
            NbtCompound nbt = ((IEntityDataSaver)this).getPersistentData().getCompound("alt");
            String animations = nbt.getString("animations");
            if(!animations.isEmpty()){
                return Identifier.of(FnafUniverseResuited.MOD_ID, animations);
            }
        }

        return Identifier.of(FnafUniverseResuited.MOD_ID, "animations/entity/classic/cl_fred/cl_fred.animation.json");
    }
    public RenderLayer getRenderType(){
        return RenderLayer.getEntityTranslucent(getTexture(getWorld()));
    }
}

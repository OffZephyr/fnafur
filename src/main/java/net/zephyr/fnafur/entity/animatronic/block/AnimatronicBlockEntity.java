package net.zephyr.fnafur.entity.animatronic.block;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader.ChipReaderBlockEntity;
import net.zephyr.fnafur.blocks.utility_blocks.server_monitor.ServerMonitorBlockEntity;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.networking.block.LinkVisualUpdateS2CPayload;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.cache.GeckoLibResources;
import software.bernie.geckolib.loading.object.BakedAnimations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AnimatronicBlockEntity extends GeoPropBlockEntity implements LinkTarget {
    int updateDepth = 0;
    public List<IEntityDataSaver> sources = new ArrayList<>();
    int blinkDelay;
    public BlockState previewState = null;
    public AnimatronicBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, true);
    }
    public AnimatronicBlockEntity(BlockPos pos, BlockState state, boolean createLink) {
        super(BlockEntityInit.ANIMATRONIC_BLOCK, pos, state);
        if(createLink){
            LinkTarget.allTargets.add((IEntityDataSaver) this);
        }
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

        if(!world.isClient()){
            for(IEntityDataSaver ent : getSources()){
                if(ent instanceof ServerMonitorBlockEntity ent2){
                    BlockPos readerPos = ent2.getActiveChipReaderPos();
                    if(readerPos != null && world.getBlockEntity(readerPos) instanceof ChipReaderBlockEntity ent3){
                        if(!((IEntityDataSaver)ent3).getPersistentData().getCompound("cpu").orElse(new NbtCompound()).isEmpty()){
                            AnimatronicEntity anim = new AnimatronicEntity(EntityInit.ANIMATRONIC, world);
                            NbtCompound thisData = ((IEntityDataSaver)this).getPersistentData();
                            ((IEntityDataSaver)anim).getPersistentData().copyFrom(thisData);
                            double posx = blockPos.getX() + thisData.getFloat("xOffset", 0);
                            double posy = blockPos.getY() - 1 + thisData.getFloat("yOffset", 0);
                            double posz = blockPos.getZ() + thisData.getFloat("zOffset", 0);
                            float yaw = thisData.getFloat("Rotation", 0) + 180;
                            anim.setPos(posx, posy, posz);
                            anim.setHeadYaw(yaw);
                            anim.setBodyYaw(yaw);
                            anim.setYaw(yaw);
                            anim.setAngles(yaw, 0);
                            world.spawnEntity(anim);

                            ((LinkTarget)anim).getSources().add(ent);
                            ent2.getTargets().add((IEntityDataSaver) anim);
                            ent2.markDirty();
                            ((IEntityDataSaver) anim).getPersistentData().putString("getupAnim", world.getBlockState(pos).get(AnimatronicBlock.POSES).getMain() + "activate");
                            GoopyNetworkingUtils.saveEntityNbt(anim.getId(), ((IEntityDataSaver)anim).getPersistentData(), world);
                            ent2.updateSources(world, ent2.getPos());
                            world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                            break;
                        }
                    }
                }
            }
        }
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

    @Override
    public void markRemoved() {
        super.markRemoved();

        cleanSources();
    }

    @Override
    public List<IEntityDataSaver> getSources() {
        return sources;
    }

    @Override
    public int getUpdateDepth() {
        return updateDepth;
    }

    @Override
    public void setUpdateDepth(int depth) {
        updateDepth = depth;
    }
}

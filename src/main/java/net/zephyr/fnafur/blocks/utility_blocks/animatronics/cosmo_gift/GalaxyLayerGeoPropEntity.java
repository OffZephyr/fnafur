package net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.object.PlayState;
import software.bernie.geckolib.animation.state.AnimationTest;
import software.bernie.geckolib.animation.*;

public class GalaxyLayerGeoPropEntity extends GeoPropBlockEntity implements GeoBlockEntity {
    public GalaxyLayerGeoPropEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.GALAXY_GEO_PROPS, pos, state);
    }
    public GalaxyLayerGeoPropEntity(BlockPos pos, BlockState state, GeoPropBlock block) {
        this(pos, state);
        this.block = block;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("spawn", 0, this::spawnController));
        controllers.add(new AnimationController<>("idle", 0, this::idleController));
    }

    private PlayState spawnController(AnimationTest<GeoAnimatable> galaxyLayerGeoPropEntityAnimationState) {
        if(item) {
            item = false;
            return PlayState.CONTINUE;
        }
        return galaxyLayerGeoPropEntityAnimationState.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.cosmo_gift.spawn"));
    }
    private PlayState idleController(AnimationTest<GeoAnimatable> galaxyLayerGeoPropEntityAnimationState) {
        return galaxyLayerGeoPropEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop("animation.cosmo_gift.idle"));
    }

    @Override
    public Identifier getReRenderModel(Level world) {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/giftbox_overlay");
    }

    @Override
    public Identifier getReRenderTexture(Level world) {
        return super.getReRenderTexture(world);
    }
}

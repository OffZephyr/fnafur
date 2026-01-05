package net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift;

import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    public Identifier getReRenderModel(World world) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/props/giftbox_overlay");
    }

    @Override
    public Identifier getReRenderTexture(World world) {
        return super.getReRenderTexture(world);
    }
}

package net.zephyr.fnafur.blocks.utility_blocks.cosmo_gift;

import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

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
        controllers.add(new AnimationController<>(this, "spawn", 0, this::spawnController));
        controllers.add(new AnimationController<>(this, "idle", 0, this::idleController));
    }

    private PlayState spawnController(AnimationState<GalaxyLayerGeoPropEntity> galaxyLayerGeoPropEntityAnimationState) {
        if(item) {
            item = false;
            return PlayState.CONTINUE;
        }
        return galaxyLayerGeoPropEntityAnimationState.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.cosmo_gift.spawn"));
    }
    private PlayState idleController(AnimationState<GalaxyLayerGeoPropEntity> galaxyLayerGeoPropEntityAnimationState) {
        return galaxyLayerGeoPropEntityAnimationState.setAndContinue(RawAnimation.begin().thenLoop("animation.cosmo_gift.idle"));
    }

    @Override
    public Identifier getReRenderModel(World world) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geo/block/props/giftbox_overlay.geo.json");
    }

    @Override
    public Identifier getReRenderTexture(World world) {
        return super.getReRenderTexture(world);
    }
}

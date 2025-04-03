package net.zephyr.fnafur.blocks.utility_blocks.cosmo_gift;

import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
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
    public Identifier getReRenderModel(World world) {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/props/giftbox_overlay.geo.json");
    }

    @Override
    public Identifier getReRenderTexture(World world) {
        return super.getReRenderTexture(world);
    }
}

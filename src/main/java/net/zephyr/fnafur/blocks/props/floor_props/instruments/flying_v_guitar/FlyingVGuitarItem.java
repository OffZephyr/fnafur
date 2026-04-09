package net.zephyr.fnafur.blocks.props.floor_props.instruments.flying_v_guitar;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class FlyingVGuitarItem extends BlockItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FlyingVGuitarItem(Block block, Item.Properties settings) {
        super(block, settings);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean isPerspectiveAware() {
        return true;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private FlyingVGuitarItemRenderer renderer;

            @Override
            public FlyingVGuitarItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new FlyingVGuitarItemRenderer();

                return this.renderer;
            }
        });
    }
}

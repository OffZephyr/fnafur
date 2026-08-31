package net.zephyr.fnafur.blocks.props.floor_props.instruments.flying_v_guitar;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;

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

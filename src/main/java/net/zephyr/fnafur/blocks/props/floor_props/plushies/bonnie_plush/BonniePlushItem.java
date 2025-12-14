package net.zephyr.fnafur.blocks.props.floor_props.plushies.bonnie_plush;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.freddy_plush.FreddyPlushItemRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class BonniePlushItem extends BlockItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BonniePlushItem(Block block, Settings settings) {
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
            private BonniePlushItemRenderer renderer;

            @Override
            public BonniePlushItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new BonniePlushItemRenderer();

                return this.renderer;
            }
        });
    }
}

package net.zephyr.fnafur.item.masks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class VanniMaskItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public VanniMaskItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isPerspectiveAware() { // state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE).
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        System.out.println(world.isClient() + " " + user.getInventory().getStack(46));
        return super.use(world, user, hand);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private VanniMaskItemRenderer renderer;

            @Override
            public VanniMaskItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new VanniMaskItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

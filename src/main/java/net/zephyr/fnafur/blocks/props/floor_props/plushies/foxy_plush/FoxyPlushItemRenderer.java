package net.zephyr.fnafur.blocks.props.floor_props.plushies.foxy_plush;

import net.zephyr.fnafur.blocks.props.floor_props.plushies.freddy_plush.FreddyPlushItem;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.freddy_plush.FreddyPlushItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FoxyPlushItemRenderer extends GeoItemRenderer<FoxyPlushItem> {
    public FoxyPlushItemRenderer() {
        super(new FoxyPlushItemModel());
    }
}

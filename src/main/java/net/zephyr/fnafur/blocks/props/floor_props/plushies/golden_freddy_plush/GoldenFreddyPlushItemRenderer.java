package net.zephyr.fnafur.blocks.props.floor_props.plushies.golden_freddy_plush;

import net.zephyr.fnafur.blocks.props.floor_props.plushies.freddy_plush.FreddyPlushItem;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.freddy_plush.FreddyPlushItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GoldenFreddyPlushItemRenderer extends GeoItemRenderer<GoldenFreddyPlushItem> {
    public GoldenFreddyPlushItemRenderer() {
        super(new GoldenFreddyPlushItemModel());
    }
}

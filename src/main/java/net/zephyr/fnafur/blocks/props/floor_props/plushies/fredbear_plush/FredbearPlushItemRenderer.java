package net.zephyr.fnafur.blocks.props.floor_props.plushies.fredbear_plush;

import net.zephyr.fnafur.blocks.props.floor_props.plushies.freddy_plush.FreddyPlushItem;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.freddy_plush.FreddyPlushItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FredbearPlushItemRenderer extends GeoItemRenderer<FredbearPlushItem> {
    public FredbearPlushItemRenderer() {
        super(new FredbearPlushItemModel());
    }
}

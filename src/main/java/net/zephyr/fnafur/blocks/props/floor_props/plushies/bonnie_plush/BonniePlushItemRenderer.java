package net.zephyr.fnafur.blocks.props.floor_props.plushies.bonnie_plush;

import net.zephyr.fnafur.blocks.props.floor_props.plushies.freddy_plush.FreddyPlushItem;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.freddy_plush.FreddyPlushItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BonniePlushItemRenderer extends GeoItemRenderer<BonniePlushItem> {
    public BonniePlushItemRenderer() {
        super(new BonniePlushItemModel());
    }
}

package net.zephyr.fnafur.blocks.props.floor_props.plushies.chica_plush;

import net.zephyr.fnafur.blocks.props.floor_props.plushies.bonnie_plush.BonniePlushItem;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.bonnie_plush.BonniePlushItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ChicaPlushItemRenderer extends GeoItemRenderer<ChicaPlushItem> {
    public ChicaPlushItemRenderer() {
        super(new ChicaPlushItemModel());
    }
}

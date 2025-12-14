package net.zephyr.fnafur.blocks.props.floor_props.plushies.freddy_plush;

import net.zephyr.fnafur.blocks.props.floor_props.instruments.flying_v_guitar.FlyingVGuitarItem;
import net.zephyr.fnafur.blocks.props.floor_props.instruments.flying_v_guitar.FlyingVGuitarItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FreddyPlushItemRenderer extends GeoItemRenderer<FreddyPlushItem> {
    public FreddyPlushItemRenderer() {
        super(new FreddyPlushItemModel());
    }
}

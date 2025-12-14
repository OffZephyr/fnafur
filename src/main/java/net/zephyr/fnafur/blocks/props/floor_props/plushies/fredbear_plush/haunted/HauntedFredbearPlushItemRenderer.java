package net.zephyr.fnafur.blocks.props.floor_props.plushies.fredbear_plush.haunted;

import net.zephyr.fnafur.blocks.props.floor_props.plushies.fredbear_plush.FredbearPlushItem;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.fredbear_plush.FredbearPlushItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class HauntedFredbearPlushItemRenderer extends GeoItemRenderer<HauntedFredbearPlushItem> {
    public HauntedFredbearPlushItemRenderer() {
        super(new HauntedFredbearPlushItemModel());
    }
}

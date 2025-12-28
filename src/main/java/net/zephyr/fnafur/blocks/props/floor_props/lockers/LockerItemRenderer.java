package net.zephyr.fnafur.blocks.props.floor_props.lockers;

import net.zephyr.fnafur.blocks.props.floor_props.plushies.bonnie_plush.BonniePlushItem;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.bonnie_plush.BonniePlushItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class LockerItemRenderer extends GeoItemRenderer<LockerItem> {
    public LockerItemRenderer() {
        super(new LockerItemModel());
    }
}

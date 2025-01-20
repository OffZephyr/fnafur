package net.zephyr.fnafur.blocks;

import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.zephyr.fnafur.item.tools.TapeMesurerItem;

public interface CallableByMesurer {

    public ActionResult ExecuteAction(ItemUsageContext context);

}

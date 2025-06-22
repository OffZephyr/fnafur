package net.zephyr.fnafur.blocks;

import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public interface CallableByMesurer {

    public ActionResult ExecuteAction(ItemUsageContext context);

}

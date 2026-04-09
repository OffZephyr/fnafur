package net.zephyr.fnafur.blocks;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;

public interface CallableByMesurer {

    public InteractionResult ExecuteAction(UseOnContext context);

}

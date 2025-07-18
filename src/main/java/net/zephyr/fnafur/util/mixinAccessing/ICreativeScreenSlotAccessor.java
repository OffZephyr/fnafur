package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.screen.slot.Slot;

public interface ICreativeScreenSlotAccessor {
    Slot createCreativeSlot(Slot slot, int invSlot, int x, int y);
}

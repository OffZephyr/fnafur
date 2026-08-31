package net.zephyr.fnafur.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.util.ItemUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @Shadow
    private ItemStack carried = ItemStack.EMPTY;
    @Inject(method = "getCarried", at = @At("HEAD"), cancellable = true)
    void getCursorStack(CallbackInfoReturnable<ItemStack> ci){
        if(carried.getItem() instanceof VanniMaskItem){
            CompoundTag nbt = ItemUtil.getNbt(carried);
            nbt.putBoolean("inVanniMask", false);
            ItemUtil.setNbt(carried, nbt);
        }
    }
}

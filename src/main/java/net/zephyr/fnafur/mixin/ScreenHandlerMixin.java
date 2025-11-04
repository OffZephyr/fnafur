package net.zephyr.fnafur.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.util.ItemUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Shadow
    private ItemStack cursorStack = ItemStack.EMPTY;
    @Inject(method = "getCursorStack", at = @At("HEAD"), cancellable = true)
    void getCursorStack(CallbackInfoReturnable<ItemStack> ci){
        if(cursorStack.getItem() instanceof VanniMaskItem){
            NbtCompound nbt = ItemUtil.getNbt(cursorStack);
            nbt.putBoolean("inVanniMask", false);
            ItemUtil.setNbt(cursorStack, nbt);
        }
    }
}

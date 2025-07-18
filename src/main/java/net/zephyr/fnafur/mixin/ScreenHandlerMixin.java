package net.zephyr.fnafur.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.util.ItemNbtUtil;
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
            NbtCompound nbt = ItemNbtUtil.getNbt(cursorStack);
            nbt.putBoolean("inVanniMask", false);
            ItemNbtUtil.setNbt(cursorStack, nbt);
        }
    }
}

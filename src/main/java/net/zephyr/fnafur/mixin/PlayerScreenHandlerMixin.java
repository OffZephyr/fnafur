package net.zephyr.fnafur.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.item.DeathCoin;
import net.zephyr.fnafur.item.IllusionDisc;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerScreenHandler.class)
public class PlayerScreenHandlerMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    void fnafInit(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo callbackInfo) {

        ((PlayerScreenHandler) (Object) this).addSlot(
                new Slot(inventory, FnafInventoryScreen.SLOTS_OFFSET, 77, 8) {
                    @Override
                    public void setStack(ItemStack stack, ItemStack previousStack) {
                        super.setStack(stack, previousStack);
                    }

                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return stack.getItem() instanceof VanniMaskItem;
                    }

                    @Override
                    public Identifier getBackgroundSprite() {
                        return FnafInventoryScreen.EMPTY_AR_DEVICE_SLOT_TEXTURE;
                    }
                }
        );

        ((PlayerScreenHandler) (Object) this).addSlot(
                new Slot(inventory, FnafInventoryScreen.SLOTS_OFFSET + 1, 77, 26) {
                    @Override
                    public void setStack(ItemStack stack, ItemStack previousStack) {
                        super.setStack(stack, previousStack);
                    }

                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return stack.getItem() instanceof IllusionDisc;
                    }

                    @Override
                    public Identifier getBackgroundSprite() {
                        return FnafInventoryScreen.EMPTY_ILLUSION_DISC_SLOT_TEXTURE;
                    }
                }

        );
    }

}

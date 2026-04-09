package net.zephyr.fnafur.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.item.DeathCoin;
import net.zephyr.fnafur.item.IllusionDisc;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public class InventoryMenuMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    void fnafInit(Inventory inventory, boolean onServer, Player owner, CallbackInfo callbackInfo) {

        ((InventoryMenu) (Object) this).addSlot(
                new Slot(inventory, FnafInventoryScreen.SLOTS_OFFSET, 77, 8) {
                    @Override
                    public void setByPlayer(ItemStack stack, ItemStack previousStack) {
                        super.setByPlayer(stack, previousStack);
                    }

                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return stack.getItem() instanceof VanniMaskItem;
                    }

                    @Override
                    public Identifier getNoItemIcon() {
                        return FnafInventoryScreen.EMPTY_AR_DEVICE_SLOT_TEXTURE;
                    }
                }
        );

        ((InventoryMenu) (Object) this).addSlot(
                new Slot(inventory, FnafInventoryScreen.SLOTS_OFFSET + 1, 77, 26) {
                    @Override
                    public void setByPlayer(ItemStack stack, ItemStack previousStack) {
                        super.setByPlayer(stack, previousStack);
                    }

                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return stack.getItem() instanceof IllusionDisc;
                    }

                    @Override
                    public Identifier getNoItemIcon() {
                        return FnafInventoryScreen.EMPTY_ILLUSION_DISC_SLOT_TEXTURE;
                    }
                }

        );
    }

}

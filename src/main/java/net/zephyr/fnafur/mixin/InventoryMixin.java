package net.zephyr.fnafur.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.NonNullList;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class InventoryMixin {
    @Shadow
    private NonNullList<ItemStack> items;
    @Shadow
    public Player player;
    @Shadow
    public static Int2ObjectMap<EquipmentSlot> EQUIPMENT_SLOT_MAPPING;

    @Shadow
    public ItemStack getItem(int slot){
        return null;
    }
    @Shadow
    public void setItem(int slot, ItemStack stack) {

    }
    @Shadow
    public int getFreeSlot() {
        return 0;
    }
    @Shadow
    public int addResource(ItemStack stack) {
        return 0;
    }
    @Shadow
    public int addResource(int slot, ItemStack stack) {
        return 0;
    }

    @Unique
    private final NonNullList<ItemStack> extra = NonNullList.withSize(2, ItemStack.EMPTY);

    @Inject(method = "setItem", at = @At("HEAD"))
    void setStack(int slot, ItemStack stack, CallbackInfo ci) {

        if (slot >= FnafInventoryScreen.SLOTS_OFFSET) {
            this.extra.set(slot - FnafInventoryScreen.SLOTS_OFFSET, stack);
        }
    }
    @Inject(method = "findSlotMatchingItem", at = @At("HEAD"), cancellable = true)
    void getSlotWithStack(ItemStack stack, CallbackInfoReturnable<Integer> ci) {
        for (int i = 0; i < this.extra.size(); i++) {
            if (!this.extra.get(i).isEmpty() && ItemStack.isSameItemSameComponents(stack, this.extra.get(i))) {
                ci.setReturnValue(i + FnafInventoryScreen.SLOTS_OFFSET);
            }
        }
    }
    @Inject(method = "findSlotMatchingCraftingIngredient", at = @At("HEAD"), cancellable = true)
    void getMatchingSlot(Holder<Item> item, ItemStack stack, CallbackInfoReturnable<Integer> ci) {
        for (int i = 0; i < this.extra.size(); i++) {
            ItemStack itemStack = this.extra.get(i);
            if (!itemStack.isEmpty()
                    && itemStack.is(item)
                    && Inventory.isUsableForCrafting(itemStack)
                    && (stack.isEmpty() || ItemStack.isSameItemSameComponents(stack, itemStack))) {
                ci.setReturnValue(i + FnafInventoryScreen.SLOTS_OFFSET);
            }
        }
    }
    @Inject(method = "tick", at = @At("HEAD"))
    void updateItems(CallbackInfo ci) {
        for (int i = 0; i < this.extra.size(); i++) {
            ItemStack itemStack = this.getItem(FnafInventoryScreen.SLOTS_OFFSET + i);
            if (!itemStack.isEmpty()) {
                itemStack.inventoryTick(this.player.level(), this.player, null);
            }
        }
    }
    @Inject(method = "removeItem(II)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    void removeStack(int slot, int amount, CallbackInfoReturnable<ItemStack> ci) {
        if (slot >= FnafInventoryScreen.SLOTS_OFFSET) {
            ci.setReturnValue(ContainerHelper.removeItem(this.extra, slot - FnafInventoryScreen.SLOTS_OFFSET, amount));
        }
    }
    @Inject(method = "removeItem(Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"))
    void removeOne(ItemStack stack, CallbackInfo ci) {
        for (int i = 0; i < this.extra.size(); i++) {
            if (this.extra.get(i) == stack) {
                this.extra.set(i, ItemStack.EMPTY);
                return;
            }
        }
    }
    @Inject(method = "removeItemNoUpdate(I)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    void removeStack(int slot, CallbackInfoReturnable<ItemStack> ci) {
        if (slot >= FnafInventoryScreen.SLOTS_OFFSET) {
            ItemStack itemStack = this.extra.get(slot - FnafInventoryScreen.SLOTS_OFFSET);
            this.extra.set(slot - FnafInventoryScreen.SLOTS_OFFSET, ItemStack.EMPTY);
            ci.setReturnValue(itemStack);
        }
    }
    @Inject(method = "save", at = @At("HEAD"))
    public void writeData(ValueOutput.TypedOutputList<ItemStackWithSlot> list, CallbackInfo ci) {
        for (int i = 0; i < this.extra.size(); i++) {
            ItemStack itemStack = this.extra.get(i);
            if (!itemStack.isEmpty()) {
                list.add(new ItemStackWithSlot(i + FnafInventoryScreen.SLOTS_OFFSET, itemStack));
            }
        }
    }

    @Inject(method = "load", at = @At("HEAD"), cancellable = true)
    public void readData(ValueInput.TypedInputList<ItemStackWithSlot> list, CallbackInfo ci) {
        this.items.clear();
        this.extra.clear();

        for (ItemStackWithSlot stackWithSlot : list) {
            if (stackWithSlot.isValidInContainer(this.items.size())) {
                this.setItem(stackWithSlot.slot(), stackWithSlot.stack());
            }
            if (stackWithSlot.isValidInContainer(this.extra.size() + FnafInventoryScreen.SLOTS_OFFSET)) {
                this.setItem(stackWithSlot.slot(), stackWithSlot.stack());
            }
        }
        ci.cancel();
    }
    @Inject(method = "getContainerSize", at = @At("HEAD"), cancellable = true)
    void size(CallbackInfoReturnable<Integer> ci) {
        ci.setReturnValue(FnafInventoryScreen.SLOTS_OFFSET + 2);
    }
    @Inject(method = "getItem", at = @At("HEAD"), cancellable = true)
    void getStack(int slot, CallbackInfoReturnable<ItemStack> ci) {

        if (slot >= FnafInventoryScreen.SLOTS_OFFSET) {
            ci.setReturnValue(this.extra.get(slot - FnafInventoryScreen.SLOTS_OFFSET));
        }
    }
    @Inject(method = "dropAll", at = @At("HEAD"))
    void dropAll(CallbackInfo ci) {
        for (int i = 0; i < this.extra.size(); i++) {
            ItemStack itemStack = this.extra.get(i);
            if (!itemStack.isEmpty()) {
                this.player.drop(itemStack, true, false);
                this.extra.set(i, ItemStack.EMPTY);
            }
        }
    }
    @Inject(method = "isEmpty", at = @At("HEAD"), cancellable = true)
    void isEmpty(CallbackInfoReturnable<Boolean> ci) {

        for (ItemStack itemStack : this.extra) {
            if (!itemStack.isEmpty()) {
                ci.setReturnValue(false);
            }
        }
    }
    @Inject(method = "clearContent", at = @At("HEAD"))
    void clear(CallbackInfo ci) {
        this.extra.clear();
    }

    @Inject(method = "add(ILnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    void insertStack(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
        if (stack.isEmpty()) {
            ci.setReturnValue(false);
        } else {
            try {
                if (stack.isDamaged()) {
                    if (slot == -1) {
                        slot = this.getFreeSlot();
                    }

                    if (slot >= 0 && slot < FnafInventoryScreen.SLOTS_OFFSET) {
                        this.items.set(slot, stack.copyAndClear());
                        this.items.get(slot).setPopTime(5);
                        ci.setReturnValue(true);
                    }
                    else if (slot >= FnafInventoryScreen.SLOTS_OFFSET) {
                        this.extra.set(slot - FnafInventoryScreen.SLOTS_OFFSET, stack.copyAndClear());
                        this.extra.get(slot - FnafInventoryScreen.SLOTS_OFFSET).setPopTime(5);
                        ci.setReturnValue(true);
                    } else if (this.player.hasInfiniteMaterials()) {
                        stack.setCount(0);
                        ci.setReturnValue(true);
                    } else {
                        ci.setReturnValue(true);
                    }
                } else {
                    int i;
                    do {
                        i = stack.getCount();
                        if (slot == -1) {
                            stack.setCount(this.addResource(stack));
                        } else {
                            stack.setCount(this.addResource(slot, stack));
                        }
                    } while (!stack.isEmpty() && stack.getCount() < i);

                    if (stack.getCount() == i && this.player.hasInfiniteMaterials()) {
                        stack.setCount(0);
                        ci.setReturnValue(true);
                    } else {
                        ci.setReturnValue(stack.getCount() < i);
                    }
                }
            } catch (Throwable var6) {
                CrashReport crashReport = CrashReport.forThrowable(var6, "Adding item to inventory");
                CrashReportCategory crashReportSection = crashReport.addCategory("Item being added");
                crashReportSection.setDetail("Item ID", Item.getId(stack.getItem()));
                crashReportSection.setDetail("Item data", stack.getDamageValue());
                crashReportSection.setDetail("Item name", (CrashReportDetail<String>)(() -> stack.getHoverName().getString()));
                throw new ReportedException(crashReport);
            }
        }
    }

}

package net.zephyr.fnafur.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.StackWithSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.crash.CrashCallable;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Shadow
    private DefaultedList<ItemStack> main;
    @Shadow
    public PlayerEntity player;
    @Shadow
    public static Int2ObjectMap<EquipmentSlot> EQUIPMENT_SLOTS;

    @Shadow
    public ItemStack getStack(int slot){
        return null;
    }
    @Shadow
    public void setStack(int slot, ItemStack stack) {

    }
    @Shadow
    public int getEmptySlot() {
        return 0;
    }
    @Shadow
    public int addStack(ItemStack stack) {
        return 0;
    }
    @Shadow
    public int addStack(int slot, ItemStack stack) {
        return 0;
    }

    @Unique
    private final DefaultedList<ItemStack> extra = DefaultedList.ofSize(2, ItemStack.EMPTY);

    @Inject(method = "setStack", at = @At("HEAD"))
    void setStack(int slot, ItemStack stack, CallbackInfo ci) {

        if (slot >= FnafInventoryScreen.SLOTS_OFFSET) {
            this.extra.set(slot - FnafInventoryScreen.SLOTS_OFFSET, stack);
        }
    }
    @Inject(method = "getSlotWithStack", at = @At("HEAD"), cancellable = true)
    void getSlotWithStack(ItemStack stack, CallbackInfoReturnable<Integer> ci) {
        for (int i = 0; i < this.extra.size(); i++) {
            if (!this.extra.get(i).isEmpty() && ItemStack.areItemsAndComponentsEqual(stack, this.extra.get(i))) {
                ci.setReturnValue(i + FnafInventoryScreen.SLOTS_OFFSET);
            }
        }
    }
    @Inject(method = "getMatchingSlot", at = @At("HEAD"), cancellable = true)
    void getMatchingSlot(RegistryEntry<Item> item, ItemStack stack, CallbackInfoReturnable<Integer> ci) {
        for (int i = 0; i < this.extra.size(); i++) {
            ItemStack itemStack = this.extra.get(i);
            if (!itemStack.isEmpty()
                    && itemStack.itemMatches(item)
                    && PlayerInventory.usableWhenFillingSlot(itemStack)
                    && (stack.isEmpty() || ItemStack.areItemsAndComponentsEqual(stack, itemStack))) {
                ci.setReturnValue(i + FnafInventoryScreen.SLOTS_OFFSET);
            }
        }
    }
    @Inject(method = "updateItems", at = @At("HEAD"))
    void updateItems(CallbackInfo ci) {
        for (int i = 0; i < this.extra.size(); i++) {
            ItemStack itemStack = this.getStack(FnafInventoryScreen.SLOTS_OFFSET + i);
            if (!itemStack.isEmpty()) {
                itemStack.inventoryTick(this.player.getEntityWorld(), this.player, null);
            }
        }
    }
    @Inject(method = "removeStack(II)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    void removeStack(int slot, int amount, CallbackInfoReturnable<ItemStack> ci) {
        if (slot >= FnafInventoryScreen.SLOTS_OFFSET) {
            ci.setReturnValue(Inventories.splitStack(this.extra, slot - FnafInventoryScreen.SLOTS_OFFSET, amount));
        }
    }
    @Inject(method = "removeOne", at = @At("HEAD"))
    void removeOne(ItemStack stack, CallbackInfo ci) {
        for (int i = 0; i < this.extra.size(); i++) {
            if (this.extra.get(i) == stack) {
                this.extra.set(i, ItemStack.EMPTY);
                return;
            }
        }
    }
    @Inject(method = "removeStack(I)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    void removeStack(int slot, CallbackInfoReturnable<ItemStack> ci) {
        if (slot >= FnafInventoryScreen.SLOTS_OFFSET) {
            ItemStack itemStack = this.extra.get(slot - FnafInventoryScreen.SLOTS_OFFSET);
            this.extra.set(slot - FnafInventoryScreen.SLOTS_OFFSET, ItemStack.EMPTY);
            ci.setReturnValue(itemStack);
        }
    }
    @Inject(method = "writeData", at = @At("HEAD"))
    public void writeData(WriteView.ListAppender<StackWithSlot> list, CallbackInfo ci) {
        for (int i = 0; i < this.extra.size(); i++) {
            ItemStack itemStack = this.extra.get(i);
            if (!itemStack.isEmpty()) {
                list.add(new StackWithSlot(i + FnafInventoryScreen.SLOTS_OFFSET, itemStack));
            }
        }
    }

    @Inject(method = "readData", at = @At("HEAD"), cancellable = true)
    public void readData(ReadView.TypedListReadView<StackWithSlot> list, CallbackInfo ci) {
        this.main.clear();
        this.extra.clear();

        for (StackWithSlot stackWithSlot : list) {
            if (stackWithSlot.isValidSlot(this.main.size())) {
                this.setStack(stackWithSlot.slot(), stackWithSlot.stack());
            }
            if (stackWithSlot.isValidSlot(this.extra.size() + FnafInventoryScreen.SLOTS_OFFSET)) {
                this.setStack(stackWithSlot.slot(), stackWithSlot.stack());
            }
        }
        ci.cancel();
    }
    @Inject(method = "size", at = @At("HEAD"), cancellable = true)
    void size(CallbackInfoReturnable<Integer> ci) {
        ci.setReturnValue(FnafInventoryScreen.SLOTS_OFFSET + 2);
    }
    @Inject(method = "getStack", at = @At("HEAD"), cancellable = true)
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
                this.player.dropItem(itemStack, true, false);
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
    @Inject(method = "clear", at = @At("HEAD"))
    void clear(CallbackInfo ci) {
        this.extra.clear();
    }

    @Inject(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    void insertStack(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
        if (stack.isEmpty()) {
            ci.setReturnValue(false);
        } else {
            try {
                if (stack.isDamaged()) {
                    if (slot == -1) {
                        slot = this.getEmptySlot();
                    }

                    if (slot >= 0 && slot < FnafInventoryScreen.SLOTS_OFFSET) {
                        this.main.set(slot, stack.copyAndEmpty());
                        this.main.get(slot).setBobbingAnimationTime(5);
                        ci.setReturnValue(true);
                    }
                    else if (slot >= FnafInventoryScreen.SLOTS_OFFSET) {
                        this.extra.set(slot - FnafInventoryScreen.SLOTS_OFFSET, stack.copyAndEmpty());
                        this.extra.get(slot - FnafInventoryScreen.SLOTS_OFFSET).setBobbingAnimationTime(5);
                        ci.setReturnValue(true);
                    } else if (this.player.isInCreativeMode()) {
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
                            stack.setCount(this.addStack(stack));
                        } else {
                            stack.setCount(this.addStack(slot, stack));
                        }
                    } while (!stack.isEmpty() && stack.getCount() < i);

                    if (stack.getCount() == i && this.player.isInCreativeMode()) {
                        stack.setCount(0);
                        ci.setReturnValue(true);
                    } else {
                        ci.setReturnValue(stack.getCount() < i);
                    }
                }
            } catch (Throwable var6) {
                CrashReport crashReport = CrashReport.create(var6, "Adding item to inventory");
                CrashReportSection crashReportSection = crashReport.addElement("Item being added");
                crashReportSection.add("Item ID", Item.getRawId(stack.getItem()));
                crashReportSection.add("Item data", stack.getDamage());
                crashReportSection.add("Item name", (CrashCallable<String>)(() -> stack.getName().getString()));
                throw new CrashException(crashReport);
            }
        }
    }

}

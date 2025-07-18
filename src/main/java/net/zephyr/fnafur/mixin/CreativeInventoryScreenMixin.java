package net.zephyr.fnafur.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.HotbarStorage;
import net.minecraft.client.option.HotbarStorageEntry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;
import net.zephyr.fnafur.client.gui.screens.FnafCreativeInventoryScreen;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.util.mixinAccessing.ICreativeScreenSlotAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * When drawing a mirror, always use the mirror's framebuffer instead of the normal one.
 */
@Mixin(net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin implements ICreativeScreenSlotAccessor {
    @Unique
    ClientPlayerEntity mixin$player;
    @Unique
    FeatureSet mixin$enabledFeatures;
    @Unique
    boolean mixin$operatorTabEnabled;
    @Shadow
    static final SimpleInventory INVENTORY = new SimpleInventory(45);
    @Shadow
    private List<Slot> slots;
    @Shadow
    private Slot deleteItemSlot;
    @Shadow
    private static ItemGroup selectedTab = ItemGroups.getDefaultTab();
    @Shadow
    private TextFieldWidget searchBox;
    @Shadow
    private float scrollPosition;
    @Shadow
    private void search() {

    }

    @Inject(method = "<init>", at = @At("TAIL"))
    void fnafCreativeTabInit(ClientPlayerEntity player, FeatureSet enabledFeatures, boolean operatorTabEnabled, CallbackInfo callbackInfo){
        mixin$player = player;
        mixin$enabledFeatures = enabledFeatures;
        mixin$operatorTabEnabled = operatorTabEnabled;
    }
    @Inject(method = "handledScreenTick", at = @At("HEAD"), cancellable = true)
    void fnafCreativeTabTick(CallbackInfo callbackInfo){
        if(!(((CreativeInventoryScreen) (Object) this) instanceof FnafCreativeInventoryScreen)){
            MinecraftClient.getInstance().setScreen(new FnafCreativeInventoryScreen(mixin$player, mixin$enabledFeatures, mixin$operatorTabEnabled));
            callbackInfo.cancel();
        }
    }
    @Inject(method = "setSelectedTab", at = @At("HEAD"), cancellable = true)
    void fnafSetSelectedTab(ItemGroup group, CallbackInfo callbackInfo){
        if(((CreativeInventoryScreen) (Object) this) instanceof FnafCreativeInventoryScreen screen) {
            this.deleteItemSlot = new Slot(INVENTORY, 0, 173, 112);
            ItemGroup itemGroup = selectedTab;
            selectedTab = group;
            slots = screen.createSlots(selectedTab, itemGroup, slots, deleteItemSlot);

            if (selectedTab.getType() == ItemGroup.Type.SEARCH) {
                this.searchBox.setVisible(true);
                this.searchBox.setFocusUnlocked(false);
                this.searchBox.setFocused(true);
                if (itemGroup != group) {
                    this.searchBox.setText("");
                }

                this.search();
            } else {
                this.searchBox.setVisible(false);
                this.searchBox.setFocusUnlocked(true);
                this.searchBox.setFocused(false);
                this.searchBox.setText("");
            }

            this.scrollPosition = 0.0F;
            callbackInfo.cancel();
        }
    }

    @Override
    public Slot createCreativeSlot(Slot slot, int invSlot, int x, int y){
        return new CreativeInventoryScreen.CreativeSlot(slot, invSlot, x, y);
    }
}

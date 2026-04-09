package net.zephyr.fnafur.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.Slot;
import net.zephyr.fnafur.client.gui.screens.creative_menu.FnafCreativeInventoryScreen;
import net.zephyr.fnafur.util.mixinAccessing.ICreativeScreenSlotAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * When drawing a mirror, always use the mirror's framebuffer instead of the normal one.
 */
@Mixin(net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin implements ICreativeScreenSlotAccessor {
    @Unique
    LocalPlayer mixin$player;
    @Unique
    FeatureFlagSet mixin$enabledFeatures;
    @Unique
    boolean mixin$operatorTabEnabled;
    @Shadow
    static final SimpleContainer CONTAINER = new SimpleContainer(45);
    @Shadow
    private List<Slot> originalSlots;
    @Shadow
    private Slot destroyItemSlot;
    @Shadow
    private static CreativeModeTab selectedTab = CreativeModeTabs.getDefaultTab();
    @Shadow
    private EditBox searchBox;
    @Shadow
    private float scrollOffs;
    @Shadow
    private void refreshSearchResults() {

    }

    @Inject(method = "<init>", at = @At("TAIL"))
    void fnafCreativeTabInit(LocalPlayer player, FeatureFlagSet enabledFeatures, boolean operatorTabEnabled, CallbackInfo callbackInfo){
        mixin$player = player;
        mixin$enabledFeatures = enabledFeatures;
        mixin$operatorTabEnabled = operatorTabEnabled;
    }
    @Inject(method = "containerTick", at = @At("HEAD"), cancellable = true)
    void fnafCreativeTabTick(CallbackInfo callbackInfo){
        if(!(((CreativeModeInventoryScreen) (Object) this) instanceof FnafCreativeInventoryScreen)){
            Minecraft.getInstance().setScreen(new FnafCreativeInventoryScreen(mixin$player, mixin$enabledFeatures, mixin$operatorTabEnabled));
            callbackInfo.cancel();
        }
    }
    @Inject(method = "selectTab", at = @At("HEAD"), cancellable = true)
    void fnafSetSelectedTab(CreativeModeTab group, CallbackInfo callbackInfo){
        if(((CreativeModeInventoryScreen) (Object) this) instanceof FnafCreativeInventoryScreen screen) {
            this.destroyItemSlot = new Slot(CONTAINER, 0, 173, 112);
            CreativeModeTab itemGroup = selectedTab;
            selectedTab = group;
            originalSlots = screen.createSlots(selectedTab, itemGroup, originalSlots, destroyItemSlot);

            if (selectedTab.getType() == CreativeModeTab.Type.SEARCH) {
                this.searchBox.setVisible(true);
                this.searchBox.setCanLoseFocus(false);
                this.searchBox.setFocused(true);
                if (itemGroup != group) {
                    this.searchBox.setValue("");
                }

                this.refreshSearchResults();
            } else {
                this.searchBox.setVisible(false);
                this.searchBox.setCanLoseFocus(true);
                this.searchBox.setFocused(false);
                this.searchBox.setValue("");
            }

            this.scrollOffs = 0.0F;
            callbackInfo.cancel();
        }
    }

    @Override
    public Slot createCreativeSlot(Slot slot, int invSlot, int x, int y){
        return new CreativeModeInventoryScreen.SlotWrapper(slot, invSlot, x, y);
    }
}

package net.zephyr.fnafur.item.battery;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.zephyr.fnafur.client.freshUI.base.FUI_Screen;
import net.zephyr.fnafur.client.freshUI.components.FUI_QuickActionsMenu;

public class TestFreshUI extends Item {

    public TestFreshUI(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {

        if(clickType == ClickType.LEFT){
            System.out.println("loaded!");
            // Init the screen
            FUI_Screen myScreen = new FUI_Screen(Text.of("Hello"));
            MinecraftClient.getInstance().setScreen((Screen)myScreen);

            //Add & Create a button
            FUI_QuickActionsMenu b = new FUI_QuickActionsMenu();
            b.setAnchor(0.5f,0.5f);
            b.positionFromScreen(0.5f,0.5f, true);
            myScreen.addComponent(b);
        }


        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }
}

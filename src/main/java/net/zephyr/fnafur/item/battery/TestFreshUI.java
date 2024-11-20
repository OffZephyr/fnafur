package net.zephyr.fnafur.item.battery;

import com.ibm.icu.impl.CollectionSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.zephyr.fnafur.freshUI.Components.FUI_Button;
import net.zephyr.fnafur.freshUI.FUI_Component;
import net.zephyr.fnafur.freshUI.FUI_Screen;

import java.util.ArrayList;

public class TestFreshUI extends Item {

    FUI_Screen myScreen = null;

    public TestFreshUI(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {

        if(myScreen == null) {
            System.out.println("loaded!");
            myScreen = new FUI_Screen();
            FUI_Button b = new FUI_Button();
            b.sizeFromPixel(250,25, true);
            b.positionFromPixel(25,25, true);
            myScreen.components.add(b);
            myScreen.registerScreen();
        }

        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }
}

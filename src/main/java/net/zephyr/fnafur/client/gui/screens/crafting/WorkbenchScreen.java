package net.zephyr.fnafur.client.gui.screens.crafting;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;

public class WorkbenchScreen extends GoopyScreen {
    public WorkbenchScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }
}

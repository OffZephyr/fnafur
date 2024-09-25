package net.zephyr.fnafur.client.gui.screens.killscreens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class DefaultKillScreen extends AbstractKillScreen {

    public DefaultKillScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
    }

    @Override
    SoundEvent jumpscareSound() {
        return SoundEvents.ENTITY_GOAT_SCREAMING_AMBIENT;
    }

    @Override
    public void renderDeathImage(DrawContext context, int mouseX, int mouseY, float delta) {

    }

}

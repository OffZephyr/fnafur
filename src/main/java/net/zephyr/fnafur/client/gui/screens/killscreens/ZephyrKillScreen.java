package net.zephyr.fnafur.client.gui.screens.killscreens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.SoundsInit;

public class ZephyrKillScreen extends AbstractKillScreen {
    public ZephyrKillScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
    }

    @Override
    SoundEvent jumpscareSound() {
        return SoundsInit.ZEPHYR_JUMPSCARE;
    }

    @Override
    public void renderDeathImage(DrawContext context, int mouseX, int mouseY, float delta) {
        String textureName = entity.getSkin(skin).name;
        String textureTrimmed = textureName.replace("entity.fnafur.zephyr.", "");
        Identifier texture = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/killscreens/zephyr/" + textureTrimmed + ".png");

        drawRecolorableTexture(context, texture, 0, 0, 0, this.width, this.height, 0, 0, this.width, this.height, 1, 1, 1, 1);
    }
}

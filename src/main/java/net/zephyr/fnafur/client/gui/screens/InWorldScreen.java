package net.zephyr.fnafur.client.gui.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public abstract class InWorldScreen extends GoopyScreen {


    public InWorldScreen(Text title, NbtCompound nbt, long l) {
        super(title, nbt, l);
    }

    public InWorldScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
    }

    public abstract Vec3d getCameraPos();

    public abstract Vector3f getCameraAngle();
}

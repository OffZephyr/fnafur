package net.zephyr.fnafur.client.gui.screens;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public abstract class InWorldScreen extends GoopyScreen {


    public InWorldScreen(Component title, CompoundTag nbt, long l) {
        super(title, nbt, l);
    }

    public InWorldScreen(Component text, CompoundTag CompoundTag, Object o) {
        super(text, CompoundTag, o);
    }

    public abstract Vec3 getCameraPos();

    public abstract Vector3f getCameraAngle();
}

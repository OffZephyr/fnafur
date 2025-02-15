package net.zephyr.fnafur.blocks.utility_blocks.computer.Apps;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.util.Computer.ComputerApp;

public class RemoteApp extends ComputerApp {
    public RemoteApp(String name, Identifier iconTexture) {
        super(name, iconTexture);
    }

    @Override
    public void init() {
    }

    @Override
    public void tickWhenOpen() {
    }
    @Override
    public void tickAlways() {
    }
}

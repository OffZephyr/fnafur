package net.zephyr.fnafur.util.Computer;

import net.minecraft.util.Identifier;

public abstract class ComputerApp {
    String name;
    Identifier iconTexture;
    public ComputerApp(String name, Identifier iconTexture){
        this.name = name;
        this.iconTexture = iconTexture;
    }
    public String getName(){
        return this.name;
    }
    public Identifier getIconTexture(){
        return this.iconTexture;
    }

    public void init(){

    }
    public void tickWhenOpen(){

    }
    public void tickAlways(){

    }
}

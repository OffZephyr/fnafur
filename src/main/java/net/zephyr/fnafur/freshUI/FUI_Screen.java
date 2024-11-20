package net.zephyr.fnafur.freshUI;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class FUI_Screen {

    public List<FUI_Component> components;

    public FUI_Screen(List<FUI_Component> cps){
        components = cps;
        registerScreen();
    }

    public FUI_Screen(){
        components = new ArrayList<>();
    }

    public void registerScreen(){
        HudRenderCallback.EVENT.register((context, dt)->{
            for(FUI_Component c : components){
                c.render(context, dt);
            }
        });
    }

    public void DestroyCps(){
        components.clear();
    }

}

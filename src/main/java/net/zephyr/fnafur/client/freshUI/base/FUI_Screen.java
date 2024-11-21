package net.zephyr.fnafur.client.freshUI.base;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class FUI_Screen extends Screen {

    List<FUI_Component> components;
    MinecraftClient currentClient;

    public FUI_Screen(Text title) {
        super(title);
        currentClient = MinecraftClient.getInstance();
        components = new ArrayList<>();
    }

    public void addComponent(FUI_Component component){
        components.add(component);
        component.root = this;
    }

    public void destroy(){
        components.clear();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        for(FUI_Component c : components){
            c.call(context, currentClient, delta);
        }

        for(int i = 0; i < components.size();i++){
            if(components.get(i).kill) components.remove(components.get(i));
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(FUI_Component c : components){
            c.whenClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}

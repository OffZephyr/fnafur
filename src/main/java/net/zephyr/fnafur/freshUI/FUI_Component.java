package net.zephyr.fnafur.freshUI;

import net.minecraft.client.Mouse;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.Vector2f;
import org.joml.Vector2i;

import java.util.List;

public class FUI_Component implements Element {

    public List<FUI_Component> components;
    public Vector2f anchor = new Vector2f(0,0);
    public Vector2f screenPosition = new Vector2f(0,0), screenSize = new Vector2f(0,0);
    public Vector2i pixelPosition = new Vector2i(0,0), pixelSize = new Vector2i(100,100);

    public void tick(){}
    public void render(DrawContext context, RenderTickCounter dt){
        Vector2i finalPos = new Vector2i(
                (int) (pixelPosition.x + (context.getScaledWindowWidth() * screenPosition.getX()) + (anchor.getX() * pixelSize.x)),
                (int) (pixelPosition.y + (context.getScaledWindowHeight() * screenPosition.getY()) + (anchor.getX() * pixelSize.y))
        );
        Vector2i finalSize = new Vector2i(
                (int) (pixelSize.x + (context.getScaledWindowWidth() * screenSize.getX())),
                (int) (pixelSize.x + (context.getScaledWindowHeight() * screenSize.getY()))
        );


        tick();
        draw(context, finalPos, finalSize, dt);

        if(components == null) return;
        for(FUI_Component component : components){
            component.tick();
            component.render(context, dt);
        }

    }

    public void draw(DrawContext context, Vector2i finalPos, Vector2i finalSize, RenderTickCounter dt){ }

    @Override
    public void setFocused(boolean focused) {}

    @Override
    public boolean isFocused() {
        return false;
    }

    public void sizeFromPixel(int x, int y, boolean clearScreen){
        pixelSize = new Vector2i(x,y);
        if(clearScreen) screenSize = new Vector2f(0,0);
    }

    public void sizeFromScreen(float x, float y, boolean clearPixel){
        screenSize = new Vector2f(x,y);
        if(clearPixel) pixelSize = new Vector2i(0,0);
    }

    public void positionFromPixel(int x, int y, boolean clearScreen){
        pixelPosition = new Vector2i(x,y);
        if(clearScreen) screenPosition = new Vector2f(0,0);
    }

    public void positionFromScreen(float x, float y, boolean clearPixel){
        screenPosition = new Vector2f(x,y);
        if(clearPixel) pixelPosition = new Vector2i(0,0);
    }

}

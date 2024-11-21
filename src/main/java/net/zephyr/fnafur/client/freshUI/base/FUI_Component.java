package net.zephyr.fnafur.client.freshUI.base;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.Vector2f;
import org.joml.Vector2i;

import java.util.List;

@Environment(EnvType.CLIENT)
public class FUI_Component {

    public FUI_Screen root;
    public boolean kill;
    public List<FUI_Component> components;
    Vector2f anchor = new Vector2f(0,0);
    Vector2f screenPosition = new Vector2f(0,0), screenSize = new Vector2f(0,0);
    Vector2i pixelPosition = new Vector2i(0,0), pixelSize = new Vector2i(100,100);

    /// Called for the logic
    public void tick(MinecraftClient client){}

    /// Called for the drawing
    public void draw(DrawContext context, MinecraftClient client, float dt){ }

    /// Call the component (tick + draw) and it's children
    public void call(DrawContext context, MinecraftClient client, float dt){

        tick(client);
        draw(context, client, dt);
        if(components == null) return;
        for(FUI_Component component : components){
            if(component.kill) components.remove(component);
            component.call(context,client, dt);
        }

    }

    /// destroy this component (by passing 'kill')
    public void destroy(){
        kill = true;
    }

    /// when the user perform a click
    public void whenClicked(double x, double y, int button){}

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
    public void setAnchor(float x, float y){
        anchor = new Vector2f(x,y);
    }


    /// Know if this (xTest;yTest) is inside of the box (Min;Max)
    public static boolean isInside(int xTest, int yTest, int xMin, int yMin, int xMax, int yMax){
        return xTest >= xMin && xTest <= xMax && yTest >= yMin && yTest <= yMax;
    }

    /// Know if this (xTest;yTest) is inside of the box (Min;Max)
    public static boolean isInside(int xTest, int yTest, Vector2i pos, Vector2i siz){
        return isInside(xTest,yTest, pos.x, pos.y, pos.x+siz.x, pos.y+siz.y);
    }

    /// Get the mouse position into the HUD
    public static Vector2i getMousePosition(){
        MinecraftClient client = MinecraftClient.getInstance();
        int factorGui = client.getWindow()
                .calculateScaleFactor(MinecraftClient.getInstance().options.getGuiScale().getValue(), false);

        return new Vector2i(
                (int) client.mouse.getX() / factorGui,
                (int) client.mouse.getY() / factorGui
        );
    }

    /// Get the pixel position (based on pixel position + screen position)
    public Vector2i getPosition(){
        MinecraftClient client = MinecraftClient.getInstance();
        return new Vector2i(
                (int) (pixelPosition.x + (client.getWindow().getScaledWidth() * screenPosition.getX()) - (anchor.getX() * getSize().x)),
                (int) (pixelPosition.y + (client.getWindow().getScaledHeight() * screenPosition.getY()) - (anchor.getY() * getSize().y))
        );
    }

    /// Get the pixel size (based on pixel size + screen size)
    public Vector2i getSize(){
        MinecraftClient client = MinecraftClient.getInstance();
        return new Vector2i(
                (int) (pixelSize.x + (client.getWindow().getScaledWidth() * screenSize.getX())),
                (int) (pixelSize.y + (client.getWindow().getScaledHeight() * screenSize.getY()))
        );
    }
}

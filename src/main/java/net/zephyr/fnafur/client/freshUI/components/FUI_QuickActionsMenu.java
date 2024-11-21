package net.zephyr.fnafur.client.freshUI.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.zephyr.fnafur.client.freshUI.base.FUI_Component;
import net.zephyr.fnafur.client.freshUI.base.FUI_COLOR;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Map;

public class FUI_QuickActionsMenu extends FUI_Component {

    final int TOP = 1;
    final int LEFT = 3;
    final int MIDDLE = 4;
    final int RIGHT = 5;
    final int BOTTOM = 7;

    final int CELL_SIZE = 25;

    Map<Integer, Runnable> actions = new HashMap<>(){{
        put(MIDDLE, ()->{destroy();});
    }};

    void setActions(Map<Integer, Runnable> nActions){
        actions = nActions;
    }

    Vector2i getPosByInt(int i){
        return new Vector2i(
                i%3,
                i/3
        ).mul(CELL_SIZE).add(getPosition());
    }

    @Override
    public void whenClicked(double x, double y, int button) {

        for(Map.Entry pair : actions.entrySet()){
            Vector2i pos = getPosByInt((int)pair.getKey());
            if(isInside(getMousePosition().x, getMousePosition().y, pos, new Vector2i(CELL_SIZE,CELL_SIZE))){
                ((Runnable)pair.getValue()).run();
            }
        }

        super.whenClicked(x, y, button);
    }

    @Override
    public Vector2i getSize() {
        return new Vector2i(
                CELL_SIZE * 3,
                CELL_SIZE * 3
        );
    }

    @Override
    public void draw(DrawContext context, MinecraftClient client, float dt) {

        for(Map.Entry pair : actions.entrySet()){
            Vector2i pos = getPosByInt((int)pair.getKey());
            context.drawBorder(pos.x,pos.y, CELL_SIZE, CELL_SIZE, FUI_COLOR.BLACK);
        }

        super.draw(context, client, dt);
    }
}

package net.zephyr.fnafur.freshUI.Components;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.ColorCode;
import net.zephyr.fnafur.freshUI.FUI_Component;
import org.joml.Vector2i;

public class FUI_Button extends FUI_Component {

    int NOTHING_COLOR   = 0xFFF;
    int MOVED_COLOR     = 0xF55;
    boolean isMoved;

    @Override
    public void draw(DrawContext context, Vector2i finalPos, Vector2i finalSize, RenderTickCounter dt) {
        context.fill(finalPos.x,finalPos.y,finalPos.x + finalSize.x, finalPos.y + finalSize.y, (isMoved ? MOVED_COLOR : NOTHING_COLOR));
        super.draw(context, finalPos, finalSize, dt);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        isMoved = true;
        return super.isMouseOver(mouseX, mouseY);
    }
}

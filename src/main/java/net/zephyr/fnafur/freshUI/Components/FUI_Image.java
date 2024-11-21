package net.zephyr.fnafur.freshUI.Components;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.freshUI.FUI_Component;
import org.joml.Vector2i;

public class FUI_Image extends FUI_Component {

    Identifier texture;

    public FUI_Image(Identifier nTexture){
        texture = nTexture;
    }

    @Override
    public void draw(DrawContext context, Vector2i finalPos, Vector2i finalSize, RenderTickCounter dt) {
        //TODO Add texture X and Y, gives error if missing
        //context.drawTexture(RenderLayer::getGuiTextured, texture, finalPos.x, finalPos.y, 0,0, finalSize.x, finalSize.y, TextureX, TextureY);
        super.draw(context, finalPos, finalSize, dt);
    }
}

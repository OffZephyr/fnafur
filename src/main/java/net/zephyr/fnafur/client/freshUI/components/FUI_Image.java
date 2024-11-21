package net.zephyr.fnafur.client.freshUI.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.client.freshUI.base.FUI_Component;

public class FUI_Image extends FUI_Component {

    Identifier texture;

    public FUI_Image(Identifier nTexture){
        texture = nTexture;
    }

    @Override
    public void draw(DrawContext context, MinecraftClient client, float dt) {
        //TODO Add texture X and Y, gives error if missing
        //context.drawTexture(RenderLayer::getGuiTextured, texture, finalPos.x, finalPos.y, 0,0, finalSize.x, finalSize.y, TextureX, TextureY);
        super.draw(context, client, dt);
    }
}

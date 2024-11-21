package net.zephyr.fnafur.client.freshUI.components;

import net.zephyr.fnafur.client.freshUI.base.FUI_Component;
import net.zephyr.fnafur.client.freshUI.base.FUI_COLOR;

public class FUI_Button extends FUI_Component {

    int PRESSED_COLOR   = FUI_COLOR.WHITE;
    int NORMAL_COLOR    = FUI_COLOR.CYAN;

    boolean isHover(){
        return isInside(getMousePosition().x, getMousePosition().y, getPosition(), getSize());
    }

    @Override
    public void whenClicked(double x, double y, int button) {
        if(isHover()){
            System.out.println("Hello from the button class!");
        }
        super.whenClicked(x, y, button);
    }
}

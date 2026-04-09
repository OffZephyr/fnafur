package net.zephyr.fnafur.client.gui;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.networking.payloads.MoneySyncDataC2SPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.joml.Matrix3x2fStack;

public class TabOverlayClass implements HudRenderCallback {
    @Override
    public void onHudRender(GuiGraphics drawContext, DeltaTracker tickCounter) {

        Minecraft client = Minecraft.getInstance();
        int width = client.getWindow().getGuiScaledWidth();
        int height = client.getWindow().getGuiScaledHeight();
        Font renderer = client.font;

        if(!client.options.hideGui) {
            if (client.options.keyPlayerList.isDown()) {
                ClientPlayNetworking.send(new MoneySyncDataC2SPayload(0, false));

                float moneyX = 0;
                float y = 0;
                float timeX = 0;
                final float scale = 2f;

                moneyX = (width / scale) / 48;
                timeX = ((width / scale) / 48) * 47;
                y = (height / scale) / 24;
                CompoundTag data = ((IEntityDataSaver) client.player).getPersistentData();
                int money = data.getInt("Credits").orElse(0);

                String HourDisplay = renderClock()[0];
                String day = renderClock()[1];

                Matrix3x2fStack matrices = drawContext.pose();
                //VertexConsumerProvider verticies = ((IDCVertexConsumersAcc)drawContext).getVertexConsumers();

                //matrices.pushMatrix();
                //matrices.scale(scale, scale, scale);
                //renderer.draw("F$: " + money, moneyX, y, 0xFFFFFFFF, false, matrices., verticies, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
                //renderer.draw(HourDisplay, timeX - renderer.getWidth(HourDisplay), y, 0xFFFFFFFF, false, matrices.peek().getPositionMatrix(), verticies, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
                //matrices.popMatrix();
                //renderer.draw(day, timeX * scale - renderer.getWidth(day), y * scale + (scale * 9), 0xFFFFFFFF, false, matrices.peek().getPositionMatrix(), verticies, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
            }
        }
    }

    public static String[] renderClock(){
        Minecraft client = Minecraft.getInstance();
        Level world = client.level;

        if (world != null) {
            long dayTime = (world.getDayTime());
            double currentDay = dayTime / 24000d;

            long hour = (dayTime / 1000) - ((24000 * (dayTime / 24000)) / 1000);

            boolean isMorning = hour >= 0 && hour < 6;
            boolean isAfternoon = hour >= 12 && hour < 18;
            boolean isNight = hour >= 18 && hour < 24;

            String dayHalf = hour >= 6 && hour < 18 ? " PM" : " AM";
            hour = hour > 6 && hour <= 18 ? hour - 12 : hour > 18 ? hour - 24 : hour;
            String HourDisplay = (6 + hour) + dayHalf;

            String dayPrefix = isNight ? "Night " : isMorning ? "Morning " : isAfternoon ? "Evening " : "Day ";
            dayPrefix = dayPrefix + " ";
            String day = dayPrefix + (int) (currentDay + 1);
            return new String[]{HourDisplay, day};
        }
        return new String[]{"Hour", "Day"};
    }
}

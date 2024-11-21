package net.zephyr.fnafur.client.gui;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.networking.payloads.MoneySyncDataC2SPayload;
import net.zephyr.fnafur.util.CameraMapUiDrawer;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IDCVertexConsumersAcc;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class TabOverlayClass implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {

        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        TextRenderer renderer = client.textRenderer;

        if(!client.options.hudHidden) {
            if (client.options.playerListKey.isPressed()) {
                ClientPlayNetworking.send(new MoneySyncDataC2SPayload(0, false));

                float moneyX = 0;
                float y = 0;
                float timeX = 0;
                final float scale = 2f;

                moneyX = (width / scale) / 48;
                timeX = ((width / scale) / 48) * 47;
                y = (height / scale) / 24;
                NbtCompound data = ((IEntityDataSaver) client.player).getPersistentData();
                int money = data.getInt("Credits");

                String HourDisplay = renderClock()[0];
                String day = renderClock()[1];

                MatrixStack matrices = drawContext.getMatrices();
                VertexConsumerProvider verticies = ((IDCVertexConsumersAcc)drawContext).getVertexConsumers();

                matrices.push();
                matrices.scale(scale, scale, scale);
                renderer.draw("F$: " + money, moneyX, y, 0xFFFFFFFF, false, matrices.peek().getPositionMatrix(), verticies, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
                renderer.draw(HourDisplay, timeX - renderer.getWidth(HourDisplay), y, 0xFFFFFFFF, false, matrices.peek().getPositionMatrix(), verticies, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
                matrices.pop();
                renderer.draw(day, timeX * scale - renderer.getWidth(day), y * scale + (scale * 9), 0xFFFFFFFF, false, matrices.peek().getPositionMatrix(), verticies, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);

            } else if (client.player.getOffHandStack().isOf(ItemInit.TABLET)) {
                if (client.player.getMainHandStack().isOf(ItemInit.TAPEMEASURE) || client.player.getMainHandStack().isOf(ItemInit.PAINTBRUSH)) {
                    MatrixStack matrices = drawContext.getMatrices();
                    VertexConsumerProvider verticies = ((IDCVertexConsumersAcc)drawContext).getVertexConsumers();

                    CameraMapUiDrawer drawer = new CameraMapUiDrawer();

                    int mapCornerPosX = width - width / 2;
                    int mapCornerPosY = height - (height / 2 + height / 12);
                    int mapEndPosX = width - width / 18;
                    int mapEndPosY = height - height / 8;

                    NbtCompound data = ItemNbtUtil.getNbt(client.player.getOffHandStack());
                    NbtList mapNbt = data.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).copy();
                    int minX = Integer.MAX_VALUE;
                    int minZ = Integer.MAX_VALUE;
                    int maxX = Integer.MIN_VALUE;
                    int maxZ = Integer.MIN_VALUE;
                    for (int i = 0; i < mapNbt.size(); i++) {
                        if (mapNbt.get(i).getType() == NbtElement.LONG_ARRAY_TYPE) {
                            long[] line = mapNbt.getLongArray(i);
                            BlockPos pos1 = BlockPos.fromLong(line[0]);
                            BlockPos pos2 = BlockPos.fromLong(line[1]);
                            minX = Math.min(pos1.getX(), Math.min(pos2.getX(), minX));
                            minZ = Math.min(pos1.getZ(), Math.min(pos2.getZ(), minZ));
                            maxX = Math.max(pos1.getX(), Math.max(pos2.getX(), maxX));
                            maxZ = Math.max(pos1.getZ(), Math.max(pos2.getZ(), maxZ));
                        }
                    }
                    BlockPos minPos = new BlockPos(minX, 0, minZ);
                    BlockPos maxPos = new BlockPos(maxX, 0, maxZ);
                    data.putLong("mapMinCorner", minPos.asLong());
                    data.putLong("mapMaxCorner", maxPos.asLong());
                    drawer.drawMap(drawContext, 0, 0, 1, data, mapEndPosX, mapEndPosY, mapCornerPosX, mapCornerPosY, 255, false, false, 0);

                    if (client.player.getMainHandStack().isOf(ItemInit.TAPEMEASURE)) {
                        Text line1 = Text.translatable("item.fnafur.tapemeasure.map_cam.line1");
                        Text line2 = Text.translatable("item.fnafur.tapemeasure.map_cam.line2");
                        Text line3 = Text.translatable("item.fnafur.tapemeasure.map_cam.line3");
                        Text line4 = Text.translatable("item.fnafur.tapemeasure.map_cam.line4");

                        drawContext.fill(0, 0, width, 50, 0x66000000);

                        GoopyScreen.drawResizableText(drawContext, renderer, line1, 1.25f, 5, 5, 0xFFFFFFFF, 0, true, false);
                        GoopyScreen.drawResizableText(drawContext, renderer, line2, 0.8f, 5, 22, 0xFFFFFFFF, 0, true, false);
                        GoopyScreen.drawResizableText(drawContext, renderer, line3, 0.8f, 5, 30, 0xFFFFFFFF, 0, true, false);
                        GoopyScreen.drawResizableText(drawContext, renderer, line4, 0.8f, 5, 40, 0xFFFFFFFF, 0, true, false);
                    }
                    else if(client.player.getMainHandStack().isOf(ItemInit.PAINTBRUSH)) {
                        Text line1 = Text.translatable("item.fnafur.paintbrush.map_cam.line1");
                        Text line2 = Text.translatable("item.fnafur.paintbrush.map_cam.line2");
                        Text line3 = Text.translatable("item.fnafur.paintbrush.map_cam.line3");
                        Text line4 = Text.translatable("item.fnafur.paintbrush.map_cam.line4");

                        drawContext.fill(0, 0, width, 32, 0x66000000);

                        GoopyScreen.drawResizableText(drawContext, renderer, line1, 1.25f, 5, 5, 0xFFFFFFFF, 0, true, false);
                        GoopyScreen.drawResizableText(drawContext, renderer, line2, 0.8f, 5, 22, 0xFFFFFFFF, 0, true, false);

                    }
                }
            }
        }
    }

    public static String[] renderClock(){
        MinecraftClient client = MinecraftClient.getInstance();
        World world = client.world;

        if (world != null) {
            long dayTime = (world.getTimeOfDay());
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

package net.zephyr.fnafur.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.joml.Matrix3x2fStack;

import java.util.ArrayList;
import java.util.List;

public class CameraMapUiDrawer {
    public int mapWidth = 0;
    public int mapHeight = 0;
    public int mapMultiplier = 1;
    public float mapAlpha = 100;
    public void drawMap(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CompoundTag data, int mapEndPosX, int mapEndPosY, int mapCornerPosX, int mapCornerPosY, float mapAlpha, boolean isMonitor, boolean nvOutline, long currentCam){
        List<Long> cams = new ArrayList<>();
        long[] camsData = data.getLongArray("Cameras").get();
        for (long cam : camsData) {
            if(false) cams.add(cam);
        }
        BlockPos minPos = BlockPos.of(data.getLong("mapMinCorner").get());
        BlockPos maxPos = BlockPos.of(data.getLong("mapMaxCorner").get());

        boolean bl = data.getList("CamMap").get().isEmpty();
        if(!bl) {

            int mapMaxWidth = mapEndPosX - mapCornerPosX;
            int mapMaxHeight = mapEndPosY - mapCornerPosY;

            ListTag mapNbt = data.getList("CamMap").get().copy();

            mapWidth = Math.abs(maxPos.getX() - minPos.getX());
            mapHeight = Math.abs(maxPos.getZ() - minPos.getZ());

            mapMultiplier = 1;
            while((mapWidth * (mapMultiplier+1) <= mapMaxWidth) && (mapHeight * (mapMultiplier+1) <= mapMaxHeight)){
                mapMultiplier++;
            }

            int bg1 = mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier;
            int bg2 = mapEndPosY - (mapHeight * mapMultiplier) - mapMultiplier;

            int goalAlpha = mouseX > bg1 && mouseX < mapEndPosX && mouseY > bg2 && mouseY < mapEndPosY ?
                    255 : 100;
            if(mapAlpha > goalAlpha){
                this.mapAlpha = this.mapAlpha - delta < goalAlpha ? goalAlpha : this.mapAlpha - delta;
            }
            if(mapAlpha < goalAlpha){
                this.mapAlpha = this.mapAlpha + delta > goalAlpha ? goalAlpha : this.mapAlpha + delta;
            }
            int alpha = isMonitor ? (int)this.mapAlpha : (int)mapAlpha;

            int color = ARGB.color(alpha, 255, 255, 255);
            //context.fill(bg1, bg2, mapEndPosX + (mapMultiplier*2), mapEndPosY + (mapMultiplier*2), 0x55000000);

            for(int i = 0; i < mapNbt.size(); i++) {
                if (mapNbt.get(i).getId() == Tag.TAG_LONG_ARRAY) {
                    BlockPos pos1 = BlockPos.of(mapNbt.getLongArray(i).get()[0]);
                    BlockPos pos2 = BlockPos.of(mapNbt.getLongArray(i).get()[1]);

                    int x1 = (Math.min(pos1.getX(), pos2.getX())- minPos.getX()) * mapMultiplier;
                    int z1 = (Math.min(pos1.getZ(), pos2.getZ())- minPos.getZ()) * mapMultiplier;
                    int x2 = mapMultiplier + ((Math.max(pos1.getX(), pos2.getX())- minPos.getX()) * mapMultiplier);
                    int z2 = mapMultiplier + ((Math.max(pos1.getZ(), pos2.getZ())- minPos.getZ()) * mapMultiplier);

                    x1 += mapEndPosX - (mapWidth * mapMultiplier);
                    x2 += mapEndPosX - (mapWidth * mapMultiplier);
                    z1 += mapEndPosY - (mapHeight * mapMultiplier);
                    z2 += mapEndPosY - (mapHeight * mapMultiplier);

                    context.fill(x1, z1, x2, z2, color);
                }
            }

            float camScale = 0.25f;
            Matrix3x2fStack matrices = context.pose();

            matrices.pushMatrix();
            matrices.scale(camScale, camScale);
            for (Long cam : cams) {
                BlockPos pos = BlockPos.of(cam);

                int x = (pos.getX() - minPos.getX()) * mapMultiplier;
                int z = (pos.getZ() - minPos.getZ()) * mapMultiplier;

                x += mapEndPosX - (mapWidth * mapMultiplier);
                z += mapEndPosY - (mapHeight * mapMultiplier);

                boolean bl2 = GoopyScreen.isOnButton(mouseX, mouseY, x, z, mapMultiplier, mapMultiplier);

                x *= (int)(1 / camScale);
                z *= (int)(1 / camScale);

                if(isMonitor){
                    if (bl2 && Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) != null) {
                        String name = ((IEntityDataSaver) Minecraft.getInstance().level.getBlockEntity(pos)).getPersistentData().getString("Name").get();
                        matrices.popMatrix();
                        context.setTooltipForNextFrame(Minecraft.getInstance().font, Component.literal(name), mouseX, mouseY);
                        matrices.pushMatrix();
                        matrices.scale(camScale, camScale);
                    }
                }

                int camColor = ARGB.color(alpha, 100, 100, 100);
                int camOutline = ARGB.color(alpha, 255, 255, 255);
                if(isMonitor){
                    camColor = bl2 || cam == currentCam ? ARGB.color(alpha, 75, 255, 75) : ARGB.color(alpha, 100, 100, 100);
                    camOutline = nvOutline && cam == currentCam ? ARGB.color(alpha, 133, 210, 255) : ARGB.color(alpha, 255, 255, 255);
                }

                context.fill(x - (mapMultiplier / 2) * (int)(1 / camScale), z - (mapMultiplier / 2) * (int)(1 / camScale), x + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 2) * (int)(1 / camScale)), z + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 2) * (int)(1 / camScale)), camOutline);
                context.fill(x - ((mapMultiplier / 4) * (int)(1 / camScale)), z - ((mapMultiplier / 4) * (int)(1 / camScale)), x + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 4) * (int)(1 / camScale)), z + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 4) * (int)(1 / camScale)), camColor);
            }
            matrices.popMatrix();
        }
    }
}

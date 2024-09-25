package net.zephyr.fnafur.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.zephyr.fnafur.blocks.camera.CameraBlockEntity;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.ArrayList;
import java.util.List;

public class CameraMapUiDrawer {
    public int mapWidth = 0;
    public int mapHeight = 0;
    public int mapMultiplier = 1;
    public float mapAlpha = 100;
    public void drawMap(DrawContext context, int mouseX, int mouseY, float delta, NbtCompound data, int mapEndPosX, int mapEndPosY, int mapCornerPosX, int mapCornerPosY, float mapAlpha, boolean isMonitor, boolean nvOutline, long currentCam){
        List<Long> cams = new ArrayList<>();
        long[] camsData = data.getLongArray("Cameras");
        for (long cam : camsData) {
            cams.add(cam);
        }
        BlockPos minPos = BlockPos.fromLong(data.getLong("mapMinCorner"));
        BlockPos maxPos = BlockPos.fromLong(data.getLong("mapMaxCorner"));

        boolean bl = data.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).isEmpty();
        if(!bl) {

            int mapMaxWidth = mapEndPosX - mapCornerPosX;
            int mapMaxHeight = mapEndPosY - mapCornerPosY;

            NbtList mapNbt = data.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).copy();

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

            int color = ColorHelper.Argb.getArgb(alpha, 255, 255, 255);
            //context.fill(bg1, bg2, mapEndPosX + (mapMultiplier*2), mapEndPosY + (mapMultiplier*2), 0x55000000);

            for(int i = 0; i < mapNbt.size(); i++) {
                if (mapNbt.get(i).getType() == NbtElement.LONG_ARRAY_TYPE) {
                    BlockPos pos1 = BlockPos.fromLong(mapNbt.getLongArray(i)[0]);
                    BlockPos pos2 = BlockPos.fromLong(mapNbt.getLongArray(i)[1]);

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
            MatrixStack matrices = context.getMatrices();

            matrices.push();
            matrices.scale(camScale, camScale, camScale);
            for (Long cam : cams) {
                BlockPos pos = BlockPos.fromLong(cam);

                int x = (pos.getX() - minPos.getX()) * mapMultiplier;
                int z = (pos.getZ() - minPos.getZ()) * mapMultiplier;

                x += mapEndPosX - (mapWidth * mapMultiplier);
                z += mapEndPosY - (mapHeight * mapMultiplier);

                boolean bl2 = GoopyScreen.isOnButton(mouseX, mouseY, x, z, mapMultiplier, mapMultiplier);

                x *= (int)(1 / camScale);
                z *= (int)(1 / camScale);

                if(isMonitor){
                    if (bl2 && MinecraftClient.getInstance().world != null) {
                        String name = ((IEntityDataSaver) MinecraftClient.getInstance().world.getBlockEntity(pos)).getPersistentData().getString("Name");
                        matrices.pop();
                        context.drawTooltip(MinecraftClient.getInstance().textRenderer, Text.literal(name), mouseX, mouseY);
                        matrices.push();
                        matrices.scale(camScale, camScale, camScale);
                    }
                }

                int camColor = ColorHelper.Argb.getArgb(alpha, 100, 100, 100);
                int camOutline = ColorHelper.Argb.getArgb(alpha, 255, 255, 255);
                if(isMonitor){
                    camColor = bl2 || cam == currentCam ? ColorHelper.Argb.getArgb(alpha, 75, 255, 75) : ColorHelper.Argb.getArgb(alpha, 100, 100, 100);
                    camOutline = nvOutline && cam == currentCam ? ColorHelper.Argb.getArgb(alpha, 133, 210, 255) : ColorHelper.Argb.getArgb(alpha, 255, 255, 255);
                }

                context.fill(x - (mapMultiplier / 2) * (int)(1 / camScale), z - (mapMultiplier / 2) * (int)(1 / camScale), x + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 2) * (int)(1 / camScale)), z + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 2) * (int)(1 / camScale)), camOutline);
                context.fill(x - ((mapMultiplier / 4) * (int)(1 / camScale)), z - ((mapMultiplier / 4) * (int)(1 / camScale)), x + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 4) * (int)(1 / camScale)), z + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 4) * (int)(1 / camScale)), camColor);
            }
            matrices.pop();
        }
    }
}

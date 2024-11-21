package net.zephyr.fnafur.client.gui.screens.computer.apps;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.computer.ComputerData;
import net.zephyr.fnafur.util.Computer.ComputerPlaylist;
import net.zephyr.fnafur.util.Computer.ComputerSong;

import java.util.*;

public class COMPMusicPlayerScreen extends COMPBaseAppScreen {
    public Identifier SMALL_BAR = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/computer/window_smallbar_20.png");
    boolean playlistsScreen = false;
    float playlistOffset = 0;
    float playlistOffsetDiff = 0;
    float playlistOffsetOld = 0;
    int playlistIndex = -1;
    List<ComputerPlaylist> lists = new ArrayList<>();

    ComputerPlaylist allTracks = new ComputerPlaylist("All Tracks");
    public COMPMusicPlayerScreen(Text title, NbtCompound nbt, long l) {
        super(title, nbt, l);
    }

    @Override
    protected void init() {
        for(ComputerSong song : ComputerData.getSongs())
            allTracks.addSong(song);

        lists.add(allTracks);

        lists.addAll(ComputerData.getPlaylists());

        playlistsScreen = false;
        super.init();
    }

    @Override
    public void tick() {
        super.tick();
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        float buttonOffset = (2/256f)*screenSize;
        float buttonSize = (16/256f)*screenSize;

        if(mouseX > (int)topCornerX + (int)appAvailableSizeX - (int)buttonSize - (int)buttonOffset && mouseX < (int)topCornerX + (int)appAvailableSizeX - (int)buttonOffset && mouseY >(int)topCornerY + (int)buttonOffset && mouseY < (int)topCornerY + (int)buttonOffset + (int) buttonSize) {
            playlistsScreen = !playlistsScreen;
        }

        if (playlistsScreen && mouseX > topCornerX && mouseY > topCornerY && mouseX < topCornerX + appAvailableSizeX && mouseY < topCornerY + appAvailableSizeY && !dragging) {
            float topBarPos = (20 / 256f) * screenSize;
            float textOffset = (8 / 256f) * screenSize;
            int spacingOffset = 12;
            int spacing = 0;

            for (int i = 0; i < ComputerData.getPlaylists().size() + 1; i++) {
                ComputerPlaylist play = i > 0 ? ComputerData.getPlaylists().get(i - 1) : this.allTracks;
                float iconSize = 10;
                int yPos = (int) topCornerY + (int) topBarPos + (int) textOffset + spacing + (int) playlistOffset;

                if (mouseX > topCornerX && mouseX < topCornerX + appAvailableSizeX && mouseY > yPos - 2 && mouseY < yPos - 2 + spacingOffset && !dragging) {
                    openClosePlaylist(i);
                }

                if (i == playlistIndex) {
                    for (int j = 0; j < play.getList().size(); j++) {
                        int songYPos = yPos - 2 + spacingOffset + spacingOffset * j;
                        if (mouseX > topCornerX && mouseX < topCornerX + appAvailableSizeX && mouseY > songYPos && mouseY < songYPos  + (int) iconSize + 2 && !dragging) {
                            System.out.println(play.getList().get(j).getName());
                        }
                    }
                    spacing += spacingOffset * (play.getList().size() + 1);
                } else {
                    spacing += spacingOffset;
                }
            }
        }
            return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    void openClosePlaylist(int setList){
        this.playlistIndex = this.playlistIndex == setList ? -1 : setList;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(this.width/2 - this.screenSize/2, this.height/2 - this.screenSize/2, this.width/2 + this.screenSize/2, this.height/2 + this.screenSize/2, 0xFF182562);

        float topBarPos = (20/256f) * screenSize;
        float textOffset = (8/256f) * screenSize;
        float buttonOffset = (2/256f) * screenSize;
        float buttonSize = (16/256f) * screenSize;
        float buttonTextureSize = (128/256f)*screenSize;

        if(playlistsScreen) {

            context.fill(this.width / 2 - this.screenSize / 2, this.height / 2 - this.screenSize / 2, this.width / 2 + this.screenSize / 2, this.height / 2 + this.screenSize / 2, 0x99000000);

            int spacingOffset = 12;
            int spacing = 0;
            for (int i = 0; i < ComputerData.getPlaylists().size() + 1; i++) {
                ComputerPlaylist play = i > 0 ? ComputerData.getPlaylists().get(i - 1) : this.allTracks;
                float iconSize = 10;
                int yPos = (int) topCornerY + (int) topBarPos + (int) textOffset + spacing + (int)playlistOffset;

                String playlistName = i == 0 ? play.getName() + "..." : play.getName();
                context.drawText(this.textRenderer, playlistName, this.width / 2 - (int) appAvailableSizeX / 2 + (int) iconSize, yPos, 0xFFFFFFFF, false);

                if (mouseX > topCornerX && mouseX < topCornerX + appAvailableSizeX && mouseY > yPos - 2 && mouseY < yPos - 2 + spacingOffset && !dragging) {
                    if (holding) {
                        context.fill((int) topCornerX, yPos - 2, (int) topCornerX + (int) appAvailableSizeX, yPos - 2 + spacingOffset, 0x99FFFFFF);
                    } else {
                        context.fill((int) topCornerX, yPos - 2, (int) topCornerX + (int) appAvailableSizeX, yPos - 2 + spacingOffset, 0x66FFFFFF);
                    }
                }

                if (i == playlistIndex) {
                    for (int j = 0; j < play.getList().size(); j++) {

                        int songYPos = yPos - 2 + spacingOffset + spacingOffset * j;
                        ComputerSong song = play.getList().get(j);

                        String text = song.getName() + " | " + song.getAuthor();

                        if(j % 2 == 0)
                            context.fill((int) topCornerX, songYPos, (int) topCornerX + (int) appAvailableSizeX, songYPos + (int) iconSize + 2, 0x44000011);

                        context.drawTexture(RenderLayer::getGuiTextured, song.getIconTexture(), this.width / 2 - (int) appAvailableSizeX / 2 + (int) iconSize, songYPos + 1, 0, 0, (int) iconSize, (int) iconSize, (int) iconSize, (int) iconSize);
                        context.drawText(this.textRenderer, text, this.width / 2 - (int) appAvailableSizeX / 2 + (int) iconSize * 3, songYPos + (int) buttonOffset, 0xFFFFFFFF, false);

                        if (mouseX > topCornerX && mouseX < topCornerX + appAvailableSizeX && mouseY > songYPos && mouseY < songYPos  + (int) iconSize + 2 && !dragging) {
                            if (holding) {
                            context.fill((int) topCornerX, songYPos, (int) topCornerX + (int) appAvailableSizeX, songYPos + (int) iconSize + 2, 0x66FFFFFF);
                            } else {
                            context.fill((int) topCornerX, songYPos, (int) topCornerX + (int) appAvailableSizeX, songYPos + (int) iconSize + 2, 0x33FFFFFF);
                            }
                        }

                    }
                    spacing += spacingOffset * (play.getList().size() + 1);
                } else {
                    spacing += spacingOffset;
                }
            }

            if (dragging) {
                playlistOffset = playlistOffsetOld + playlistOffsetDiff;
                playlistOffsetDiff = mouseY - gotMouseY;
            } else {
                playlistOffsetOld = playlistOffset;
                playlistOffsetDiff = 0;

                if (playlistOffset > 0) {
                    playlistOffset += (0 - playlistOffset) / 15;
                }
                float bottomHeight = -(spacing - (appAvailableSizeY - (4 * textOffset)));
                if(bottomHeight > 0) bottomHeight = 0;
                if (playlistOffset < bottomHeight) {
                    playlistOffset += (bottomHeight - playlistOffset) / 15;
                }
            }
        }

        context.drawTexture(RenderLayer::getGuiTextured, SMALL_BAR, (int)topCornerX, (int)topCornerY, 0, 0, (int)appAvailableSizeX, (int)topBarPos, (int)appAvailableSizeX, (int)topBarPos);

        if(mouseX > (int)topCornerX + (int)appAvailableSizeX - (int)buttonSize - (int)buttonOffset && mouseX < (int)topCornerX + (int)appAvailableSizeX - (int)buttonOffset && mouseY >(int)topCornerY + (int)buttonOffset && mouseY < (int)topCornerY + (int)buttonOffset + (int) buttonSize){
            if(holding){
                context.drawTexture(RenderLayer::getGuiTextured, BUTTONS, (int)topCornerX + (int)appAvailableSizeX - (int)buttonSize - (int)buttonOffset, (int)topCornerY + (int)buttonOffset, buttonSize*5, buttonSize, (int)buttonSize, (int)buttonSize, (int)buttonTextureSize, (int)buttonTextureSize);
            }
            else {
                context.drawTexture(RenderLayer::getGuiTextured, BUTTONS, (int)topCornerX + (int)appAvailableSizeX - (int)buttonSize - (int)buttonOffset, (int)topCornerY + (int)buttonOffset, buttonSize*4, buttonSize, (int)buttonSize, (int)buttonSize, (int)buttonTextureSize, (int)buttonTextureSize);
            }
        }
        else {
            if (playlistsScreen) {
                context.drawTexture(RenderLayer::getGuiTextured, BUTTONS, (int) topCornerX + (int) appAvailableSizeX - (int) buttonSize - (int) buttonOffset, (int) topCornerY + (int) buttonOffset, buttonSize * 5, buttonSize, (int) buttonSize, (int) buttonSize, (int) buttonTextureSize, (int) buttonTextureSize);
            } else {
                context.drawTexture(RenderLayer::getGuiTextured, BUTTONS, (int) topCornerX + (int) appAvailableSizeX - (int) buttonSize - (int) buttonOffset, (int) topCornerY + (int) buttonOffset, buttonSize * 3, buttonSize, (int) buttonSize, (int) buttonSize, (int) buttonTextureSize, (int) buttonTextureSize);
            }
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public String appName() {
        return "music_player";
    }
}

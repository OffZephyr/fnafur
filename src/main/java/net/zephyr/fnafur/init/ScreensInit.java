package net.zephyr.fnafur.init;

import net.zephyr.fnafur.client.gui.screens.CameraEditScreen;
import net.zephyr.fnafur.client.gui.screens.CameraTabletScreen;
import net.zephyr.fnafur.client.gui.screens.crafting.SuitMakingScreen;
import net.zephyr.fnafur.client.gui.screens.editing.DecalBookEditScreen;
import net.zephyr.fnafur.client.gui.screens.crafting.CpuConfigScreen;
import net.zephyr.fnafur.client.gui.screens.editing.DoorEditScreen;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public class ScreensInit {
    public static final String CAMERA_TABLET = "camera_tablet";
    public static final String COMPUTER_BOOT = "computer_boot";
    public static final String COMPUTER_DESKTOP = "desktop";
    public static final String COMPUTER_CODE = "code";
    public static final String ARCADE_MACHINE = "arcade_machine";
    public static final String CAMERA_EDIT = "camera_edit";
    public static final String CPU_CONFIG = "cpu_config";
    public static final String WORKBENCH = "workbench";
    public static final String PAINTBRUSH = "paintbrush";
    public static final String DECAL_BOOK_EDIT = "decal_book_edit";
    public static final String DOOR_EDIT = "door_edit";
    public static void init(){
        GoopyNetworkingUtils.registerScreen(CAMERA_TABLET, CameraTabletScreen::new);

        GoopyNetworkingUtils.registerScreen(CAMERA_EDIT, CameraEditScreen::new);
        GoopyNetworkingUtils.registerScreen(CPU_CONFIG, CpuConfigScreen::new);
        GoopyNetworkingUtils.registerScreen(WORKBENCH, SuitMakingScreen::new);

        GoopyNetworkingUtils.registerScreen(DECAL_BOOK_EDIT, DecalBookEditScreen::new);
        GoopyNetworkingUtils.registerScreen(DOOR_EDIT, DoorEditScreen::new);


        /*

        ScreenUtils.registerScreen("music_player", COMPMusicPlayerScreen::new);
        ScreenUtils.registerScreen("browser", COMPBrowserScreen::new);
        ScreenUtils.registerScreen("remote", COMPRemoteScreen::new);
         */
    }
}

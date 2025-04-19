package net.zephyr.fnafur.init;

import net.zephyr.fnafur.client.gui.screens.CameraEditScreen;
import net.zephyr.fnafur.client.gui.screens.CameraTabletScreen;
import net.zephyr.fnafur.client.gui.screens.DecalBookEditScreen;
import net.zephyr.fnafur.client.gui.screens.EntitySkinScreen;
import net.zephyr.fnafur.client.gui.screens.crafting.CpuConfigScreen;
import net.zephyr.fnafur.client.gui.screens.crafting.WorkbenchScreen;
import net.zephyr.fnafur.client.gui.screens.killscreens.DefaultKillScreen;
import net.zephyr.fnafur.client.gui.screens.killscreens.ZephyrKillScreen;
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
    public static final String SKINS = "skins";
    public static final String DEFAULT_KILLSCREEN = "death_goopy_default";
    public static final String ZEPHYR_KILLSCREEN = "death_goopy";
    public static void init(){
        GoopyNetworkingUtils.registerScreen(CAMERA_TABLET, CameraTabletScreen::new);

        GoopyNetworkingUtils.registerScreen(CAMERA_EDIT, CameraEditScreen::new);
        GoopyNetworkingUtils.registerScreen(CPU_CONFIG, CpuConfigScreen::new);
        GoopyNetworkingUtils.registerScreen(WORKBENCH, WorkbenchScreen::new);
        GoopyNetworkingUtils.registerScreen(SKINS, EntitySkinScreen::new);

        GoopyNetworkingUtils.registerScreen(DECAL_BOOK_EDIT, DecalBookEditScreen::new);

        GoopyNetworkingUtils.registerScreen(DEFAULT_KILLSCREEN, DefaultKillScreen::new);
        GoopyNetworkingUtils.registerScreen(ZEPHYR_KILLSCREEN, ZephyrKillScreen::new);


        /*

        ScreenUtils.registerScreen("music_player", COMPMusicPlayerScreen::new);
        ScreenUtils.registerScreen("browser", COMPBrowserScreen::new);
        ScreenUtils.registerScreen("remote", COMPRemoteScreen::new);
         */
    }
}

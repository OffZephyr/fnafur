package net.zephyr.fnafur.client;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

public class JavaModels {
    public static final EntityModelLayer CAMERA_HEAD =
            new EntityModelLayer(Identifier.of(FnafUniverseRebuilt.MOD_ID, "camera_head"), "main");
    public static final EntityModelLayer CAMERA_SCREEN =
            new EntityModelLayer(Identifier.of(FnafUniverseRebuilt.MOD_ID, "camera_screen"), "main");
    public static final EntityModelLayer CAMERA_MAP =
            new EntityModelLayer(Identifier.of(FnafUniverseRebuilt.MOD_ID, "camera_map"), "main");
    public static final EntityModelLayer ZEPHYR =
            new EntityModelLayer(Identifier.of(FnafUniverseRebuilt.MOD_ID, "zephyr"), "main");
}

package net.zephyr.fnafur.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

public class JavaModels {
    public static final ModelLayerLocation CAMERA_HEAD =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "camera_head"), "main");
    public static final ModelLayerLocation CAMERA_SCREEN =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "camera_screen"), "main");
    public static final ModelLayerLocation CAMERA_MAP =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "camera_map"), "main");
    public static final ModelLayerLocation ZEPHYR =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "zephyr"), "main");
}

package net.zephyr.fnafur.client.gui.screens.CameraTablet;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.MapColor;
import net.minecraft.client.texture.MapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.map.MapState;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

@Environment(EnvType.CLIENT)
public class CameraMapTextureManager implements AutoCloseable {
    private final Int2ObjectMap<MapTexture> texturesByMapId = new Int2ObjectOpenHashMap<>();
    final TextureManager textureManager;

    public CameraMapTextureManager(TextureManager textureManager) {
        this.textureManager = textureManager;
    }

    public void setNeedsUpdate(MapIdComponent mapIdComponent, MapState mapState) {
        this.getMapTexture(mapIdComponent, mapState).setNeedsUpdate();
    }

    public Identifier getTextureId(MapIdComponent mapIdComponent, MapState mapState) {
        MapTexture mapTexture = this.getMapTexture(mapIdComponent, mapState);
        mapTexture.updateTexture();
        return mapTexture.textureId;
    }

    public void clear() {
        for (MapTexture mapTexture : this.texturesByMapId.values()) {
            mapTexture.close();
        }

        this.texturesByMapId.clear();
    }

    private MapTexture getMapTexture(MapIdComponent mapIdComponent, MapState MapState) {
        return this.texturesByMapId.compute(mapIdComponent.id(), (id, mapTexture) -> {
            if (mapTexture == null) {
                return new MapTexture(id, MapState);
            } else {
                mapTexture.setState(MapState);
                return mapTexture;
            }
        });
    }

    public void close() {
        this.clear();
    }

    @Environment(EnvType.CLIENT)
    class MapTexture implements AutoCloseable {
        private MapState state;
        private final NativeImageBackedTexture texture;
        private boolean needsUpdate = true;
        final Identifier textureId;

        MapTexture(final int id, final MapState state) {
            this.state = state;
            this.texture = new NativeImageBackedTexture(128, 128, true);
            this.textureId = Identifier.of(FnafUniverseRebuilt.MOD_ID, "cam_id" + id);
            CameraMapTextureManager.this.textureManager.registerTexture(this.textureId, this.texture);
        }

        void setState(MapState state) {
            boolean bl = this.state != state;
            this.state = state;
            this.needsUpdate |= bl;
        }

        public void setNeedsUpdate() {
            this.needsUpdate = true;
        }

        void updateTexture() {
            if (this.needsUpdate) {
                NativeImage nativeImage = this.texture.getImage();
                if (nativeImage != null) {
                    for (int i = 0; i < 128; i++) {
                        for (int j = 0; j < 128; j++) {
                            int k = j + i * 128;
                            nativeImage.setColorArgb(j, i, MapColor.getRenderColor(this.state.colors[k]));
                        }
                    }
                }

                this.texture.upload();
                this.needsUpdate = false;
            }
        }

        public void close() {
            this.texture.close();
        }
    }
}

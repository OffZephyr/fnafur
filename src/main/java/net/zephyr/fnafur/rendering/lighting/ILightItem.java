package net.zephyr.fnafur.rendering.lighting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface ILightItem {

    default boolean isLightOn(ItemStack stack){
        return true;
    }

    default void setLightOn(boolean isOn, ItemStack stack){

    }

    default Identifier getLightTexture(){
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/lighting/default_full");
    }
    Identifier getLightMaskTexture();

    Vec3d getLightWorldPos(LivingEntity parent);
    Vector3f getLightRotation(LivingEntity parent);
    float getLength();
    float getIntensity();
    float getEdgeSmoothness();
    float getDistanceSmoothness();
    float getNormalInfluence();
    float getMinRadius();
    float getMaxRadius();
    Vector3f getLightColor();
}

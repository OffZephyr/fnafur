package net.zephyr.fnafur.rendering.lighting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface ILightItem {

    default boolean isLightAutoUpdate(ItemStack stack){
        return false;
    }

    default boolean isLightOn(ItemStack stack){
        return true;
    }

    default void setLightOn(boolean isOn, ItemStack stack){

    }

    default Identifier getLightTexture(){
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/lighting/default_full");
    }
    Identifier getLightMaskTexture();

    Vec3 getLightWorldPos(LivingEntity parent);
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

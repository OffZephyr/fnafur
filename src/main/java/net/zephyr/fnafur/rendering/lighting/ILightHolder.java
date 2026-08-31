package net.zephyr.fnafur.rendering.lighting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface ILightHolder {
    default boolean isLightOn(Entity stack){
        return true;
    }
    default boolean isLightAutoUpdate(Entity stack){
        return false;
    }

    default void setLightOn(boolean isOn, Entity stack){

    }

    default Identifier getLightTexture(){
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/lighting/default_full");
    }

    Identifier getLightMaskTexture();

    default Vector4f getUVs(Identifier texture){

        TextureAtlasSprite sprite = Minecraft.getInstance().getBlockRenderer().materials.get(new Material(TextureAtlas.LOCATION_BLOCKS, texture));

        float u0 = sprite.getU0();
        float v0 = sprite.getV0();
        float u1 = sprite.getU1();
        float v1 = sprite.getV1();
        return new Vector4f(u0, v0, u1, v1);
    }

    Vec3 getLightWorldPos();
    Vector3f getLightRotation();
    float getLength();
    float getIntensity();
    float getEdgeSmoothness();
    float getDistanceSmoothness();
    float getNormalInfluence();
    float getMinRadius();
    float getMaxRadius();

    Vector3f getLightColor();

    default AreaLightInstance getLightInstance(){
        if(AreaLightManager.WORLD_LIGHT_MAP.containsKey(this)){
            return AreaLightManager.WORLD_LIGHT_MAP.get(this);
        }
        return createInstance();
    }

    default AreaLightInstance createInstance(){
        return new AreaLightInstance(
                getLightWorldPos(),
                getLightRotation(),
                getLightRotation().mul(-1),
                getMaxRadius(),
                getLength(),
                getLightColor(),
                getIntensity(),
                getEdgeSmoothness(),
                getDistanceSmoothness(),
                getNormalInfluence(),
                getMinRadius(),
                getUVs(getLightTexture()),
                getUVs(getLightMaskTexture())

        );
    }

    default void updateLightInstance(){
        AreaLightInstance instance = getLightInstance();
        instance.updateData(
                getLightWorldPos(),
                getLightRotation(),
                getLightRotation().mul(-1),
                getMaxRadius(),
                getLength(),
                getLightColor(),
                getIntensity(),
                getEdgeSmoothness(),
                getDistanceSmoothness(),
                getNormalInfluence(),
                getMinRadius(),
                getUVs(getLightTexture()),
                getUVs(getLightMaskTexture())
        );
    }

    default void addToWorldIfUnique(){
        AreaLightInstance instance = getLightInstance();
        if(!AreaLightManager.WORLD_LIGHTS.contains(instance)){
            AreaLightManager.WORLD_LIGHTS.add(instance);
            AreaLightManager.WORLD_LIGHT_MAP.put(this, instance);
        }
    }

    default void removeFromWorld(){
        if(AreaLightManager.WORLD_LIGHT_MAP.containsKey(this)){
            System.out.println("Removed");
            AreaLightInstance instance = getLightInstance();
            AreaLightManager.WORLD_LIGHTS.remove(instance);
            AreaLightManager.WORLD_LIGHT_MAP.remove(this);
        }
    }

    default CompoundTag getNbt(){
        if(this instanceof IEntityDataSaver data){
            return data.getPersistentData();
        }
        return new CompoundTag();
    }
}

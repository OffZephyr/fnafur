package net.zephyr.fnafur.rendering.lighting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface ILightHolder {
    default boolean isLightOn(Entity stack){
        return true;
    }

    default void setLightOn(boolean isOn, Entity stack){

    }

    default Identifier getLightTexture(){
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/lighting/default_full");
    }

    Identifier getLightMaskTexture();

    default Vector4f getUVs(Identifier texture){

        Sprite sprite = MinecraftClient.getInstance().getBlockRenderManager().spriteHolder.getSprite(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture));

        float u0 = sprite.getMinU();
        float v0 = sprite.getMinV();
        float u1 = sprite.getMaxU();
        float v1 = sprite.getMaxV();
        return new Vector4f(u0, v0, u1, v1);
    }

    Vec3d getLightWorldPos();
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

    default NbtCompound getNbt(){
        if(this instanceof IEntityDataSaver data){
            return data.getPersistentData();
        }
        return new NbtCompound();
    }
}

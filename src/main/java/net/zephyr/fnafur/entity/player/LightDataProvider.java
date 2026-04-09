package net.zephyr.fnafur.entity.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.zephyr.fnafur.rendering.lighting.ILightHolder;
import net.zephyr.fnafur.rendering.lighting.ILightItem;
import org.joml.Vector3f;

public class LightDataProvider {

    Player parent;

    public LightDataProvider(Player parent){
        this.parent = parent;
    }

    public boolean isLightAutoUpdate() {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            return holder.isLightAutoUpdate(parent.getMainHandItem());
        }
        return false;
    }

    public boolean isLightOn() {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            return holder.isLightOn(parent.getMainHandItem());
        }
        return false;
    }

    public void setLightOn(boolean isOn) {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            holder.setLightOn(isOn, parent.getMainHandItem());
        }
    }

    public Identifier getLightMaskTexture() {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            return holder.getLightMaskTexture();
        }
        return null;
    }

    public Vec3 getLightWorldPos() {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            return holder.getLightWorldPos(parent);
        }
        return null;
    }

    public Vector3f getLightRotation() {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            return holder.getLightRotation(parent);
        }
        return null;
    }

    public float getLength() {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            return holder.getLength();
        }
        return 0;
    }

    public float getIntensity() {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            return holder.getIntensity();
        }
        return 0;
    }

    public float getEdgeSmoothness() {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            return holder.getEdgeSmoothness();
        }
        return 0;
    }

    public float getDistanceSmoothness() {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            return holder.getDistanceSmoothness();
        }
        return 0;
    }

    public float getNormalInfluence() {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            return holder.getNormalInfluence();
        }
        return 0;
    }

    public float getMinRadius() {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            return holder.getMinRadius();
        }
        return 0;
    }

    public float getMaxRadius() {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            return holder.getMaxRadius();
        }
        return 0;
    }

    public Vector3f getLightColor() {
        if(parent.getMainHandItem().getItem() instanceof ILightItem holder){
            return holder.getLightColor();
        }
        return null;
    }
}

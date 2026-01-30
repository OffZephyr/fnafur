package net.zephyr.fnafur.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.rendering.lighting.ILightHolder;
import net.zephyr.fnafur.rendering.lighting.ILightItem;
import org.joml.Vector3f;

public class LightDataProvider {

    PlayerEntity parent;

    public LightDataProvider(PlayerEntity parent){
        this.parent = parent;
    }

    public boolean isLightOn() {
        if(parent.getMainHandStack().getItem() instanceof ILightItem holder){
            return holder.isLightOn(parent.getMainHandStack());
        }
        return false;
    }

    public void setLightOn(boolean isOn) {
        if(parent.getMainHandStack().getItem() instanceof ILightItem holder){
            holder.setLightOn(isOn, parent.getMainHandStack());
        }
    }

    public Identifier getLightMaskTexture() {
        if(parent.getMainHandStack().getItem() instanceof ILightItem holder){
            return holder.getLightMaskTexture();
        }
        return null;
    }

    public Vec3d getLightWorldPos() {
        if(parent.getMainHandStack().getItem() instanceof ILightItem holder){
            return holder.getLightWorldPos(parent);
        }
        return null;
    }

    public Vector3f getLightRotation() {
        if(parent.getMainHandStack().getItem() instanceof ILightItem holder){
            return holder.getLightRotation(parent);
        }
        return null;
    }

    public float getLength() {
        if(parent.getMainHandStack().getItem() instanceof ILightItem holder){
            return holder.getLength();
        }
        return 0;
    }

    public float getIntensity() {
        if(parent.getMainHandStack().getItem() instanceof ILightItem holder){
            return holder.getIntensity();
        }
        return 0;
    }

    public float getEdgeSmoothness() {
        if(parent.getMainHandStack().getItem() instanceof ILightItem holder){
            return holder.getEdgeSmoothness();
        }
        return 0;
    }

    public float getDistanceSmoothness() {
        if(parent.getMainHandStack().getItem() instanceof ILightItem holder){
            return holder.getDistanceSmoothness();
        }
        return 0;
    }

    public float getNormalInfluence() {
        if(parent.getMainHandStack().getItem() instanceof ILightItem holder){
            return holder.getNormalInfluence();
        }
        return 0;
    }

    public float getMinRadius() {
        if(parent.getMainHandStack().getItem() instanceof ILightItem holder){
            return holder.getMinRadius();
        }
        return 0;
    }

    public float getMaxRadius() {
        if(parent.getMainHandStack().getItem() instanceof ILightItem holder){
            return holder.getMaxRadius();
        }
        return 0;
    }

    public Vector3f getLightColor() {
        if(parent.getMainHandStack().getItem() instanceof ILightItem holder){
            return holder.getLightColor();
        }
        return null;
    }
}

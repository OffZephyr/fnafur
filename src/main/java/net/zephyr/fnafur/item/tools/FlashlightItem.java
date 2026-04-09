package net.zephyr.fnafur.item.tools;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.rendering.lighting.ILightItem;
import net.zephyr.fnafur.util.IHasArmPos;
import net.zephyr.fnafur.rendering.lighting.ILightHolder;
import net.zephyr.fnafur.util.ItemUtil;
import org.joml.Vector3f;

public class FlashlightItem extends Item implements IHasArmPos, ILightItem {
    public FlashlightItem(Properties settings) {
        super(settings);
    }

    @Override
    public Vec3 getLeftArmPos(boolean isMainStack) {
        return new Vec3(0, 0, 0);
    }

    @Override
    public Vec3 getRightArmPos(boolean isMainStack) {
        return new Vec3(0, 0, 0);
    }

    @Override
    public Identifier getLightMaskTexture() {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/lighting/flashlight_map");
    }


    @Override
    public boolean isLightAutoUpdate(ItemStack stack) {
        return isLightOn(stack);
    }

    @Override
    public boolean isLightOn(ItemStack stack) {
        return ItemUtil.getNbt(stack).getBooleanOr("lightOn", true);
    }

    @Override
    public void setLightOn(boolean isOn, ItemStack stack) {
        CompoundTag nbt = ItemUtil.getNbt(stack);
        nbt.putBoolean("lightOn", isOn);
        ItemUtil.setNbt(stack, nbt);
    }

    @Override
    public Vec3 getLightWorldPos(LivingEntity parent) {
        float progress = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks()/20f;
        return parent.getPosition(progress).add(0, parent.getDimensions(parent.getPose()).eyeHeight(), 0);
    }

    @Override
    public Vector3f getLightRotation(LivingEntity parent) {
        float progress = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks()/20f;
        Vec3 impl = parent.getViewVector(progress);
        return impl.toVector3f();
    }

    @Override
    public float getLength() {
        return 15;
    }

    @Override
    public float getIntensity() {
        return 5;
    }

    @Override
    public float getEdgeSmoothness() {
        return 0;
    }

    @Override
    public float getDistanceSmoothness() {
        return 2;
    }

    @Override
    public float getNormalInfluence() {
        return 1;
    }

    @Override
    public float getMinRadius() {
        return 0.25f;
    }

    @Override
    public float getMaxRadius() {
        return 12;
    }

    @Override
    public Vector3f getLightColor() {
        return new Vector3f(0.8f, 0.6f, 0.5f);
    }
}

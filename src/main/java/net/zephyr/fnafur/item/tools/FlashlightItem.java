package net.zephyr.fnafur.item.tools;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.rendering.lighting.ILightItem;
import net.zephyr.fnafur.util.IHasArmPos;
import net.zephyr.fnafur.rendering.lighting.ILightHolder;
import net.zephyr.fnafur.util.ItemUtil;
import org.joml.Vector3f;

public class FlashlightItem extends Item implements IHasArmPos, ILightItem {
    public FlashlightItem(Settings settings) {
        super(settings);
    }

    @Override
    public Vec3d getLeftArmPos(boolean isMainStack) {
        return new Vec3d(0, 0, 0);
    }

    @Override
    public Vec3d getRightArmPos(boolean isMainStack) {
        return new Vec3d(0, 0, 0);
    }

    @Override
    public Identifier getLightMaskTexture() {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/lighting/flashlight_map");
    }



    @Override
    public boolean isLightOn(ItemStack stack) {
        return ItemUtil.getNbt(stack).getBoolean("lightOn", true);
    }

    @Override
    public void setLightOn(boolean isOn, ItemStack stack) {
        NbtCompound nbt = ItemUtil.getNbt(stack);
        nbt.putBoolean("lightOn", isOn);
        ItemUtil.setNbt(stack, nbt);
    }

    @Override
    public Vec3d getLightWorldPos(LivingEntity parent) {
        return parent.getEyePos();
    }

    @Override
    public Vector3f getLightRotation(LivingEntity parent) {
        float progress = MinecraftClient.getInstance().getRenderTickCounter().getDynamicDeltaTicks();
        Vec3d impl = parent.getRotationVec(progress);
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

package net.zephyr.fnafur.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.entity.player.LightDataProvider;
import net.zephyr.fnafur.entity.player.PlayerHook;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.rendering.lighting.ILightHolder;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements IUniversePlayer, ILightHolder {
    @Unique
    float maskOnDelta = 0;
    @Unique
    boolean crawling = false;
    @Unique
    boolean canAnimateMask = false;
    @Nullable LivingEntity currentEntity;
    float mimicBodyYaw = 0;

    @Unique
    LightDataProvider lightDataProvider;

    @Shadow
    PlayerInventory inventory;

    @Inject (method = "<init>", at = @At("TAIL"))
    public void init(World world, GameProfile profile, CallbackInfo ci) {
        //this.inventory = new FnafPlayerInventory(((PlayerEntity)(Object)this), ((PlayerEntity)(Object)this).equipment);

    }

    @Unique
    LightDataProvider getLightDataProvider(){
        if(lightDataProvider == null){
            lightDataProvider = new LightDataProvider(((PlayerEntity) (Object)this));
        }
        return lightDataProvider;
    }

    @Inject (method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity) (Object)this);
        PlayerHook.playerTick(player);
        //ItemStack stack = player.getInventory()..get(2);
        //if(stack.isOf(ItemInit.ILLUSIONDISC)){
            /*
            String animatronic = ItemNbtUtil.getNbt(stack).getString("entity");
            NbtCompound animatronicData = ItemNbtUtil.getNbt(stack).getCompound("entityData");
            if (!animatronic.isEmpty() && ComputerData.getAIAnimatronic(animatronic) instanceof ComputerData.Initializer.AnimatronicAI ai) {

                if (((IPlayerCustomModel) player).getCurrentEntity() == null || ((IPlayerCustomModel)player).getCurrentEntity().getType() != ai.entityType()) {
                    LivingEntity entity1 = ai.entityType().create(player.getWorld(), SpawnReason.TRIGGERED);
                    ((IEntityDataSaver) entity1).getPersistentData().copyFrom(animatronicData);
                    if (entity1 instanceof DefaultEntity ent) {
                        ent.mimicPlayer = player;
                        ent.mimic = true;
                        ent.setAiDisabled(true);
                        setCurrentEntity(ent);
                    }
                }
            }
            if(getCurrentEntity() != null) {
                DefaultEntity entity = getCurrentEntity();

                entity.tick();

                World world = entity.mimicPlayer.getWorld();
                entity.setCrawling(entity.mimicPlayer.isCrawling(), world);
                entity.setRunning(entity.mimicPlayer.isSprinting(), world);

                if(player.getAttacking() instanceof PlayerEntity p) {
                    ((IEntityDataSaver)p).getPersistentData().putInt("JumpscareID", entity.getId());
                }

                player.calculateDimensions();
            }
            */
        //}
        //else {
        //    resetCurrentEntity();
        //}
    }

    @Inject(method = "updatePose", at = @At("HEAD"), cancellable = true)
    public void updatePose(CallbackInfo ci){
        PlayerEntity player = ((PlayerEntity) (Object)this);
        if(shouldBeCrawling()) {
            player.setPose(EntityPose.SWIMMING);
            ci.cancel();
        }
    }

    @Override
    public float getMaskDelta() {
        return maskOnDelta;
    }

    @Override
    public void setMaskDelta(float delta) {
        maskOnDelta = delta;
    }

    @Override
    public boolean hasVanniMaskOn() {
        ItemStack stack = ((PlayerEntity) (Object)this).getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET);
        if(stack.getItem() instanceof VanniMaskItem){
            NbtCompound nbt = ItemUtil.getNbt(stack);
            return nbt.getBoolean("inVanniMask", false);
        }
        return false;
    }

    @Override
    public boolean hasVanniMaskEquipped() {
        ItemStack stack = ((PlayerEntity) (Object)this).getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET);
        return stack.getItem() instanceof VanniMaskItem;
    }

    @Override
    public boolean isUsingVanniMask() {

        boolean bl = true;
        if(((PlayerEntity) (Object)this).getEntityWorld().isClient()){
            bl = getMaskDelta() > 1.42f;
        }
        return bl && hasVanniMaskOn();
    }

    @Override
    public boolean canAnimateMask() {
        return canAnimateMask;
    }

    @Override
    public void setCanAnimateMask(boolean can) {
        canAnimateMask = can;
    }

    @Override
    public LivingEntity getCurrentEntity() {
        return currentEntity;
    }

    @Override
    public void setCurrentEntity(@Nullable LivingEntity entity) {
        currentEntity = entity;
    }
    @Override
    public void resetCurrentEntity() {
        PlayerEntity player = ((PlayerEntity) (Object)this);
        if(currentEntity != null) {
            //currentEntity.mimicPlayer.calculateDimensions(); //TODO FIX THIS
            currentEntity.remove(Entity.RemovalReason.DISCARDED);
            currentEntity = null;
            player.calculateDimensions();
        }
    }

    @Override
    public float getMimicYaw() {
        return mimicBodyYaw;
    }

    @Override
    public void setMimicYaw(float yaw) {
        mimicBodyYaw = yaw;
    }

    @Override
    public boolean shouldBeCrawling() {
        return crawling;
    }

    @Override
    public void setCrawling(boolean crawling) {
        this.crawling = crawling;
    }


    @Override
    public boolean isLightOn(Entity stack) {
        return getLightDataProvider().isLightOn();
    }

    @Override
    public void setLightOn(boolean isOn, Entity stack) {
        getLightDataProvider().setLightOn(isOn);
    }

    @Override
    public Identifier getLightMaskTexture() {
        return getLightDataProvider().getLightMaskTexture();
    }

    @Override
    public Vec3d getLightWorldPos() {
        return getLightDataProvider().getLightWorldPos();
    }

    @Override
    public Vector3f getLightRotation() {
        return getLightDataProvider().getLightRotation();
    }

    @Override
    public float getLength() {
        return getLightDataProvider().getLength();
    }

    @Override
    public float getIntensity() {
        return getLightDataProvider().getIntensity();
    }

    @Override
    public float getEdgeSmoothness() {
        return getLightDataProvider().getEdgeSmoothness();
    }

    @Override
    public float getDistanceSmoothness() {
        return getLightDataProvider().getDistanceSmoothness();
    }

    @Override
    public float getNormalInfluence() {
        return getLightDataProvider().getNormalInfluence();
    }

    @Override
    public float getMinRadius() {
        return getLightDataProvider().getMinRadius();
    }

    @Override
    public float getMaxRadius() {
        return getLightDataProvider().getMaxRadius();
    }

    @Override
    public Vector3f getLightColor() {
        return getLightDataProvider().getLightColor();
    }

    //@Inject(method = "getEquippedStack", at = @At("HEAD"), cancellable = true)
    //public void getDualHandItem(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> ci) {
        /*if(slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND){
            ItemStack stack = ItemStack.fromNbtOrEmpty(((PlayerEntity)(Object)this).getWorld().getRegistryManager(),((IEntityDataSaver)((Object)this)).getPersistentData().getCompound("dualHandItem"));

            if(stack.isOf(PropInit.COSMO_GIFT.asItem())){
                ci.setReturnValue(stack);
            }
        }*/
    //}
}

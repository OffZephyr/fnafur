package net.zephyr.fnafur.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin{
    @Inject(method = "onEquipStack", at = @At("HEAD"))
    public void onEquipStack(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci){
        LivingEntity player = ((LivingEntity) (Object)this);

        if(player instanceof PlayerEntity p) {
            if ((oldStack.isOf(ItemInit.ILLUSIONDISC) || newStack.isOf(ItemInit.ILLUSIONDISC)) && slot.isArmorSlot()) {
                if(p.getWorld() instanceof ServerWorld world) {
                    double width = p.getBoundingBox().getLengthX() / 2f;
                    double height = p.getBoundingBox().getLengthY() / 2f;
                    double amount = (height + 1) * 100f;

                    world.spawnParticles(ParticleTypes.CLOUD, p.getX(), (p.getY() + height), p.getZ(), (int) amount, width, height, width, 0.15f);
                }
                p.calculateDimensions();
                p.setPose(EntityPose.STANDING);
            }
        }
    }
    @Inject(method = "updatePostDeath", at = @At("HEAD"), cancellable = true)
    public void updatePostDeath(CallbackInfo info) {
        LivingEntity player = ((LivingEntity) (Object) this);
        if(player != null) {
        Entity entity = player.getWorld().getEntityById(((IEntityDataSaver)player).getPersistentData().getInt("JumpscareID"));

        if (player.getRecentDamageSource() != null &&
                player.getRecentDamageSource().getAttacker() instanceof DefaultEntity &&
                entity instanceof DefaultEntity ent &&
                ent.hasJumpScare()) {
            ++player.deathTime;

            if (player.deathTime >= ent.JumpScareLength()) {
                player.remove(Entity.RemovalReason.KILLED);
            }
            info.cancel();
        }
    }
    }
}

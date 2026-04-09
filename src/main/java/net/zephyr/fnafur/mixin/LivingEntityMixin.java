package net.zephyr.fnafur.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "onEquipItem", at = @At("HEAD"))
    public void onEquipStack(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci){
        LivingEntity player = ((LivingEntity) (Object)this);

        if(player instanceof Player p) {
            if ((oldStack.is(ItemInit.ILLUSIONDISC) || newStack.is(ItemInit.ILLUSIONDISC)) && slot.isArmor()) {
                if(p.level() instanceof ServerLevel world) {
                    double width = p.getBoundingBox().getXsize() / 2f;
                    double height = p.getBoundingBox().getYsize() / 2f;
                    double amount = (height + 1) * 100f;

                    world.sendParticles(ParticleTypes.CLOUD, p.getX(), (p.getY() + height), p.getZ(), (int) amount, width, height, width, 0.15f);
                }
                p.refreshDimensions();
                p.setPose(Pose.STANDING);
            }
        }
    }



    @Inject(method = "getDefaultDimensions", at = @At("HEAD"), cancellable = true)
    public void getBaseDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> ci){
        if((LivingEntity)(Object)this instanceof Player p) {
            if (((IUniversePlayer) p).getCurrentEntity() != null) {
                ci.setReturnValue(((IUniversePlayer) p).getCurrentEntity().getType().getDimensions().scale(((IUniversePlayer) p).getCurrentEntity().getAgeScale()));
            }
        }
    }
    //@Inject(method = "updatePostDeath", at = @At("HEAD"), cancellable = true)
    //public void updatePostDeath(CallbackInfo info) {
    //    LivingEntity player = ((LivingEntity) (Object) this);
    //    if (player != null) {
    //        Entity entity = player.getWorld().getEntityById(((IEntityDataSaver) player).getPersistentData().getInt("JumpscareID"));

    //        if (player.getRecentDamageSource() != null &&
    //                player.getRecentDamageSource().getAttacker() instanceof DefaultEntity &&
    //                entity instanceof DefaultEntity ent &&
    //                ent.hasJumpScare()) {
    //            ++player.deathTime;

    //            if (player.deathTime >= ent.JumpScareLength()) {
    //                player.remove(Entity.RemovalReason.KILLED);
    //            }
    //            info.cancel();
    //        }
    //    }
    //}
}

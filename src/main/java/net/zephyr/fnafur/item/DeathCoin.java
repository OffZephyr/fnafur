package net.zephyr.fnafur.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.zephyr.fnafur.init.ParticlesInit;

public class DeathCoin extends ItemWithDescription{
    public DeathCoin(Settings settings) {
        super(settings, ItemWithDescription.COIN);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(target.getWorld() instanceof ServerWorld level) {
            double width = target.getBoundingBox().getLengthX() / 2f;
            double height = target.getBoundingBox().getLengthY() / 2f;
            double amount = (height + 1) * 100f;

            level.spawnParticles(ParticlesInit.FOG_PARTICLE, target.getX(), (target.getY() + height), target.getZ(), (int)amount, width, height, width, 0.15);
        }
        target.getWorld().playSound(target, target.getBlockPos(), SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST.value(), SoundCategory.PLAYERS, 1, 0);

        if(target instanceof PlayerEntity ent){
            ent.damage(ent.getWorld().getDamageSources().generic(), 999999999);
        }
        else {
            target.remove(Entity.RemovalReason.DISCARDED);
        }

        stack.decrementUnlessCreative(1, attacker);
        return super.postHit(stack, target, attacker);
    }
}

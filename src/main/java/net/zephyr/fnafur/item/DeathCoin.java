package net.zephyr.fnafur.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.zephyr.fnafur.init.ParticlesInit;

public class DeathCoin extends Item {
    public DeathCoin(Properties settings) {
        super(settings);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(target.level() instanceof ServerLevel level) {
            double width = target.getBoundingBox().getXsize() / 2f;
            double height = target.getBoundingBox().getYsize() / 2f;
            double amount = (height + 1) * 100f;

            level.sendParticles(ParticlesInit.FOG_PARTICLE, target.getX(), (target.getY() + height), target.getZ(), (int)amount, width, height, width, 0.15);
        }
        target.level().playSound(target, target.blockPosition(), SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.PLAYERS, 1, 0);

        if(target instanceof Player ent && ent.level() instanceof ServerLevel serverWorld){
            ent.hurtServer(serverWorld, ent.level().damageSources().generic(), 999999999);
        }
        else {
            target.remove(Entity.RemovalReason.DISCARDED);
        }

        stack.consume(1, attacker);
    }
}

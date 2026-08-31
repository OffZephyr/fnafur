package net.zephyr.fnafur.entity.other.bear5;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.SoundUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.UUID;

public class Bear5Entity extends PathfinderMob {

    public Player target;
    public Bear5Entity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
        noPhysics = true;
        setNoGravity(true);
    }

    @Override
    public boolean isUnderWater() {
        return false;
    }

    @Override
    public void lavaHurt() {
    }

    @Override
    public boolean isInLava() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isInLiquid() {
        return false;
    }
    @Override
    protected boolean updateFluidInteraction() {
        return false;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void tick() {
        if(level().isClientSide()) {
            if (!SoundUtils.playingSound(this, SoundsInit.BEAR5)) {
                SoundUtils.playMutableSound(this, SoundsInit.BEAR5, 1, 1);
            }
        }
        if(((IEntityDataSaver)this).getPersistentData().contains("TargetID")) {
            if(!level().isClientSide()){
                GoopyNetworkingUtils.saveEntityNbt(getId(), ((IEntityDataSaver)this).getPersistentData(), level());
            }
            UUID targetID = UUID.fromString(((IEntityDataSaver)this).getPersistentData().getString("TargetID").get());
            Player entity = level().getPlayerByUUID(targetID);
            if(entity != null){
                entity.displayClientMessage(Component.literal("§9Something §1§lWICKED §9this way comes......"), true);
                this.target = entity;
            }
        }

        if(this.target != null){
            Vec3 distance = this.target.position().add(position().scale(-1));
            Vec3 direction = distance.normalize();
            float dist = this.distanceTo(this.target);
            setDeltaMovement(getDeltaMovement().add(direction.scale(0.01f + 0.01f * (dist/10f))));

            if(dist < 0.5f && level() instanceof ServerLevel world){
                this.target.kill(world);
                this.remove(RemovalReason.DISCARDED);
            }
        }

        setNoGravity(true);
        super.tick();
    }

    public static AttributeSupplier.Builder setAttributes() {

        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15f)
                .add(Attributes.ATTACK_DAMAGE, 10f)
                .add(Attributes.ATTACK_SPEED, 1f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.25f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10f)
                .add(Attributes.FOLLOW_RANGE, 12D);
    }
}

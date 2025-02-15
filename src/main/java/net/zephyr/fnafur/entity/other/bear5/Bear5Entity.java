package net.zephyr.fnafur.entity.other.bear5;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.SoundUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.UUID;

public class Bear5Entity extends PathAwareEntity {

    public PlayerEntity target;
    public Bear5Entity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        noClip = true;
        setNoGravity(true);
    }

    @Override
    public boolean isPersistent() {
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
        if(!SoundUtils.playingSound(this, SoundsInit.BEAR5)){
            SoundUtils.playMutableSound(this, SoundsInit.BEAR5, 1, 1);
        }
        if(((IEntityDataSaver)this).getPersistentData().contains("TargetID")) {
            if(!getWorld().isClient()){
                GoopyNetworkingUtils.saveEntityNbt(getId(), ((IEntityDataSaver)this).getPersistentData(), getWorld());
            }
            UUID targetID = ((IEntityDataSaver)this).getPersistentData().getUuid("TargetID");
            PlayerEntity entity = getWorld().getPlayerByUuid(targetID);
            if(entity != null){
                this.target = entity;
            }
        }

        if(this.target != null){
            Vec3d distance = this.target.getPos().add(getPos().multiply(-1));
            Vec3d direction = distance.normalize();
            float dist = this.distanceTo(this.target);
            setVelocity(getVelocity().add(direction.multiply(0.01f + 0.01f * (dist/10f))));

            if(dist < 0.25f && getWorld() instanceof ServerWorld world){
                this.target.kill(world);
                this.remove(RemovalReason.DISCARDED);
            }
        }

        setNoGravity(true);
        super.tick();
    }

    public static DefaultAttributeContainer.Builder setAttributes() {

        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 15f)
                .add(EntityAttributes.ATTACK_DAMAGE, 10f)
                .add(EntityAttributes.ATTACK_SPEED, 1f)
                .add(EntityAttributes.ATTACK_KNOCKBACK, 0.25f)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 10f)
                .add(EntityAttributes.FOLLOW_RANGE, 12D);
    }
}

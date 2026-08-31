package net.zephyr.fnafur.blocks.special;

import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class SeatEntity extends Entity {
    public SeatEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        return false;
    }


    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        return this.position();
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {

    }

    @Override
    public void tick() {

        BlockPos pos = BlockPos.of(((IEntityDataSaver) this).getPersistentData().getLong("chair").orElse(0L));
        int timer = ((IEntityDataSaver) this).getPersistentData().getInt("despawnTimer").orElse(0);
        if (!isVehicle() || level().getBlockState(pos).is(Blocks.AIR)) {
            if (timer >= 5) {

                if (level().getBlockEntity(pos) instanceof BlockEntity ent) {
                    ((IEntityDataSaver) ent).getPersistentData().putBoolean("playerSitting", false);
                }
                remove(RemovalReason.DISCARDED);

            } else {
                ((IEntityDataSaver) this).getPersistentData().putInt("despawnTimer", timer + 1);
            }
        } else {
            if (timer != 0) {
                ((IEntityDataSaver) this).getPersistentData().putInt("despawnTimer", 0);
            }
        }
    }

    @Override
    public void onRemoval(RemovalReason reason) {

        BlockPos pos = BlockPos.of(((IEntityDataSaver)this).getPersistentData().getLong("chair").orElse(0L));
        if(level().getBlockEntity(pos) instanceof BlockEntity ent){
            ((IEntityDataSaver)ent).getPersistentData().putBoolean("playerSitting", false);
        }
        super.onRemoval(reason);
    }

    /*@Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Vec3d vec3d = getPassengerDismountOffset(this.getWidth(), passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.RIGHT ? 90.0F : -90.0F));
        Vec3d vec3d2 = this.locateSafeDismountingPos(vec3d, passenger);
        if (vec3d2 != null) {
            return vec3d2;
        } else {
            Vec3d vec3d3 = getPassengerDismountOffset(this.getWidth(), passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.LEFT ? 90.0F : -90.0F));
            Vec3d vec3d4 = this.locateSafeDismountingPos(vec3d3, passenger);
            return vec3d4 != null ? vec3d4 : this.getPos();
        }
    }*/
}

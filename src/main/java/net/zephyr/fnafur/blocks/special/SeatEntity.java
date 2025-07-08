package net.zephyr.fnafur.blocks.special;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class SeatEntity extends Entity {
    public SeatEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }


    @Override
    public Vec3d getPassengerRidingPos(Entity passenger) {
        return this.getPos();
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return false;
    }

    @Override
    protected void readCustomData(ReadView view) {

    }

    @Override
    protected void writeCustomData(WriteView view) {

    }

    @Override
    public void tick() {

        BlockPos pos = BlockPos.fromLong(((IEntityDataSaver) this).getPersistentData().getLong("chair").orElse(0L));
        int timer = ((IEntityDataSaver) this).getPersistentData().getInt("despawnTimer").orElse(0);
        if (!hasPassengers() || getWorld().getBlockState(pos).isOf(Blocks.AIR)) {
            if (timer >= 5) {

                if (getWorld().getBlockEntity(pos) instanceof BlockEntity ent) {
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
    public void onRemove(RemovalReason reason) {

        BlockPos pos = BlockPos.fromLong(((IEntityDataSaver)this).getPersistentData().getLong("chair").orElse(0L));
        if(getWorld().getBlockEntity(pos) instanceof BlockEntity ent){
            ((IEntityDataSaver)ent).getPersistentData().putBoolean("playerSitting", false);
        }
        super.onRemove(reason);
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

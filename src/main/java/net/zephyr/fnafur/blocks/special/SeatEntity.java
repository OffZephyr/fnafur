package net.zephyr.fnafur.blocks.special;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
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
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

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
    public void tick() {

        BlockPos pos = BlockPos.fromLong(((IEntityDataSaver) this).getPersistentData().getLong("chair"));
        if (getWorld().getBlockEntity(pos) instanceof BlockEntity ent) {
            int timer = ((IEntityDataSaver) ent).getPersistentData().getInt("despawnTimer");
            if (!hasPassengers() || getWorld().getBlockState(pos).isOf(Blocks.AIR)) {

                if (timer >= 20) {

                    ((IEntityDataSaver) ent).getPersistentData().putBoolean("playerSitting", false);
                    remove(RemovalReason.DISCARDED);

                } else {
                    ((IEntityDataSaver) ent).getPersistentData().putInt("despawnTimer", timer + 1);
                }
            } else {
                if (timer != 0) {
                    ((IEntityDataSaver) ent).getPersistentData().putInt("despawnTimer", 0);
                }
            }
        }
    }

    @Override
    public void onRemove(RemovalReason reason) {

        BlockPos pos = BlockPos.fromLong(((IEntityDataSaver)this).getPersistentData().getLong("chair"));
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

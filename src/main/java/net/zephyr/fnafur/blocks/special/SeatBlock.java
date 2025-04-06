package net.zephyr.fnafur.blocks.special;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public interface SeatBlock {
    float getSittingHeight(World world, BlockPos pos);
    float getSittingOffset(World world, BlockPos pos);
    default float getSittingAngle(World world, BlockPos pos){
        return ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getFloat("Rotation") + 180;
    }
    default Vec3d getSittingPos(World world, float angle, float offset, float height, BlockPos pos) {
        double x = ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().getDouble("xOffset");
        double z = ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().getDouble("zOffset");

        angle = (angle + 90) * MathHelper.RADIANS_PER_DEGREE;
        Vec3d vec = new Vec3d(offset * MathHelper.cos(angle), 0, offset * MathHelper.sin(angle));
        Vec3d propOffset = new Vec3d(x - 0.5f, height, z - 0.5f);
        return pos.toCenterPos().add(propOffset).add(vec);
    }

    default SeatEntity sit(PlayerEntity player, BlockPos pos){
        SeatEntity entity = new SeatEntity(EntityInit.SEAT, player.getWorld());

        entity.setHeadYaw(getSittingAngle(player.getWorld(), pos));
        entity.setYaw(getSittingAngle(player.getWorld(), pos));
        entity.setBodyYaw(getSittingAngle(player.getWorld(), pos));
        entity.setPitch(0);

        entity.setPosition(getSittingPos(player.getWorld(), getSittingAngle(player.getWorld(), pos), getSittingOffset(player.getWorld(), pos), getSittingHeight(player.getWorld(), pos), pos));

        ((IEntityDataSaver)entity).getPersistentData().putLong("chair", pos.asLong());

        ((IEntityDataSaver)entity).getPersistentData().putInt("playerID", player.getId());

        player.getWorld().spawnEntity(entity);

        player.setHeadYaw(getSittingAngle(player.getWorld(), pos));
        player.setYaw(getSittingAngle(player.getWorld(), pos));
        player.setBodyYaw(getSittingAngle(player.getWorld(), pos));

        ((IEntityDataSaver)player.getWorld().getBlockEntity(pos)).getPersistentData().putBoolean("playerSitting", true);
        return entity;
    }
    default boolean isUsed(World world, BlockPos pos){
        return ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getBoolean("playerSitting");
    }
}

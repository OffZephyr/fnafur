package net.zephyr.fnafur.blocks.special;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateEntityNbtS2CPongPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public interface SeatBlock {
    float getSittingHeight(World world, BlockPos pos);
    float getSittingOffset(World world, BlockPos pos);
    default float getSittingAngle(World world, BlockPos pos){
        return ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getFloat("Rotation").get() + 180;
    }
    default Vec3d getSittingPos(World world, float angle, float offset, float height, BlockPos pos) {
        double x = ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().getDouble("xOffset").get();
        double z = ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().getDouble("zOffset").get();

        angle = (angle + 90) * MathHelper.RADIANS_PER_DEGREE;
        Vec3d vec = new Vec3d(offset * MathHelper.cos(angle), 0, offset * MathHelper.sin(angle));
        Vec3d propOffset = new Vec3d(x - 0.5f, height, z - 0.5f);
        return pos.toCenterPos().add(propOffset).add(vec);
    }

    default SeatEntity sit(PlayerEntity player, BlockPos pos){
        SeatEntity entity = new SeatEntity(EntityInit.SEAT, player.getEntityWorld());

        entity.setHeadYaw(getSittingAngle(player.getEntityWorld(), pos));
        entity.setYaw(getSittingAngle(player.getEntityWorld(), pos));
        entity.setBodyYaw(getSittingAngle(player.getEntityWorld(), pos));
        entity.setPitch(0);

        entity.setPosition(getSittingPos(player.getEntityWorld(), getSittingAngle(player.getEntityWorld(), pos), getSittingOffset(player.getEntityWorld(), pos), getSittingHeight(player.getEntityWorld(), pos), pos));
        ((IEntityDataSaver)entity).getPersistentData().putLong("chair", pos.asLong());

        ((IEntityDataSaver)entity).getPersistentData().putInt("playerID", player.getId());

        player.getEntityWorld().spawnEntity(entity);

        if(!player.getEntityWorld().isClient()){
            for(ServerPlayerEntity p : PlayerLookup.all(player.getEntityWorld().getServer())){
                ServerPlayNetworking.send(p, new UpdateEntityNbtS2CPongPayload(entity.getId(), ((IEntityDataSaver)entity).getPersistentData()));
            }
        }

        player.setHeadYaw(getSittingAngle(player.getEntityWorld(), pos));
        player.setYaw(getSittingAngle(player.getEntityWorld(), pos));
        player.setBodyYaw(getSittingAngle(player.getEntityWorld(), pos));

        ((IEntityDataSaver)player.getEntityWorld().getBlockEntity(pos)).getPersistentData().putBoolean("playerSitting", true);

        return entity;
    }
    default boolean isUsed(World world, BlockPos pos){
        return ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getBoolean("playerSitting").orElse(false);
    }
}

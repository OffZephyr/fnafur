package net.zephyr.fnafur.blocks.special;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateEntityNbtS2CPongPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public interface SeatBlock {
    float getSittingHeight(Level world, BlockPos pos);
    float getSittingOffset(Level world, BlockPos pos);
    default float getSittingAngle(Level world, BlockPos pos){
        return ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getFloat("Rotation").get() + 180;
    }
    default Vec3 getSittingPos(Level world, float angle, float offset, float height, BlockPos pos) {
        double x = ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().getDouble("xOffset").get();
        double z = ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().getDouble("zOffset").get();

        angle = (angle + 90) * Mth.DEG_TO_RAD;
        Vec3 vec = new Vec3(offset * Mth.cos(angle), 0, offset * Mth.sin(angle));
        Vec3 propOffset = new Vec3(x - 0.5f, height, z - 0.5f);
        return pos.getCenter().add(propOffset).add(vec);
    }

    default SeatEntity sit(Player player, BlockPos pos){
        SeatEntity entity = new SeatEntity(EntityInit.SEAT, player.level());

        entity.setYHeadRot(getSittingAngle(player.level(), pos));
        entity.setYRot(getSittingAngle(player.level(), pos));
        entity.setYBodyRot(getSittingAngle(player.level(), pos));
        entity.setXRot(0);

        entity.setPos(getSittingPos(player.level(), getSittingAngle(player.level(), pos), getSittingOffset(player.level(), pos), getSittingHeight(player.level(), pos), pos));
        ((IEntityDataSaver)entity).getPersistentData().putLong("chair", pos.asLong());

        ((IEntityDataSaver)entity).getPersistentData().putInt("playerID", player.getId());

        player.level().addFreshEntity(entity);

        if(!player.level().isClientSide()){
            for(ServerPlayer p : PlayerLookup.all(player.level().getServer())){
                ServerPlayNetworking.send(p, new UpdateEntityNbtS2CPongPayload(entity.getId(), ((IEntityDataSaver)entity).getPersistentData()));
            }
        }

        player.setYHeadRot(getSittingAngle(player.level(), pos));
        player.setYRot(getSittingAngle(player.level(), pos));
        player.setYBodyRot(getSittingAngle(player.level(), pos));

        ((IEntityDataSaver)player.level().getBlockEntity(pos)).getPersistentData().putBoolean("playerSitting", true);

        return entity;
    }
    default boolean isUsed(Level world, BlockPos pos){
        return ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getBoolean("playerSitting").orElse(false);
    }
}

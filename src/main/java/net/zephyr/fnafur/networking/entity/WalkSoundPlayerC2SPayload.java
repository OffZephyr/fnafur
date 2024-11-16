package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.networking.nbt_updates.NbtPayloads;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record WalkSoundPlayerC2SPayload(int EntityID) implements CustomPayload {
    public static final Id<WalkSoundPlayerC2SPayload> ID = new Id<>(EntityPayloads.C2SWalkSoundPlayer);
    public static final PacketCodec<RegistryByteBuf, WalkSoundPlayerC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, WalkSoundPlayerC2SPayload::EntityID,
            WalkSoundPlayerC2SPayload::new);

    public static void receive(WalkSoundPlayerC2SPayload payload, ServerPlayNetworking.Context context) {
        Entity entity = context.player().getWorld().getEntityById(payload.EntityID);
        if(entity instanceof DefaultEntity entity1){
            entity1.playWalkSound(context.player().getWorld());
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

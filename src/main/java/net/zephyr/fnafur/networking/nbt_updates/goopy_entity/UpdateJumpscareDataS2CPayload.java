package net.zephyr.fnafur.networking.nbt_updates.goopy_entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.networking.nbt_updates.NbtPayloads;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateJumpscareDataS2CPayload(int entityID) implements CustomPayload {
    public static final CustomPayload.Id<UpdateJumpscareDataS2CPayload> ID = new CustomPayload.Id<>(NbtPayloads.S2CJumpscareData);
    public static final PacketCodec<RegistryByteBuf, UpdateJumpscareDataS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, UpdateJumpscareDataS2CPayload::entityID,
            UpdateJumpscareDataS2CPayload::new);

    public static void receive(UpdateJumpscareDataS2CPayload payload, ClientPlayNetworking.Context context) {
        ((IEntityDataSaver) context.player()).getPersistentData().putInt("JumpscareID", payload.entityID());

    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

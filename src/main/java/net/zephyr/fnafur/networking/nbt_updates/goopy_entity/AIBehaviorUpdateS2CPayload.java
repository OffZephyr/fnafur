package net.zephyr.fnafur.networking.nbt_updates.goopy_entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.networking.nbt_updates.NbtPayloads;

public record AIBehaviorUpdateS2CPayload(String behavior, String option, int entityID) implements CustomPayload {
    public static final Id<AIBehaviorUpdateS2CPayload> ID = new Id<>(NbtPayloads.S2CAIUpdate);
    public static final PacketCodec<RegistryByteBuf, AIBehaviorUpdateS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, AIBehaviorUpdateS2CPayload::behavior,
            PacketCodecs.STRING, AIBehaviorUpdateS2CPayload::option,
            PacketCodecs.INTEGER, AIBehaviorUpdateS2CPayload::entityID,
            AIBehaviorUpdateS2CPayload::new);

    public static void receive(AIBehaviorUpdateS2CPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.client().world.getEntityById(payload.entityID);
        if(entity instanceof DefaultEntity ent) {
            int index = ent.getIndex(payload.behavior, payload.option);
            ClientPlayNetworking.send(new AIBehaviorUpdateC2SPayload(payload.behavior(), payload.option(), index, payload.entityID));
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

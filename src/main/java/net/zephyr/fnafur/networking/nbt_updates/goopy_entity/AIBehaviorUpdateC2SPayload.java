package net.zephyr.fnafur.networking.nbt_updates.goopy_entity;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.networking.nbt_updates.NbtPayloads;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record AIBehaviorUpdateC2SPayload(String behavior, String option, int index, int EntityID) implements CustomPayload {
    public static final CustomPayload.Id<AIBehaviorUpdateC2SPayload> ID = new CustomPayload.Id<>(NbtPayloads.C2SAIUpdate);
    public static final PacketCodec<RegistryByteBuf, AIBehaviorUpdateC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, AIBehaviorUpdateC2SPayload::behavior,
            PacketCodecs.STRING, AIBehaviorUpdateC2SPayload::option,
            PacketCodecs.INTEGER, AIBehaviorUpdateC2SPayload::index,
            PacketCodecs.INTEGER, AIBehaviorUpdateC2SPayload::EntityID,
            AIBehaviorUpdateC2SPayload::new);

    public static void receive(AIBehaviorUpdateC2SPayload payload, ServerPlayNetworking.Context context) {

        Entity entity = context.player().getWorld().getEntityById(payload.EntityID);
        if(entity instanceof DefaultEntity ent) {
            ((IEntityDataSaver)ent).getPersistentData().putInt(payload.behavior() + "." + payload.option(), payload.index());
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

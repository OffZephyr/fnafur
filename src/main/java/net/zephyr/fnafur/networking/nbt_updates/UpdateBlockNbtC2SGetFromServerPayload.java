package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateBlockNbtC2SGetFromServerPayload(long pos) implements CustomPayload {

    public static final Id<UpdateBlockNbtC2SGetFromServerPayload> ID = new Id<>(NbtPayloads.S2CBlockUpdateClient);

    public static final PacketCodec<RegistryByteBuf, UpdateBlockNbtC2SGetFromServerPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_LONG, UpdateBlockNbtC2SGetFromServerPayload::pos,
            UpdateBlockNbtC2SGetFromServerPayload::new);

    public static void receive(UpdateBlockNbtC2SGetFromServerPayload payload, ServerPlayNetworking.Context context) {
        BlockEntity entity = context.player().getWorld().getBlockEntity(BlockPos.fromLong(payload.pos()));
        if (entity == null) return;
        for (ServerPlayerEntity p : PlayerLookup.all(context.server())) {
            //p.sendMessage(Text.literal("UPDATING FROM SERVER"), false);
            ServerPlayNetworking.send(p, new UpdateBlockNbtS2CPongPayload(payload.pos(), ((IEntityDataSaver) entity).getPersistentData()));
        }

    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}

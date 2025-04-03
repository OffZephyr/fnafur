package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.utility_blocks.cosmo_gift.GalaxyLayerGeoPropEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record WorkbenchSaveS2CPayload(long pos, NbtCompound nbt) implements CustomPayload {
    public static final Id<WorkbenchSaveS2CPayload> ID = new Id<>(EntityPayloads.S2CWorkbenchSave);

    public static final PacketCodec<RegistryByteBuf, WorkbenchSaveS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.LONG, WorkbenchSaveS2CPayload::pos,
            PacketCodecs.NBT_COMPOUND, WorkbenchSaveS2CPayload::nbt,
            WorkbenchSaveS2CPayload::new);

    public static void receive(WorkbenchSaveS2CPayload payload, ClientPlayNetworking.Context context) {
        if(context.player().getWorld().getBlockEntity(BlockPos.fromLong(payload.pos())) instanceof GalaxyLayerGeoPropEntity ent){
            ((IEntityDataSaver)ent).getPersistentData().copyFrom(payload.nbt());
        }
    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.tile_doors.TileDoorBlock;
import net.zephyr.fnafur.blocks.utility_blocks.cosmo_gift.GalaxyLayerGeoPropEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record TileDoorUpdateS2CPayload(long pos, boolean open) implements CustomPayload {
    public static final Id<TileDoorUpdateS2CPayload> ID = new Id<>(BlockPayloads.S2CTileDoorOpenUpdate);

    public static final PacketCodec<RegistryByteBuf, TileDoorUpdateS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.LONG, TileDoorUpdateS2CPayload::pos,
            PacketCodecs.BOOLEAN, TileDoorUpdateS2CPayload::open,
            TileDoorUpdateS2CPayload::new);

    public static void receive(TileDoorUpdateS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().world.setBlockState(BlockPos.fromLong(payload.pos()), context.client().world.getBlockState(BlockPos.fromLong(payload.pos())).with(TileDoorBlock.OPEN, payload.open()), 0);
    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift.GalaxyLayerGeoPropEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record WorkbenchSaveS2CPayload(long pos, CompoundTag nbt) implements CustomPacketPayload {
    public static final Type<WorkbenchSaveS2CPayload> ID = new Type<>(EntityPayloads.S2CWorkbenchSave);

    public static final StreamCodec<RegistryFriendlyByteBuf, WorkbenchSaveS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, WorkbenchSaveS2CPayload::pos,
            ByteBufCodecs.COMPOUND_TAG, WorkbenchSaveS2CPayload::nbt,
            WorkbenchSaveS2CPayload::new);

    public static void receive(WorkbenchSaveS2CPayload payload, ClientPlayNetworking.Context context) {
        if(context.player().level().getBlockEntity(BlockPos.of(payload.pos())) instanceof GalaxyLayerGeoPropEntity ent){
            ((IEntityDataSaver)ent).getPersistentData().merge(payload.nbt());
        }
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

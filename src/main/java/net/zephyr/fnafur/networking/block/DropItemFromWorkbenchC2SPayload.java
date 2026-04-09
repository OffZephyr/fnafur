package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.workbench.WorkbenchBlock;

public record DropItemFromWorkbenchC2SPayload(long pos, String chara, String alt, String eyes) implements CustomPacketPayload {
    public static final Type<DropItemFromWorkbenchC2SPayload> ID = new Type<>(BlockPayloads.C2SDropItemFromWorkbench);
    public static final StreamCodec<RegistryFriendlyByteBuf, DropItemFromWorkbenchC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, DropItemFromWorkbenchC2SPayload::pos,
            ByteBufCodecs.STRING_UTF8, DropItemFromWorkbenchC2SPayload::chara,
            ByteBufCodecs.STRING_UTF8, DropItemFromWorkbenchC2SPayload::alt,
            ByteBufCodecs.STRING_UTF8, DropItemFromWorkbenchC2SPayload::eyes,
            DropItemFromWorkbenchC2SPayload::new);

    public static void receive(DropItemFromWorkbenchC2SPayload payload, ServerPlayNetworking.Context context) {
        if(context.player().level().getBlockState(BlockPos.of(payload.pos())).getBlock() instanceof WorkbenchBlock){
            WorkbenchBlock.spawnItem(context.player().level(), BlockPos.of(payload.pos()), payload.chara, payload.alt, payload.eyes);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

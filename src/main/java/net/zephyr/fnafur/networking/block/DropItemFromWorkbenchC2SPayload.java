package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.workbench.WorkbenchBlock;

public record DropItemFromWorkbenchC2SPayload(long pos, String chara, String alt, String eyes) implements CustomPayload {
    public static final Id<DropItemFromWorkbenchC2SPayload> ID = new Id<>(BlockPayloads.C2SDropItemFromWorkbench);
    public static final PacketCodec<RegistryByteBuf, DropItemFromWorkbenchC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.LONG, DropItemFromWorkbenchC2SPayload::pos,
            PacketCodecs.STRING, DropItemFromWorkbenchC2SPayload::chara,
            PacketCodecs.STRING, DropItemFromWorkbenchC2SPayload::alt,
            PacketCodecs.STRING, DropItemFromWorkbenchC2SPayload::eyes,
            DropItemFromWorkbenchC2SPayload::new);

    public static void receive(DropItemFromWorkbenchC2SPayload payload, ServerPlayNetworking.Context context) {
        if(context.player().getEntityWorld().getBlockState(BlockPos.fromLong(payload.pos())).getBlock() instanceof WorkbenchBlock){
            WorkbenchBlock.spawnItem(context.player().getEntityWorld(), BlockPos.fromLong(payload.pos()), payload.chara, payload.alt, payload.eyes);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift.GalaxyLayerGeoPropEntity;
import net.zephyr.fnafur.init.block_init.PropInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record WorkbenchSaveC2SPayload(long pos, CompoundTag nbt) implements CustomPacketPayload {
    public static final Type<WorkbenchSaveC2SPayload> ID = new Type<>(EntityPayloads.C2SWorkbenchSave);

    public static final StreamCodec<RegistryFriendlyByteBuf, WorkbenchSaveC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, WorkbenchSaveC2SPayload::pos,
            ByteBufCodecs.COMPOUND_TAG, WorkbenchSaveC2SPayload::nbt,
            WorkbenchSaveC2SPayload::new);

    public static void receive(WorkbenchSaveC2SPayload payload, ServerPlayNetworking.Context context) {
        context.player().level().setBlockAndUpdate(BlockPos.of(payload.pos()), PropInit.COSMO_GIFT.defaultBlockState());

        if(context.player().level().getBlockEntity(BlockPos.of(payload.pos())) instanceof GalaxyLayerGeoPropEntity ent){
            if(context.player().level().getBlockEntity(BlockPos.of(payload.pos()).below()) instanceof PropBlockEntity ent2) {
                CompoundTag nbt2 = ((IEntityDataSaver) ent2).getPersistentData().copy();
                BlockState state = context.player().level().getBlockState(BlockPos.of(payload.pos()).below());

                float rotation = nbt2.getFloat("Rotation").get() + 270;

                double offsetX = nbt2.getDouble("xOffset").get();
                double offsetY = nbt2.getDouble("yOffset").get();
                double offsetZ = nbt2.getDouble("zOffset").get();

                float rot = (rotation) * Mth.DEG_TO_RAD;
                Vec2 angle = new Vec2(Mth.cos(rot), Mth.sin(rot)).scale(0.25f);
                Vec3 vec = new Vec3(offsetX + angle.x, offsetY, offsetZ + angle.y);

                nbt2.putDouble("xOffset", vec.x());
                nbt2.putDouble("yOffset", vec.y());
                nbt2.putDouble("zOffset", vec.z());

                ((IEntityDataSaver) ent).getPersistentData().merge(nbt2);
            }
            ((IEntityDataSaver)ent).getPersistentData().put("contains", payload.nbt());

            for(ServerPlayer p : PlayerLookup.all(context.server())){
                ServerPlayNetworking.send(p, new WorkbenchSaveS2CPayload(payload.pos(), ((IEntityDataSaver)ent).getPersistentData()));
            }
        }
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

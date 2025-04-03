package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.utility_blocks.cosmo_gift.GalaxyLayerGeoPropEntity;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.init.block_init.PropInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record WorkbenchSaveC2SPayload(long pos, NbtCompound nbt) implements CustomPayload {
    public static final Id<WorkbenchSaveC2SPayload> ID = new Id<>(EntityPayloads.C2SWorkbenchSave);

    public static final PacketCodec<RegistryByteBuf, WorkbenchSaveC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.LONG, WorkbenchSaveC2SPayload::pos,
            PacketCodecs.NBT_COMPOUND, WorkbenchSaveC2SPayload::nbt,
            WorkbenchSaveC2SPayload::new);

    public static void receive(WorkbenchSaveC2SPayload payload, ServerPlayNetworking.Context context) {
        context.player().getWorld().setBlockState(BlockPos.fromLong(payload.pos()), PropInit.COSMO_GIFT.getDefaultState());

        if(context.player().getWorld().getBlockEntity(BlockPos.fromLong(payload.pos())) instanceof GalaxyLayerGeoPropEntity ent){
            if(context.player().getWorld().getBlockEntity(BlockPos.fromLong(payload.pos()).down()) instanceof PropBlockEntity ent2) {
                NbtCompound nbt2 = ((IEntityDataSaver) ent2).getPersistentData().copy();
                BlockState state = context.player().getWorld().getBlockState(BlockPos.fromLong(payload.pos()).down());

                float rotation = nbt2.getFloat("Rotation") + 270;

                double offsetX = nbt2.getDouble("xOffset");
                double offsetY = nbt2.getDouble("yOffset");
                double offsetZ = nbt2.getDouble("zOffset");

                float rot = (rotation) * MathHelper.RADIANS_PER_DEGREE;
                Vec2f angle = new Vec2f(MathHelper.cos(rot), MathHelper.sin(rot)).multiply(0.25f);
                Vec3d vec = new Vec3d(offsetX + angle.x, offsetY, offsetZ + angle.y);

                nbt2.putDouble("xOffset", vec.getX());
                nbt2.putDouble("yOffset", vec.getY());
                nbt2.putDouble("zOffset", vec.getZ());

                ((IEntityDataSaver) ent).getPersistentData().copyFrom(nbt2);
            }
            ((IEntityDataSaver)ent).getPersistentData().put("contains", payload.nbt());

            for(ServerPlayerEntity p : PlayerLookup.all(context.server())){
                ServerPlayNetworking.send(p, new WorkbenchSaveS2CPayload(payload.pos(), ((IEntityDataSaver)ent).getPersistentData()));
            }
        }
    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

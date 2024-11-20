package net.zephyr.fnafur.blocks.battery.blocks.base;

import com.google.common.collect.FluentIterable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.battery.IElectricNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseElectricBlockEntity extends BlockEntity implements IElectricNode {

    List<BlockPos> nodes = new ArrayList<>();

    public BaseElectricBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putLongArray("nodes", nodes.stream().map(BlockPos::asLong).toList());
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        this.nodes.clear();
        this.nodes.addAll(Arrays.stream(nbt.getLongArray("nodes")).mapToObj(BlockPos::fromLong).toList());
        super.readNbt(nbt, registryLookup);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public void addNode(BlockPos newNode) {
        this.nodes.add(newNode);
    }

    @Override
    public void remNode(BlockPos node) {
        this.nodes.remove(node);
    }


    @Override
    public boolean isPowered() {
        return false;
    }
}

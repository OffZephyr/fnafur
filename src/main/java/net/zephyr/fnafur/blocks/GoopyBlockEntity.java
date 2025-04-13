package net.zephyr.fnafur.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import org.jetbrains.annotations.Nullable;

public abstract class GoopyBlockEntity extends BlockEntity {
    NbtCompound customData = new NbtCompound();
    String NbtIndex = FnafUniverseRebuilt.MOD_ID;
    public GoopyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public NbtCompound getCustomData(){
        return customData;
    }
    public void putCustomData(NbtCompound nbt){
        customData = nbt.copy();
        markDirty();
    }

    public void setNbtIndex(String index){
        NbtIndex = index;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.put(NbtIndex, customData);
    }

    public void tick(World world, BlockPos blockPos, BlockState state, GoopyBlockEntity entity){
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if(nbt.contains(NbtIndex)){
            customData = nbt.getCompound(NbtIndex).copy();
        }
        super.readNbt(nbt, registryLookup);
    }

    @Override
    public void markDirty() {
        this.getWorld().updateListeners(this.getPos(), getCachedState(), getCachedState(), 3);
        super.markDirty();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }


    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}

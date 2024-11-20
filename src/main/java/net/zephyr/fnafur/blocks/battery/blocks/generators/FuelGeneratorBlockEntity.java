package net.zephyr.fnafur.blocks.battery.blocks.generators;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.battery.blocks.base.BaseGeneratorBlockEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class FuelGeneratorBlockEntity extends BaseGeneratorBlockEntity {
    // constants
    final int FUEL_RATE = 1;
    final int TICK_RATE = 20;

    // variables
    int currentTick = 0;
    int fuel = 0;
    boolean enabled = false;

    public FuelGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void tick() {
        // Timer handler
        if(!enabled) return;
        if(currentTick > 0) { currentTick --; return; }
        currentTick = TICK_RATE;

        fuel = Math.max(0, fuel - FUEL_RATE);
        System.out.println("fuel left : " + fuel);

    }

    @Override
    public void interact() {
        enabled = !enabled;
    }

    @Override
    public boolean isPowered() {
        return fuel > 0 && enabled;
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        fuel = nbt.getInt("fuel");
        enabled = nbt.getBoolean("enabled");
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("fuel", fuel);
        nbt.putBoolean("enabled", enabled);
    }

    public void addFuel(int amount) {
        fuel = Math.max(0,fuel + amount);
        System.out.println("provided "+ amount +" units");
    }
}

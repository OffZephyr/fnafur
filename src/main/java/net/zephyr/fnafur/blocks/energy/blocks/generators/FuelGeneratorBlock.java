package net.zephyr.fnafur.blocks.energy.blocks.generators;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtInt;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.energy.entity.BaseEnergyBlockEntity;
import org.jetbrains.annotations.Nullable;

public class FuelGeneratorBlock extends  BaseGeneratorBlock {

    //Constants
    public static String KEY_FUEL = "fuel";
    public static int TICK_RATE = 20;
    public static int FUEL_CONSUMPTION = 2;
    public static int MAX_CAPACITY = 1000;

    //Variables
    int tick;

    public FuelGeneratorBlock(Settings settings) {
        super(settings);
    }

    /// add fuel amount to a block
    public void AddFuel(World world, BlockPos pos, int amount) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(!(entity instanceof BaseEnergyBlockEntity base)) return;

        int newQuantity = Math.min(base.getData().getInt(KEY_FUEL) + amount, MAX_CAPACITY);
        base.setData(KEY_FUEL, NbtInt.of(newQuantity));

        System.out.println("[FUEL_GEN]: refuel!");

    }

    @Override
    public @Nullable BlockEntityTicker<BaseEnergyBlockEntity> getTicker(World world, BlockState state, BlockEntityType type) {
        return ((world1, pos, state1, blockEntity) -> {
            if(world1.isClient) return;

            if(tick > 0){ tick--; return; }
            tick = TICK_RATE;

            //System.out.println("[FUEL_GEN]: tick! "+pos);

            BlockEntity ent = world1.getBlockEntity(pos);
            if(!(ent instanceof BaseEnergyBlockEntity base)) return;

            int newQuantity = Math.max(base.getData().getInt(KEY_FUEL) - FUEL_CONSUMPTION, 0);
            base.setData(KEY_FUEL,NbtInt.of(newQuantity));

        });
    }

    @Override
    public boolean isPowered(BlockView world, BlockPos pos) {
        BlockEntity ent = world.getBlockEntity(pos);
        if(!(ent instanceof BaseEnergyBlockEntity base)) return false;
        return base.getData().getInt(KEY_FUEL) > 0;
    }
}

// TODO: find model problem
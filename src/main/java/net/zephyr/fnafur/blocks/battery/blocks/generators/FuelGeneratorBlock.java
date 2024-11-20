package net.zephyr.fnafur.blocks.battery.blocks.generators;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.zephyr.fnafur.blocks.battery.IElectricNode;
import net.zephyr.fnafur.blocks.battery.blocks.base.BaseGeneratorBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import org.jetbrains.annotations.Nullable;

public class FuelGeneratorBlock extends Block implements BlockEntityProvider {

    BlockPos blockPos;
    BlockState blockState;
    BaseGeneratorBlockEntity entityBlock;

    int currentTick = 0;

    public FuelGeneratorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        blockPos = pos;
        blockState = state;
        entityBlock = BlockEntityInit.FUEL_GENERATOR.instantiate(pos, state);
        return entityBlock;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(!(entity instanceof BaseGeneratorBlockEntity)) return super.onUse(state, world, pos, player, hit);

        ((BaseGeneratorBlockEntity)entity).interact();
        entity.markDirty();
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> {
            BlockEntity ent = world.getBlockEntity(pos);
            if(world.isClient) return;
            if(ent instanceof BaseGeneratorBlockEntity){
                ((BaseGeneratorBlockEntity) ent).tick();
            }
        };
    }
}

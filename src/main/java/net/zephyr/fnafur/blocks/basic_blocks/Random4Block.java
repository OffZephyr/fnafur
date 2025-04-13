package net.zephyr.fnafur.blocks.basic_blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class Random4Block extends Block {
    public static final IntProperty RANDOM_STATE = IntProperty.of("random", 0, 4);
    public Random4Block(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FnafUniverseRebuilt.print("test");
        Random random = new Random();
        int num = random.nextInt(0, 4);

        return getDefaultState().with(RANDOM_STATE, num);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(RANDOM_STATE);
        super.appendProperties(builder);
    }
}

package net.zephyr.fnafur.blocks.basic_blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class Random3Block extends Block {
    public static final IntProperty RANDOM_STATE = IntProperty.of("random", 0, 3);
    public Random3Block(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Random random = new Random();
        int num = random.nextInt(0, 3);

        return getDefaultState().with(RANDOM_STATE, num);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(RANDOM_STATE);
        super.appendProperties(builder);
    }
}

package net.zephyr.fnafur.blocks.basic_blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class Random3Block extends Block {
    public static final IntegerProperty RANDOM_STATE = IntegerProperty.create("random", 0, 3);
    public Random3Block(Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Random random = new Random();
        int num = random.nextInt(0, 3);

        return defaultBlockState().setValue(RANDOM_STATE, num);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RANDOM_STATE);
        super.createBlockStateDefinition(builder);
    }
}

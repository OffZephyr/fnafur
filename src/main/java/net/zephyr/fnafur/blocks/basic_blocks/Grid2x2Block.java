package net.zephyr.fnafur.blocks.basic_blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class Grid2x2Block extends Block {

    public static final IntegerProperty TEXTURE = IntegerProperty.create("texture", 0, 3);
    public Grid2x2Block(Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean one, two, three;
        if(ctx.getClickedPos().getY() % 2 == 0) {
            one = ctx.getClickedPos().getX() % 2 == 0 && ctx.getClickedPos().getZ() % 2 == 0;
            two = ctx.getClickedPos().getX() % 2 != 0 && ctx.getClickedPos().getZ() % 2 == 0;
            three = ctx.getClickedPos().getX() % 2 != 0 && ctx.getClickedPos().getZ() % 2 != 0;
        }
        else {
            three = ctx.getClickedPos().getX() % 2 != 0 && ctx.getClickedPos().getZ() % 2 == 0;
            two = ctx.getClickedPos().getX() % 2 != 0 && ctx.getClickedPos().getZ() % 2 != 0;
            one = ctx.getClickedPos().getX() % 2 == 0 && ctx.getClickedPos().getZ() % 2 != 0;
        }
        int num = one ? 0 : two ? 1 : three ? 2 : 3;
        return defaultBlockState().setValue(TEXTURE, num);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(TEXTURE));
    }
}

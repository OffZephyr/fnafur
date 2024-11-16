package net.zephyr.fnafur.blocks.basic_blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import org.jetbrains.annotations.Nullable;

public class Grid2x2Block extends Block {

    public static final IntProperty TEXTURE = IntProperty.of("texture", 0, 3);
    public Grid2x2Block(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        boolean one, two, three;
        if(ctx.getBlockPos().getY() % 2 == 0) {
            one = ctx.getBlockPos().getX() % 2 == 0 && ctx.getBlockPos().getZ() % 2 == 0;
            two = ctx.getBlockPos().getX() % 2 != 0 && ctx.getBlockPos().getZ() % 2 == 0;
            three = ctx.getBlockPos().getX() % 2 != 0 && ctx.getBlockPos().getZ() % 2 != 0;
        }
        else {
            three = ctx.getBlockPos().getX() % 2 != 0 && ctx.getBlockPos().getZ() % 2 == 0;
            two = ctx.getBlockPos().getX() % 2 != 0 && ctx.getBlockPos().getZ() % 2 != 0;
            one = ctx.getBlockPos().getX() % 2 == 0 && ctx.getBlockPos().getZ() % 2 != 0;
        }
        int num = one ? 0 : two ? 1 : three ? 2 : 3;
        return getDefaultState().with(TEXTURE, num);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(TEXTURE));
    }
}

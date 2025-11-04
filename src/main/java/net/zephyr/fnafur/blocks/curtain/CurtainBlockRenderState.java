package net.zephyr.fnafur.blocks.curtain;

import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CurtainBlockRenderState extends BlockEntityRenderState {
    CurtainData front;
    CurtainData back;
    CurtainData prev_front;
    CurtainData prev_back;
    boolean isLast = false;

    public int height = 0;
    BlockPos previous = BlockPos.ORIGIN;
    BlockPos next = BlockPos.ORIGIN;
    Direction facing = Direction.SOUTH;
    Direction nextFacing = Direction.SOUTH;
    boolean isOpening = false;
    int totalLength = 0;
    float openIndex = 0;
}

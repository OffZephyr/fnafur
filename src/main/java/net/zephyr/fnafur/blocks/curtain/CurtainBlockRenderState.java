package net.zephyr.fnafur.blocks.curtain;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class CurtainBlockRenderState extends BlockEntityRenderState {
    CurtainData front;
    CurtainData back;
    CurtainData prev_front;
    CurtainData prev_back;
    boolean isLast = false;

    public int height = 0;
    BlockPos previous = BlockPos.ZERO;
    BlockPos next = BlockPos.ZERO;
    Direction facing = Direction.SOUTH;
    Direction nextFacing = Direction.SOUTH;
    boolean isOpening = false;
    int totalLength = 0;
    float openIndex = 0;
}

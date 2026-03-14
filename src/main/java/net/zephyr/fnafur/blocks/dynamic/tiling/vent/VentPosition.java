package net.zephyr.fnafur.blocks.dynamic.tiling.vent;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public enum VentPosition implements StringIdentifiable {
    C("c"),
    BL("bl"),
    BM("bm"),
    BR("br"),
    CL("cl"),
    CR("cr"),
    UL("ul"),
    UM("um"),
    UR("ur"),
    ;

    final String NAME;
    VentPosition(String name){
        this.NAME = name;
    }

    @Override
    public String asString() {
        return NAME;
    }

    public static BlockPos getCenter(VentPosition pos, BlockPos blockPos, Direction facing){
        return switch (pos){
            case BL -> blockPos.up().offset(facing.rotateYCounterclockwise());
            case BM -> blockPos.up();
            case BR -> blockPos.up().offset(facing.rotateYClockwise());
            case UL -> blockPos.down().offset(facing.rotateYCounterclockwise());
            case UM -> blockPos.down();
            case UR -> blockPos.down().offset(facing.rotateYClockwise());
            case CL -> blockPos.offset(facing.rotateYCounterclockwise());
            case CR -> blockPos.offset(facing.rotateYClockwise());
            default -> blockPos;
        };
    }
}

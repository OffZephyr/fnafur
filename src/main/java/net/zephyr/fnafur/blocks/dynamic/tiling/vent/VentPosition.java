package net.zephyr.fnafur.blocks.dynamic.tiling.vent;

import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public enum VentPosition implements StringRepresentable {
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
    public String getSerializedName() {
        return NAME;
    }

    public static BlockPos getCenter(VentPosition pos, BlockPos blockPos, Direction facing){
        return switch (pos){
            case BL -> blockPos.above().relative(facing.getCounterClockWise());
            case BM -> blockPos.above();
            case BR -> blockPos.above().relative(facing.getClockWise());
            case UL -> blockPos.below().relative(facing.getCounterClockWise());
            case UM -> blockPos.below();
            case UR -> blockPos.below().relative(facing.getClockWise());
            case CL -> blockPos.relative(facing.getCounterClockWise());
            case CR -> blockPos.relative(facing.getClockWise());
            default -> blockPos;
        };
    }
}

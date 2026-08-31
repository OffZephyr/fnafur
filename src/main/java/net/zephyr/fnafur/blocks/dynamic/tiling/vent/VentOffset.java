package net.zephyr.fnafur.blocks.dynamic.tiling.vent;

import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public enum VentOffset implements StringRepresentable {
    SINGLE("single"),
    EVEN("even"),
    ODD("odd"),
    END_EVEN("end_even"),
    END_ODD("end_odd")
    ;

    final String NAME;
    VentOffset(String name){
        this.NAME = name;
    }

    //TODO this is 🔥
    //TO-DO this aint it chief
    @Override
    public String getSerializedName() {
        return NAME;
    }
}

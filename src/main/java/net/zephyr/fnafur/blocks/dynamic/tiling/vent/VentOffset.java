package net.zephyr.fnafur.blocks.dynamic.tiling.vent;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public enum VentOffset implements StringIdentifiable {
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
    public String asString() {
        return NAME;
    }
}

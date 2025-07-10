package net.zephyr.fnafur.blocks.linking;

import com.mojang.serialization.Codec;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.ArrayList;
import java.util.List;

public interface LinkTarget {
    List<IEntityDataSaver> getSources();
    List<IEntityDataSaver> allTargets = new ArrayList<>();
    default<T extends IEntityDataSaver> void addSource(T source) {
        getSources().add(source);
    }

    default<T extends IEntityDataSaver> void removeSource(T source) {
        getSources().remove(source);
    }
    default<T extends IEntityDataSaver> boolean isTargetOf(T source) {
        return getSources().contains(source);
    }


    default void writeData(WriteView view, World world){

    }
    default void readData(ReadView view, World world){
        if(!allTargets.contains((IEntityDataSaver)this)){
            allTargets.add((IEntityDataSaver)this);
        }
    }
}

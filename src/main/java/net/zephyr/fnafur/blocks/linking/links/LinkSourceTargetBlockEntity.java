package net.zephyr.fnafur.blocks.linking.links;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.ArrayList;
import java.util.List;

public abstract class LinkSourceTargetBlockEntity extends LinkSourceBlockEntity implements LinkTarget {
    int updateDepth = 0;
    public List<IEntityDataSaver> sources = new ArrayList<>();
    boolean wasUpdated = false;

    public LinkSourceTargetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        LinkTarget.allTargets.add((IEntityDataSaver) this);
    }
    @Override
    public void markRemoved() {
        super.markRemoved();
        LinkSource.allSources.remove((IEntityDataSaver) this);
        cleanSources();
    }

    @Override
    public List<IEntityDataSaver> getSources() {
        return sources;
    }


    @Override
    public int getUpdateDepth() {
        return updateDepth;
    }

    @Override
    public void setUpdateDepth(int updateDepth) {
        this.updateDepth = updateDepth;
    }
}

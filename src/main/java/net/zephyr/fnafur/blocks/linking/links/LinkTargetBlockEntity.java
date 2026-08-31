package net.zephyr.fnafur.blocks.linking.links;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.ArrayList;
import java.util.List;

public abstract class LinkTargetBlockEntity<T extends BlockEntityType<?>> extends BlockEntity implements LinkTarget {

    int updateDepth = 0;
    public List<IEntityDataSaver> sources = new ArrayList<>();

    public LinkTargetBlockEntity(T type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        LinkTarget.allTargets.add((IEntityDataSaver) this);
    }
    public void tick(Level world, BlockPos blockPos, BlockState state, LinkSourceTargetBlockEntity entity) {

    }
    @Override
    public void setRemoved() {
        super.setRemoved();
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

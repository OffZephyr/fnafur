package net.zephyr.fnafur.blocks.linking.links.energy;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.linking.EnergySource;
import net.zephyr.fnafur.blocks.linking.EnergyTarget;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.blocks.linking.links.LinkSourceTargetBlockEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.ArrayList;
import java.util.List;

public abstract class EnergyTargetBlockEntity<T extends BlockEntityType<?>> extends BlockEntity implements EnergyTarget {

    int updateDepth = 0;
    public List<IEntityDataSaver> sources = new ArrayList<>();

    public EnergyTargetBlockEntity(T type, BlockPos pos, BlockState state, boolean canAdd) {
        super(type, pos, state);
        LinkTarget.allTargets.add((IEntityDataSaver) this);
    }
    public EnergyTargetBlockEntity(T type, BlockPos pos, BlockState state) {
        this(type, pos, state, true);
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
    public boolean isReceivingPower() {
        for(IEntityDataSaver ent : getSources()){
            if(ent instanceof EnergySource s){
                if(s.isSendingPower((IEntityDataSaver) this)) return true;
            }
        }
        return false;
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

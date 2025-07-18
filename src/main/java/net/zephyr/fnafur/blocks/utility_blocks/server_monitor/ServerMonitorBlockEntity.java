package net.zephyr.fnafur.blocks.utility_blocks.server_monitor;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader.ChipReaderBlockEntity;
import net.zephyr.fnafur.blocks.linking.links.LinkSourceBlockEntity;
import net.zephyr.fnafur.blocks.linking.links.LinkSourceTargetBlockEntity;
import net.zephyr.fnafur.entity.animatronic.block.AnimatronicBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ServerMonitorBlockEntity extends LinkSourceTargetBlockEntity {
    public ServerMonitorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.SERVER_MONITOR, pos, state);
    }

    @Override
    public void tick(World world, BlockPos blockPos, BlockState state, LinkSourceBlockEntity entity) {
        super.tick(world, blockPos, state, entity);
    }

    public List<BlockPos> getChipReaderPoses(){
        List<BlockPos> poses = new ArrayList<>();
        for(IEntityDataSaver ent : sources){
            if(ent instanceof ChipReaderBlockEntity ent2){
                poses.add(ent2.getPos());
            }
        }
        return poses;
    }
    @Nullable
    public BlockPos getActiveChipReaderPos(){
        List<BlockPos> poses = getChipReaderPoses();
        if(poses.isEmpty()) return null;

        int index = ((IEntityDataSaver)this).getPersistentData().getInt("chipReaderIndex", 0);
        if(index >= poses.size()){
            index = Math.max(poses.size() - 1, 0);
            ((IEntityDataSaver)this).getPersistentData().putInt("chipReaderIndex", index);
        }
        return getChipReaderPoses().get(index);
    }

    @Override
    public void markRemoved() {

        super.markRemoved();
        LinkSource.allSources.remove((IEntityDataSaver) this);

        cleanSources();
    }
    @Override
    public boolean canLink(IEntityDataSaver link) {
        return link instanceof AnimatronicBlockEntity;
    }
}

package net.zephyr.fnafur.blocks.utility_blocks.animatronics.server_monitor;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader.ChipReaderBlockEntity;
import net.zephyr.fnafur.blocks.linking.links.LinkSourceBlockEntity;
import net.zephyr.fnafur.blocks.linking.links.LinkSourceTargetBlockEntity;
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
    public void tick(Level world, BlockPos blockPos, BlockState state, LinkSourceBlockEntity entity) {
        super.tick(world, blockPos, state, entity);
    }

    public List<BlockPos> getChipReaderPoses(){
        List<BlockPos> poses = new ArrayList<>();
        for(IEntityDataSaver ent : sources){
            if(ent instanceof ChipReaderBlockEntity ent2){
                poses.add(ent2.getBlockPos());
            }
        }
        return poses;
    }
    @Nullable
    public BlockPos getActiveChipReaderPos(){
        List<BlockPos> poses = getChipReaderPoses();
        if(poses.isEmpty()) return null;

        int index = ((IEntityDataSaver)this).getPersistentData().getIntOr("usedConnectionIndex", 0);
        for(BlockPos pos : poses){
            if(getLevel().getBlockEntity(pos) instanceof ChipReaderBlockEntity ent){
                int itsIndex = ((IEntityDataSaver)ent).getPersistentData().getIntOr("connectionIndex", -1);
                if(itsIndex == index) return pos;
            }
        }
        return BlockPos.ZERO;
    }

    @Override
    public void setRemoved() {

        super.setRemoved();
        LinkSource.allSources.remove((IEntityDataSaver) this);

        cleanSources();
    }
    @Override
    public boolean canLink(IEntityDataSaver link) {
        return false;
    }

    @Override
    public <T extends IEntityDataSaver> void removeSource(T source) {
        if(source instanceof ChipReaderBlockEntity chip){
            CompoundTag nbt = ((IEntityDataSaver)chip).getPersistentData();
            int index = nbt.getIntOr("connectionIndex", 0);

            for(BlockPos pos : this.getChipReaderPoses()){
                BlockEntity entity = level.getBlockEntity(pos);
                if(entity instanceof ChipReaderBlockEntity ent){
                    CompoundTag nbt2 = ((IEntityDataSaver)ent).getPersistentData();
                    int itsIndex = nbt2.getIntOr("connectionIndex", 0);
                    if(itsIndex > index) {
                        nbt2.putInt("connectionIndex", itsIndex - 1);
                    }
                }
            }

            nbt.remove("connectionIndex");
        }
        super.removeSource(source);
    }

    @Override
    public <T extends IEntityDataSaver> void addSource(T source) {
        if(source instanceof ChipReaderBlockEntity chip) {
            CompoundTag nbt = ((IEntityDataSaver) chip).getPersistentData();
            if (!nbt.contains("connectionIndex")) {
                nbt.putInt("connectionIndex", this.getChipReaderPoses().size());
            }
        }
        super.addSource(source);
    }
}

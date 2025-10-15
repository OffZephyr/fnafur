package net.zephyr.fnafur.blocks.utility_blocks.animatronics.server_monitor;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
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

        int index = ((IEntityDataSaver)this).getPersistentData().getInt("usedConnectionIndex", 0);
        for(BlockPos pos : poses){
            if(getWorld().getBlockEntity(pos) instanceof ChipReaderBlockEntity ent){
                int itsIndex = ((IEntityDataSaver)ent).getPersistentData().getInt("connectionIndex", -1);
                if(itsIndex == index) return pos;
            }
        }
        return BlockPos.ORIGIN;
    }

    @Override
    public void markRemoved() {

        super.markRemoved();
        LinkSource.allSources.remove((IEntityDataSaver) this);

        cleanSources();
    }
    @Override
    public boolean canLink(IEntityDataSaver link) {
        return (link instanceof AnimatronicBlockEntity && getTargets().isEmpty());
    }

    @Override
    public <T extends IEntityDataSaver> void removeSource(T source) {
        if(source instanceof ChipReaderBlockEntity chip){
            NbtCompound nbt = ((IEntityDataSaver)chip).getPersistentData();
            int index = nbt.getInt("connectionIndex", 0);

            for(BlockPos pos : this.getChipReaderPoses()){
                BlockEntity entity = world.getBlockEntity(pos);
                if(entity instanceof ChipReaderBlockEntity ent){
                    NbtCompound nbt2 = ((IEntityDataSaver)ent).getPersistentData();
                    int itsIndex = nbt2.getInt("connectionIndex", 0);
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
            NbtCompound nbt = ((IEntityDataSaver) chip).getPersistentData();
            if (!nbt.contains("connectionIndex")) {
                nbt.putInt("connectionIndex", this.getChipReaderPoses().size());
            }
        }
        super.addSource(source);
    }
}

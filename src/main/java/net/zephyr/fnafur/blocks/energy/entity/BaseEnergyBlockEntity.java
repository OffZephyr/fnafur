package net.zephyr.fnafur.blocks.energy.entity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseEnergyBlockEntity extends PropBlockEntity {

    //KEYS
    public static String KEY_NODES = "nodes";

    public BaseEnergyBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.ENERGY, pos, state);
    }

    /// Use to save data into the energy block ; use the appropriate Nbt*T*.of() for the param 'element'
    public void setData(String key, Tag element){
        ((IEntityDataSaver)this).getPersistentData().put(key, element);
        this.setChanged();
    }

    /// Get the nodes connected to this entity block
    public BlockPos[] getNodes(){
        if(getData().getLongArray(KEY_NODES) == null) return new BlockPos[]{};
        long[] longs = getData().getLongArray(KEY_NODES).get();
        List<BlockPos> poses = new ArrayList<>();
        for(long l: longs){
            poses.add(BlockPos.of(l));
        }
        return poses.toArray(BlockPos[]::new);
    }

    /// Get the data of the blockEntity
    public CompoundTag getData(){ return ((IEntityDataSaver)this).getPersistentData(); }

}

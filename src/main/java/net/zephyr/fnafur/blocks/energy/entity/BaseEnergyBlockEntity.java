package net.zephyr.fnafur.blocks.energy.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
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
    public void setData(String key, NbtElement element){
        ((IEntityDataSaver)this).getPersistentData().put(key, element);
        this.markDirty();
    }

    /// Get the nodes connected to this entity block
    public BlockPos[] getNodes(){
        if(getData().getLongArray(KEY_NODES) == null) return new BlockPos[]{};
        long[] longs = getData().getLongArray(KEY_NODES);
        List<BlockPos> poses = new ArrayList<>();
        for(long l: longs){
            poses.add(BlockPos.fromLong(l));
        }
        return poses.toArray(BlockPos[]::new);
    }

    /// Get the data of the blockEntity
    public NbtCompound getData(){ return ((IEntityDataSaver)this).getPersistentData(); }

}

package net.zephyr.fnafur.blocks.energy.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class BaseEnergyBlockEntity extends PropBlockEntity {

    public BaseEnergyBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.ENERGY, pos, state);
    }

    /// Use to save data into the energy block ; use the appropriate Nbt*T*.of() for the param 'element'
    public void setData(String key, NbtElement element){
        ((IEntityDataSaver)this).getPersistentData().put(key, element);
        this.markDirty();
    }

    /// Get the data of the blockEntity
    public NbtCompound getData(){ return ((IEntityDataSaver)this).getPersistentData(); };

}

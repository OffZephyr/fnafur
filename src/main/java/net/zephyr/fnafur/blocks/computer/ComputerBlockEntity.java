package net.zephyr.fnafur.blocks.computer;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class ComputerBlockEntity extends BlockEntity {
    String currentApp = "";
    public ComputerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.COMPUTER, pos, state);
    }

    public void tick(World world, BlockPos blockPos, BlockState state, ComputerBlockEntity entity) {
        NbtCompound data = ((IEntityDataSaver)entity).getPersistentData();
        /*for(ComputerApp app : ComputerData.getApps()){
            if(!Objects.equals(currentApp, data.getString("Window"))) {
                if(Objects.equals(app.getName(), data.getString("Window"))){
                    app.init();
                    data.putString("Window", app.getName());
                    currentApp = app.getName();
                }
            }
            if(Objects.equals(data.getString("Window"), app.getName())){
                app.tickWhenOpen();
            }

            app.tickAlways();
        }*/
    }
}

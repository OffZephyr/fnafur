package net.zephyr.fnafur.blocks.camera;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class CameraBlockEntity extends BlockEntity {
    final float cameraBaseSpeed = 0.416f;
    public CameraBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.CAMERA, pos, state);
    }

    public void tick(World world, BlockPos blockPos, BlockState state, CameraBlockEntity entity) {
        NbtCompound data = ((IEntityDataSaver)this).getPersistentData();
        if(data.getByte("ModeX").get() == 1){
            byte speedX = data.getByte("yawSpeed").get();

            float offset = (cameraBaseSpeed * (speedX+1));
            offset = data.getBoolean("panningXReverse").get() ? -offset : offset;
            if(state.get(CameraBlock.POWERED)) offset = 0;

            if(data.getFloat("panningXProgress").get() >= 150) data.putBoolean("panningXReverse", true);
            else if(data.getFloat("panningXProgress").get() <= -50) data.putBoolean("panningXReverse", false);

            data.putFloat("panningXProgress", data.getFloat("panningXProgress").get() + offset);


            float panningX = data.getFloat("panningXProgress").get() > 100 ? 100 : data.getFloat("panningXProgress").get() < 0 ? 0 : data.getFloat("panningXProgress").get();
            double minX = data.getFloat("minYaw").get();
            double maxX = data.getFloat("maxYaw").get();
            double newYaw = panningX/100 * (maxX - minX);
            double endYaw = minX + newYaw;

            data.putDouble("yaw", -endYaw);
        }
        if(data.getByte("ModeY").get() == 1){
            byte speedX = data.getByte("pitchSpeed").get();

            float offset = (cameraBaseSpeed * (speedX+1));
            offset = data.getBoolean("panningYReverse").get() ? -offset : offset;
            if(state.get(CameraBlock.POWERED)) offset = 0;

            if(data.getFloat("panningYProgress").get() >= 150) data.putBoolean("panningYReverse", true);
            else if(data.getFloat("panningYProgress").get() <= -50) data.putBoolean("panningYReverse", false);

            data.putFloat("panningYProgress", data.getFloat("panningYProgress").get() + offset);


            float panningY = data.getFloat("panningYProgress").get() > 100 ? 100 : data.getFloat("panningYProgress").get() < 0 ? 0 : data.getFloat("panningYProgress").get();
            double minY = data.getFloat("minPitch").get();
            double maxY = data.getFloat("maxPitch").get();
            double newPitch = panningY/100 * (maxY - minY);
            double endPitch = minY + newPitch;

            data.putDouble("pitch", -endPitch);
        }

        if(state.get(CameraBlock.LIT) != ((IEntityDataSaver)this).getPersistentData().getBoolean("Lit").get()) {
            world.setBlockState(blockPos, state.with(CameraBlock.LIT, ((IEntityDataSaver)this).getPersistentData().getBoolean("Lit").get()), Block.NOTIFY_ALL);
        }
        if(state.get(CameraBlock.POWERED) != ((IEntityDataSaver)this).getPersistentData().getBoolean("Powered").get()) {
            world.setBlockState(blockPos, state.with(CameraBlock.POWERED, ((IEntityDataSaver)this).getPersistentData().getBoolean("Powered").get()), Block.NOTIFY_ALL);
        }
    }
}

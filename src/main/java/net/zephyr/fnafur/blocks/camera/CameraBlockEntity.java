package net.zephyr.fnafur.blocks.camera;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.GoopyBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class CameraBlockEntity extends GoopyBlockEntity {
    final float cameraBaseSpeed = 0.416f;
    public CameraBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.CAMERA, pos, state);
    }

    @Override
    public void tick(World world, BlockPos blockPos, BlockState state, GoopyBlockEntity entity) {
        NbtCompound data = ((IEntityDataSaver)this).getPersistentData();
        if(data.getByte("ModeX") == 1){
            byte speedX = data.getByte("yawSpeed");

            float offset = (cameraBaseSpeed * (speedX+1));
            offset = data.getBoolean("panningXReverse") ? -offset : offset;
            if(state.get(CameraBlock.POWERED)) offset = 0;

            if(data.getFloat("panningXProgress") >= 150) data.putBoolean("panningXReverse", true);
            else if(data.getFloat("panningXProgress") <= -50) data.putBoolean("panningXReverse", false);

            data.putFloat("panningXProgress", data.getFloat("panningXProgress") + offset);


            float panningX = data.getFloat("panningXProgress") > 100 ? 100 : data.getFloat("panningXProgress") < 0 ? 0 : data.getFloat("panningXProgress");
            double minX = data.getFloat("minYaw");
            double maxX = data.getFloat("maxYaw");
            double newYaw = panningX/100 * (maxX - minX);
            double endYaw = minX + newYaw;

            data.putDouble("yaw", -endYaw);
        }
        if(data.getByte("ModeY") == 1){
            byte speedX = data.getByte("pitchSpeed");

            float offset = (cameraBaseSpeed * (speedX+1));
            offset = data.getBoolean("panningYReverse") ? -offset : offset;
            if(state.get(CameraBlock.POWERED)) offset = 0;

            if(data.getFloat("panningYProgress") >= 150) data.putBoolean("panningYReverse", true);
            else if(data.getFloat("panningYProgress") <= -50) data.putBoolean("panningYReverse", false);

            data.putFloat("panningYProgress", data.getFloat("panningYProgress") + offset);


            float panningY = data.getFloat("panningYProgress") > 100 ? 100 : data.getFloat("panningYProgress") < 0 ? 0 : data.getFloat("panningYProgress");
            double minY = data.getFloat("minPitch");
            double maxY = data.getFloat("maxPitch");
            double newPitch = panningY/100 * (maxY - minY);
            double endPitch = minY + newPitch;

            data.putDouble("pitch", -endPitch);
        }

        if(state.get(CameraBlock.LIT) != ((IEntityDataSaver)this).getPersistentData().getBoolean("Lit")) {
            world.setBlockState(blockPos, state.with(CameraBlock.LIT, ((IEntityDataSaver)this).getPersistentData().getBoolean("Lit")), Block.NOTIFY_ALL);
        }
        if(state.get(CameraBlock.POWERED) != ((IEntityDataSaver)this).getPersistentData().getBoolean("Powered")) {
            world.setBlockState(blockPos, state.with(CameraBlock.POWERED, ((IEntityDataSaver)this).getPersistentData().getBoolean("Powered")), Block.NOTIFY_ALL);
        }
        super.tick(world, blockPos, state, entity);
    }
}

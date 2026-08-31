package net.zephyr.fnafur.blocks.linking.links;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SGetFromServerPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.ArrayList;
import java.util.List;

public abstract class LinkSourceBlockEntity extends BlockEntity implements LinkSource {

    public List<IEntityDataSaver> targets = new ArrayList<>();
    public List<BlockPos> posQueue = new ArrayList<>();
    public List<Integer> idQueue = new ArrayList<>();
    int sourceAmountSync = 0;

    public LinkSourceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        LinkSource.allSources.add((IEntityDataSaver) this);
    }

    public void tick(Level world, BlockPos blockPos, BlockState state, LinkSourceBlockEntity entity) {
        if(getSourceAmountSync() != getTargets().size()){
            updateSources(world, blockPos);
        }

        if (world.isClientSide()) {
            if (!((IEntityDataSaver) entity).getPersistentData().contains("synced")) {
                ClientPlayNetworking.send(new UpdateBlockNbtC2SGetFromServerPayload(getBlockPos().asLong()));
                world.setBlock(blockPos, world.getBlockState(blockPos), Block.UPDATE_ALL_IMMEDIATE);
            }

            if (((IEntityDataSaver) this).getServerUpdateStatus()) {
                //Minecraft.getInstance().player.sendMessage(Text.literal("SYNCING PROP"), false);
                ClientPlayNetworking.send(new UpdateBlockNbtC2SPayload(getBlockPos().asLong(), ((IEntityDataSaver) this).getPersistentData()));
            }
        }
    }
    @Override
    public void setRemoved() {
        super.setRemoved();
        LinkSource.allSources.remove((IEntityDataSaver) this);
    }

    @Override
    public List<IEntityDataSaver> getTargets() {
        return targets;
    }

    @Override
    public List<BlockPos> blockTargetQueue() {
        return posQueue;
    }

    @Override
    public List<Integer> entityTargetQueue() {
        return idQueue;
    }

    @Override
    public int getSourceAmountSync() {
        return sourceAmountSync;
    }
    @Override
    public void setSourceAmountSync(int amount) {
        sourceAmountSync = amount;
    }
}

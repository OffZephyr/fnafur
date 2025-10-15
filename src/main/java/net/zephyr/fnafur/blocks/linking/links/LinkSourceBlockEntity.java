package net.zephyr.fnafur.blocks.linking.links;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

    public void tick(World world, BlockPos blockPos, BlockState state, LinkSourceBlockEntity entity) {
        if(getSourceAmountSync() != getTargets().size()){
            updateSources(world, blockPos);
        }

        if (world.isClient()) {
            if (!((IEntityDataSaver) entity).getPersistentData().contains("synced")) {
                ClientPlayNetworking.send(new UpdateBlockNbtC2SGetFromServerPayload(getPos().asLong()));
                world.setBlockState(blockPos, world.getBlockState(blockPos), Block.NOTIFY_ALL_AND_REDRAW);
            }

            if (((IEntityDataSaver) this).getServerUpdateStatus()) {
                //MinecraftClient.getInstance().player.sendMessage(Text.literal("SYNCING PROP"), false);
                ClientPlayNetworking.send(new UpdateBlockNbtC2SPayload(getPos().asLong(), ((IEntityDataSaver) this).getPersistentData()));
            }
        }
    }
    @Override
    public void markRemoved() {
        super.markRemoved();
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

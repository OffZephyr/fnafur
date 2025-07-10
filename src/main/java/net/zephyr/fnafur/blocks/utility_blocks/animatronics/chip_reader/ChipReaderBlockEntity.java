package net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.blocks.utility_blocks.server_monitor.ServerMonitorBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.item.tools.WrenchItem;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;

import java.util.ArrayList;
import java.util.List;

public class ChipReaderBlockEntity extends BlockEntity implements LinkSource {

    List<IEntityDataSaver> targets = new ArrayList<>();
    List<BlockPos> posQueue = new ArrayList<>();
    List<Integer> idQueue = new ArrayList<>();
    public ChipReaderBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.CHIP_READER, pos, state);
        LinkSource.allSources.add((IEntityDataSaver) this);
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
    public void markRemoved() {
        super.markRemoved();

        LinkSource.allSources.remove((IEntityDataSaver) this);
    }

    public ActionResult use(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit){

        System.out.println(getTargets().size());
        ItemStack stack = player.getMainHandStack();
        if(stack.getItem() instanceof WrenchItem && ((IUniversePlayer)player).isUsingVanniMask()){
            NbtCompound nbt = ItemNbtUtil.getNbt(stack);

            BlockPos startPos = nbt.get("startLink", BlockPos.CODEC).orElse(BlockPos.ORIGIN);
            if(player.isSneaking()){
                nbt.remove("startLink");
                System.out.println("CLEAR");
                for(IEntityDataSaver ent : getTargets()){
                    unlink(ent);
                }
            }
            else if(startPos.equals(pos) || startPos != BlockPos.ORIGIN){
                System.out.println("CLEAR");
                nbt.remove("startLink");
            }
            else{
                System.out.println("START LINK");
                nbt.put("startLink", BlockPos.CODEC, pos);
            }
            ItemNbtUtil.setNbt(stack, nbt);
            return ActionResult.SUCCESS;
        }

        return null;
    }

    @Override
    public boolean canLink(IEntityDataSaver link) {
        return link instanceof ServerMonitorBlockEntity;
    }
}

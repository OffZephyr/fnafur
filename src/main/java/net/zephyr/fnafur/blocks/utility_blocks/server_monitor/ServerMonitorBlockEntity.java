package net.zephyr.fnafur.blocks.utility_blocks.server_monitor;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.item.tools.WrenchItem;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEditCamera;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.ArrayList;
import java.util.List;

public class ServerMonitorBlockEntity extends BlockEntity implements LinkTarget {
    List<IEntityDataSaver> sources = new ArrayList<>();
    public ServerMonitorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.SERVER_MONITOR, pos, state);
        LinkTarget.allTargets.add((IEntityDataSaver) this);
    }

    public void tick(World world, BlockPos blockPos, BlockState state, ServerMonitorBlockEntity entity) {

    }

    @Override
    public void markRemoved() {

        super.markRemoved();

        if(!getSources().isEmpty()){
            for(int i = 0; i < getSources().size(); i++){
                if(getSources().get(i) instanceof IEntityDataSaver ent && ent instanceof LinkSource s){
                    s.getTargets().remove(((IEntityDataSaver) this));
                    getSources().remove(ent);
                }
            }
        }
        LinkTarget.allTargets.remove((IEntityDataSaver) this);
    }

    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        return null;
    }
    public ActionResult use(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit){

        ItemStack stack = player.getMainHandStack();
        if(player.getMainHandStack().getItem() instanceof WrenchItem){
            NbtCompound nbt = ItemNbtUtil.getNbt(stack);

            BlockPos startPos = nbt.get("startLink", BlockPos.CODEC).orElse(BlockPos.ORIGIN);

            if(!nbt.contains("startLink")){
                return null;
            }
            else if(!startPos.equals(pos)){
                if(world.getBlockEntity(startPos) instanceof BlockEntity source){
                    if(((LinkSource)source).getTargets().contains((IEntityDataSaver)this)){
                        ((LinkSource)source).unlink((IEntityDataSaver) this);
                        System.out.println("REMOVE LINK");
                    }
                    else{
                        ((LinkSource)source).makeLink((IEntityDataSaver) this);
                        System.out.println("ADD LINK");
                    }
                    markDirty();
                    source.markDirty();
                }
            }

            System.out.println("CLEAR");
            nbt.remove("startLink");
            ItemNbtUtil.setNbt(stack, nbt);
            return ActionResult.SUCCESS;
        }

        return null;
    }

    @Override
    public List<IEntityDataSaver> getSources() {
        return sources;
    }
}

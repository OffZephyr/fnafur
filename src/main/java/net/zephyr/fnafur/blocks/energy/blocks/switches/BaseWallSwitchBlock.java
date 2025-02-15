package net.zephyr.fnafur.blocks.energy.blocks.switches;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.CallableByMesurer;
import net.zephyr.fnafur.blocks.energy.entity.BaseEnergyBlockEntity;
import net.zephyr.fnafur.blocks.energy.enums.EnergyNodeType;
import net.zephyr.fnafur.blocks.energy.enums.EnergyNode;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseWallSwitchBlock extends WallPropBlock implements BlockEntityProvider, EnergyNode, CallableByMesurer {

    int tick;
    final int TICK_RATE = 20;

    public BaseWallSwitchBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public Class<EnergyNodeType> COLOR_ENUM() {
        return null;
    }


    @Override
    public ActionResult addNode(World world, BlockPos pos, BlockPos toAdd, Vec3d hit) {
        //System.out.println("[SWITCH]: try to connect node...");
        if(pos == toAdd) return ActionResult.FAIL;
        if(!(world.getBlockEntity(pos) instanceof BaseEnergyBlockEntity base)) return ActionResult.FAIL;
        List<Long> p = new ArrayList<>(
                Arrays.stream(base.getData().getLongArray(BaseEnergyBlockEntity.KEY_NODES)).boxed().toList()
        );

        if(p.contains(toAdd.asLong()))return ActionResult.SUCCESS;
        p.add(toAdd.asLong());

        base.setData(BaseEnergyBlockEntity.KEY_NODES, new NbtLongArray(p) );
        //System.out.println("[SWITCH]: data nodes : "+ base.getNodes().toString());
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult remNode(World world, BlockPos pos, BlockPos toRem, Vec3d hit) {
        if(!(world.getBlockEntity(pos) instanceof BaseEnergyBlockEntity base)) return ActionResult.FAIL;
        List<Long> p = new ArrayList<>(
                Arrays.stream(base.getData().getLongArray(BaseEnergyBlockEntity.KEY_NODES)).boxed().toList()
        );

        p.remove(toRem.asLong());
        base.setData(BaseEnergyBlockEntity.KEY_NODES, new NbtLongArray(p) );
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean isPowered(BlockView world, BlockPos pos) {
        if(!(world.getBlockEntity(pos) instanceof BaseEnergyBlockEntity base)) return false;
        //System.out.println("[SWITCH] nodes l: "+base.getNodes().length);
        for(BlockPos p  : base.getNodes()){
            if(pos == p) continue;
            if(!(world.getBlockState(p).getBlock() instanceof EnergyNode node)) continue;
            if(!node.typeOf(EnergyNodeType.OUTPUT) && node.isPowered(world,p)) return true;
        }
        return false;
    }

    @Override
    public EnergyNodeType nodeType() {
        return EnergyNodeType.SWITCH;
    }


    @Override
    public ActionResult ExecuteAction(ItemUsageContext context) {
        NbtCompound data = context.getStack().getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();

        if(data.getBoolean("needConnection")){
            if(context.getPlayer().isSneaking()){
                remNode(
                        context.getWorld(),
                        context.getBlockPos(),
                        BlockPos.fromLong(data.getLong("posConnection")),
                        context.getHitPos()
                );
                //System.out.println("[SWITCH] node removed! ");
            } else {
                addNode(
                        context.getWorld(),
                        context.getBlockPos(),
                        BlockPos.fromLong(data.getLong("posConnection")),
                        context.getHitPos()
                );
                //System.out.println("[SWITCH] node added! ");
            }

            data.putBoolean("needConnection", false);
            data.putLong("posConnection", 0L);

        }
        else {
            data.putBoolean("needConnection", true);
            data.putLong("posConnection", context.getBlockPos().asLong());
        }

        //System.out.println("[SWITCH] tool data: "+data.toString());

        context.getStack().apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.copyFrom(data);
        }));

        return ActionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntityTicker getTicker(World world, BlockState state, BlockEntityType type) {
        return ((world1, pos, state1, blockEntity) -> {
            if(world1.isClient) return;

            tick = Math.max(tick-1, 0);
            if(tick > 0) return;
            tick = TICK_RATE;

            world1.updateNeighbors(pos, world1.getBlockState(pos).getBlock());
        });
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.ENERGY.instantiate(pos, state);
    }

    @Override
    public boolean rotates() {
        return false;
    }

    @Override
    public boolean lockY(BlockState state) {
        return false;
    }

    @Override
    public boolean goesOnFloor(BlockStateComponent state) {
        return false;
    }

}

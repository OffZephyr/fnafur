package net.zephyr.fnafur.blocks.energy.blocks.switches;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.CallableByMesurer;
import net.zephyr.fnafur.blocks.energy.entity.BaseEnergyBlockEntity;
import net.zephyr.fnafur.blocks.energy.enums.IElectricNode;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseSwitchBlock extends Block implements BlockEntityProvider, IElectricNode, CallableByMesurer {


    public BaseSwitchBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult addNode(World world, BlockPos pos, BlockPos toAdd, BlockHitResult hit) {
        System.out.println("[SWITCH]: try to connect node...");
        if(pos == toAdd) return ActionResult.FAIL;
        if(!(world.getBlockEntity(pos) instanceof BaseEnergyBlockEntity base)) return ActionResult.FAIL;
        List<Long> p = new ArrayList<>(
                Arrays.stream(base.getData().getLongArray(BaseEnergyBlockEntity.KEY_NODES)).boxed().toList()
        );

        p.add(toAdd.asLong());

        base.setData(BaseEnergyBlockEntity.KEY_NODES, new NbtLongArray(p) );
        System.out.println("[SWITCH]: data nodes : "+ base.getNodes().toString());
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult remNode(World world, BlockPos pos, BlockHitResult hit) {
        return ActionResult.PASS;
    }

    @Override
    public boolean isPowered(BlockView world, BlockPos pos) {
        if(!(world.getBlockEntity(pos) instanceof BaseEnergyBlockEntity base)) return false;
        for(BlockPos p  : base.getNodes()){
            if(!(world.getBlockState(p).getBlock() instanceof IElectricNode node)) continue;
            if(node.isPowered(world,p)) return true;
        }
        return false;
    }

    @Override
    public boolean canBeParent() {
        return false;
    }

    @Override
    public ActionResult ExecuteAction(ItemUsageContext context) {
        NbtCompound data = context.getStack().getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();

        if(data.getBoolean("needConnection")){
            addNode(
                    context.getWorld(),
                    context.getBlockPos(),
                    BlockPos.fromLong(data.getLong("posConnection")),
                    null
            );
            data.putBoolean("needConnection", false);
            data.putLong("posConnection", 0L);
        }
        else {
            data.putBoolean("needConnection", true);
            data.putLong("posConnection", context.getBlockPos().asLong());
        }

        System.out.println("[SWITCH] tool data: "+data.toString());

        context.getStack().apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.copyFrom(data);
        }));

        return ActionResult.SUCCESS;
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return isPowered(world, pos) ? 16 : 0;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.ENERGY.instantiate(pos, state);
    }
}

    // TODO: auto-refresh weakRedstonePower()

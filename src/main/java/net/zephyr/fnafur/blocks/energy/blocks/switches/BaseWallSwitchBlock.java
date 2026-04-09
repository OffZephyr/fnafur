package net.zephyr.fnafur.blocks.energy.blocks.switches;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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

public class BaseWallSwitchBlock extends WallPropBlock implements EntityBlock, EnergyNode, CallableByMesurer {

    int tick;
    final int TICK_RATE = 20;

    public BaseWallSwitchBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public Class<EnergyNodeType> COLOR_ENUM() {
        return null;
    }


    @Override
    public InteractionResult addNode(Level world, BlockPos pos, BlockPos toAdd, Vec3 hit) {
        //System.out.println("[SWITCH]: try to connect node...");
        if(pos == toAdd) return InteractionResult.FAIL;
        if(!(world.getBlockEntity(pos) instanceof BaseEnergyBlockEntity base)) return InteractionResult.FAIL;
        List<Long> p = new ArrayList<>(
                Arrays.stream(base.getData().getLongArray(BaseEnergyBlockEntity.KEY_NODES).get()).boxed().toList()
        );

        if(p.contains(toAdd.asLong()))return InteractionResult.SUCCESS;
        p.add(toAdd.asLong());

        //base.setData(BaseEnergyBlockEntity.KEY_NODES, new NbtLongArray(p) );
        //System.out.println("[SWITCH]: data nodes : "+ base.getNodes().toString());
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult remNode(Level world, BlockPos pos, BlockPos toRem, Vec3 hit) {
        if(!(world.getBlockEntity(pos) instanceof BaseEnergyBlockEntity base)) return InteractionResult.FAIL;
        List<Long> p = new ArrayList<>(
                Arrays.stream(base.getData().getLongArray(BaseEnergyBlockEntity.KEY_NODES).get()).boxed().toList()
        );

        p.remove(toRem.asLong());
        //base.setData(BaseEnergyBlockEntity.KEY_NODES, new NbtLongArray(p) );
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isPowered(BlockGetter world, BlockPos pos) {
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
    public InteractionResult ExecuteAction(UseOnContext context) {
        CompoundTag data = context.getItemInHand().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

        if(data.getBoolean("needConnection").get()){
            if(context.getPlayer().isShiftKeyDown()){
                remNode(
                        context.getLevel(),
                        context.getClickedPos(),
                        BlockPos.of(data.getLong("posConnection").get()),
                        context.getClickLocation()
                );
                //System.out.println("[SWITCH] node removed! ");
            } else {
                addNode(
                        context.getLevel(),
                        context.getClickedPos(),
                        BlockPos.of(data.getLong("posConnection").get()),
                        context.getClickLocation()
                );
                //System.out.println("[SWITCH] node added! ");
            }

            data.putBoolean("needConnection", false);
            data.putLong("posConnection", 0L);

        }
        else {
            data.putBoolean("needConnection", true);
            data.putLong("posConnection", context.getClickedPos().asLong());
        }

        //System.out.println("[SWITCH] tool data: "+data.toString());

        context.getItemInHand().update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, comp -> comp.update(currentNbt -> {
            currentNbt.merge(data);
        }));

        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntityTicker getTicker(Level world, BlockState state, BlockEntityType type) {
        return ((world1, pos, state1, blockEntity) -> {
            if(world1.isClientSide()) return;

            tick = Math.max(tick-1, 0);
            if(tick > 0) return;
            tick = TICK_RATE;

            world1.updateNeighborsAt(pos, world1.getBlockState(pos).getBlock());
        });
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.ENERGY.create(pos, state);
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
    public boolean goesOnFloor(BlockItemStateProperties state) {
        return false;
    }

}

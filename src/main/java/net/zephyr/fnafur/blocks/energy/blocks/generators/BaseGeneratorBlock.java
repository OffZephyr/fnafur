package net.zephyr.fnafur.blocks.energy.blocks.generators;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.CallableByMesurer;
import net.zephyr.fnafur.blocks.energy.enums.EnergyNodeType;
import net.zephyr.fnafur.blocks.energy.enums.EnergyNode;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import org.jetbrains.annotations.Nullable;

public class BaseGeneratorBlock extends FloorPropBlock implements BlockEntityProvider, EnergyNode, CallableByMesurer {

    public BaseGeneratorBlock(Settings settings) {
        super(settings);
    }



    @Override
    public Class<EnergyNodeType> COLOR_ENUM() {
        return null;
    }

    @Override
    public boolean rotates() {
        return false;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.ENERGY.instantiate(pos, state);
    }

    @Override
    public ActionResult addNode(World world, BlockPos pos, BlockPos toAdd, Vec3d hit) {
        return ActionResult.PASS;
    }

    @Override
    public ActionResult remNode(World world, BlockPos pos, BlockPos toRem, Vec3d hit) {
        return ActionResult.PASS;
    }

    @Override
    public boolean isPowered(BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public EnergyNodeType nodeType() {
        return EnergyNodeType.GENERATOR;
    }

    @Override
    public ActionResult ExecuteAction(ItemUsageContext context) {
        NbtCompound data = context.getStack().getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();

        data.putBoolean("needConnection", true);
        data.putLong("posConnection", context.getBlockPos().asLong());

        context.getStack().apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.copyFrom(data);
        }));
        return ActionResult.SUCCESS;
    }
}

package net.zephyr.fnafur.blocks.energy.blocks.generators;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.energy.enums.GenericEnum;
import net.zephyr.fnafur.blocks.energy.enums.IElectricNode;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import org.jetbrains.annotations.Nullable;

public class BaseGeneratorBlock extends FloorPropBlock<GenericEnum> implements BlockEntityProvider, IElectricNode {

    public BaseGeneratorBlock(Settings settings) {
        super(settings);
    }



    @Override
    public Class COLOR_ENUM() {
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
    public ActionResult addNode(World world, BlockPos pos, BlockPos toAdd, BlockHitResult hit) {
        return null;
    }

    @Override
    public ActionResult remNode(World world, BlockPos pos, BlockHitResult hit) {
        return null;
    }

    @Override
    public boolean isPowered(World world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canBeParent() {
        return false;
    }
}

package net.zephyr.fnafur.blocks.utility_blocks.workbench;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.init.ScreensInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class WorkbenchBlock extends FloorPropBlock<DefaultPropColorEnum> {
    public WorkbenchBlock(Settings settings) {
        super(settings);
    }

    @Override
    public Class COLOR_ENUM() {
        return DefaultPropColorEnum.class;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient()){
            GoopyNetworkingUtils.setScreen(player, ScreensInit.WORKBENCH, ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData(), pos);
        }

        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public boolean rotates() {
        return true;
    }
}

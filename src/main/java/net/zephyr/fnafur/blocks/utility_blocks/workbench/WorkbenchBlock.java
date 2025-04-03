package net.zephyr.fnafur.blocks.utility_blocks.workbench;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.init.ScreensInit;
import net.zephyr.fnafur.init.block_init.PropInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class WorkbenchBlock extends FloorPropBlock<DefaultPropColorEnum> {
    public WorkbenchBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.EAST));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();

        switch (state.get(FACING).getAxis()){
            default -> shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(-0.5f, 0f, 0f, 1.5f, 1.9f, 1f)));
            //case Direction.Axis.Z -> shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0f, 0f, -0.5f, 1f, 1.9f, 1.5f)));
        }

        return drawingOutline ? shape : VoxelShapes.fullCube();

        //return VoxelShapes.fullCube();
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if(stack.isEmpty() || stack.isOf(PropInit.COSMO_GIFT.asItem())) {
            if (!world.isClient()) {
                NbtCompound nbt2 = ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().copy();
                if (stack.isOf(PropInit.COSMO_GIFT.asItem())) {
                    NbtCompound nbt = ItemNbtUtil.getNbt(stack);

                    if (nbt.isEmpty()) return super.onUseWithItem(stack, state, world, pos, player, hand, hit);

                        nbt2.put("GiftData", nbt);
                        stack.decrement(1);
                }
                GoopyNetworkingUtils.setScreen(player, ScreensInit.WORKBENCH, nbt2, pos);
            }
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public boolean rotates() {
        return true;
    }
}

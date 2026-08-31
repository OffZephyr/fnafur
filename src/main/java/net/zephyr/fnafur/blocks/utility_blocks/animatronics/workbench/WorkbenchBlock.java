package net.zephyr.fnafur.blocks.utility_blocks.animatronics.workbench;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.init.ScreensInit;
import net.zephyr.fnafur.init.block_init.PropInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class WorkbenchBlock extends FloorPropBlock<DefaultPropColorEnum> {
    public WorkbenchBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();

        switch (state.getValue(FACING).getAxis()){
            default -> shape = Shapes.or(shape, Shapes.create(new AABB(-0.5f, 0f, 0f, 1.5f, 1.9f, 1f)));
            //case Direction.Axis.Z -> shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0f, 0f, -0.5f, 1f, 1.9f, 1.5f)));
        }

        return drawingOutline ? shape : Shapes.block();

        //return VoxelShapes.fullCube();
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if(stack.isEmpty() || stack.is(PropInit.COSMO_GIFT.asItem())) {
            if (!world.isClientSide()) {
                CompoundTag nbt2 = ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().copy();
                if (stack.is(PropInit.COSMO_GIFT.asItem())) {
                    CompoundTag nbt = ItemUtil.getNbt(stack);

                    if (nbt.isEmpty()) return super.useItemOn(stack, state, world, pos, player, hand, hit);

                    nbt2.put("GiftData", nbt);
                    stack.shrink(1);
                }
                GoopyNetworkingUtils.setScreen(player, ScreensInit.WORKBENCH, nbt2, pos);
            }
        }
        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    public static void spawnItem(Level world, BlockPos pos, String chara, String alt, String eyes){
        ItemStack stack = new ItemStack(ItemInit.ANIMATRONIC_SUIT);
        CompoundTag nbt = new CompoundTag();
        nbt.putString("chara", chara);
        nbt.putString("alt", alt);
        nbt.putString("eyes", eyes);
        ItemUtil.setNbt(stack, nbt);
        popResourceFromFace(world, pos, Direction.UP, stack);
    }

    @Override
    public boolean rotates() {
        return false;
    }
}

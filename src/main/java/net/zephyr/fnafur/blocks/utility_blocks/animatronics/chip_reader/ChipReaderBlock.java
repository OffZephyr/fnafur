package net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class ChipReaderBlock extends BlockWithEntity {
    public static final BooleanProperty CPU = BooleanProperty.of("cpu");
    public static final BooleanProperty ANIMATION_CHIP = BooleanProperty.of("anim");
    public static final BooleanProperty PATHING_CHIP = BooleanProperty.of("path");
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

    public ChipReaderBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(CPU, false).with(ANIMATION_CHIP, false).with(PATHING_CHIP, false));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.25, 0, 0.25, 0.75, 0.5, 0.75);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BlockEntityInit.CHIP_READER,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {

        if(world.getBlockEntity(pos) instanceof ChipReaderBlockEntity ent){
            ActionResult result = ent.tryStartLink(player, pos);
            if(result != null) return result;
        }

        if(player.isSneaking()){
            ActionResult result = state.get(CPU) || state.get(ANIMATION_CHIP) || state.get(PATHING_CHIP) ? ActionResult.SUCCESS : ActionResult.PASS;
            if(state.get(CPU)){
                ItemStack stack = new ItemStack(ItemInit.CPU, 1);
                if(world.getBlockEntity(pos) instanceof ChipReaderBlockEntity ent){
                    NbtCompound CPU = ((IEntityDataSaver)ent).getPersistentData().getCompoundOrEmpty("cpu");
                    if(!CPU.isEmpty()){
                        ItemUtil.setNbt(stack, CPU);
                        ((IEntityDataSaver)ent).getPersistentData().remove("cpu");
                    }
                }
                dropStack(world, pos, state.get(FACING), stack);
                world.setBlockState(pos, state.with(CPU, false));
            }
            if(state.get(ANIMATION_CHIP)){
                world.setBlockState(pos, state.with(ANIMATION_CHIP, false));
            }
            if(state.get(PATHING_CHIP)){
                world.setBlockState(pos, state.with(PATHING_CHIP, false));
            }
            return result;
        }
        else{
            if(player.getMainHandStack().isOf(ItemInit.CPU)){
                world.setBlockState(pos, state.with(CPU, true));

                if(world.getBlockEntity(pos) instanceof ChipReaderBlockEntity ent){
                    NbtCompound cpu_data = ItemUtil.getNbt(player.getMainHandStack());
                    ((IEntityDataSaver)ent).getPersistentData().put("cpu", NbtCompound.CODEC, cpu_data);
                }

                player.getMainHandStack().split(1);
                return ActionResult.SUCCESS;
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ChipReaderBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(FACING, PATHING_CHIP, CPU, ANIMATION_CHIP));
    }
}

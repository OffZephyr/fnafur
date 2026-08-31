package net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class ChipReaderBlock extends BaseEntityBlock {
    public static final BooleanProperty CPU = BooleanProperty.create("cpu");
    public static final BooleanProperty ANIMATION_CHIP = BooleanProperty.create("anim");
    public static final BooleanProperty PATHING_CHIP = BooleanProperty.create("path");
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public ChipReaderBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(CPU, false).setValue(ANIMATION_CHIP, false).setValue(PATHING_CHIP, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.box(0.25, 0, 0.25, 0.75, 0.5, 0.75);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityInit.CHIP_READER,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {

        if(world.getBlockEntity(pos) instanceof ChipReaderBlockEntity ent){
            InteractionResult result = ent.tryStartLink(player, pos);
            if(result != null) return result;
        }

        if(player.isShiftKeyDown()){
            InteractionResult result = state.getValue(CPU) || state.getValue(ANIMATION_CHIP) || state.getValue(PATHING_CHIP) ? InteractionResult.SUCCESS : InteractionResult.PASS;
            if(state.getValue(CPU)){
                ItemStack stack = new ItemStack(ItemInit.CPU, 1);
                if(world.getBlockEntity(pos) instanceof ChipReaderBlockEntity ent){
                    CompoundTag CPU = ((IEntityDataSaver)ent).getPersistentData().getCompoundOrEmpty("cpu");
                    if(!CPU.isEmpty()){
                        ItemUtil.setNbt(stack, CPU);
                        ((IEntityDataSaver)ent).getPersistentData().remove("cpu");
                    }
                }
                popResourceFromFace(world, pos, state.getValue(FACING), stack);
                world.setBlockAndUpdate(pos, state.setValue(CPU, false));
            }
            if(state.getValue(ANIMATION_CHIP)){
                world.setBlockAndUpdate(pos, state.setValue(ANIMATION_CHIP, false));
            }
            if(state.getValue(PATHING_CHIP)){
                world.setBlockAndUpdate(pos, state.setValue(PATHING_CHIP, false));
            }
            return result;
        }
        else{
            if(player.getMainHandItem().is(ItemInit.CPU)){
                world.setBlockAndUpdate(pos, state.setValue(CPU, true));

                if(world.getBlockEntity(pos) instanceof ChipReaderBlockEntity ent){
                    CompoundTag cpu_data = ItemUtil.getNbt(player.getMainHandItem());
                    ((IEntityDataSaver)ent).getPersistentData().store("cpu", CompoundTag.CODEC, cpu_data);
                }

                player.getMainHandItem().split(1);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChipReaderBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx).setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, PATHING_CHIP, CPU, ANIMATION_CHIP));
    }
}

package net.zephyr.fnafur.blocks.energy.blocks.switches;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.zephyr.fnafur.blocks.CallableByMesurer;
import net.zephyr.fnafur.blocks.energy.enums.EnergyNodeType;
import net.zephyr.fnafur.blocks.energy.enums.EnergyNode;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ElectricalLockerBlock extends BaseEntityBlock implements EntityBlock, EnergyNode, CallableByMesurer {
    public static final String KEY_OPEN = "open";
    public static final BooleanProperty OPEN = BooleanProperty.create(KEY_OPEN);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty MAIN = BooleanProperty.create("main");
    public static final BooleanProperty SIDE = BooleanProperty.create("side");
    public static final List<BooleanProperty> LEVERS = List.of(
            BooleanProperty.create("lever_0"),
            BooleanProperty.create("lever_1"),
            BooleanProperty.create("lever_2"),
            BooleanProperty.create("lever_3"),
            BooleanProperty.create("lever_4"),
            BooleanProperty.create("lever_5")
    );

    List<VoxelShape> leverShapes = new ArrayList<>();
    public AABB[] levers;

    public ElectricalLockerBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(MAIN, false).setValue(SIDE, false).setValue(OPEN, false));
        for(BooleanProperty p : LEVERS){
            registerDefaultState(defaultBlockState().setValue(p, false));
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    public void buildLevers(){
        /*
        Vec3d min = shape.getBoundingBox().getMinPos();
        double leverSize  = 0.25;
        double leverDepth = 0.1;
        levers = new Box[]{
                new Box(
                        min.add(0,0,0),
                        min.add(0.5,0.5,0.1)
                ).offset(0,0,0)

        };
        */
    }


    @Override
    public InteractionResult ExecuteAction(UseOnContext context) {
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult addNode(Level world, BlockPos pos, BlockPos toAdd, Vec3 hit) {
        return null;
    }

    @Override
    public InteractionResult remNode(Level world, BlockPos pos, BlockPos toRem, Vec3 hit) {
        return null;
    }

    @Override
    public boolean isPowered(BlockGetter world, BlockPos pos) {
        return false;
    }

    @Override
    public EnergyNodeType nodeType() {
        return EnergyNodeType.SWITCH;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockPos sidePos = pos.relative(state.getValue(FACING).getCounterClockWise());

        BlockPos mainPos = pos.above();

        world.setBlockAndUpdate(sidePos, state.setValue(MAIN, false).setValue(SIDE, true));
        world.setBlockAndUpdate(pos.above(), state.setValue(MAIN, true));
        world.setBlockAndUpdate(sidePos.above(), state.setValue(MAIN, false).setValue(SIDE, true));
        world.setBlockAndUpdate(pos.above().above(), state.setValue(MAIN, false));
        world.setBlockAndUpdate(sidePos.above().above(), state.setValue(MAIN, false).setValue(SIDE, true));

        ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().putLong("mainBlock", mainPos.asLong());
        ((IEntityDataSaver)world.getBlockEntity(pos.above())).getPersistentData().putLong("mainBlock", mainPos.asLong());
        ((IEntityDataSaver)world.getBlockEntity(pos.above().above())).getPersistentData().putLong("mainBlock", mainPos.asLong());
        ((IEntityDataSaver)world.getBlockEntity(sidePos)).getPersistentData().putLong("mainBlock", mainPos.asLong());
        ((IEntityDataSaver)world.getBlockEntity(sidePos.above())).getPersistentData().putLong("mainBlock", mainPos.asLong());
        ((IEntityDataSaver)world.getBlockEntity(sidePos.above().above())).getPersistentData().putLong("mainBlock", mainPos.asLong());

        if(!world.isClientSide()) {
            GoopyNetworkingUtils.saveBlockNbt(pos, ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData(), world);
            GoopyNetworkingUtils.saveBlockNbt(pos.above(), ((IEntityDataSaver) world.getBlockEntity(pos.above())).getPersistentData(), world);
            GoopyNetworkingUtils.saveBlockNbt(pos.above().above(), ((IEntityDataSaver) world.getBlockEntity(pos.above().above())).getPersistentData(), world);
            GoopyNetworkingUtils.saveBlockNbt(sidePos, ((IEntityDataSaver) world.getBlockEntity(sidePos)).getPersistentData(), world);
            GoopyNetworkingUtils.saveBlockNbt(sidePos.above(), ((IEntityDataSaver) world.getBlockEntity(sidePos.above())).getPersistentData(), world);
            GoopyNetworkingUtils.saveBlockNbt(sidePos.above().above(), ((IEntityDataSaver) world.getBlockEntity(sidePos.above().above())).getPersistentData(), world);
        }

        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        BlockPos mainPos = BlockPos.of(((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getLong("mainBlock").orElse(0L));
        BlockPos sidePos = mainPos.relative(state.getValue(FACING).getCounterClockWise());

        world.setBlockAndUpdate(mainPos.below(), Blocks.AIR.defaultBlockState());
        world.setBlockAndUpdate(sidePos.below(), Blocks.AIR.defaultBlockState());
        world.setBlockAndUpdate(mainPos, Blocks.AIR.defaultBlockState());
        world.setBlockAndUpdate(sidePos, Blocks.AIR.defaultBlockState());
        world.setBlockAndUpdate(mainPos.above(), Blocks.AIR.defaultBlockState());
        world.setBlockAndUpdate(sidePos.above(), Blocks.AIR.defaultBlockState());

        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return state.getValue(MAIN) ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.ENERGY.create(pos, state);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }
    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {

        if(world.getBlockEntity(pos) instanceof BlockEntity entity) {
            BlockPos mainPos = BlockPos.of(((IEntityDataSaver) entity).getPersistentData().getLong("mainBlock").orElse(0L));
            if (!state.getValue(MAIN)) return this.useWithoutItem(world.getBlockState(mainPos), world, mainPos, player, hit);
            BlockPos sidePos = mainPos.relative(state.getValue(FACING).getCounterClockWise());

            buildLevers();

            // open the locker
            if (state.getValue(OPEN)) {

                float[][] leverOffsets = getLeverOffsets(state.getValue(FACING));
                for (int i = 0; i < 6; i++) {
                    AABB box = new AABB(pos.getX() + leverOffsets[i][0], pos.getY() + leverOffsets[i][1], pos.getZ() + leverOffsets[i][2], pos.getX() + leverOffsets[i][0] + 0.25f, pos.getY() + leverOffsets[i][1] + 0.25f, pos.getZ() + leverOffsets[i][2] + 0.25f).inflate(0.025f);

                    if (box.contains(hit.getLocation())) {
                        world.setBlockAndUpdate(pos, state.cycle(LEVERS.get(i)));
                        return InteractionResult.SUCCESS;
                    }
                }

                // check for levers
            /*for (int i = 0; i < levers.length; i++) {
                HitResult hitResult = player.raycast(20.0, 0.0f, false);
                if (!(hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHit))
                    continue;

                Box voxel = levers[i];
                if (!voxel.contains(blockHit.getPos())) continue;

                System.out.println("clicked!");
                return InteractionResult.SUCCESS;
            }
             */
            }
            // close the locker
            world.setBlockAndUpdate(pos, state.cycle(OPEN));

            BlockState newState = world.getBlockState(pos).setValue(MAIN, false);

            world.setBlockAndUpdate(pos.below(), newState);
            world.setBlockAndUpdate(pos.above(), newState);
            world.setBlockAndUpdate(sidePos.below(), newState.setValue(SIDE, true));
            world.setBlockAndUpdate(sidePos, newState.setValue(SIDE, true));
            world.setBlockAndUpdate(sidePos.above(), newState.setValue(SIDE, true));

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        for(BooleanProperty p : LEVERS){
            builder = builder.add(p);
        }

        super.createBlockStateDefinition(builder
                .add(OPEN)
                .add(MAIN)
                .add(FACING)
                .add(SIDE)
        );
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        float thickness = state.getValue(OPEN) ? 0.25f : 0.675f;
        float sideThickness = state.getValue(SIDE) ? 0.5f : 1;

        VoxelShape shape;
        if(world.getBlockEntity(pos) instanceof BlockEntity ent) {
            BlockPos mainPos = BlockPos.of(((IEntityDataSaver)ent).getPersistentData().getLong("mainBlock").orElse(0L));

            Vec3 offset = Vec3.atLowerCornerOf(mainPos.offset(pos.multiply(-1)));

            int[] leverPos = new int[18];
            switch (state.getValue(FACING)) {

                default -> shape = Shapes.box(1 - sideThickness, 0, 1 - thickness, 1, 1, 1);
                case SOUTH -> shape = Shapes.box(0, 0, 0, sideThickness, 1, thickness);
                case WEST -> shape = Shapes.box(1 - thickness, 0, 0, 1, 1, sideThickness);
                case EAST ->  shape = Shapes.box(0, 0, 1 - sideThickness, thickness, 1, 1);

            };

            float[][] leverOffsets = getLeverOffsets(state.getValue(FACING));

            for(int i = 0; i < 6; i++){
                shape = Shapes.or(shape, Shapes.box(0, 0, 0, 0.25f, 0.25f, 0.25f).move(leverOffsets[i][0], leverOffsets[i][1], leverOffsets[i][2]).move(offset));
            }

            return shape;
        }
        return Shapes.empty();
    }

    private float[][] getLeverOffsets(Direction direction){
        return switch (direction){

            default -> new float[][]{
                    {0.6f, 0.35f, 0.70f},
                    {0.3f, 0.35f, 0.70f},
                    {0.6f, 0, 0.70f},
                    {0.3f, 0, 0.70f},
                    {0.6f, -0.35f, 0.70f},
                    {0.3f, -0.35f, 0.70f}
            };

            case SOUTH -> new float[][]{
                    {0.15f, 0.35f, 0.05f},
                    {0.45f, 0.35f, 0.05f},
                    {0.15f, 0, 0.05f},
                    {0.45f, 0, 0.05f},
                    {0.15f, -0.35f, 0.05f},
                    {0.45f, -0.35f, 0.05f}
            };

            case EAST -> new float[][]{
                    {0.05f, 0.35f, 0.6f},
                    {0.05f, 0.35f, 0.3f},
                    {0.05f, 0, 0.6f},
                    {0.05f, 0, 0.3f},
                    {0.05f, -0.35f, 0.6f},
                    {0.05f, -0.35f, 0.3f}
            };

            case WEST -> new float[][]{
                    {0.7f, 0.35f, 0.15f},
                    {0.7f, 0.35f, 0.45f},
                    {0.7f, 0, 0.15f},
                    {0.7f, 0, 0.45f},
                    {0.7f, -0.35f, 0.15f},
                    {0.7f, -0.35f, 0.45f}
            };

        };
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return super.canSurvive(state, world, pos);
    }
}

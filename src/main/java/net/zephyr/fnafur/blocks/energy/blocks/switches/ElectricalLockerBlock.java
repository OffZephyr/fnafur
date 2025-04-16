package net.zephyr.fnafur.blocks.energy.blocks.switches;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
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

public class ElectricalLockerBlock extends BlockWithEntity implements BlockEntityProvider, EnergyNode, CallableByMesurer {
    public static final String KEY_OPEN = "open";
    public static final BooleanProperty OPEN = BooleanProperty.of(KEY_OPEN);
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty MAIN = BooleanProperty.of("main");
    public static final BooleanProperty SIDE = BooleanProperty.of("side");
    public static final List<BooleanProperty> LEVERS = List.of(
            BooleanProperty.of("lever_0"),
            BooleanProperty.of("lever_1"),
            BooleanProperty.of("lever_2"),
            BooleanProperty.of("lever_3"),
            BooleanProperty.of("lever_4"),
            BooleanProperty.of("lever_5")
    );

    List<VoxelShape> leverShapes = new ArrayList<>();
    public Box[] levers;

    public ElectricalLockerBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(MAIN, false).with(SIDE, false).with(OPEN, false));
        for(BooleanProperty p : LEVERS){
            setDefaultState(getDefaultState().with(p, false));
        }
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
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
    public ActionResult ExecuteAction(ItemUsageContext context) {
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult addNode(World world, BlockPos pos, BlockPos toAdd, Vec3d hit) {
        return null;
    }

    @Override
    public ActionResult remNode(World world, BlockPos pos, BlockPos toRem, Vec3d hit) {
        return null;
    }

    @Override
    public boolean isPowered(BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public EnergyNodeType nodeType() {
        return EnergyNodeType.SWITCH;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockPos sidePos = pos.offset(state.get(FACING).rotateYCounterclockwise());

        BlockPos mainPos = pos.up();

        world.setBlockState(sidePos, state.with(MAIN, false).with(SIDE, true));
        world.setBlockState(pos.up(), state.with(MAIN, true));
        world.setBlockState(sidePos.up(), state.with(MAIN, false).with(SIDE, true));
        world.setBlockState(pos.up().up(), state.with(MAIN, false));
        world.setBlockState(sidePos.up().up(), state.with(MAIN, false).with(SIDE, true));

        ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().putLong("mainBlock", mainPos.asLong());
        ((IEntityDataSaver)world.getBlockEntity(pos.up())).getPersistentData().putLong("mainBlock", mainPos.asLong());
        ((IEntityDataSaver)world.getBlockEntity(pos.up().up())).getPersistentData().putLong("mainBlock", mainPos.asLong());
        ((IEntityDataSaver)world.getBlockEntity(sidePos)).getPersistentData().putLong("mainBlock", mainPos.asLong());
        ((IEntityDataSaver)world.getBlockEntity(sidePos.up())).getPersistentData().putLong("mainBlock", mainPos.asLong());
        ((IEntityDataSaver)world.getBlockEntity(sidePos.up().up())).getPersistentData().putLong("mainBlock", mainPos.asLong());

        if(!world.isClient()) {
            GoopyNetworkingUtils.saveBlockNbt(pos, ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData(), world);
            GoopyNetworkingUtils.saveBlockNbt(pos.up(), ((IEntityDataSaver) world.getBlockEntity(pos.up())).getPersistentData(), world);
            GoopyNetworkingUtils.saveBlockNbt(pos.up().up(), ((IEntityDataSaver) world.getBlockEntity(pos.up().up())).getPersistentData(), world);
            GoopyNetworkingUtils.saveBlockNbt(sidePos, ((IEntityDataSaver) world.getBlockEntity(sidePos)).getPersistentData(), world);
            GoopyNetworkingUtils.saveBlockNbt(sidePos.up(), ((IEntityDataSaver) world.getBlockEntity(sidePos.up())).getPersistentData(), world);
            GoopyNetworkingUtils.saveBlockNbt(sidePos.up().up(), ((IEntityDataSaver) world.getBlockEntity(sidePos.up().up())).getPersistentData(), world);
        }

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos mainPos = BlockPos.fromLong(((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getLong("mainBlock"));
        BlockPos sidePos = mainPos.offset(state.get(FACING).rotateYCounterclockwise());

        world.setBlockState(mainPos.down(), Blocks.AIR.getDefaultState());
        world.setBlockState(sidePos.down(), Blocks.AIR.getDefaultState());
        world.setBlockState(mainPos, Blocks.AIR.getDefaultState());
        world.setBlockState(sidePos, Blocks.AIR.getDefaultState());
        world.setBlockState(mainPos.up(), Blocks.AIR.getDefaultState());
        world.setBlockState(sidePos.up(), Blocks.AIR.getDefaultState());

        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return state.get(MAIN) ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.ENERGY.instantiate(pos, state);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {

        if(world.getBlockEntity(pos) instanceof BlockEntity entity) {
            BlockPos mainPos = BlockPos.fromLong(((IEntityDataSaver) entity).getPersistentData().getLong("mainBlock"));
            if (!state.get(MAIN)) return this.onUse(world.getBlockState(mainPos), world, mainPos, player, hit);
            BlockPos sidePos = mainPos.offset(state.get(FACING).rotateYCounterclockwise());

            buildLevers();

            // open the locker
            if (state.get(OPEN)) {

                float[][] leverOffsets = getLeverOffsets(state.get(FACING));
                for (int i = 0; i < 6; i++) {
                    Box box = new Box(pos.getX() + leverOffsets[i][0], pos.getY() + leverOffsets[i][1], pos.getZ() + leverOffsets[i][2], pos.getX() + leverOffsets[i][0] + 0.25f, pos.getY() + leverOffsets[i][1] + 0.25f, pos.getZ() + leverOffsets[i][2] + 0.25f).expand(0.025f);

                    if (box.contains(hit.getPos())) {
                        world.setBlockState(pos, state.cycle(LEVERS.get(i)));
                        System.out.println("LEVER " + i);
                        return ActionResult.SUCCESS;
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
                return ActionResult.SUCCESS;
            }
             */
            }
            // close the locker
            world.setBlockState(pos, state.cycle(OPEN));

            BlockState newState = world.getBlockState(pos).with(MAIN, false);

            world.setBlockState(pos.down(), newState);
            world.setBlockState(pos.up(), newState);
            world.setBlockState(sidePos.down(), newState.with(SIDE, true));
            world.setBlockState(sidePos, newState.with(SIDE, true));
            world.setBlockState(sidePos.up(), newState.with(SIDE, true));

            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        for(BooleanProperty p : LEVERS){
            builder = builder.add(p);
        }

        super.appendProperties(builder
                .add(OPEN)
                .add(MAIN)
                .add(FACING)
                .add(SIDE)
        );
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        float thickness = state.get(OPEN) ? 0.25f : 0.675f;
        float sideThickness = state.get(SIDE) ? 0.5f : 1;

        VoxelShape shape;
        if(world.getBlockEntity(pos) instanceof BlockEntity ent) {
            BlockPos mainPos = BlockPos.fromLong(((IEntityDataSaver)ent).getPersistentData().getLong("mainBlock"));

            Vec3d offset = Vec3d.of(mainPos.add(pos.multiply(-1)));

            int[] leverPos = new int[18];
            switch (state.get(FACING)) {

                default -> shape = VoxelShapes.cuboid(1 - sideThickness, 0, 1 - thickness, 1, 1, 1);
                case SOUTH -> shape = VoxelShapes.cuboid(0, 0, 0, sideThickness, 1, thickness);
                case WEST -> shape = VoxelShapes.cuboid(1 - thickness, 0, 0, 1, 1, sideThickness);
                case EAST ->  shape = VoxelShapes.cuboid(0, 0, 1 - sideThickness, thickness, 1, 1);

            };

            float[][] leverOffsets = getLeverOffsets(state.get(FACING));

            for(int i = 0; i < 6; i++){
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0, 0.25f, 0.25f, 0.25f).offset(leverOffsets[i][0], leverOffsets[i][1], leverOffsets[i][2]).offset(offset));
            }

            return shape;
        }
        return VoxelShapes.empty();
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
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return super.canPlaceAt(state, world, pos);
    }
}

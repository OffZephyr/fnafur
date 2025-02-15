package net.zephyr.fnafur.blocks.energy.blocks.switches;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.CallableByMesurer;
import net.zephyr.fnafur.blocks.energy.enums.EnergyNodeType;
import net.zephyr.fnafur.blocks.energy.enums.EnergyNode;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import org.jetbrains.annotations.Nullable;

public class ElectricalLockerBlock extends WallPropBlock implements BlockEntityProvider, EnergyNode, CallableByMesurer {


    public static final String KEY_OPEN = "open";
    public static final BooleanProperty OPEN = BooleanProperty.of(KEY_OPEN);

    protected VoxelShape shape =
            VoxelShapes.cuboid(0,0,0,1.5,2.6,0.5).offset(-0.25,-.8,0.5);
    public Box[] levers;

    public ElectricalLockerBlock(Settings settings) {
        super(settings);
        buildLevers();
    }

    public void buildLevers(){
        Vec3d min = shape.getBoundingBox().getMinPos();
        double leverSize  = 0.25;
        double leverDepth = 0.1;
        levers = new Box[]{
                new Box(
                        min.add(0,0,0),
                        min.add(0.5,0.5,0.1)
                ).offset(0,0,0)

        };
    }

    @Override
    public Class COLOR_ENUM() {
        return null;
    }

    @Override
    public boolean lockY(BlockState state) {
        return true;
    }

    @Override
    public boolean goesOnFloor(BlockStateComponent state) {
        return false;
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


    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.ENERGY.instantiate(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        buildLevers();

        // open the locker
        if(!state.get(OPEN)){
            world.setBlockState(pos, state.cycle(OPEN));
            return super.onUse(state, world, pos, player, hit);
        }

        // check for levers
        for(int i = 0; i < levers.length;i++){
            HitResult hitResult = player.raycast(20.0, 0.0f, false);
            if(!(hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHit)) continue;

            Box voxel = levers[i];
            if(!voxel.contains(blockHit.getPos()))continue;

            System.out.println("clicked!");
            return ActionResult.SUCCESS;
        }

        // close the locker
        world.setBlockState(pos, state.cycle(OPEN));
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder builder) {
        builder.add(OPEN);
        super.appendProperties(builder);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return drawingOutline ? shape : VoxelShapes.fullCube();
    }
}

package net.zephyr.fnafur.blocks.props.base;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrame;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrameModel;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public abstract class WallPropBlock<T extends Enum<T> & ColorEnumInterface & StringRepresentable> extends PropBlock<T> {

    public static final EnumProperty<WallHalfProperty> HALF = EnumProperty.create("half", WallHalfProperty.class);
    protected WallPropBlock(Properties settings) {
        super(settings);
    }

    @Override
    public boolean snapsVertically() {
        return false;
    }
    @Override
    public boolean rotates() {
        return true;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {

        if(state.getValue(HALF) == WallHalfProperty.WALL) {
            if (state.hasProperty(FACING)) {
                VoxelShape shape;

                switch (state.getValue(FACING)) {
                    default -> shape = Shapes.box(0, 0, 0, 1, 1, 0.1f);
                    case EAST -> shape = Shapes.box(0, 0, 0, 0.1f, 1, 1);
                    case NORTH -> shape = Shapes.box(0, 0, 0.9f, 1, 1, 1);
                    case WEST -> shape = Shapes.box(0.9f, 0, 0, 1, 1, 1);
                    case UP -> shape = Shapes.box(0, 0.9f, 0, 1, 1, 1);
                    case DOWN -> shape = Shapes.box(0, 0, 0, 1, 0.1f, 1);
                }

                return shape;
            }
        }
        else if(state.getValue(HALF) == WallHalfProperty.CEILING){
            return Shapes.box(0, 0.9f, 0, 1, 1, 1);
        }
        else{
            return Shapes.box(0, 0, 0, 1, 0.1f, 1);
        }
        return super.getInteractionShape(state, world, pos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing;
        WallHalfProperty half;
        BlockItemStateProperties blockStateComponent = ctx.getItemInHand().getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
        if(ctx.getClickedFace().getAxis() == Direction.Axis.Y && goesOnFloor(blockStateComponent)){
            facing = ctx.getHorizontalDirection().getOpposite();
            half = ctx.getClickedFace() == Direction.UP ? WallHalfProperty.FLOOR : WallHalfProperty.CEILING;
        }
        else {
            facing = ctx.getClickedFace().getAxis() == Direction.Axis.Y ? ctx.getHorizontalDirection().getOpposite() : ctx.getClickedFace();
            half = WallHalfProperty.WALL;
        }
        return defaultBlockState()
                .setValue(FACING, facing).setValue(HALF, half);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.getBlockEntity(pos) != null && placer instanceof Player player) {


            if (player.getMainHandItem() != null && player.getMainHandItem().getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof WallPropBlock<?> && world.isClientSide()) {
                    FloorPropBlock.drawingOutline = true;
                    HitResult blockHit = Minecraft.getInstance().hitResult;
                    if (blockHit.getType() == HitResult.Type.BLOCK && blockHit instanceof BlockHitResult blockHitResult) {

                        Direction facing = state.getValue(FACING);

                        double x = blockHitResult.getLocation().x() - blockHitResult.getBlockPos().getX();
                        double y = blockHitResult.getLocation().y() - blockHitResult.getBlockPos().getY();
                        double z = blockHitResult.getLocation().z() - blockHitResult.getBlockPos().getZ();

                        x = Math.clamp(x, 0, 1);
                        y = Math.clamp(y, 0, 1);
                        z = Math.clamp(z, 0, 1);

                        if (player.isShiftKeyDown()) {
                            x = Math.round(x / PropBlock.gridSnap) * PropBlock.gridSnap;
                            y = Math.round(y / PropBlock.gridSnap) * PropBlock.gridSnap;
                            z = Math.round(z / PropBlock.gridSnap) * PropBlock.gridSnap;
                        }

                        if(blockHitResult.getDirection().getAxis() == Direction.Axis.Y){
                            y = 0.5f;
                            if(lockY(state)) {
                                x = 0.5f;
                                z = 0.5f;
                            }
                        }
                        else {
                            x = facing.getAxis() == Direction.Axis.X ? 0.5f : x;
                            z = facing.getAxis() == Direction.Axis.Z ? 0.5f : z;
                        }

                        if(lockY(state)) y = 0.5f;

                        BlockState hitBlockState = world.getBlockState(blockHitResult.getBlockPos());
                        Direction d = Direction.UP;
                        float rotation = 0;
                        if (blockHitResult.getDirection().getAxis() != Direction.Axis.Y) {
                            if (hitBlockState.getBlock() instanceof DiagonalMimicFrame f) {
                                if (f.isDiagonal(hitBlockState)) {
                                    BooleanProperty p = f.getDiagonalDirection(hitBlockState);
                                    if (p == DiagonalMimicFrame.NEXT_MAP.get(blockHitResult.getDirection())) {
                                        d = blockHitResult.getDirection();
                                        rotation = -45f;
                                    }
                                    if (p == DiagonalMimicFrame.NEXT_MAP.get(blockHitResult.getDirection().getCounterClockWise())) {
                                        d = blockHitResult.getDirection().getCounterClockWise();
                                        rotation = 45f;
                                    }
                                }

                            }
                        }
                        if (hitBlockState.getBlock() instanceof DiagonalMimicFrame && d != Direction.UP) {

                            Vec3 editPos = new Vec3(x, y, z);

                            editPos = getDiagonalOffset(editPos, d, blockHitResult.getDirection(), blockHitResult.getBlockPos());

                            x += editPos.x();
                            y += editPos.y();
                            z += editPos.z();
                            ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().putDouble("Rotation", rotation);
                        }

                        ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().putDouble("yOffset", y);
                        ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().putDouble("zOffset", z);
                        ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().putDouble("xOffset", x);

                        GoopyNetworkingUtils.saveBlockNbt(pos, ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData());
                    }
                }
            }
        }
        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    public static Vec3 getDiagonalOffset(Vec3 editPos, Direction d, Direction side, BlockPos pos){
        Vec3 newVec = new Vec3(0, 0, 0);
        if(d.getAxis() == Direction.Axis.Z) {
            if (d == side){
                newVec = new Vec3((-editPos.x()/2f), 0, (editPos.x()/2f));
                newVec = newVec.add(new Vec3(0.25f, 0, -0.25f));
            } else {
                newVec = new Vec3((editPos.z()/2f), 0, (-editPos.z()/2f));
                newVec = newVec.add(new Vec3(-0.25f, 0, 0.25f));
            }
        } else {

            //newVec = newVec.add(new Vec3d(0.5f * Math.abs(side.getVector().getX()), editPos.getY(), 0.5f * Math.abs(side.getVector().getZ())));
            if (d == side){
                newVec = new Vec3((-editPos.z()/2f), 0, (-editPos.z()/2f));
            } else {
                newVec = new Vec3((-editPos.x()/2f), 0, (-editPos.x()/2f));
            }
            newVec = newVec.add(new Vec3(0.25f, 0, 0.25f));
        }
        return newVec;
    }

    public abstract boolean lockY(BlockState state);
    public abstract boolean goesOnFloor(BlockItemStateProperties state);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        if(COLOR_ENUM() != null) {
            builder.add(COLOR_PROPERTY());
        }
        builder.add(FACING, HALF);
    }
}

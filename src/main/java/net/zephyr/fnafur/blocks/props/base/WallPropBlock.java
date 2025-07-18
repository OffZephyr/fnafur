package net.zephyr.fnafur.blocks.props.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrame;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrameModel;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public abstract class WallPropBlock<T extends Enum<T> & ColorEnumInterface & StringIdentifiable> extends PropBlock<T> {

    public static final EnumProperty<WallHalfProperty> HALF = EnumProperty.of("half", WallHalfProperty.class);
    protected WallPropBlock(Settings settings) {
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
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {

        if(state.get(HALF) == WallHalfProperty.WALL) {
            if (state.contains(FACING)) {
                VoxelShape shape;

                switch (state.get(FACING)) {
                    default -> shape = VoxelShapes.cuboid(0, 0, 0, 1, 1, 0.1f);
                    case EAST -> shape = VoxelShapes.cuboid(0, 0, 0, 0.1f, 1, 1);
                    case NORTH -> shape = VoxelShapes.cuboid(0, 0, 0.9f, 1, 1, 1);
                    case WEST -> shape = VoxelShapes.cuboid(0.9f, 0, 0, 1, 1, 1);
                    case UP -> shape = VoxelShapes.cuboid(0, 0.9f, 0, 1, 1, 1);
                    case DOWN -> shape = VoxelShapes.cuboid(0, 0, 0, 1, 0.1f, 1);
                }

                return shape;
            }
        }
        else if(state.get(HALF) == WallHalfProperty.CEILING){
            return VoxelShapes.cuboid(0, 0.9f, 0, 1, 1, 1);
        }
        else{
            return VoxelShapes.cuboid(0, 0, 0, 1, 0.1f, 1);
        }
        return super.getRaycastShape(state, world, pos);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing;
        WallHalfProperty half;
        BlockStateComponent blockStateComponent = ctx.getStack().getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT);
        if(ctx.getSide().getAxis() == Direction.Axis.Y && goesOnFloor(blockStateComponent)){
            facing = ctx.getHorizontalPlayerFacing().getOpposite();
            half = ctx.getSide() == Direction.UP ? WallHalfProperty.FLOOR : WallHalfProperty.CEILING;
        }
        else {
            facing = ctx.getSide().getAxis() == Direction.Axis.Y ? ctx.getHorizontalPlayerFacing().getOpposite() : ctx.getSide();
            half = WallHalfProperty.WALL;
        }
        return getDefaultState()
                .with(FACING, facing).with(HALF, half);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.getBlockEntity(pos) != null && placer instanceof PlayerEntity player) {


            if (player.getMainHandStack() != null && player.getMainHandStack().getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof WallPropBlock<?> && world.isClient()) {
                    FloorPropBlock.drawingOutline = true;
                    HitResult blockHit = MinecraftClient.getInstance().crosshairTarget;
                    if (blockHit.getType() == HitResult.Type.BLOCK && blockHit instanceof BlockHitResult blockHitResult) {

                        Direction facing = state.get(FACING);

                        double x = blockHitResult.getPos().getX() - blockHitResult.getBlockPos().getX();
                        double y = blockHitResult.getPos().getY() - blockHitResult.getBlockPos().getY();
                        double z = blockHitResult.getPos().getZ() - blockHitResult.getBlockPos().getZ();

                        x = Math.clamp(x, 0, 1);
                        y = Math.clamp(y, 0, 1);
                        z = Math.clamp(z, 0, 1);

                        if (player.isSneaking()) {
                            x = Math.round(x / PropBlock.gridSnap) * PropBlock.gridSnap;
                            y = Math.round(y / PropBlock.gridSnap) * PropBlock.gridSnap;
                            z = Math.round(z / PropBlock.gridSnap) * PropBlock.gridSnap;
                        }

                        if(blockHitResult.getSide().getAxis() == Direction.Axis.Y){
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
                        if (blockHitResult.getSide().getAxis() != Direction.Axis.Y) {
                            if (hitBlockState.getBlock() instanceof DiagonalMimicFrame f) {
                                if (f.isDiagonal(hitBlockState)) {
                                    BooleanProperty p = f.getDiagonalDirection(hitBlockState);
                                    if (p == DiagonalMimicFrame.NEXT_MAP.get(blockHitResult.getSide())) {
                                        d = blockHitResult.getSide();
                                        rotation = -45f;
                                    }
                                    if (p == DiagonalMimicFrame.NEXT_MAP.get(blockHitResult.getSide().rotateYCounterclockwise())) {
                                        d = blockHitResult.getSide().rotateYCounterclockwise();
                                        rotation = 45f;
                                    }
                                }

                            }
                        }
                        if (hitBlockState.getBlock() instanceof DiagonalMimicFrame && d != Direction.UP) {

                            Vec3d editPos = new Vec3d(x, y, z);

                            editPos = getDiagonalOffset(editPos, d, blockHitResult.getSide(), blockHitResult.getBlockPos());

                            x += editPos.getX();
                            y += editPos.getY();
                            z += editPos.getZ();
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
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    public static Vec3d getDiagonalOffset(Vec3d editPos, Direction d, Direction side, BlockPos pos){
        Vec3d newVec = new Vec3d(0, 0, 0);
        if(d.getAxis() == Direction.Axis.Z) {
            if (d == side){
                newVec = new Vec3d((-editPos.getX()/2f), 0, (editPos.getX()/2f));
                newVec = newVec.add(new Vec3d(0.25f, 0, -0.25f));
            } else {
                newVec = new Vec3d((editPos.getZ()/2f), 0, (-editPos.getZ()/2f));
                newVec = newVec.add(new Vec3d(-0.25f, 0, 0.25f));
            }
        } else {

            //newVec = newVec.add(new Vec3d(0.5f * Math.abs(side.getVector().getX()), editPos.getY(), 0.5f * Math.abs(side.getVector().getZ())));
            if (d == side){
                newVec = new Vec3d((-editPos.getZ()/2f), 0, (-editPos.getZ()/2f));
            } else {
                newVec = new Vec3d((-editPos.getX()/2f), 0, (-editPos.getX()/2f));
            }
            newVec = newVec.add(new Vec3d(0.25f, 0, 0.25f));
        }
        return newVec;
    }

    public abstract boolean lockY(BlockState state);
    public abstract boolean goesOnFloor(BlockStateComponent state);

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if(COLOR_ENUM() != null) {
            builder.add(COLOR_PROPERTY());
        }
        builder.add(FACING, HALF);
    }
}

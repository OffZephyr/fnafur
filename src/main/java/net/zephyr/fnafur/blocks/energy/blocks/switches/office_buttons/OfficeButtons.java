package net.zephyr.fnafur.blocks.energy.blocks.switches.office_buttons;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.OrientationHelper;
import net.minecraft.world.block.WireOrientation;
import net.zephyr.fnafur.blocks.linking.EnergyTarget;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.WallHalfProperty;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OfficeButtons extends WallPropBlock<OfficeButtonsColors> {
    public static boolean checkingButtons = false;
    public static final BooleanProperty DOOR_ON = BooleanProperty.of("door");
    public static final BooleanProperty LIGHT_ON = BooleanProperty.of("light");
    public OfficeButtons(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(DOOR_ON, false).with(LIGHT_ON, false));
    }

    @Nullable
    @Override
    public <Q extends BlockEntity> BlockEntityTicker<Q> getTicker(World world, BlockState state, BlockEntityType<Q> type) {

        return validateTicker(type, BlockEntityInit.OFFICE_BUTTONS,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }
    @Override
    public boolean lockY(BlockState state) {
        return state.get(COLOR_PROPERTY()) == OfficeButtonsColors.DEFAULT;
    }

    @Override
    public boolean goesOnFloor(BlockStateComponent state) {
        ColorEnumInterface color = state.getValue(COLOR_PROPERTY());
        return true;
    }

    @Override
    public Class<OfficeButtonsColors> COLOR_ENUM() {
        return OfficeButtonsColors.class;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {


        BlockEntity entity = world.getBlockEntity(pos);

        if (entity != null) {

            double offsetX = ((IEntityDataSaver) entity).getPersistentData().getDouble("xOffset").orElse(0.0);
            double offsetY = ((IEntityDataSaver) entity).getPersistentData().getDouble("yOffset").orElse(0.0);
            double offsetZ = ((IEntityDataSaver) entity).getPersistentData().getDouble("zOffset").orElse(0.0);

            Box door = new Box(
                    getDoorHitbox(state).minX + offsetX - 0.5f,
                    getDoorHitbox(state).minY + offsetY - 0.5f,
                    getDoorHitbox(state).minZ + offsetZ - 0.5f,
                    getDoorHitbox(state).maxX + offsetX - 0.5f,
                    getDoorHitbox(state).maxY + offsetY - 0.5f,
                    getDoorHitbox(state).maxZ + offsetZ - 0.5f
            );
            Box light = new Box(
                    getLightHitbox(state).minX + offsetX - 0.5f,
                    getLightHitbox(state).minY + offsetY - 0.5f,
                    getLightHitbox(state).minZ + offsetZ - 0.5f,
                    getLightHitbox(state).maxX + offsetX - 0.5f,
                    getLightHitbox(state).maxY + offsetY - 0.5f,
                    getLightHitbox(state).maxZ + offsetZ - 0.5f
            );
            VoxelShape door_light = VoxelShapes.cuboid(door);
            door_light = VoxelShapes.union(door_light, VoxelShapes.cuboid(light));
            if (!drawingOutline) return door_light;
        }

        if (state.get(HALF) == WallHalfProperty.WALL) {
            float y0 = state.get(COLOR_PROPERTY()) == OfficeButtonsColors.DEFAULT ? 0.05f : 0.3f;
            float y1 = state.get(COLOR_PROPERTY()) == OfficeButtonsColors.DEFAULT ? 0.95f : 0.7f;
            VoxelShape northShape = VoxelShapes.cuboid(new Box(0.3f, y0, 0f, 0.7f, y1, 0.1f));
            VoxelShape eastShape = VoxelShapes.cuboid(new Box(0.9f, y0, 0.3f, 1f, y1, 0.7f));
            VoxelShape southShape = VoxelShapes.cuboid(new Box(0.7f, y0, 0.9f, 0.3f, y1, 1f));
            VoxelShape westShape = VoxelShapes.cuboid(new Box(0f, y0, 0.3f, 0.1f, y1, 0.7f));

            return switch (state.get(FACING)) {
                default -> northShape;
                case SOUTH -> southShape;
                case WEST -> westShape;
                case EAST -> eastShape;
            };
        } else {
            float y0 = state.get(HALF) == WallHalfProperty.CEILING ? 0.9f : 0;
            float y1 = state.get(HALF) == WallHalfProperty.CEILING ? 1 : 0.1f;

            if (state.get(COLOR_PROPERTY()) == OfficeButtonsColors.DEFAULT) {

                Box box = switch (state.get(FACING).getAxis()) {
                    default -> new Box(0, 0, 0, 0, 0, 0);
                    case Direction.Axis.X -> new Box(0.05f, y0, 0.3f, 0.95f, y1, 0.7f);
                    case Direction.Axis.Z -> new Box(0.3f, y0, 0.05f, 0.7f, y1, 0.95f);
                };

                return VoxelShapes.cuboid(box);
            }
            else {
                return VoxelShapes.cuboid(0.3f, y0, 0.3f, 0.7f, y1, 0.7f);
            }
        }
    }

    @Override
    public List<Box> getClickHitBoxes(BlockState state) {
        List<Box> list = new ArrayList<>();
        list.add(getDoorHitbox(state));
        list.add(getLightHitbox(state));
        return list;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {


        checkingButtons = true;
        ActionResult result = super.onUse(state, world, pos, player, hit);
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof OfficeButtonsBlockEntity ent) {

            int hitButton = getButton(ent, getDoorHitbox(state), getLightHitbox(state), pos, hit.getPos());

            ActionResult result2 = ent.tryEndLink(player, world, pos);
            if (result2 != null) return result2;

            ActionResult result3 = ent.tryStartLink(player, pos, hitButton);
            if (result3 != null) return result3;

            ItemStack stack = player.getMainHandStack();
            if (stack != null && stack.getItem() == ItemInit.PAINTBRUSH) {
                if (state.get(DOOR_ON)) {
                    toggleDoor(state, world, pos, player);
                }
                if (state.get(LIGHT_ON)) {
                    toggleLight(state, world, pos, player);
                }
                return super.onUse(world.getBlockState(pos), world, pos, player, hit);
            }


            if (hitButton == 0) {
                this.toggleDoor(state, world, pos, null);
                return ActionResult.SUCCESS;
            }
            if (hitButton == 1) {
                this.toggleLight(state, world, pos, null);
                return ActionResult.SUCCESS;
            }
        }
        checkingButtons = false;
        return result;
    }

    int getButton(PropBlockEntity ent, Box doorHitbox, Box lightHitbox, BlockPos pos, Vec3d hitPos){
        double offsetX = ((IEntityDataSaver) ent).getPersistentData().getDouble("xOffset").orElse(0.0);
        double offsetY = ((IEntityDataSaver) ent).getPersistentData().getDouble("yOffset").orElse(0.0);
        double offsetZ = ((IEntityDataSaver) ent).getPersistentData().getDouble("zOffset").orElse(0.0);

        Box door = new Box(
                doorHitbox.minX + pos.getX() + offsetX - 0.5f,
                doorHitbox.minY + pos.getY() + offsetY - 0.5f,
                doorHitbox.minZ + pos.getZ() + offsetZ - 0.5f,
                doorHitbox.maxX + pos.getX() + offsetX - 0.5f,
                doorHitbox.maxY + pos.getY() + offsetY - 0.5f,
                doorHitbox.maxZ + pos.getZ() + offsetZ - 0.5f
        ).expand(0.025f);
        Box light = new Box(
                lightHitbox.minX + pos.getX() + offsetX - 0.5f,
                lightHitbox.minY + pos.getY() + offsetY - 0.5f,
                lightHitbox.minZ + pos.getZ() + offsetZ - 0.5f,
                lightHitbox.maxX + pos.getX() + offsetX - 0.5f,
                lightHitbox.maxY + pos.getY() + offsetY - 0.5f,
                lightHitbox.maxZ + pos.getZ() + offsetZ - 0.5f
        ).expand(0.025f);

        if (door.contains(hitPos)) {
            return 0;
        }
        if (light.contains(hitPos)) {
            return 1;
        }
        return -1;
    }

    public void toggleDoor(BlockState state, World world, BlockPos pos, @Nullable PlayerEntity player) {
        state = state.cycle(DOOR_ON);
        world.setBlockState(pos, state, Block.NOTIFY_ALL);

        if(world.getBlockEntity(pos) instanceof OfficeButtonsBlockEntity ent){
            for(IEntityDataSaver ent2 : ent.getTargets()){
                if(ent2 instanceof LinkTarget t){
                    if(t.getButtonId((IEntityDataSaver)ent) == 0){
                        t.updateStatus(world, pos, ((IEntityDataSaver) ent));
                    }
                }
            }
        }
        this.updateNeighbors(state, world, pos);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new OfficeButtonsBlockEntity(pos, state);
    }

    public void toggleLight(BlockState state, World world, BlockPos pos, @Nullable PlayerEntity player) {
        state = state.cycle(LIGHT_ON);
        world.setBlockState(pos, state, Block.NOTIFY_ALL);

        if(world.getBlockEntity(pos) instanceof OfficeButtonsBlockEntity ent){
            for(IEntityDataSaver ent2 : ent.getTargets()){
                if(ent2 instanceof LinkTarget t){
                    if(t.getButtonId((IEntityDataSaver)ent) == 1){
                        t.updateStatus(world, pos, ((IEntityDataSaver) ent));
                    }
                }
            }
        }

        this.updateNeighbors(state, world, pos);
        // LIGHT TRIGGER

        // PLAY SOUND
        //world.emitGameEvent(player, state.get(LIGHT_ON) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
    }

    public Box getLightHitbox(BlockState state) {
        if(state.get(COLOR_PROPERTY()) == OfficeButtonsColors.DEFAULT) {
            if(state.get(HALF) == WallHalfProperty.WALL) {
                return switch (state.get(FACING)) {
                    default -> new Box(0.3, 0.125f, 0.9f, 0.7f, 0.525f, 1);
                    case EAST -> new Box(0, 0.125f, 0.3f, 0.1f, 0.525f, 0.7f);
                    case SOUTH -> new Box(0.3, 0.125f, 0, 0.7, 0.525f, 0.1f);
                    case WEST -> new Box(0.9f, 0.125f, 0.3f, 1, 0.525f, 0.7);
                };
            }
            else {
                float x0 = 0.3f;
                float x1 = 0.7f;

                float y0 = state.get(HALF) == WallHalfProperty.CEILING ? 0.9f : 0;
                float y1 = state.get(HALF) == WallHalfProperty.CEILING ? 1 : 0.1f;

                float z0 = state.get(HALF) == WallHalfProperty.CEILING ? 0.475f : 0.125f;
                float z1 = state.get(HALF) == WallHalfProperty.CEILING ? 0.875f : 0.525f;

                float z2 = state.get(HALF) == WallHalfProperty.CEILING ? 0.125f : 0.475f;
                float z3 = state.get(HALF) == WallHalfProperty.CEILING ? 0.525f : 0.875f;

                return switch (state.get(FACING)) {
                    default -> new Box(x0, y0, z0, x1, y1, z1);
                    case EAST -> new Box(z2, y0, x0, z3, y1, x1);
                    case SOUTH -> new Box(x0, y0, z2, x1, y1, z3);
                    case WEST -> new Box(z0, y0, x0, z1, y1, x1);
                };
            }
        }
        else if(!state.get(COLOR_PROPERTY()).isDoor()){
            if(state.get(HALF) == WallHalfProperty.WALL) {
                return switch (state.get(FACING)) {
                    default -> new Box(0.3, 0.3f, 0.9f, 0.7f, 0.7f, 1);
                    case EAST -> new Box(0, 0.3f, 0.3f, 0.1f, 0.7f, 0.7f);
                    case SOUTH -> new Box(0.3, 0.3f, 0, 0.7, 0.7f, 0.1f);
                    case WEST -> new Box(0.9f, 0.3f, 0.3f, 1, 0.7f, 0.7);
                };
            }
            else {
                float y0 = state.get(HALF) == WallHalfProperty.CEILING ? 0.9f : 0;
                float y1 = state.get(HALF) == WallHalfProperty.CEILING ? 1 : 0.1f;

                return new Box(0.3f, y0, 0.3f, 0.7f, y1, 0.7f);
            }
        }
        else {
            return new Box(0, 0, 0, 0, 0, 0);
        }
    }
    public Box getDoorHitbox(BlockState state) {
        if(state.get(COLOR_PROPERTY()) == OfficeButtonsColors.DEFAULT) {
            if(state.get(HALF) == WallHalfProperty.WALL) {
                return switch (state.get(FACING)) {
                    default -> new Box(0.3, 0.55f, 0.9f, 0.7f, 0.95f, 1);
                    case EAST -> new Box(0, 0.55f, 0.3f, 0.1f, 0.95f, 0.7f);
                    case SOUTH -> new Box(0.3, 0.55f, 0, 0.7, 0.95f, 0.1f);
                    case WEST -> new Box(0.9f, 0.55f, 0.3f, 1, 0.95f, 0.7);
                };
            }
            else {
                float x0 = 0.3f;
                float x1 = 0.7f;

                float y0 = state.get(HALF) == WallHalfProperty.CEILING ? 0.9f : 0;
                float y1 = state.get(HALF) == WallHalfProperty.CEILING ? 1 : 0.1f;

                float z0 = state.get(HALF) == WallHalfProperty.CEILING ? 0.45f : 0.55f;
                float z1 = state.get(HALF) == WallHalfProperty.CEILING ? 0.05f : 0.95f;

                float z2 = state.get(HALF) == WallHalfProperty.CEILING ? 0.55f : 0.45f;
                float z3 = state.get(HALF) == WallHalfProperty.CEILING ? 0.95f : 0.05f;

                return switch (state.get(FACING)) {
                    default -> new Box(x0, y0, z0, x1, y1, z1);
                    case EAST -> new Box(z2, y0, x0, z3, y1, x1);
                    case SOUTH -> new Box(x0, y0, z2, x1, y1, z3);
                    case WEST -> new Box(z0, y0, x0, z1, y1, x1);
                };
            }
        }
        else if(state.get(COLOR_PROPERTY()).isDoor()){
            if(state.get(HALF) == WallHalfProperty.WALL) {
                return switch (state.get(FACING)) {
                    default -> new Box(0.3, 0.3f, 0.9f, 0.7f, 0.7f, 1);
                    case EAST -> new Box(0, 0.3f, 0.3f, 0.1f, 0.7f, 0.7f);
                    case SOUTH -> new Box(0.3, 0.3f, 0, 0.7, 0.7f, 0.1f);
                    case WEST -> new Box(0.9f, 0.3f, 0.3f, 1, 0.7f, 0.7);
                };
            }
            else {
                float y0 = state.get(HALF) == WallHalfProperty.CEILING ? 0.9f : 0;
                float y1 = state.get(HALF) == WallHalfProperty.CEILING ? 1 : 0.1f;

                return new Box(0.3f, y0, 0.3f, 0.7f, y1, 0.7f);
            }
        }
        else {
            return new Box(0, 0, 0, 0, 0, 0);
        }
    }

    private void updateNeighbors(BlockState state, World world, BlockPos pos) {
        Direction direction = getDirection(state).getOpposite();
        WireOrientation wireOrientation = OrientationHelper.getEmissionOrientation(
                world, direction, direction.getAxis().isHorizontal() ? Direction.UP : state.get(FACING)
        );
        world.updateNeighborsAlways(pos, this, wireOrientation);
        world.updateNeighborsAlways(pos.offset(direction), this, wireOrientation);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(DOOR_ON, LIGHT_ON));
    }

    protected static Direction getDirection(BlockState state) {
        return switch (state.get(HALF)) {
            case CEILING -> Direction.DOWN;
            case FLOOR -> Direction.UP;
            default -> state.get(FACING);
        };
    }


    public static BlockPos[] getLightCheckPoses(BlockPos pos, Direction direction, boolean big) {
        return big ?
                new BlockPos[]{
                        pos,
                        pos.up(),
                        pos.down(),
                        pos.offset(direction.rotateYClockwise()),
                        pos.offset(direction.rotateYCounterclockwise()),
                        pos.offset(direction),
                        pos.offset(direction).up(),
                        pos.offset(direction).up().offset(direction.rotateYClockwise()),
                        pos.offset(direction).up().offset(direction.rotateYCounterclockwise()),
                        pos.offset(direction).up().up(),
                        pos.offset(direction).down(),
                        pos.offset(direction).down().offset(direction.rotateYClockwise()),
                        pos.offset(direction).down().offset(direction.rotateYCounterclockwise()),
                        pos.offset(direction).down().down(),
                        pos.offset(direction).offset(direction.rotateYClockwise()),
                        pos.offset(direction).offset(direction.rotateYClockwise()).offset(direction.rotateYClockwise()),
                        pos.offset(direction).offset(direction.rotateYCounterclockwise()),
                        pos.offset(direction).offset(direction.rotateYCounterclockwise()).offset(direction.rotateYCounterclockwise())
                }
                :
                new BlockPos[]{
                        pos,
                        pos.up(),
                        pos.down(),
                        pos.offset(direction.rotateYClockwise()),
                        pos.offset(direction.rotateYCounterclockwise())
                };
    }
}

package net.zephyr.fnafur.blocks.energy.blocks.switches.office_buttons;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
import net.minecraft.world.level.redstone.Orientation;
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
    public static final BooleanProperty DOOR_ON = BooleanProperty.create("door");
    public static final BooleanProperty LIGHT_ON = BooleanProperty.create("light");
    public OfficeButtons(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(DOOR_ON, false).setValue(LIGHT_ON, false));
    }

    @Nullable
    @Override
    public <Q extends BlockEntity> BlockEntityTicker<Q> getTicker(Level world, BlockState state, BlockEntityType<Q> type) {

        return createTickerHelper(type, BlockEntityInit.OFFICE_BUTTONS,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }
    @Override
    public boolean lockY(BlockState state) {
        return state.getValue(COLOR_PROPERTY()) == OfficeButtonsColors.DEFAULT;
    }

    @Override
    public boolean goesOnFloor(BlockItemStateProperties state) {
        ColorEnumInterface color = state.get(COLOR_PROPERTY());
        return true;
    }

    @Override
    public Class<OfficeButtonsColors> COLOR_ENUM() {
        return OfficeButtonsColors.class;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {


        BlockEntity entity = world.getBlockEntity(pos);

        if (entity != null) {

            double offsetX = ((IEntityDataSaver) entity).getPersistentData().getDouble("xOffset").orElse(0.0);
            double offsetY = ((IEntityDataSaver) entity).getPersistentData().getDouble("yOffset").orElse(0.0);
            double offsetZ = ((IEntityDataSaver) entity).getPersistentData().getDouble("zOffset").orElse(0.0);

            AABB door = new AABB(
                    getDoorHitbox(state).minX + offsetX - 0.5f,
                    getDoorHitbox(state).minY + offsetY - 0.5f,
                    getDoorHitbox(state).minZ + offsetZ - 0.5f,
                    getDoorHitbox(state).maxX + offsetX - 0.5f,
                    getDoorHitbox(state).maxY + offsetY - 0.5f,
                    getDoorHitbox(state).maxZ + offsetZ - 0.5f
            );
            AABB light = new AABB(
                    getLightHitbox(state).minX + offsetX - 0.5f,
                    getLightHitbox(state).minY + offsetY - 0.5f,
                    getLightHitbox(state).minZ + offsetZ - 0.5f,
                    getLightHitbox(state).maxX + offsetX - 0.5f,
                    getLightHitbox(state).maxY + offsetY - 0.5f,
                    getLightHitbox(state).maxZ + offsetZ - 0.5f
            );
            VoxelShape door_light = Shapes.create(door);
            door_light = Shapes.or(door_light, Shapes.create(light));
            if (!drawingOutline) return door_light;
        }

        if (state.getValue(HALF) == WallHalfProperty.WALL) {
            float y0 = state.getValue(COLOR_PROPERTY()) == OfficeButtonsColors.DEFAULT ? 0.05f : 0.3f;
            float y1 = state.getValue(COLOR_PROPERTY()) == OfficeButtonsColors.DEFAULT ? 0.95f : 0.7f;
            VoxelShape northShape = Shapes.create(new AABB(0.3f, y0, 0f, 0.7f, y1, 0.1f));
            VoxelShape eastShape = Shapes.create(new AABB(0.9f, y0, 0.3f, 1f, y1, 0.7f));
            VoxelShape southShape = Shapes.create(new AABB(0.7f, y0, 0.9f, 0.3f, y1, 1f));
            VoxelShape westShape = Shapes.create(new AABB(0f, y0, 0.3f, 0.1f, y1, 0.7f));

            return switch (state.getValue(FACING)) {
                default -> northShape;
                case SOUTH -> southShape;
                case WEST -> westShape;
                case EAST -> eastShape;
            };
        } else {
            float y0 = state.getValue(HALF) == WallHalfProperty.CEILING ? 0.9f : 0;
            float y1 = state.getValue(HALF) == WallHalfProperty.CEILING ? 1 : 0.1f;

            if (state.getValue(COLOR_PROPERTY()) == OfficeButtonsColors.DEFAULT) {

                AABB box = switch (state.getValue(FACING).getAxis()) {
                    default -> new AABB(0, 0, 0, 0, 0, 0);
                    case Direction.Axis.X -> new AABB(0.05f, y0, 0.3f, 0.95f, y1, 0.7f);
                    case Direction.Axis.Z -> new AABB(0.3f, y0, 0.05f, 0.7f, y1, 0.95f);
                };

                return Shapes.create(box);
            }
            else {
                return Shapes.box(0.3f, y0, 0.3f, 0.7f, y1, 0.7f);
            }
        }
    }

    @Override
    public List<AABB> getClickHitBoxes(BlockState state) {
        List<AABB> list = new ArrayList<>();
        list.add(getDoorHitbox(state));
        list.add(getLightHitbox(state));
        return list;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {


        checkingButtons = true;
        InteractionResult result = super.useWithoutItem(state, world, pos, player, hit);
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof OfficeButtonsBlockEntity ent) {

            int hitButton = getButton(ent, getDoorHitbox(state), getLightHitbox(state), pos, hit.getLocation());

            InteractionResult result2 = ent.tryEndLink(player, world, pos);
            if (result2 != null) return result2;

            InteractionResult result3 = ent.tryStartLink(player, pos, hitButton);
            if (result3 != null) return result3;

            ItemStack stack = player.getMainHandItem();
            if (stack != null && stack.getItem() == ItemInit.PAINTBRUSH) {
                if (state.getValue(DOOR_ON)) {
                    toggleDoor(state, world, pos, player);
                }
                if (state.getValue(LIGHT_ON)) {
                    toggleLight(state, world, pos, player);
                }
                return super.useWithoutItem(world.getBlockState(pos), world, pos, player, hit);
            }


            if (hitButton == 0) {
                this.toggleDoor(state, world, pos, null);
                return InteractionResult.SUCCESS;
            }
            if (hitButton == 1) {
                this.toggleLight(state, world, pos, null);
                return InteractionResult.SUCCESS;
            }
        }
        checkingButtons = false;
        return result;
    }

    int getButton(PropBlockEntity ent, AABB doorHitbox, AABB lightHitbox, BlockPos pos, Vec3 hitPos){
        double offsetX = ((IEntityDataSaver) ent).getPersistentData().getDouble("xOffset").orElse(0.0);
        double offsetY = ((IEntityDataSaver) ent).getPersistentData().getDouble("yOffset").orElse(0.0);
        double offsetZ = ((IEntityDataSaver) ent).getPersistentData().getDouble("zOffset").orElse(0.0);

        AABB door = new AABB(
                doorHitbox.minX + pos.getX() + offsetX - 0.5f,
                doorHitbox.minY + pos.getY() + offsetY - 0.5f,
                doorHitbox.minZ + pos.getZ() + offsetZ - 0.5f,
                doorHitbox.maxX + pos.getX() + offsetX - 0.5f,
                doorHitbox.maxY + pos.getY() + offsetY - 0.5f,
                doorHitbox.maxZ + pos.getZ() + offsetZ - 0.5f
        ).inflate(0.025f);
        AABB light = new AABB(
                lightHitbox.minX + pos.getX() + offsetX - 0.5f,
                lightHitbox.minY + pos.getY() + offsetY - 0.5f,
                lightHitbox.minZ + pos.getZ() + offsetZ - 0.5f,
                lightHitbox.maxX + pos.getX() + offsetX - 0.5f,
                lightHitbox.maxY + pos.getY() + offsetY - 0.5f,
                lightHitbox.maxZ + pos.getZ() + offsetZ - 0.5f
        ).inflate(0.025f);

        if (door.contains(hitPos)) {
            return 0;
        }
        if (light.contains(hitPos)) {
            return 1;
        }
        return -1;
    }

    public void toggleDoor(BlockState state, Level world, BlockPos pos, @Nullable Player player) {
        state = state.cycle(DOOR_ON);
        world.setBlock(pos, state, Block.UPDATE_ALL);

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
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OfficeButtonsBlockEntity(pos, state);
    }

    public void toggleLight(BlockState state, Level world, BlockPos pos, @Nullable Player player) {
        state = state.cycle(LIGHT_ON);
        world.setBlock(pos, state, Block.UPDATE_ALL);

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

    public AABB getLightHitbox(BlockState state) {
        if(state.getValue(COLOR_PROPERTY()) == OfficeButtonsColors.DEFAULT) {
            if(state.getValue(HALF) == WallHalfProperty.WALL) {
                return switch (state.getValue(FACING)) {
                    default -> new AABB(0.3, 0.125f, 0.9f, 0.7f, 0.525f, 1);
                    case EAST -> new AABB(0, 0.125f, 0.3f, 0.1f, 0.525f, 0.7f);
                    case SOUTH -> new AABB(0.3, 0.125f, 0, 0.7, 0.525f, 0.1f);
                    case WEST -> new AABB(0.9f, 0.125f, 0.3f, 1, 0.525f, 0.7);
                };
            }
            else {
                float x0 = 0.3f;
                float x1 = 0.7f;

                float y0 = state.getValue(HALF) == WallHalfProperty.CEILING ? 0.9f : 0;
                float y1 = state.getValue(HALF) == WallHalfProperty.CEILING ? 1 : 0.1f;

                float z0 = state.getValue(HALF) == WallHalfProperty.CEILING ? 0.475f : 0.125f;
                float z1 = state.getValue(HALF) == WallHalfProperty.CEILING ? 0.875f : 0.525f;

                float z2 = state.getValue(HALF) == WallHalfProperty.CEILING ? 0.125f : 0.475f;
                float z3 = state.getValue(HALF) == WallHalfProperty.CEILING ? 0.525f : 0.875f;

                return switch (state.getValue(FACING)) {
                    default -> new AABB(x0, y0, z0, x1, y1, z1);
                    case EAST -> new AABB(z2, y0, x0, z3, y1, x1);
                    case SOUTH -> new AABB(x0, y0, z2, x1, y1, z3);
                    case WEST -> new AABB(z0, y0, x0, z1, y1, x1);
                };
            }
        }
        else if(!state.getValue(COLOR_PROPERTY()).isDoor()){
            if(state.getValue(HALF) == WallHalfProperty.WALL) {
                return switch (state.getValue(FACING)) {
                    default -> new AABB(0.3, 0.3f, 0.9f, 0.7f, 0.7f, 1);
                    case EAST -> new AABB(0, 0.3f, 0.3f, 0.1f, 0.7f, 0.7f);
                    case SOUTH -> new AABB(0.3, 0.3f, 0, 0.7, 0.7f, 0.1f);
                    case WEST -> new AABB(0.9f, 0.3f, 0.3f, 1, 0.7f, 0.7);
                };
            }
            else {
                float y0 = state.getValue(HALF) == WallHalfProperty.CEILING ? 0.9f : 0;
                float y1 = state.getValue(HALF) == WallHalfProperty.CEILING ? 1 : 0.1f;

                return new AABB(0.3f, y0, 0.3f, 0.7f, y1, 0.7f);
            }
        }
        else {
            return new AABB(0, 0, 0, 0, 0, 0);
        }
    }
    public AABB getDoorHitbox(BlockState state) {
        if(state.getValue(COLOR_PROPERTY()) == OfficeButtonsColors.DEFAULT) {
            if(state.getValue(HALF) == WallHalfProperty.WALL) {
                return switch (state.getValue(FACING)) {
                    default -> new AABB(0.3, 0.55f, 0.9f, 0.7f, 0.95f, 1);
                    case EAST -> new AABB(0, 0.55f, 0.3f, 0.1f, 0.95f, 0.7f);
                    case SOUTH -> new AABB(0.3, 0.55f, 0, 0.7, 0.95f, 0.1f);
                    case WEST -> new AABB(0.9f, 0.55f, 0.3f, 1, 0.95f, 0.7);
                };
            }
            else {
                float x0 = 0.3f;
                float x1 = 0.7f;

                float y0 = state.getValue(HALF) == WallHalfProperty.CEILING ? 0.9f : 0;
                float y1 = state.getValue(HALF) == WallHalfProperty.CEILING ? 1 : 0.1f;

                float z0 = state.getValue(HALF) == WallHalfProperty.CEILING ? 0.45f : 0.55f;
                float z1 = state.getValue(HALF) == WallHalfProperty.CEILING ? 0.05f : 0.95f;

                float z2 = state.getValue(HALF) == WallHalfProperty.CEILING ? 0.55f : 0.45f;
                float z3 = state.getValue(HALF) == WallHalfProperty.CEILING ? 0.95f : 0.05f;

                return switch (state.getValue(FACING)) {
                    default -> new AABB(x0, y0, z0, x1, y1, z1);
                    case EAST -> new AABB(z2, y0, x0, z3, y1, x1);
                    case SOUTH -> new AABB(x0, y0, z2, x1, y1, z3);
                    case WEST -> new AABB(z0, y0, x0, z1, y1, x1);
                };
            }
        }
        else if(state.getValue(COLOR_PROPERTY()).isDoor()){
            if(state.getValue(HALF) == WallHalfProperty.WALL) {
                return switch (state.getValue(FACING)) {
                    default -> new AABB(0.3, 0.3f, 0.9f, 0.7f, 0.7f, 1);
                    case EAST -> new AABB(0, 0.3f, 0.3f, 0.1f, 0.7f, 0.7f);
                    case SOUTH -> new AABB(0.3, 0.3f, 0, 0.7, 0.7f, 0.1f);
                    case WEST -> new AABB(0.9f, 0.3f, 0.3f, 1, 0.7f, 0.7);
                };
            }
            else {
                float y0 = state.getValue(HALF) == WallHalfProperty.CEILING ? 0.9f : 0;
                float y1 = state.getValue(HALF) == WallHalfProperty.CEILING ? 1 : 0.1f;

                return new AABB(0.3f, y0, 0.3f, 0.7f, y1, 0.7f);
            }
        }
        else {
            return new AABB(0, 0, 0, 0, 0, 0);
        }
    }

    private void updateNeighbors(BlockState state, Level world, BlockPos pos) {
        Direction direction = getDirection(state).getOpposite();
        Orientation wireOrientation = ExperimentalRedstoneUtils.initialOrientation(
                world, direction, direction.getAxis().isHorizontal() ? Direction.UP : state.getValue(FACING)
        );
        world.updateNeighborsAt(pos, this, wireOrientation);
        world.updateNeighborsAt(pos.relative(direction), this, wireOrientation);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(DOOR_ON, LIGHT_ON));
    }

    protected static Direction getDirection(BlockState state) {
        return switch (state.getValue(HALF)) {
            case CEILING -> Direction.DOWN;
            case FLOOR -> Direction.UP;
            default -> state.getValue(FACING);
        };
    }


    public static BlockPos[] getLightCheckPoses(BlockPos pos, Direction direction, boolean big) {
        return big ?
                new BlockPos[]{
                        pos,
                        pos.above(),
                        pos.below(),
                        pos.relative(direction.getClockWise()),
                        pos.relative(direction.getCounterClockWise()),
                        pos.relative(direction),
                        pos.relative(direction).above(),
                        pos.relative(direction).above().relative(direction.getClockWise()),
                        pos.relative(direction).above().relative(direction.getCounterClockWise()),
                        pos.relative(direction).above().above(),
                        pos.relative(direction).below(),
                        pos.relative(direction).below().relative(direction.getClockWise()),
                        pos.relative(direction).below().relative(direction.getCounterClockWise()),
                        pos.relative(direction).below().below(),
                        pos.relative(direction).relative(direction.getClockWise()),
                        pos.relative(direction).relative(direction.getClockWise()).relative(direction.getClockWise()),
                        pos.relative(direction).relative(direction.getCounterClockWise()),
                        pos.relative(direction).relative(direction.getCounterClockWise()).relative(direction.getCounterClockWise())
                }
                :
                new BlockPos[]{
                        pos,
                        pos.above(),
                        pos.below(),
                        pos.relative(direction.getClockWise()),
                        pos.relative(direction.getCounterClockWise())
                };
    }
}

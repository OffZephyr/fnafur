package net.zephyr.fnafur.blocks.props.office_buttons;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.zephyr.fnafur.blocks.props.ColorEnumInterface;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.WallHalfProperty;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OfficeButtons extends WallPropBlock<OfficeButtonsColors> {

    public static boolean checkingButtons = false;
    public static final BooleanProperty DOOR_ON = BooleanProperty.of("door");
    public static final BooleanProperty LIGHT_ON = BooleanProperty.of("light");
    public static final EnumProperty<OfficeButtonsColors> COLOR = EnumProperty.of("color", OfficeButtonsColors.class);
    public OfficeButtons(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(DOOR_ON, false).with(LIGHT_ON, false));
    }

    @Override
    public boolean lockY(BlockState state) {
        return state.get(COLOR) == OfficeButtonsColors.DEFAULT;
    }

    @Override
    public boolean goesOnFloor(BlockStateComponent state) {
        ColorEnumInterface color = state.getValue(COLOR_PROPERTY());
        return true;
    }

    @Override
    public EnumProperty<OfficeButtonsColors> COLOR_PROPERTY() {
        return COLOR;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {


        BlockEntity entity = world.getBlockEntity(pos);

        if (entity != null) {

            double offsetX = ((IEntityDataSaver) entity).getPersistentData().getDouble("xOffset");
            double offsetY = ((IEntityDataSaver) entity).getPersistentData().getDouble("yOffset");
            double offsetZ = ((IEntityDataSaver) entity).getPersistentData().getDouble("zOffset");

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
            float y0 = state.get(COLOR) == OfficeButtonsColors.DEFAULT ? 0.05f : 0.3f;
            float y1 = state.get(COLOR) == OfficeButtonsColors.DEFAULT ? 0.95f : 0.7f;
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

            if (state.get(COLOR) == OfficeButtonsColors.DEFAULT) {

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
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
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
        
        ItemStack stack = player.getMainHandStack();
        if(stack != null && stack.getItem() == ItemInit.PAINTBRUSH)
            return super.onUse(state, world, pos, player, hit);

        checkingButtons = true;
        ActionResult result = super.onUse(state, world, pos, player, hit);
        HitResult hitResult = player.raycast(20.0, 0.0f, false);
        if(hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHit) {

            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof PropBlockEntity ent) {

                double offsetX = ((IEntityDataSaver) ent).getPersistentData().getDouble("xOffset");
                double offsetY = ((IEntityDataSaver) ent).getPersistentData().getDouble("yOffset");
                double offsetZ = ((IEntityDataSaver) ent).getPersistentData().getDouble("zOffset");

                Box door = new Box(
                        getDoorHitbox(state).minX + pos.getX() + offsetX - 0.5f,
                        getDoorHitbox(state).minY + pos.getY() + offsetY - 0.59f,
                        getDoorHitbox(state).minZ + pos.getZ() + offsetZ - 0.5f,
                        getDoorHitbox(state).maxX + pos.getX() + offsetX - 0.5f,
                        getDoorHitbox(state).maxY + pos.getY() + offsetY - 0.59f,
                        getDoorHitbox(state).maxZ + pos.getZ() + offsetZ - 0.5f
                ).expand(0.1f);
                Box light = new Box(
                        getLightHitbox(state).minX + pos.getX() + offsetX - 0.5f,
                        getLightHitbox(state).minY + pos.getY() + offsetY - 0.59f,
                        getLightHitbox(state).minZ + pos.getZ() + offsetZ - 0.5f,
                        getLightHitbox(state).maxX + pos.getX() + offsetX - 0.5f,
                        getLightHitbox(state).maxY + pos.getY() + offsetY - 0.59f,
                        getLightHitbox(state).maxZ + pos.getZ() + offsetZ - 0.5f
                ).expand(0.1f);

                if (door.contains(blockHit.getPos())) {
                    if (world.isClient) {
                        result = ActionResult.SUCCESS;
                    } else {
                        this.toggleDoor(state, world, pos, null);
                        result = ActionResult.CONSUME;
                    }
                }
                if (light.contains(blockHit.getPos())) {
                    if (world.isClient) {
                        result = ActionResult.SUCCESS;
                    } else {
                        this.toggleLight(state, world, pos, null);
                        result = ActionResult.CONSUME;
                    }
                }
            }
        }
        checkingButtons = false;
        return result;
    }

    public void toggleDoor(BlockState state, World world, BlockPos pos, @Nullable PlayerEntity player) {
        state = state.cycle(DOOR_ON);
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
        this.updateNeighbors(state, world, pos);
        // PLAY SOUND
        world.emitGameEvent(player, state.get(DOOR_ON) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
    }
    public void toggleLight(BlockState state, World world, BlockPos pos, @Nullable PlayerEntity player) {
        state = state.cycle(LIGHT_ON);
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
        this.updateNeighbors(state, world, pos);
        // PLAY SOUND
        world.emitGameEvent(player, state.get(LIGHT_ON) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
    }

    public Box getLightHitbox(BlockState state) {
        if(state.get(COLOR) == OfficeButtonsColors.DEFAULT) {
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
        else if(!state.get(COLOR).isDoor()){
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
        if(state.get(COLOR) == OfficeButtonsColors.DEFAULT) {
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
        else if(state.get(COLOR).isDoor()){
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

    @Override
    public boolean isTransparent(BlockState state) { return true;}

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    private void updateNeighbors(BlockState state, World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
        world.updateNeighborsAlways(pos.offset(getDirection(state).getOpposite()), this);
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
}

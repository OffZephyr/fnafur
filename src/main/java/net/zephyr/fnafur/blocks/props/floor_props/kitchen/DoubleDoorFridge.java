package net.zephyr.fnafur.blocks.props.floor_props.kitchen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.RawAnimation;

public class DoubleDoorFridge extends FloorPropBlock<DefaultPropColorEnum> implements GeoPropBlock {
    private Identifier texture;
    private Identifier model;
    private Identifier animations;
    public static final BooleanProperty OPEN = BooleanProperty.of("open");
    public DoubleDoorFridge(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(OPEN, true));
    }

    @Override
    public @Nullable BlockEntityTicker<GeoPropBlockEntity> getTicker(World world, BlockState state, BlockEntityType type) {
        return validateTicker(type, BlockEntityInit.GEO_PROPS,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }
    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GeoPropBlockEntity(pos, state, this);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(-0.32f, 0f, 0f, 1.32f, 2.0f, 1.0f)));
        return drawingOutline ? shape : VoxelShapes.fullCube();
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(OPEN, false);
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        world.setBlockState(pos, state.cycle(OPEN));
        super.onUse(state, world, pos, player, hit);
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean rotates() {
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(OPEN));
    }

    @Override
    public void setModelInfo(Identifier texture, Identifier model, Identifier animations) {
        this.model = model;
        this.texture = texture;
        this.animations = animations;
    }

    @Override
    public Identifier getTexture(BlockState state, BlockPos pos) {
        return this.texture;
    }

    @Override
    public Identifier getModel(BlockState state, BlockPos pos) {
        return this.model;
    }

    @Override
    public Identifier getAnimations(BlockState state, BlockPos pos) {
        return this.animations;
    }

    @Override
    public RenderLayer getRenderType(BlockState state, BlockPos pos) {
        return RenderLayer.getEntityCutout(getTexture(state, pos));
    }

    @Override
    public RawAnimation getCurrentAnimation(BlockState state, BlockPos pos) {
        if(state.contains(OPEN)){
            String name = state.get(OPEN) ? "animation.double_door_fridge.open" : "animation.double_door_fridge.close";
            return RawAnimation.begin().thenPlay(name);
        }
        return RawAnimation.begin().thenPlay("animation.double_door_fridge.close");
    }
}

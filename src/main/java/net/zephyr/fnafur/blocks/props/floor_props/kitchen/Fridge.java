package net.zephyr.fnafur.blocks.props.floor_props.kitchen;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import org.jetbrains.annotations.Nullable;
import com.geckolib.animation.RawAnimation;

public class Fridge extends FloorPropBlock<DefaultPropColorEnum> implements GeoPropBlock {
    private Identifier texture;
    private Identifier model;
    private Identifier animations;
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public Fridge(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(OPEN, true));
    }

    @Override
    public @Nullable BlockEntityTicker<GeoPropBlockEntity> getTicker(Level world, BlockState state, BlockEntityType type) {
        return createTickerHelper(type, BlockEntityInit.GEO_PROPS,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }
    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GeoPropBlockEntity(pos, state, this);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.create(new AABB(0, 0f, 0f, 1, 2.0f, 1.0f)));
        return drawingOutline ? shape : Shapes.block();
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(OPEN, false);
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        world.setBlockAndUpdate(pos, state.cycle(OPEN));
        super.useWithoutItem(state, world, pos, player, hit);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean rotates() {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(OPEN));
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
    public RenderType getRenderType(BlockState state, BlockPos pos) {
        return RenderTypes.entityCutout(getTexture(state, pos));
    }

    @Override
    public RawAnimation getCurrentAnimation(BlockState state, BlockPos pos) {
        if(state.hasProperty(OPEN)){
            String name = state.getValue(OPEN) ? "animation.fridge.open" : "animation.fridge.close";
            return RawAnimation.begin().thenPlay(name);
        }
        return RawAnimation.begin().thenPlay("animation.fridge.close");
    }
}

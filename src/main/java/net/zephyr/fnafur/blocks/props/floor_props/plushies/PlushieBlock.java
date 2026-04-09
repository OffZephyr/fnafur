package net.zephyr.fnafur.blocks.props.floor_props.plushies;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.RawAnimation;

public class PlushieBlock extends FloorPropBlock<PlushieColorEnum> implements GeoPropBlock {
    private Identifier texture;
    private Identifier model;
    private Identifier animations;
    public PlushieBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GeoPropBlockEntity(pos, state, this);
    }

    @Override
    public @Nullable BlockEntityTicker<GeoPropBlockEntity> getTicker(Level world, BlockState state, BlockEntityType type) {
        return createTickerHelper(type, BlockEntityInit.GEO_PROPS,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.create(new AABB(0f, 0, 0f, 1f, 1f, 1f)));
        return drawingOutline ? shape : Shapes.block();
    }

    @Override
    public Class COLOR_ENUM() {
        return PlushieColorEnum.class;
    }

    @Override
    public boolean rotates() {
        return false;
    }

    @Override
    public void setModelInfo(Identifier texture, Identifier model, Identifier animations) {
        this.model = model;
        this.texture = texture;
        this.animations = animations;
    }

    @Override
    public Identifier getTexture(BlockState state, BlockPos pos) {

        texture = switch (state.getValue(COLOR_PROPERTY())){
            case FREDDY -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/freddy_plush.png");
            case BONNIE -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/bonnie_plush.png");
            case CHICA -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/chica_plush.png");
            case FOXY -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/foxy_plush.png");
            case GOLDEN_FREDDY -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/golden_freddy_plush.png");
            case FREDBEAR -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/fredbear_plush.png");
            case HAUNTED_FREDBEAR -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/haunted_fredbear_plush.png");
            case SPRING_BONNIE -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/spring_bonnie_plush.png");
        };

        return this.texture;
    }

    @Override
    public Identifier getModel(BlockState state, BlockPos pos) {

        model = switch (state.getValue(COLOR_PROPERTY())){
            case FREDDY -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/plushies/freddy_plush");
            case BONNIE,SPRING_BONNIE -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/plushies/bonnie_plush");
            case CHICA -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/plushies/chica_plush");
            case FOXY -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/plushies/foxy_plush");
            case GOLDEN_FREDDY -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/plushies/golden_freddy_plush");
            case FREDBEAR,HAUNTED_FREDBEAR -> Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/plushies/fredbear_plush");
        };

        return this.model;
    }

    @Override
    public Identifier getAnimations(BlockState state, BlockPos pos) {
        return this.animations;
    }

    @Override
    public RawAnimation getCurrentAnimation(BlockState state, BlockPos pos) {
        return null;
    }

}

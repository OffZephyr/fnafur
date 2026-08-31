package net.zephyr.fnafur.blocks.props.floor_props.chairs;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.special.SeatBlock;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import com.geckolib.animation.RawAnimation;

public class StarPlasticChair extends FloorPropBlock<DefaultPropColorEnum> implements GeoPropBlock, SeatBlock {
    private Identifier texture;
    private Identifier model;
    private Identifier animations;
    public StarPlasticChair(Properties settings) {
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
        shape = Shapes.block();
        return drawingOutline ? shape : Shapes.block();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if(!isUsed(world, pos) && player.getMainHandItem().isEmpty()){
            if(player.position().distanceTo(hit.getLocation()) < 1.25f) {
                player.startRiding(sit(player, pos));
                return InteractionResult.SUCCESS;
            }
        }

        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public Class COLOR_ENUM() { return null;
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
    public RawAnimation getCurrentAnimation(BlockState state, BlockPos pos) {
        return null;
    }

    @Override
    public float getSittingOffset(Level world, BlockPos pos) {
        return 0.1f;
    }

    @Override
    public float getSittingHeight(Level world, BlockPos pos) {
        return 0;
    }
}

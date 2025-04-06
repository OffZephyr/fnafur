package net.zephyr.fnafur.blocks.props.floor_props.chairs;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.special.SeatBlock;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.RawAnimation;

public class StarPlasticChair extends FloorPropBlock<DefaultPropColorEnum> implements GeoPropBlock, SeatBlock {
    private Identifier texture;
    private Identifier model;
    private Identifier animations;
    public StarPlasticChair(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GeoPropBlockEntity(pos, state, this);
    }

    @Override
    public @Nullable BlockEntityTicker<GeoPropBlockEntity> getTicker(World world, BlockState state, BlockEntityType type) {
        return validateTicker(type, BlockEntityInit.GEO_PROPS,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.fullCube();
        return drawingOutline ? shape : VoxelShapes.fullCube();
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(!isUsed(world, pos) && player.getMainHandStack().isEmpty()){
            player.startRiding(sit(player, pos));
            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    public Class COLOR_ENUM() {
        return DefaultPropColorEnum.class;
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
    public Identifier getTexture() {
        return this.texture;
    }

    @Override
    public Identifier getModel() {
        return this.model;
    }

    @Override
    public Identifier getAnimations() {
        return this.animations;
    }

    @Override
    public RawAnimation getCurrentAnimation(BlockState state, BlockPos pos) {
        return null;
    }

    @Override
    public float getSittingOffset(World world, BlockPos pos) {
        return 0;
    }

    @Override
    public float getSittingHeight(World world, BlockPos pos) {
        return 0;
    }
}

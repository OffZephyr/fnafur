package net.zephyr.fnafur.blocks.props.base.geo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GeoPropBlockEntity extends PropBlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public GeoPropBlock block;
    public boolean item = false;
    public GeoPropBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.GEO_PROPS, pos, state);
    }
    public GeoPropBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public GeoPropBlockEntity(BlockPos pos, BlockState state, GeoPropBlock block) {
        this(pos, state);
        this.block = block;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 2, this::mainController));
    }

    private PlayState mainController(AnimationState<GeoPropBlockEntity> geoPropBlockEntityAnimationState) {
        BlockState state = getWorld().getBlockState(getPos());

        if(block.getCurrentAnimation(state, getPos()) != null){
            state = state.getBlock() instanceof GeoPropBlock ? state : ((Block)block).getDefaultState();

            return geoPropBlockEntityAnimationState.setAndContinue(block.getCurrentAnimation(state, getPos()));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    public Identifier getTexture(World world){
        BlockState state = world.getBlockState(getPos()).isOf(this.getCachedState().getBlock()) ? world.getBlockState(getPos()) : getCachedState();
        if(block != null) return block.getTexture(state, getPos());
        return ((GeoPropBlock)world.getBlockState(getPos()).getBlock()).getTexture(state, getPos());
    }
    public Identifier getReRenderTexture(World world){
        return getTexture(world);
    }
    public Identifier getModel(World world){
        BlockState state = world.getBlockState(getPos()).isOf(this.getCachedState().getBlock()) ? world.getBlockState(getPos()) : getCachedState();
        if(block != null) return block.getModel(state, getPos());
        return ((GeoPropBlock)world.getBlockState(getPos()).getBlock()).getModel(state, getPos());
    }
    public Identifier getReRenderModel(World world){
        return getModel(world);
    }
    public Identifier getAnimations(World world){
        BlockState state = world.getBlockState(getPos()).isOf(this.getCachedState().getBlock()) ? world.getBlockState(getPos()) : getCachedState();
        if(block != null) return block.getAnimations(state, getPos());
        return ((GeoPropBlock)world.getBlockState(getPos()).getBlock()).getAnimations(state, getPos());
    }
    public RenderLayer getRenderType(){
        BlockState state = world.getBlockState(getPos()).isOf(this.getCachedState().getBlock()) ? world.getBlockState(getPos()) : getCachedState();
        if(block != null) return block.getRenderType(state, getPos());
        return null;
    }
}

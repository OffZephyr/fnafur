package net.zephyr.fnafur.blocks.props.base.geo;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.state.AnimationTest;
import software.bernie.geckolib.animation.object.PlayState;
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
        controllers.add(new AnimationController<>("main", 2, this::mainController));
    }

    private PlayState mainController(AnimationTest<GeoAnimatable> geoAnimatableAnimationTest) {
        BlockState state = getLevel().getBlockState(getBlockPos());

        if(block.getCurrentAnimation(state, getBlockPos()) != null){
            state = state.getBlock() instanceof GeoPropBlock ? state : ((Block)block).defaultBlockState();

            return geoAnimatableAnimationTest.setAndContinue(block.getCurrentAnimation(state, getBlockPos()));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    public Identifier getTexture(Level world){
        BlockState state = world.getBlockState(getBlockPos()).is(this.getBlockState().getBlock()) ? world.getBlockState(getBlockPos()) : getBlockState();
        if(block != null) return block.getTexture(state, getBlockPos());
        return ((GeoPropBlock)world.getBlockState(getBlockPos()).getBlock()).getTexture(state, getBlockPos());
    }
    public Identifier getReRenderTexture(Level world){
        return getTexture(world);
    }
    public Identifier getModel(Level world){
        BlockState state = world.getBlockState(getBlockPos()).is(this.getBlockState().getBlock()) ? world.getBlockState(getBlockPos()) : getBlockState();
        if(block != null) return block.getModel(state, getBlockPos());
        return ((GeoPropBlock)world.getBlockState(getBlockPos()).getBlock()).getModel(state, getBlockPos());
    }
    public Identifier getReRenderModel(Level world){
        return getModel(world);
    }
    public Identifier getAnimations(Level world){
        BlockState state = world.getBlockState(getBlockPos()).is(this.getBlockState().getBlock()) ? world.getBlockState(getBlockPos()) : getBlockState();
        if(block != null) return block.getAnimations(state, getBlockPos());
        return ((GeoPropBlock)world.getBlockState(getBlockPos()).getBlock()).getAnimations(state, getBlockPos());
    }
    public RenderType getRenderType(){
        BlockState state = level.getBlockState(getBlockPos()).is(this.getBlockState().getBlock()) ? level.getBlockState(getBlockPos()) : getBlockState();
        if(block != null) return block.getRenderType(state, getBlockPos());
        return null;
    }
}

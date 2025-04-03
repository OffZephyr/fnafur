package net.zephyr.fnafur.blocks.props.base.geo;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtS2CGetFromClientPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
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

    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    public Identifier getTexture(World world){
        if(block != null) return block.getTexture();
        return ((GeoPropBlock)world.getBlockState(getPos()).getBlock()).getTexture();
    }
    public Identifier getReRenderTexture(World world){
        return getTexture(world);
    }
    public Identifier getModel(World world){
        if(block != null) return block.getModel();
        return ((GeoPropBlock)world.getBlockState(getPos()).getBlock()).getModel();
    }
    public Identifier getReRenderModel(World world){
        return getModel(world);
    }
    public Identifier getAnimations(World world){
        if(block != null) return block.getAnimations();
        return ((GeoPropBlock)world.getBlockState(getPos()).getBlock()).getAnimations();
    }
}

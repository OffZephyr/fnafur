package net.zephyr.fnafur.blocks.utility_blocks.cosmo_gift;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IUniverseRenderLayers;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;
import java.util.Map;

public class CosmoGift extends FloorPropBlock<DefaultPropColorEnum> implements GeoPropBlock {
    private Identifier texture;
    private Identifier model;
    private Identifier animations;

    public CosmoGift(Settings settings) {
        super(settings);
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
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GalaxyLayerGeoPropEntity(pos, state, this);
    }

    @Override
    public @Nullable BlockEntityTicker<GeoPropBlockEntity> getTicker(World world, BlockState state, BlockEntityType type) {
        return validateTicker(type, BlockEntityInit.GALAXY_GEO_PROPS,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
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
}

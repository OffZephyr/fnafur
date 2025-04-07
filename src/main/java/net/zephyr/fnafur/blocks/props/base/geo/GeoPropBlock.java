package net.zephyr.fnafur.blocks.props.base.geo;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;
import java.util.Map;

public interface GeoPropBlock {
    public void setModelInfo(Identifier texture, Identifier model, Identifier animations);
    public Identifier getTexture(BlockState state, BlockPos pos);
    public Identifier getModel(BlockState state, BlockPos pos);
    public Identifier getAnimations(BlockState state, BlockPos pos);
    default RenderLayer getRenderType(BlockState state, BlockPos pos){
        return null;
    }
    public RawAnimation getCurrentAnimation(BlockState state, BlockPos pos);
}

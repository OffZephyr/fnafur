package net.zephyr.fnafur.blocks.props.base.geo;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import com.geckolib.animation.RawAnimation;
import com.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;
import java.util.Map;

public interface GeoPropBlock {
    public void setModelInfo(Identifier texture, Identifier model, Identifier animations);
    public Identifier getTexture(BlockState state, BlockPos pos);
    public Identifier getModel(BlockState state, BlockPos pos);
    public Identifier getAnimations(BlockState state, BlockPos pos);
    default RenderType getRenderType(BlockState state, BlockPos pos){
        return null;
    }
    public RawAnimation getCurrentAnimation(BlockState state, BlockPos pos);
}

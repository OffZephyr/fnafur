package net.zephyr.fnafur.blocks.props.wall_props.clocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.client.gui.TabOverlayClass;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import com.geckolib.animatable.GeoBlockEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.*;
import com.geckolib.constant.DataTickets;
import com.geckolib.util.GeckoLibUtil;

public class GeoClockPropBlockEntity extends GeoPropBlockEntity implements GeoBlockEntity {
    public float deltaHour = 0;
    public float deltaMinute = 0;
    public GeoClockPropBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.GEO_CLOCK_PROP, pos, state);
    }
    public GeoClockPropBlockEntity(BlockPos pos, BlockState state, GeoPropBlock block) {
        this(pos, state);
        this.block = block;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public void tick(Level world, BlockPos blockPos, BlockState state, PropBlockEntity entity) {
        long dayTime = (world.getOverworldClockTime());
        double currentDay = dayTime / 24000d;

        float minute = ((dayTime / 1000f) * 60f);
        float hour = minute / 60f;

        deltaMinute = minute / 60f;
        deltaHour = ((hour + 12) / 12f);

        super.tick(world, blockPos, state, entity);
    }
}

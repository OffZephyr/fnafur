package net.zephyr.fnafur.blocks.props.wall_props.clocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.client.gui.TabOverlayClass;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

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
    public void tick(World world, BlockPos blockPos, BlockState state, PropBlockEntity entity) {
        long dayTime = (world.getTimeOfDay());
        double currentDay = dayTime / 24000d;

        long minute = (long) ((dayTime / 1000f) * 60);
        long hour = minute / 60;
        minute = minute - (60*hour);

        deltaMinute = minute / 60f;
        deltaHour = ((hour + 6) / 12f) - (int)currentDay;

        super.tick(world, blockPos, state, entity);
    }
}

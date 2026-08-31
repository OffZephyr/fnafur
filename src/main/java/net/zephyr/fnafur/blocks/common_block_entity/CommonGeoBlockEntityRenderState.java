package net.zephyr.fnafur.blocks.common_block_entity;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.base.GeoRenderState;

import java.util.Map;

public class CommonGeoBlockEntityRenderState extends BlockEntityRenderState implements GeoRenderState {
    public CompoundTag nbt;
    public Direction facing;

    @Override
    public <D> void addGeckolibData(DataTicket<D> dataTicket, @Nullable D data) {

    }

    @Override
    public boolean hasGeckolibData(DataTicket<?> dataTicket) {
        return false;
    }

    @Override
    public <D> @Nullable D getGeckolibData(DataTicket<D> dataTicket) {
        return null;
    }

    @Override
    public Map<DataTicket<?>, Object> getDataMap() {
        return Map.of();
    }
}

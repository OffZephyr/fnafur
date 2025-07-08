package net.zephyr.fnafur.mixin;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.zephyr.fnafur.util.mixinAccessing.ILivingEntityMaskRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements ILivingEntityMaskRenderState {
    @Unique
    public final ItemRenderState maskItemRenderState = new ItemRenderState();

    @Override
    public ItemRenderState getState() {
        return maskItemRenderState;
    }
}

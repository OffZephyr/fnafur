package net.zephyr.fnafur.mixin;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.zephyr.fnafur.util.mixinAccessing.ILivingEntityMaskRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements ILivingEntityMaskRenderState {
    @Unique
    public final ItemStackRenderState maskItemRenderState = new ItemStackRenderState();

    @Override
    public ItemStackRenderState getState() {
        return maskItemRenderState;
    }
}

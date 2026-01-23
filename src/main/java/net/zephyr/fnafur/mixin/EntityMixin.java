package net.zephyr.fnafur.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.zephyr.fnafur.util.mixinAccessing.IEntityPathfindingHeightOverride;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements IEntityPathfindingHeightOverride {

    @Shadow
    private EntityDimensions dimensions;

    @Inject(method = "getHeight", at = @At("HEAD"), cancellable = true)
    void getHeight(CallbackInfoReturnable<Float> cir){
        cir.setReturnValue(getPathfindingHeightOverride());
    }

    @Inject(method = "getWidth", at = @At("HEAD"), cancellable = true)
    void getWidth(CallbackInfoReturnable<Float> cir){
        cir.setReturnValue(getPathfindingWidthOverride());
    }

    @Override
    public float getPathfindingHeightOverride() {
        return this.dimensions.height();
    }

    @Override
    public float getPathfindingWidthOverride() {
        return this.dimensions.width();
    }
}

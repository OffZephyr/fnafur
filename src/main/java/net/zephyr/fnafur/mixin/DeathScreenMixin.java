package net.zephyr.fnafur.mixin;

import net.minecraft.client.gui.screens.DeathScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void customDeathScreen(CallbackInfo ci) {
        /*if(Minecraft.getInstance().player.getLastAttacker() instanceof FnafUniverseRebuiltEntity entity && entity.hasJumpScare()) {
            CompoundTag deathNbt = new CompoundTag();
            deathNbt.putBoolean("isHardcore", Minecraft.getInstance().level.getLevelProperties().isHardcore());
            String index = entity.killScreenID;

            ClientHook.openScreen(index, deathNbt, entity.getId());
        }*/
        //if(Minecraft.getInstance().level.getEntityById(((IEntityDataSaver)Minecraft.getInstance().player).getPersistentData().getInt("JumpscareID")) instanceof DefaultEntity entity && entity.hasJumpScare()) {
        //CompoundTag deathNbt = new CompoundTag();
        //deathNbt.putBoolean("isHardcore", Minecraft.getInstance().level.getLevelProperties().isHardcore());
        //String index = entity.killScreenID;

        //GoopyNetworkingUtils.setClientScreen(index, deathNbt, entity.getId());
        //}
        // TODO Death Screen

    }
}

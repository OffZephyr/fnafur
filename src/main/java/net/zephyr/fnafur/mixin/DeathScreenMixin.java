package net.zephyr.fnafur.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.nbt.NbtCompound;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void customDeathScreen(CallbackInfo ci) {
        /*if(MinecraftClient.getInstance().player.getLastAttacker() instanceof FnafUniverseRebuiltEntity entity && entity.hasJumpScare()) {
            NbtCompound deathNbt = new NbtCompound();
            deathNbt.putBoolean("isHardcore", MinecraftClient.getInstance().world.getLevelProperties().isHardcore());
            String index = entity.killScreenID;

            ClientHook.openScreen(index, deathNbt, entity.getId());
        }*/
        if(MinecraftClient.getInstance().world.getEntityById(((IEntityDataSaver)MinecraftClient.getInstance().player).getPersistentData().getInt("JumpscareID")) instanceof DefaultEntity entity && entity.hasJumpScare()) {
        NbtCompound deathNbt = new NbtCompound();
        deathNbt.putBoolean("isHardcore", MinecraftClient.getInstance().world.getLevelProperties().isHardcore());
        String index = entity.killScreenID;

        GoopyNetworkingUtils.setClientScreen(index, deathNbt, entity.getId());
        }

    }
}

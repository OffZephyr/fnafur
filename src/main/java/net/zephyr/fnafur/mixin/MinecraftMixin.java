package net.zephyr.fnafur.mixin;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.zephyr.fnafur.init.block_init.Palettes.PaletteManager;
import net.zephyr.fnafur.init.block_init.Palettes.PaletteTextureReloader;
import net.zephyr.fnafur.util.hooks.JoinHook;
import net.zephyr.fnafur.util.jsonReaders.animatronics.AnimatronicDataManager;
import net.zephyr.fnafur.util.jsonReaders.credits.CreditsDataManager;
import net.zephyr.fnafur.util.mixinAccessing.IGetClientManagers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

/**
 * When drawing a mirror, always use the mirror's framebuffer instead of the normal one.
 */
@Mixin(Minecraft.class)
public class MinecraftMixin implements IGetClientManagers {
	@Shadow
	private long clientStartTimeMs;
	@Shadow
    ReloadableResourceManager resourceManager;
	@Shadow
	public Screen screen;

	@Shadow
	CompletableFuture<Void> reloadResourcePacks() {
		return null;
	}
	@Unique
	private PaletteTextureReloader paletteManager = new PaletteTextureReloader();
	@Unique
	private AnimatronicDataManager animatronicDataManager = new AnimatronicDataManager();
	@Unique
	private CreditsDataManager creditsDataManager = new CreditsDataManager();

	@Inject(method = "setLevel", at = @At("HEAD"), cancellable = true)
	void joinWorld(ClientLevel world, CallbackInfo ci){
		JoinHook.joinHook(world);
	}
	@Inject(method = "getMainRenderTarget", at = @At("HEAD"), cancellable = true)
	public void getFramebuffer(CallbackInfoReturnable<RenderTarget> cir) {
		//if (CameraRenderer.isDrawing()) {
		//	var framebuffer = CameraRenderer.getFramebuffer();
		//	if (framebuffer != null) {
		//		cir.setReturnValue(framebuffer);
		//	}
		//}
		//TODO cam rendering
	}
	@Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
	public void setScreen(Screen screen, CallbackInfo cir) {
		Minecraft client = ((Minecraft) (Object) this);

		LocalPlayer player = client.player;
		//if(player != null) {
		//	Entity entity = client.world.getEntityById(((IEntityDataSaver) player).getPersistentData().getInt("JumpscareID"));

		//	if (entity instanceof DefaultEntity ent &&
		//			ent.hasJumpScare()) {
		//		if (Minecraft.getInstance().currentScreen instanceof GoopyScreen && screen != null) {
		//			cir.cancel();
		//		}
		//	}
		//}
		// TODO Jumpscare Screen
    }

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;reload()V", shift = At.Shift.BEFORE))
	public void reloaders(CallbackInfo ci) {
		this.resourceManager.registerReloadListener(this.paletteManager);
		this.resourceManager.registerReloadListener(this.animatronicDataManager);
		this.resourceManager.registerReloadListener(this.creditsDataManager);
	}

	@Inject(method = "getCameraEntity", at = @At("HEAD"), cancellable = true)
	public void getCameraEntity(CallbackInfoReturnable<Entity> cir) {

	}

	@Override
	public long getStartTime() {
		return clientStartTimeMs;
	}
}

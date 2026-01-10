package net.zephyr.fnafur.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ReloadableResourceManagerImpl;
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
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements IGetClientManagers {
	@Shadow
	private long startTime;
	@Shadow
	ReloadableResourceManagerImpl resourceManager;
	@Shadow
	public Screen currentScreen;

	@Shadow
	CompletableFuture<Void> reloadResources() {
		return null;
	}
	@Unique
	private PaletteTextureReloader paletteManager = new PaletteTextureReloader();
	@Unique
	private AnimatronicDataManager animatronicDataManager = new AnimatronicDataManager();
	@Unique
	private CreditsDataManager creditsDataManager = new CreditsDataManager();

	@Inject(method = "joinWorld", at = @At("HEAD"), cancellable = true)
	void joinWorld(ClientWorld world, CallbackInfo ci){
		JoinHook.joinHook(world);
	}
	@Inject(method = "getFramebuffer", at = @At("HEAD"), cancellable = true)
	public void getFramebuffer(CallbackInfoReturnable<Framebuffer> cir) {
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
		MinecraftClient client = ((MinecraftClient) (Object) this);

		ClientPlayerEntity player = client.player;
		//if(player != null) {
		//	Entity entity = client.world.getEntityById(((IEntityDataSaver) player).getPersistentData().getInt("JumpscareID"));

		//	if (entity instanceof DefaultEntity ent &&
		//			ent.hasJumpScare()) {
		//		if (MinecraftClient.getInstance().currentScreen instanceof GoopyScreen && screen != null) {
		//			cir.cancel();
		//		}
		//	}
		//}
		// TODO Jumpscare Screen
    }

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackManager;scanPacks()V", shift = At.Shift.BEFORE))
	public void reloaders(CallbackInfo ci) {
		this.resourceManager.registerReloader(this.paletteManager);
		this.resourceManager.registerReloader(this.animatronicDataManager);
		this.resourceManager.registerReloader(this.creditsDataManager);
	}

	@Inject(method = "getCameraEntity", at = @At("HEAD"), cancellable = true)
	public void getCameraEntity(CallbackInfoReturnable<Entity> cir) {

	}

	@Override
	public long getStartTime() {
		return startTime;
	}
}

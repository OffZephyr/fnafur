package net.zephyr.fnafur.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.zephyr.fnafur.blocks.camera_desk.CameraRenderer;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.util.jsonReaders.character_models.CharacterModelManager;
import net.zephyr.fnafur.util.jsonReaders.entity_skins.EntityDataManager;
import net.zephyr.fnafur.util.jsonReaders.layered_block.LayeredBlockManager;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
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
	ReloadableResourceManagerImpl resourceManager;
	@Shadow
	public Screen currentScreen;

	@Shadow
	CompletableFuture<Void> reloadResources() {
		return null;
	}

	private LayeredBlockManager layerManager = new LayeredBlockManager();
	private EntityDataManager entityDataManager = new EntityDataManager();
	@Unique
	private CharacterModelManager characterModelManager = new CharacterModelManager();

	@Inject(method = "getFramebuffer", at = @At("HEAD"), cancellable = true)
	public void getFramebuffer(CallbackInfoReturnable<Framebuffer> cir) {
		if (CameraRenderer.isDrawing()) {
			var framebuffer = CameraRenderer.getFramebuffer();
			if (framebuffer != null) {
				cir.setReturnValue(framebuffer);
			}
		}
	}
	@Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
	public void setScreen(Screen screen, CallbackInfo cir) {
		MinecraftClient client = ((MinecraftClient) (Object) this);

		ClientPlayerEntity player = client.player;
		if(player != null) {
			Entity entity = client.world.getEntityById(((IEntityDataSaver) player).getPersistentData().getInt("JumpscareID"));

			if (entity instanceof DefaultEntity ent &&
					ent.hasJumpScare()) {
				if (MinecraftClient.getInstance().currentScreen instanceof GoopyScreen && screen != null) {
					cir.cancel();
				}
			}
		}
    }

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackManager;scanPacks()V"))
	public void reloaders(CallbackInfo ci) {
		this.resourceManager.registerReloader(this.layerManager);
		this.resourceManager.registerReloader(this.entityDataManager);
		this.resourceManager.registerReloader(this.characterModelManager);
	}

	@Inject(method = "getCameraEntity", at = @At("HEAD"), cancellable = true)
	public void getCameraEntity(CallbackInfoReturnable<Entity> cir) {

	}
	@Override
	public LayeredBlockManager getLayerManager() {
		return this.layerManager;
	}

	@Override
	public EntityDataManager getEntityDataManager() {
		return this.entityDataManager;
	}
}

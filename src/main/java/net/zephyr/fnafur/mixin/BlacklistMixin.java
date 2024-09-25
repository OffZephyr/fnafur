package net.zephyr.fnafur.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.QuickPlay;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.client.gui.screens.BlacklistScreen;
import net.zephyr.fnafur.util.GoopyBlacklist;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Mixin(TitleScreen.class)
public class BlacklistMixin {
	boolean bl = false;
	@Inject(at = @At("HEAD"), method = "init", cancellable = true)
	private void init(CallbackInfo info) {

		String UUID = MinecraftClient.getInstance().getSession().getUuidOrNull().toString();
		System.out.println(UUID);
		String Username = MinecraftClient.getInstance().getSession().getUsername();
		boolean BlacklistedName = GoopyBlacklist.getBlacklist().containsKey(Username);
		boolean BlacklistedUUID = GoopyBlacklist.getBlacklist().containsValue(UUID);
		boolean WhitelistedName = GoopyBlacklist.getWhitelist().containsKey(Username);
		boolean WhitelistedUUID = GoopyBlacklist.getWhitelist().containsValue(UUID);
		if((BlacklistedName || BlacklistedUUID) && !(WhitelistedName || WhitelistedUUID)){

			MinecraftClient.getInstance().setOverlay((Overlay)null);
			bl = true;
			FnafUniverseResuited.LOGGER.info("UH OH! Seems like you were Blacklisted.");
			MinecraftClient.getInstance().setScreen(new BlacklistScreen());
			//info.setReturnValue("test");
			info.cancel();
		}
	}
	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void render(CallbackInfo info) {
		if(bl){
			info.cancel();
		}
	}
}
package net.zephyr.fnafur;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.zephyr.fnafur.init.*;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.block_init.ModelLoading;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;

public class FnafUniverseResuitedClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		net.zephyr.fnafur.blocks.utility_blocks.computer.ComputerData.addInitializer(new DefaultComputerInit());

		ModelLoadingPlugin.register(new ModelLoading());
		net.zephyr.fnafur.blocks.utility_blocks.computer.ComputerData.runInitializersClient();
		ItemInit.clientRegisterItem();
		ScreensInit.init();
		BlockInit.registerBlocksOnClient();
		EntityInit.registerEntitiesOnClient();

		net.zephyr.fnafur.util.KeyInputHandler.register();

		ParticlesInit.registerParticlesClient();

		NetworkingInit.registerClientReceivers();
		net.zephyr.fnafur.networking.PayloadDef.registerS2CPackets();
		HudRenderCallback.EVENT.register(new net.zephyr.fnafur.client.gui.TabOverlayClass());
		FnafUniverseResuited.LOGGER.info("Client Initialized.");
	}
}
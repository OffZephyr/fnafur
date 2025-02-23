package net.zephyr.fnafur;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.EntityType;
import net.zephyr.fnafur.blocks.utility_blocks.computer.ComputerData;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.init.*;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.block_init.GeoBlockEntityInit;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.init.item_init.ItemGroupsInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.networking.PayloadDef;
import net.zephyr.fnafur.util.commands.Bear5Command;
import net.zephyr.fnafur.util.commands.MoneyCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FnafUniverseResuited implements ModInitializer {

	public static final Map<EntityType<? extends DefaultEntity>, EntityRendererFactory<?>> RENDERER_FACTORIES = new Object2ObjectOpenHashMap<>();
	public static final Map<EntityType<? extends AnimatronicEntity>, EntityRendererFactory<?>> RENDER_FACTORIES = new Object2ObjectOpenHashMap<>();

	public static final String MOD_ID = "fnafur";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		EntityInit.registerEntities();
		BlockEntityInit.registerBlockEntities();
		GeoBlockEntityInit.registerBlockEntities();
		ItemGroupsInit.registerItemGroups();
		SoundsInit.registerSounds();
		ComputerData.runInitializers();
		ItemInit.registerItems();
		ParticlesInit.registerParticles();


		registerCommands();
		NetworkingInit.registerPayloads();
		NetworkingInit.registerServerReceivers();
		PayloadDef.registerC2SPackets();
		LOGGER.info("The GOOP is in the bag.");
	}

	public void registerCommands() {
		CommandRegistrationCallback.EVENT.register(MoneyCommand::register);
		CommandRegistrationCallback.EVENT.register(Bear5Command::register);
	}
}
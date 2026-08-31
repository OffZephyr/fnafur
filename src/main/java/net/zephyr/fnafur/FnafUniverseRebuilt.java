package net.zephyr.fnafur;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.server.level.ServerPlayer;
import net.zephyr.fnafur.rendering.decals.DecalWorldState;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.init.NetworkingInit;
import net.zephyr.fnafur.init.ParticlesInit;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.block_init.GeoBlockEntityInit;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.init.item_init.ItemCategoriesInit;
import net.zephyr.fnafur.init.item_init.ItemGroupsInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.networking.PayloadDef;
import net.zephyr.fnafur.networking.block.FetchAllDecalsS2CPayload;
import net.zephyr.fnafur.util.commands.Bear5Command;
import net.zephyr.fnafur.util.commands.MoneyCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FnafUniverseRebuilt implements ModInitializer {

	public static final Map<EntityType<? extends AnimatronicEntity>, EntityRendererProvider<?>> RENDER_FACTORIES = new Object2ObjectOpenHashMap<>();

	// TODO ADD MENU CONFIG
	public static final boolean DISABLE_MAIN_MENU = false;
	public static final boolean MENU_REDUCE_MOVEMENTS = false;
	public static final boolean DISABLE_DISCLAIMER = false;
	public static final boolean DEBUG = false;

	public static final String MOD_ID = "fnafur";
	public static final String MOD_VERSION = "PRIVATE BUILD - DO NOT SHARE";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		EntityInit.registerEntities();
		BlockEntityInit.registerBlockEntities();
		GeoBlockEntityInit.registerBlockEntities();
		ItemGroupsInit.registerItemGroups();
		ItemCategoriesInit.registerItemCategories();
		SoundsInit.registerSounds();
		ItemInit.registerItems();
		ParticlesInit.registerParticles();

		registerCommands();
		NetworkingInit.registerPayloads();
		NetworkingInit.registerServerReceivers();
		PayloadDef.registerC2SPackets();

        registerEvents();
		LOGGER.info("The GOOP is in the bag.");
	}

	public void registerCommands() {
		CommandRegistrationCallback.EVENT.register(MoneyCommand::register);
		CommandRegistrationCallback.EVENT.register(Bear5Command::register);
	}

	public static void print(String print){
		if(DEBUG) {
			System.out.println(print);
		}
	}

    private static void registerEvents() {

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.player;

            DecalWorldState state = DecalWorldState.get(player.level());
            ServerPlayNetworking.send(player, new FetchAllDecalsS2CPayload(state.getDecals()));
        });

        ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register(
                (player, origin, destination) -> {

                    DecalWorldState state = DecalWorldState.get(destination);
                    ServerPlayNetworking.send(player, new FetchAllDecalsS2CPayload(state.getDecals()));
                }
        );

        FnafUniverseRebuilt.LOGGER.info("Registering EVENTS for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }
}
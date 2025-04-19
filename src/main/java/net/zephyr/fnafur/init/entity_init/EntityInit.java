package net.zephyr.fnafur.init.entity_init;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.camera_desk.CameraRenderer;
import net.zephyr.fnafur.blocks.special.SeatEntity;
import net.zephyr.fnafur.blocks.special.SeatEntityRenderer;
import net.zephyr.fnafur.entity.animatronic.AnimatronicBetaRenderer;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.entity.animatronic.AnimatronicRenderer;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.entity.other.bear5.Bear5Entity;
import net.zephyr.fnafur.entity.other.bear5.Bear5Renderer;
import net.zephyr.fnafur.entity.zephyr.ZephyrEntity;
import net.zephyr.fnafur.entity.zephyr.ZephyrRenderer;

public class EntityInit {
    public static final EntityType<ZephyrEntity> ZEPHYR = register(
            "zephyr",
            EntityType.Builder.create(ZephyrEntity::new, SpawnGroup.MISC).dimensions(0.65f, 1.65f).eyeHeight(1.55f)
    );
    public static final EntityType<AnimatronicEntity> ANIMATRONIC = register(
            "animatronic",
            EntityType.Builder.create(AnimatronicEntity::new, SpawnGroup.MISC).dimensions(0.8f, 2.25f).eyeHeight(1.8f)
    );
    public static final EntityType<Bear5Entity> BEAR5 = register(
            "bear5",
            EntityType.Builder.create(Bear5Entity::new, SpawnGroup.MISC).dimensions(1, 3).eyeHeight(2)
    );
    public static final EntityType<SeatEntity> SEAT = register(
            "seat",
            EntityType.Builder.create(SeatEntity::new, SpawnGroup.MISC).dimensions(0.01f, 0.01f)
    );

    private static <T extends Entity> EntityType<T> register(RegistryKey<EntityType<?>> key, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }
    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return register(keyOf(id), type);
    }

    private static RegistryKey<EntityType<?>> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, id));
    }

    public static void registerEntities(){
        CharacterInit.registerCharacters();

        FabricDefaultAttributeRegistry.register(EntityInit.ZEPHYR, ZephyrEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(EntityInit.ANIMATRONIC, AnimatronicEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(EntityInit.BEAR5, Bear5Entity.setAttributes());

        FnafUniverseRebuilt.LOGGER.info("Registering Entities for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }
    public static void registerEntitiesOnClient(){

        createRenderer(EntityInit.ZEPHYR, ZephyrRenderer::new);
        EntityRendererRegistry.register(EntityInit.BEAR5, Bear5Renderer::new);
        makeRenderer(EntityInit.ANIMATRONIC, AnimatronicRenderer::new);
        EntityRendererRegistry.register(EntityInit.SEAT, SeatEntityRenderer::new);

        WorldRenderEvents.LAST.register(CameraRenderer::onRenderWorld);

        FnafUniverseRebuilt.LOGGER.info("Registering Entities on CLIENT for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }

    public static <E extends AnimatronicEntity> void makeRenderer(EntityType<? extends E> entityType, EntityRendererFactory<E> entityRendererFactory) {
        FnafUniverseRebuilt.RENDER_FACTORIES.put(entityType, entityRendererFactory);
        EntityRendererRegistry.register(entityType, entityRendererFactory);
    }
    public static <E extends DefaultEntity> void createRenderer(EntityType<? extends E> entityType, EntityRendererFactory<E> entityRendererFactory) {
        FnafUniverseRebuilt.RENDERER_FACTORIES.put(entityType, entityRendererFactory);
        EntityRendererRegistry.register(entityType, entityRendererFactory);
    }
}

package net.zephyr.fnafur.init.entity_init;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.camera_desk.CameraRenderer;
import net.zephyr.fnafur.client.JavaModels;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.entity.classic.cl_fred.cl_fred;
import net.zephyr.fnafur.entity.zephyr.ZephyrEntity;
import net.zephyr.fnafur.entity.zephyr.ZephyrRenderer;

public class EntityInit {
    public static final EntityType<ZephyrEntity> ZEPHYR = register(
            "zephyr",
            EntityType.Builder.create(ZephyrEntity::new, SpawnGroup.MISC).dimensions(0.65f, 1.65f).eyeHeight(1.55f)
    );

    private static <T extends Entity> EntityType<T> register(RegistryKey<EntityType<?>> key, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }
    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return register(keyOf(id), type);
    }

    private static RegistryKey<EntityType<?>> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, id));
    }

    public static void registerEntities(){
        FabricDefaultAttributeRegistry.register(EntityInit.ZEPHYR, ZephyrEntity.setAttributes());

        ClassicInit.registerEntities();

        FnafUniverseResuited.LOGGER.info("Registering Entities for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
    public static void registerEntitiesOnClient(){

        createRenderer(EntityInit.ZEPHYR, ZephyrRenderer::new);

        ClassicInit.registerEntitiesOnClient();

        WorldRenderEvents.LAST.register(CameraRenderer::onRenderWorld);

        FnafUniverseResuited.LOGGER.info("Registering Entities on CLIENT for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }

    public static <E extends DefaultEntity> void createRenderer(EntityType<? extends E> entityType, EntityRendererFactory<E> entityRendererFactory) {
        FnafUniverseResuited.RENDERER_FACTORIES.put(entityType, entityRendererFactory);
        EntityRendererRegistry.register(entityType, entityRendererFactory);
    }
}

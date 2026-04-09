package net.zephyr.fnafur.init.entity_init;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.special.SeatEntity;
import net.zephyr.fnafur.blocks.special.SeatEntityRenderer;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.entity.animatronic.AnimatronicRenderer;
import net.zephyr.fnafur.entity.other.bear5.Bear5Entity;
import net.zephyr.fnafur.entity.other.bear5.Bear5Renderer;

public class EntityInit {
    public static final EntityType<AnimatronicEntity> ANIMATRONIC = register(
            "animatronic",
            EntityType.Builder.of(AnimatronicEntity::new, MobCategory.MISC)
                    .sized(0.8f, 2.25f).eyeHeight(1.8f)
    );
    public static final EntityType<Bear5Entity> BEAR5 = register(
            "bear5",
            EntityType.Builder.of(Bear5Entity::new, MobCategory.MISC).sized(1, 3).eyeHeight(2)
    );
    public static final EntityType<SeatEntity> SEAT = register(
            "seat",
            EntityType.Builder.of(SeatEntity::new, MobCategory.MISC).sized(0.01f, 0.01f)
    );

    private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> key, EntityType.Builder<T> type) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, type.build(key));
    }
    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return register(keyOf(id), type);
    }

    private static ResourceKey<EntityType<?>> keyOf(String id) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, id));
    }

    public static void registerEntities(){

        FabricDefaultAttributeRegistry.register(EntityInit.ANIMATRONIC, AnimatronicEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(EntityInit.BEAR5, Bear5Entity.setAttributes());

        FnafUniverseRebuilt.LOGGER.info("Registering Entities for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }
    public static void registerEntitiesOnClient(){

        EntityRendererRegistry.register(EntityInit.BEAR5, Bear5Renderer::new);
        makeRenderer(EntityInit.ANIMATRONIC, AnimatronicRenderer::new);
        EntityRendererRegistry.register(EntityInit.SEAT, SeatEntityRenderer::new);

        //WorldRenderEvents.LAST.register(CameraRenderer::onRenderWorld);

        FnafUniverseRebuilt.LOGGER.info("Registering Entities on CLIENT for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }

    public static <E extends AnimatronicEntity> void makeRenderer(EntityType<? extends E> entityType, EntityRendererProvider<E> entityRendererFactory) {
        FnafUniverseRebuilt.RENDER_FACTORIES.put(entityType, entityRendererFactory);
        EntityRendererRegistry.register(entityType, entityRendererFactory);
    }
}

package net.zephyr.fnafur.init.entity_init;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.camera_desk.CameraRenderer;
import net.zephyr.fnafur.client.JavaModels;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.entity.cameramap.CameraMappingEntity;
import net.zephyr.fnafur.entity.cameramap.CameraMappingEntityRenderer;
import net.zephyr.fnafur.entity.zephyr.ZephyrEntity;
import net.zephyr.fnafur.entity.zephyr.ZephyrRenderer;

public class EntityInit {
    public static final EntityType<CameraMappingEntity> CAMERA_MAPPING =
            Registry.register(
                    Registries.ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "camera_mapping"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MISC, CameraMappingEntity::new)
                            .dimensions(EntityDimensions.fixed(50, 40)).build()
            );
    public static final EntityType<ZephyrEntity> ZEPHYR =
            Registry.register(
                    Registries.ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "zephyr"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MISC, ZephyrEntity::new)
                            .dimensions(EntityDimensions.fixed(0.65f, 1.65f).withEyeHeight(1.55f)).build()
            );

    public static void registerEntities(){
        FabricDefaultAttributeRegistry.register(EntityInit.ZEPHYR, ZephyrEntity.setAttributes());

        ClassicInit.registerEntities();

        FnafUniverseResuited.LOGGER.info("Registering Entities for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
    public static void registerEntitiesOnClient(){

        createRenderer(EntityInit.ZEPHYR, ZephyrRenderer::new);

        ClassicInit.registerEntitiesOnClient();

        WorldRenderEvents.LAST.register(CameraRenderer::onRenderWorld);
        EntityModelLayerRegistry.registerModelLayer(JavaModels.CAMERA_MAP, CameraMappingEntityRenderer::getTexturedModelData);
        EntityRendererRegistry.register(EntityInit.CAMERA_MAPPING, CameraMappingEntityRenderer::new);

        FnafUniverseResuited.LOGGER.info("Registering Entities on CLIENT for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }

    public static <E extends DefaultEntity> void createRenderer(EntityType<? extends E> entityType, EntityRendererFactory<E> entityRendererFactory) {
        FnafUniverseResuited.RENDERER_FACTORIES.put(entityType, entityRendererFactory);
        EntityRendererRegistry.register(entityType, entityRendererFactory);
    }
}

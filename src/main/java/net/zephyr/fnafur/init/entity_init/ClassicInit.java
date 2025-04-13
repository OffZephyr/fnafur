package net.zephyr.fnafur.init.entity_init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.entity.classic.cl_fred.*;
import net.zephyr.fnafur.entity.classic.cl_bon.*;
import net.zephyr.fnafur.entity.classic.cl_chica.*;
import net.zephyr.fnafur.entity.classic.cl_foxy.*;

public class ClassicInit {
    public static final EntityType<cl_fred> CL_FRED = register(
            "cl_fred",
            EntityType.Builder.create(cl_fred::new, SpawnGroup.MISC).dimensions(0.8f, 2.25f).eyeHeight(1.8f)
    );
    public static final EntityType<cl_bon> CL_BON = register(
            "cl_bon",
            EntityType.Builder.create(cl_bon::new, SpawnGroup.MISC).dimensions(0.8f, 2.25f).eyeHeight(1.8f)
    );
    public static final EntityType<cl_chica> CL_CHICA = register(
            "cl_chica",
            EntityType.Builder.create(cl_chica::new, SpawnGroup.MISC).dimensions(0.8f, 2.25f).eyeHeight(1.8f)
    );
    public static final EntityType<cl_foxy> CL_FOXY = register(
            "cl_foxy",
            EntityType.Builder.create(cl_foxy::new, SpawnGroup.MISC).dimensions(0.8f, 2.25f).eyeHeight(1.8f)
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
        FabricDefaultAttributeRegistry.register(CL_FRED, cl_fred.setAttributes());
        FabricDefaultAttributeRegistry.register(CL_BON, cl_bon.setAttributes());
        FabricDefaultAttributeRegistry.register(CL_CHICA, cl_chica.setAttributes());
        FabricDefaultAttributeRegistry.register(CL_FOXY, cl_foxy.setAttributes());
    }

    public static void registerEntitiesOnClient(){
        EntityInit.createRenderer(CL_FRED, cl_fred_renderer::new);
        EntityInit.createRenderer(CL_BON, cl_bon_renderer::new);
        EntityInit.createRenderer(CL_CHICA, cl_chica_renderer::new);
        EntityInit.createRenderer(CL_FOXY, cl_foxy_renderer::new);
    }
}

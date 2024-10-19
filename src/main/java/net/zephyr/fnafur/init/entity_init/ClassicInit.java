package net.zephyr.fnafur.init.entity_init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.entity.classic.cl_fred.*;
import net.zephyr.fnafur.entity.classic.cl_bon.*;

public class ClassicInit {
    public static final EntityType<cl_fred> CL_FRED =
            Registry.register(
                    Registries.ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "cl_fred"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MISC, cl_fred::new)
                            .dimensions(EntityDimensions.fixed(0.8f, 2.25f).withEyeHeight(1.8f)).build()
            );
    public static final EntityType<cl_bon> CL_BON =
            Registry.register(
                    Registries.ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "cl_bon"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MISC, cl_bon::new)
                            .dimensions(EntityDimensions.fixed(0.8f, 2.25f).withEyeHeight(1.8f)).build()
            );
    public static void registerEntities(){
        FabricDefaultAttributeRegistry.register(CL_FRED, cl_fred.setAttributes());
        FabricDefaultAttributeRegistry.register(CL_BON, cl_bon.setAttributes());
    }

    public static void registerEntitiesOnClient(){
        EntityInit.createRenderer(CL_FRED, cl_fred_renderer::new);
        EntityInit.createRenderer(CL_BON, cl_bon_renderer::new);
    }
}

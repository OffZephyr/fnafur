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
import net.zephyr.fnafur.entity.classic.cl_fred.cl_fred;
import net.zephyr.fnafur.entity.classic.cl_fred.cl_fred_renderer;

public class ClassicInit {
    public static final EntityType<cl_fred> CL_FRED =
            Registry.register(
                    Registries.ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "cl_fred"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MISC, cl_fred::new)
                            .dimensions(EntityDimensions.fixed(0.8f, 2.25f).withEyeHeight(1.8f)).build()
            );
    public static void registerEntities(){
        FabricDefaultAttributeRegistry.register(CL_FRED, cl_fred.setAttributes());
    }

    public static void registerEntitiesOnClient(){
        EntityInit.createRenderer(CL_FRED, cl_fred_renderer::new);
    }
}

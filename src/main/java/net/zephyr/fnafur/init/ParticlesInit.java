package net.zephyr.fnafur.init;

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.particles.FogParticle;

public class ParticlesInit {
    public static final SimpleParticleType FOG_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "fog"),
                FOG_PARTICLE);
    }

    public static void registerParticlesClient() {
        ParticleProviderRegistry.getInstance().register(FOG_PARTICLE, FogParticle.Factory::new);
    }
}

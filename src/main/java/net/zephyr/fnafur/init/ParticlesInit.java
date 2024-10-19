package net.zephyr.fnafur.init;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.particles.FogParticle;

public class ParticlesInit {
    public static final SimpleParticleType FOG_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "fog"),
                FOG_PARTICLE);
    }

    public static void registerParticlesClient() {
        ParticleFactoryRegistry.getInstance().register(FOG_PARTICLE, FogParticle.Factory::new);
    }
}

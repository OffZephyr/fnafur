package net.zephyr.fnafur.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

import java.util.Random;

public class FogParticle extends BillboardParticle {

    protected FogParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Sprite sprite) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, sprite);

        this.velocityMultiplier = 0.6f;
        this.x = x;
        this.y = y;
        this.z = z;
        Random random = new Random();
        float speed = 0.5f;
        this.velocityX = random.nextDouble(-speed, speed);
        this.velocityY = random.nextDouble(-speed, speed);
        this.velocityZ = random.nextDouble(-speed, speed);
        this.scale *= random.nextFloat(2f, 5f);
        this.maxAge = 20;
        this.sprite = sprite;

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
    }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }

    private void fadeOut(){
        this.alpha = (-(1/(float)maxAge) * age + 1);
    }

    @Override
    protected RenderType getRenderType() {
        return RenderType.PARTICLE_ATLAS_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, net.minecraft.util.math.random.Random random) {
            return new FogParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider.getSprite(random));
        }
    }
}

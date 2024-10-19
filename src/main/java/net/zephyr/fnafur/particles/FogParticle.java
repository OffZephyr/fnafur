package net.zephyr.fnafur.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

import java.util.Random;

public class FogParticle extends SpriteBillboardParticle {

    protected FogParticle(ClientWorld clientWorld, double d, double e, double f,
                          SpriteProvider spriteSet, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);

        this.velocityMultiplier = 0.6f;
        this.x = d;
        this.y = e;
        this.z = f;
        Random random = new Random();
        float speed = 0.5f;
        this.velocityX = random.nextDouble(-speed, speed);
        this.velocityY = random.nextDouble(-speed, speed);
        this.velocityZ = random.nextDouble(-speed, speed);
        this.scale *= random.nextFloat(2f, 5f);
        this.maxAge = 20;
        this.setSpriteForAge(spriteSet);

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
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }


    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider sprites;
        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }
        public Particle createParticle(SimpleParticleType particleType, ClientWorld world, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new FogParticle(world, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}

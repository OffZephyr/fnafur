package net.zephyr.fnafur.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SingleQuadParticle.Layer;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.Random;

public class FogParticle extends SingleQuadParticle {

    protected FogParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, TextureAtlasSprite sprite) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, sprite);

        this.friction = 0.6f;
        this.x = x;
        this.y = y;
        this.z = z;
        Random random = new Random();
        float speed = 0.5f;
        this.xd = random.nextDouble(-speed, speed);
        this.yd = random.nextDouble(-speed, speed);
        this.zd = random.nextDouble(-speed, speed);
        this.quadSize *= random.nextFloat(2f, 5f);
        this.lifetime = 20;
        this.sprite = sprite;

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }

    private void fadeOut(){
        this.alpha = (-(1/(float) lifetime) * age + 1);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, net.minecraft.util.RandomSource random) {
            return new FogParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider.get(random));
        }
    }
}

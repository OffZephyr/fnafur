package net.zephyr.fnafur.blocks.fog;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.particle.v1.ParticleRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.zephyr.fnafur.client.gui.screens.CameraTabletScreen;
import net.zephyr.fnafur.init.ParticlesInit;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.block_init.BlockInit;

import java.util.Random;

public class FogBlockEntity extends BlockEntity {
    public boolean visible = true;
    public boolean visibleCache = true;
    public FogBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.FOG_BLOCK, pos, state);
    }
    void tick(World world, BlockPos pos, BlockState state, FogBlockEntity blockEntity){
        if(world.isClient()) {
            visible =
                    MinecraftClient.getInstance().player.getMainHandStack().isOf(BlockInit.FOG_BLOCK.asItem()) || (MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen)
                        && ((world.getLightLevel(LightType.BLOCK, pos) < 4 && world.getLightLevel(LightType.SKY, pos) < 4) || world.isNight());

            if(visibleCache != visible) {
                world.updateListeners(pos, state, state, Block.field_31025);
                world.setBlockState(pos, state, Block.field_31025);
                visibleCache = visible;
            }
        }
        /*if(world.isClient())
            if(MinecraftClient.getInstance().player.getMainHandStack().isOf(BlockInit.FOG_BLOCK.asItem()) || MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen) {

                Random random = new Random();
                double randomX = random.nextDouble(-0.5, 0.5);
                double randomY = random.nextDouble(-0.5, 0.5);
                double randomZ = random.nextDouble(-0.5, 0.5);
                world.addParticle(ParticlesInit.FOG_PARTICLE, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, randomX, randomY, randomZ);
            }
        }*/
    }
}

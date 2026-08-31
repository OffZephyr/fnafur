package net.zephyr.fnafur.blocks.fog;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.particle.v1.ParticleRenderEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.Level;
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
    void tick(Level world, BlockPos pos, BlockState state, FogBlockEntity blockEntity){
        if(world.isClientSide()) {
            visible =
                    Minecraft.getInstance().player.getMainHandItem().is(BlockInit.FOG_BLOCK.asItem()) || (Minecraft.getInstance().screen instanceof CameraTabletScreen)
                        && ((world.getBrightness(LightLayer.BLOCK, pos) < 4 && world.getBrightness(LightLayer.SKY, pos) < 4) || world.isDarkOutside());

            if(visibleCache != visible) {
                world.sendBlockUpdated(pos, state, state, Block.UPDATE_LIMIT);
                world.setBlock(pos, state, Block.UPDATE_LIMIT);
                visibleCache = visible;
            }
        }
        /*if(world.isClient())
            if(Minecraft.getInstance().player.getMainHandStack().isOf(BlockInit.FOG_BLOCK.asItem()) || Minecraft.getInstance().currentScreen instanceof CameraTabletScreen) {

                Random random = new Random();
                double randomX = random.nextDouble(-0.5, 0.5);
                double randomY = random.nextDouble(-0.5, 0.5);
                double randomZ = random.nextDouble(-0.5, 0.5);
                world.addParticle(ParticlesInit.FOG_PARTICLE, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, randomX, randomY, randomZ);
            }
        }*/
    }
}

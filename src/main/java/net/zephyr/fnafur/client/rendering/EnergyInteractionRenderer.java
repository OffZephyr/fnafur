package net.zephyr.fnafur.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.blocks.energy.enums.EnergyNode;
import net.zephyr.fnafur.item.tools.TapeMesurerItem;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.regex.MatchResult;

public class EnergyInteractionRenderer {

    int color_connect       = 0xFF11EE11;
    int color_disconnect    = 0xFFEE1111;

    public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ){

        var plr = MinecraftClient.getInstance().player;
        var world = MinecraftClient.getInstance().world;

        BlockPos start = new BlockPos(0,0,0);
        BlockPos end   = new BlockPos(0,0,0);
        ItemStack stack = plr.getMainHandStack();
        int color = 0xFF000000;

        if(! (stack.getItem() instanceof TapeMesurerItem item)) return;

        NbtCompound data = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        if(!data.getBoolean("needConnection")) return;


        color = plr.isSneaking() ? color_disconnect : color_connect;
        start = plr.getBlockPos();
        end   = BlockPos.fromLong(data.getLong("posConnection"));

        HitResult blockHit = MinecraftClient.getInstance().player.raycast(plr.getBlockInteractionRange(), 0.0f, false);
        if(blockHit.getType() == HitResult.Type.BLOCK){
            var p = new BlockPos((int)blockHit.getPos().x, (int)blockHit.getPos().y, (int)blockHit.getPos().z);
            var state = world.getBlockState(p);
            if(state.getBlock() instanceof EnergyNode){
                end = p;
            }
        }

        matrices.push();
        matrices.translate(-cameraX,-cameraY,-cameraZ); // model-space to world-space
        drawLine(matrices, start, end, color,0.02f);
        matrices.pop();

    }

    public final void drawLine(MatrixStack matrices, BlockPos start, BlockPos end, int color, float size){
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        Vec3d lineNormal = (start.toCenterPos().subtract(end.toCenterPos())).crossProduct(new Vec3d(0,1,0));

        Vector3f p0 = start.toCenterPos().toVector3f().add( lineNormal.toVector3f().mul(size) );
        Vector3f p1 = start.toCenterPos().toVector3f().sub( lineNormal.toVector3f().mul(size) );

        Vector3f p2 = end.toCenterPos().toVector3f().sub( lineNormal.toVector3f().mul(size) );
        Vector3f p3 = end.toCenterPos().toVector3f().add( lineNormal.toVector3f().mul(size) );


        buffer.vertex(matrices.peek().getPositionMatrix(),  p0.x, p0.y,   p0.z).color(color);
        buffer.vertex(matrices.peek().getPositionMatrix(),  p1.x, p1.y,   p1.z).color(color);
        buffer.vertex(matrices.peek().getPositionMatrix(),  p2.x, p2.y,   p2.z).color(color);
        buffer.vertex(matrices.peek().getPositionMatrix(),  p3.x, p3.y,   p3.z).color(color);

        RenderSystem.enableDepthTest();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.disableDepthTest();

    }

}

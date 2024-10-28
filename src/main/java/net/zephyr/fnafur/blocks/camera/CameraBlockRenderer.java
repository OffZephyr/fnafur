package net.zephyr.fnafur.blocks.camera;

import net.minecraft.block.BlockState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.camera_desk.CameraDeskBlockEntity;
import net.zephyr.fnafur.blocks.camera_desk.CameraRenderer;
import net.zephyr.fnafur.blocks.layered_block.LayeredBlock;
import net.zephyr.fnafur.client.JavaModels;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class CameraBlockRenderer implements BlockEntityRenderer<CameraBlockEntity> {
    private final ModelPart model;
    private static final String HEAD = "head";
    public CameraBlockRenderer(BlockEntityRendererFactory.Context context){
        ModelPart modelPart = context.getLayerModelPart(JavaModels.CAMERA_HEAD);
        this.model = modelPart.getChild(HEAD);
    }
   public static TexturedModelData getTexturedModelData() {
       ModelData modelData = new ModelData();
       ModelPartData modelPartData = modelData.getRoot();

       ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -2.0F, -6.0F, 4.0F, 4.0F, 8.0F, new Dilation(0.0F))
               .uv(20, 0).cuboid(-0.5F, -0.5F, -8.2F, 1.0F, 1.0F, 4.9F, new Dilation(0.2F))
               .uv(14, 19).cuboid(-0.95F, -0.95F, -9.0F, 1.9F, 1.9F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 13.0F, 3.0F));

       ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(0, 12).mirrored().cuboid(0.0F, -1.3F, -3.0F, 0.0F, 3.9F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(1.0F, -0.5F, 1.9F, 0.0F, 0.0F, 0.3927F));

       ModelPartData cube_r2 = head.addChild("cube_r2", ModelPartBuilder.create().uv(0, 12).cuboid(0.0F, -1.3F, -3.0F, 0.0F, 3.9F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -0.5F, 1.9F, 0.0F, 0.0F, -0.3927F));

       return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void render(CameraBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        for (BlockPos pos : CameraDeskBlockEntity.posList) {
            if (entity.getWorld().getBlockEntity(pos) instanceof CameraDeskBlockEntity mirror) {
                if(CameraRenderer.isDrawing() && BlockPos.fromLong(mirror.currentCam).equals(entity.getPos())) return;
            }
        }

        BlockPos pos = entity.getPos();

        World world = entity.getWorld();
        boolean bl = world != null;
        BlockState blockState = bl ? entity.getCachedState() : BlockInit.CAMERA.getDefaultState().with(LayeredBlock.FACING, Direction.SOUTH);

        NbtCompound data = ((IEntityDataSaver)entity).getPersistentData();
        float f = blockState.get(LayeredBlock.FACING).asRotation();

        matrices.push();
        matrices.translate(0.5f, -0.125f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f + 180));
        String id = "block/camera";
        Identifier texture = Identifier.of(FnafUniverseResuited.MOD_ID, id);
        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture);
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

        float pitch = data.getFloat("pitch");
        float yaw = data.getFloat("yaw");

        if(data.getBoolean("Active")) {
            model.roll = (float) Math.PI / 180 * 180;
            model.pitch = (float) Math.PI / 180 * pitch;
            model.yaw = (float) Math.PI / 180 * yaw;
        }
        else {
            model.roll = (float) Math.PI / 180 * 180;
            model.pitch = (float) Math.PI / 180 * 50;
            model.yaw = (float) Math.PI / 180 * 0;
        }
        model.render(matrices, vertexConsumer, getLightLevel(world, pos), overlay);

        if(data.getBoolean("isUsed") && data.getBoolean("Active")){
            String on_id = "block/camera_on";

            Identifier textureOn = Identifier.of(FnafUniverseResuited.MOD_ID, on_id);
            SpriteIdentifier spriteIdentifier2 = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, textureOn);
            VertexConsumer vertexConsumer2 = spriteIdentifier2.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

            model.render(matrices, vertexConsumer2, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, overlay);
        }
        matrices.pop();
    }
    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}

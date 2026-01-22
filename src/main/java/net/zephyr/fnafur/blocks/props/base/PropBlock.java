package net.zephyr.fnafur.blocks.props.base;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.state.OutlineRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrame;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.client.gui.screens.editing.PaintbrushAltPickerScreen;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class PropBlock<T extends Enum<T> & ColorEnumInterface & StringIdentifiable>  extends BlockWithEntity {

    public static boolean drawingOutline = false;
    public static final float angleSnap = 22.5f;
    public static final float gridSnap = 0.25f;

    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    private EnumProperty<T> COLOR;
    protected PropBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    public boolean canChangeState(Item item){
        return item == ItemInit.PAINTBRUSH;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ItemStack stack = player.getMainHandStack();
        if(stack != null && canChangeState(stack.getItem()) && state.contains(COLOR_PROPERTY())) {
            //world.setBlockState(pos, state.cycle(COLOR_PROPERTY()));
            if(world.isClient()){
                MinecraftClient.getInstance().setScreen(new PaintbrushAltPickerScreen<>(Text.literal("guh"), pos, COLOR_PROPERTY(), state.get(COLOR_PROPERTY())));
            }
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hit);
    }
    public abstract Class<T> COLOR_ENUM();
    public EnumProperty<T> COLOR_PROPERTY(){
        if(COLOR_ENUM() == null) return COLOR;
        if(COLOR == null) COLOR = EnumProperty.of("color", COLOR_ENUM());
        return COLOR;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(world.isClient()){
            if(world.getBlockEntity(pos) instanceof PropBlockEntity ent){
                ((IEntityDataSaver)ent).setServerUpdateStatus(true);
            }
        }

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PropBlockEntity(pos, state);
    }

    public abstract boolean rotates();
    public abstract boolean snapsVertically();

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return true;
    }


    @Nullable
    @Override
    public <Q extends BlockEntity> BlockEntityTicker<Q> getTicker(World world, BlockState state, BlockEntityType<Q> type) {

        return validateTicker(type, BlockEntityInit.PROPS,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }

    public List<Box> getClickHitBoxes(BlockState state){
        return new ArrayList<>();
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = super.getPickStack(world, pos, state, includeData);

        BlockStateComponent component = BlockStateComponent.DEFAULT;
        /*for(Property property : state.getProperties()){
            component = component.with(property, state.get(property));
        }*/

        if(state.contains(COLOR)) {
            component = component.with(COLOR, state.get(COLOR));
        }
        itemStack.set(DataComponentTypes.BLOCK_STATE, component);

        return itemStack;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public boolean isTransparent(BlockState state) { return true;}

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return this instanceof GeoPropBlock ? BlockRenderType.INVISIBLE : BlockRenderType.MODEL;
    }

    public static int getPreviewColor() {
        return 0x88FFFFFF;
    }

    @Environment(EnvType.CLIENT)
    public static void drawBlockOutlineHook(World world, BlockState blockState, BlockPos pos, MatrixStack matrices, VertexConsumer vertexConsumer, double x, double y, double z, OutlineRenderState state, int color, float lineWidth) {

        PropBlock.drawingOutline = true;
        matrices.push();
        float rotation = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getFloat("Rotation").orElse(0.0f) + 180;

        double offsetX = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getDouble("xOffset").orElse(0.0);
        double offsetY = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getDouble("yOffset").orElse(0.0);
        double offsetZ = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getDouble("zOffset").orElse(0.0);

        if(blockState.getBlock() instanceof WallPropBlock<?>) {

            BlockPos checkPos = pos.offset(blockState.get(WallPropBlock.FACING).getOpposite());
            if(world.getBlockState(checkPos).getBlock() instanceof DiagonalMimicFrame f){

                Direction direction = blockState.get(WallPropBlock.FACING);
                float wallOffsetRotation = 0f;
                double wallOffsetPositionX = 0f;
                double wallOffsetPositionZ = 0f;
                if (f.isDiagonal(world.getBlockState(checkPos))) {
                    BooleanProperty p = f.getDiagonalDirection(world.getBlockState(checkPos));
                    wallOffsetRotation = blockState.get(WallPropBlock.FACING).getPositiveHorizontalDegrees();
                    if (p == DiagonalMimicFrame.NEXT_MAP.get(direction)) {
                        wallOffsetRotation += 45f;
                        wallOffsetPositionX = -0.4f * direction.getOffsetX() + -0.075f * direction.getOffsetZ();
                        wallOffsetPositionZ = -0.4f * direction.getOffsetZ() + 0.125f * direction.getOffsetX();
                    }
                    if (p == DiagonalMimicFrame.NEXT_MAP.get(direction.rotateYCounterclockwise())) {
                        wallOffsetRotation += -45f;
                        wallOffsetPositionX = -0.4f * direction.getOffsetX() + 0.075f * direction.getOffsetZ();
                        wallOffsetPositionZ = -0.4f * direction.getOffsetZ() + -0.125f * direction.getOffsetX();
                    }
                    rotation = wallOffsetRotation;
                    offsetX += wallOffsetPositionX;
                    offsetZ += wallOffsetPositionZ;
                }
            }
            else{
                rotation += blockState.get(WallPropBlock.FACING).getPositiveHorizontalDegrees() + 180;
            }
        }

        double posX = pos.getX();
        double posY = pos.getY();
        double posZ = pos.getZ();

        matrices.translate(offsetX + posX - x, offsetY + posY - y, offsetZ + posZ - z);

        //matrices.translate(-cameraX,-cameraY,-cameraZ);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rotation));
        matrices.translate(-0.5f, -1, -0.5f);
        if(blockState.getBlock() instanceof WallPropBlock<?>) {
            matrices.translate(0, 0.5f, 0);
        }

        VertexRendering.drawOutline(
                matrices,
                vertexConsumer,
                blockState.getBlock().getDefaultState().getOutlineShape(world, pos),
                0,
                0,
                0,
                color,
                lineWidth

        );

        matrices.pop();
        PropBlock.drawingOutline = false;
    }
}

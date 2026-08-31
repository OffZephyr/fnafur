package net.zephyr.fnafur.blocks.props.base;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.network.chat.Component;
import net.minecraft.util.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import com.mojang.math.Axis;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
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

public abstract class PropBlock<T extends Enum<T> & ColorEnumInterface & StringRepresentable>  extends BaseEntityBlock {

    public static boolean drawingOutline = false;
    public static final float angleSnap = 22.5f;
    public static final float gridSnap = 0.25f;

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private EnumProperty<T> COLOR;
    protected PropBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.block();
    }

    public boolean canChangeState(Item item){
        return item == ItemInit.PAINTBRUSH;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        ItemStack stack = player.getMainHandItem();
        if(stack != null && canChangeState(stack.getItem()) && state.hasProperty(COLOR_PROPERTY())) {
            //world.setBlockState(pos, state.cycle(COLOR_PROPERTY()));
            if(world.isClientSide()){
                Minecraft.getInstance().setScreen(new PaintbrushAltPickerScreen<>(Component.literal("guh"), pos, COLOR_PROPERTY(), state.getValue(COLOR_PROPERTY())));
            }
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }
    public abstract Class<T> COLOR_ENUM();
    public EnumProperty<T> COLOR_PROPERTY(){
        if(COLOR_ENUM() == null) return COLOR;
        if(COLOR == null) COLOR = EnumProperty.create("color", COLOR_ENUM());
        return COLOR;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(world.isClientSide()){
            if(world.getBlockEntity(pos) instanceof PropBlockEntity ent){
                ((IEntityDataSaver)ent).setServerUpdateStatus(true);
            }
        }

        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }
    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PropBlockEntity(pos, state);
    }

    public abstract boolean rotates();
    public abstract boolean snapsVertically();

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return true;
    }


    @Nullable
    @Override
    public <Q extends BlockEntity> BlockEntityTicker<Q> getTicker(Level world, BlockState state, BlockEntityType<Q> type) {

        return createTickerHelper(type, BlockEntityInit.PROPS,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }

    public List<AABB> getClickHitBoxes(BlockState state){
        return new ArrayList<>();
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = super.getCloneItemStack(world, pos, state, includeData);

        BlockItemStateProperties component = BlockItemStateProperties.EMPTY;
        /*for(Property property : state.getProperties()){
            component = component.with(property, state.get(property));
        }*/

        if(state.hasProperty(COLOR)) {
            component = component.with(COLOR, state.getValue(COLOR));
        }
        itemStack.set(DataComponents.BLOCK_STATE, component);

        return itemStack;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state) { return true;}

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return this instanceof GeoPropBlock ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }

    public static int getPreviewColor() {
        return 0x88FFFFFF;
    }

    @Environment(EnvType.CLIENT)
    public static void drawBlockOutlineHook(Level world, BlockState blockState, BlockPos pos, PoseStack matrices, VertexConsumer vertexConsumer, double x, double y, double z, BlockOutlineRenderState state, int color, float lineWidth) {

        PropBlock.drawingOutline = true;
        matrices.pushPose();
        float rotation = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getFloat("Rotation").orElse(0.0f) + 180;

        double offsetX = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getDouble("xOffset").orElse(0.0);
        double offsetY = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getDouble("yOffset").orElse(0.0);
        double offsetZ = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getDouble("zOffset").orElse(0.0);

        if(blockState.getBlock() instanceof WallPropBlock<?>) {

            BlockPos checkPos = pos.relative(blockState.getValue(WallPropBlock.FACING).getOpposite());
            if(world.getBlockState(checkPos).getBlock() instanceof DiagonalMimicFrame f){

                Direction direction = blockState.getValue(WallPropBlock.FACING);
                float wallOffsetRotation = 0f;
                double wallOffsetPositionX = 0f;
                double wallOffsetPositionZ = 0f;
                if (f.isDiagonal(world.getBlockState(checkPos))) {
                    BooleanProperty p = f.getDiagonalDirection(world.getBlockState(checkPos));
                    wallOffsetRotation = blockState.getValue(WallPropBlock.FACING).toYRot();
                    if (p == DiagonalMimicFrame.NEXT_MAP.get(direction)) {
                        wallOffsetRotation += 45f;
                        wallOffsetPositionX = -0.4f * direction.getStepX() + -0.075f * direction.getStepZ();
                        wallOffsetPositionZ = -0.4f * direction.getStepZ() + 0.125f * direction.getStepX();
                    }
                    if (p == DiagonalMimicFrame.NEXT_MAP.get(direction.getCounterClockWise())) {
                        wallOffsetRotation += -45f;
                        wallOffsetPositionX = -0.4f * direction.getStepX() + 0.075f * direction.getStepZ();
                        wallOffsetPositionZ = -0.4f * direction.getStepZ() + -0.125f * direction.getStepX();
                    }
                    rotation = wallOffsetRotation;
                    offsetX += wallOffsetPositionX;
                    offsetZ += wallOffsetPositionZ;
                }
            }
            else{
                rotation += blockState.getValue(WallPropBlock.FACING).toYRot() + 180;
            }
        }

        double posX = pos.getX();
        double posY = pos.getY();
        double posZ = pos.getZ();

        matrices.translate(offsetX + posX - x, offsetY + posY - y, offsetZ + posZ - z);

        //matrices.translate(-cameraX,-cameraY,-cameraZ);
        matrices.mulPose(Axis.YP.rotationDegrees(-rotation));
        matrices.translate(-0.5f, -1, -0.5f);
        if(blockState.getBlock() instanceof WallPropBlock<?>) {
            matrices.translate(0, 0.5f, 0);
        }

        ShapeRenderer.renderShape(
                matrices,
                vertexConsumer,
                blockState.getBlock().defaultBlockState().getShape(world, pos),
                0,
                0,
                0,
                color,
                lineWidth

        );

        matrices.popPose();
        PropBlock.drawingOutline = false;
    }
}

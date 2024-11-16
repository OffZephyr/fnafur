package net.zephyr.fnafur.item.tools;

import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.basic_blocks.layered_block.LayeredBlock;
import net.zephyr.fnafur.blocks.basic_blocks.layered_block.LayeredBlockEntity;
import net.zephyr.fnafur.blocks.basic_blocks.layered_block.LayeredBlockModel;
import net.zephyr.fnafur.entity.cameramap.CameraMappingEntity;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.init.ScreensInit;
import net.zephyr.fnafur.item.tablet.TabletItem;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class PaintbrushItem extends Item {
    public PaintbrushItem(Settings settings) {
        super(settings);
    }
    private CameraMappingEntity map = null;

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        if(!context.getPlayer().getOffHandStack().isEmpty() && context.getPlayer().getOffHandStack().getItem() instanceof TabletItem){

            NbtCompound data = context.getStack().getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
            ItemStack tablet = context.getPlayer().getOffHandStack();
            NbtCompound tabletData = tablet.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();

            boolean bl = tabletData.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).isEmpty();
            NbtList mapNbt = bl ? new NbtList() : tabletData.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).copy();

            for(int i = 0; i < mapNbt.size(); i++) {
                if (mapNbt.get(i).getType() == NbtElement.LONG_ARRAY_TYPE) {
                    long[] line = mapNbt.getLongArray(i);
                    BlockPos pos1 = BlockPos.fromLong(line[0]);
                    BlockPos pos2 = BlockPos.fromLong(line[1]);
                    Box box = new Box(pos1.toCenterPos(), pos2.toCenterPos()).expand(0.5f);
                    if (box.contains(context.getBlockPos().toCenterPos())) {
                        return ActionResult.SUCCESS;
                    }
                }
            }

            context.getStack().apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
                currentNbt.copyFrom(data);
            }));

            tablet.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
                currentNbt.copyFrom(tabletData);
            }));
        }
        else {
            if (world.isClient()) {
                if (world.getBlockState(context.getBlockPos()).isOf(BlockInit.LAYERED_BLOCK_BASE)) {

                    BlockState state = world.getBlockState(context.getBlockPos());
                    int rotationAmount = LayeredBlockModel.getDirectionId(state.get(LayeredBlock.FACING));
                    Direction directionClicked = context.getSide();
                    Direction textureRotation = directionClicked;
                    if (directionClicked != Direction.UP && directionClicked != Direction.DOWN) {
                        for (int j = 0; j < rotationAmount; j++) {
                            textureRotation = textureRotation.rotateYClockwise();
                        }
                    }

                    byte direction = (byte) LayeredBlockModel.getDirectionId(textureRotation);

                    if (world.getBlockEntity(context.getBlockPos()) instanceof LayeredBlockEntity entity) {
                        NbtCompound data = ((IEntityDataSaver)entity).getPersistentData();
                        data.putByte("editSide", direction);

                        GoopyNetworkingUtils.setClientScreen(ScreensInit.PAINTBRUSH, data, context.getBlockPos());

                        return ActionResult.SUCCESS;
                    }
                }
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        NbtCompound data = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();

        if(map == null || map.getId() != data.getInt("mapEntityID") || !data.getBoolean("summon_entity")){
            map = new CameraMappingEntity(EntityInit.CAMERA_MAPPING, world);
        }

        boolean flag2 = entity instanceof PlayerEntity;
        Item item = flag2 ? ((PlayerEntity) entity).getOffHandStack().getItem() : null;

        if (!flag2 || !selected || !(item instanceof TabletItem)) {
            data.putBoolean("hasCorner", false);
        }

        if(!data.equals(stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt())) {
            stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
                currentNbt.copyFrom(data);
            }));
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private void copyPaste(ItemUsageContext context, LayeredBlockEntity entity){
        World world = context.getWorld();
        NbtCompound nbt = context.getStack().getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();

        NbtCompound entityNbt = ((IEntityDataSaver)entity).getPersistentData();

        BlockState state = world.getBlockState(context.getBlockPos());
        int rotationAmount = getDirectionId(state.get(LayeredBlock.FACING));
        Direction directionClicked = context.getSide();
        Direction textureRotation = directionClicked;
        if(directionClicked != Direction.UP && directionClicked != Direction.DOWN) {
            for (int j = 0; j < rotationAmount; j++) {
                textureRotation = textureRotation.rotateYClockwise();
            }
        }

        byte direction = (byte)getDirectionId(textureRotation);

        if (nbt == null || !nbt.getBoolean("hasData")) {
            NbtCompound data = new NbtCompound();
            data.putBoolean("hasData", true);
            data.putByte("editSide", direction);

            NbtCompound blockData = new NbtCompound();
            for (int i = 0; i < 3; i++) {
                blockData.putString("layer" + i, entityNbt.getCompound("layer" + i).getString("" + direction));
                blockData.putString("layer" + i, entityNbt.getCompound("layer" + i).getString("" + direction));
                blockData.putString("layer" + i, entityNbt.getCompound("layer" + i).getString("" + direction));
                for (int j = 0; j < 3; j++) {
                    blockData.putInt(i + "_color_" + j, entityNbt.getCompound("layer" + i).getInt(direction + "_" + j + "_color"));
                }
            }
            data.put("data", blockData);
            context.getStack().apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
                currentNbt.copyFrom(data);
            }));
            PlayerEntity player = context.getPlayer();
            world.playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_SPYGLASS_USE, SoundCategory.PLAYERS, 1, 1, true);
        } else {
            NbtCompound entityData = entityNbt.copy();
            NbtCompound itemData = nbt.getCompound("data");
            for (int i = 0; i < 3; i++) {

                NbtCompound layerData = entityData.getCompound("layer" + i);
                layerData.putString("" + direction, itemData.getString("layer" + i));
                for (int j = 0; j < 3; j++) {
                    layerData.putInt(direction + "_" + j + "_color", itemData.getInt(i + "_color_" + j));
                }
                entityData.put("layer" + i, layerData);
            }

            ((IEntityDataSaver)entity).getPersistentData().copyFrom(entityData);
            PlayerEntity player = context.getPlayer();
            world.playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_SPYGLASS_USE, SoundCategory.PLAYERS, 1, 1, true);
            world.playSound(context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ(), SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1, 1, true);
        }
    }
    public static int getDirectionId(Direction direction) {
        switch (direction) {
            default:
                return 0;
            case WEST:
                return 1;
            case SOUTH:
                return 2;
            case EAST:
                return 3;
            case UP:
                return 4;
            case DOWN:
                return 5;
        }
    }
}

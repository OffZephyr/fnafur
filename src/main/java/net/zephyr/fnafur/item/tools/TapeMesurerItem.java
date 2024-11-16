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
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.basic_blocks.layered_block.LayeredBlock;
import net.zephyr.fnafur.blocks.basic_blocks.layered_block.LayeredBlockEntity;
import net.zephyr.fnafur.entity.cameramap.CameraMappingEntity;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.item.tablet.TabletItem;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class TapeMesurerItem extends Item {
    private CameraMappingEntity map = null;
    public TapeMesurerItem(Settings settings) {
        super(settings);
    }


    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking() && !user.getOffHandStack().isOf(ItemInit.TABLET)) {
            resetTape(user.getStackInHand(hand), user, world);
            return TypedActionResult.success(user.getStackInHand(hand), true);
        }
        return super.use(world, user, hand);
    }
    void resetTape(ItemStack stack, PlayerEntity player, World world){
        NbtCompound data = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        data.putBoolean("hasData", false);
        Text resetText = Text.translatable(FnafUniverseResuited.MOD_ID + ".tape_measure.reset");
        player.sendMessage(resetText, true);
        world.playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_SPYGLASS_STOP_USING, SoundCategory.PLAYERS, 1, 1, true);
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.copyFrom(data);
        }));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        if(!context.getPlayer().getOffHandStack().isEmpty() && context.getPlayer().getOffHandStack().getItem() instanceof TabletItem){
            NbtCompound data = context.getStack().getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
            ItemStack tablet = context.getPlayer().getOffHandStack();
            NbtCompound tabletData = tablet.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
            boolean hasCorner = data.getBoolean("hasCorner");
            if (context.getPlayer().isSneaking()) {
                if(hasCorner){
                    data.putBoolean("hasCorner", false);
                    data.putLong("setupCorner1", 0);
                    data.putLong("setupCorner2", 0);
                }
                else{
                    boolean bl = tabletData.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).isEmpty();
                    if(!bl){
                        NbtList mapNbt = tabletData.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).copy();
                        for(int i = 0; i < mapNbt.size(); i++){
                            if(mapNbt.get(i).getType() == NbtElement.LONG_ARRAY_TYPE){
                                long[] line = mapNbt.getLongArray(i);
                                BlockPos pos1 = BlockPos.fromLong(line[0]);
                                BlockPos pos2 = BlockPos.fromLong(line[1]);
                                Box box  = new Box(pos1.toCenterPos(), pos2.toCenterPos()).expand(0.5f);
                                if(box.contains(context.getBlockPos().toCenterPos())){
                                    mapNbt.remove(i);
                                    tabletData.put("CamMap", mapNbt);
                                    context.getStack().apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
                                        currentNbt.copyFrom(data);
                                    }));
                                    tablet.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
                                        currentNbt.copyFrom(tabletData);
                                    }));
                                    return ActionResult.SUCCESS;
                                }
                            }
                        }
                    }
                }
            }
            else {
                if (!hasCorner) {
                    data.putBoolean("hasCorner", true);
                    data.putLong("setupCorner1", context.getBlockPos().asLong());
                } else {
                    boolean bl = tabletData.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).isEmpty();
                    NbtList mapNbt = bl ? new NbtList() : tabletData.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).copy();

                    long[] line = new long[]{
                            data.getLong("setupCorner1"),
                            data.getLong("setupCorner2"),
                            0xFFFFFFFFL
                    };
                    NbtLongArray lineNbt = new NbtLongArray(line);
                    mapNbt.add(lineNbt);

                    tabletData.put("CamMap", mapNbt);

                    data.putBoolean("hasCorner", false);
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
            if (world.getBlockState(context.getBlockPos()).isOf(BlockInit.LAYERED_BLOCK_BASE)) {
                if (context.getPlayer().isSneaking()) {
                    resetTape(context.getStack(), context.getPlayer(), context.getWorld());
                    return ActionResult.PASS;
                }

                if (world.getBlockEntity(context.getBlockPos()) instanceof LayeredBlockEntity entity) {
                    copyPaste(context, entity);
                    return ActionResult.SUCCESS;
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

        boolean flag1 = data.getBoolean("hasData");
        boolean flag2 = entity instanceof PlayerEntity;
        Item item = flag2 ? ((PlayerEntity) entity).getOffHandStack().getItem() : null;
        boolean flag3 = flag2 && item instanceof TabletItem && data.getBoolean("hasCorner");

        if (!flag2 || !selected || !(item instanceof TabletItem)) {
            data.putBoolean("hasCorner", false);
        }

        data.putBoolean("used", flag1 || flag3);

        boolean hasCorner = data.getBoolean("hasCorner");
        if (entity instanceof PlayerEntity p) {
            if (hasCorner) {
                long corner1 = data.getLong("setupCorner1");
                HitResult blockHit = entity.raycast(20.0, 0.0f, false);
                BlockPos pos = ((BlockHitResult) blockHit).getBlockPos();
                BlockPos startPos = BlockPos.fromLong(corner1);

                data.putLong("setupCorner2", corner2(startPos, pos));

            }
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
        NbtCompound nbt = ItemNbtUtil.getNbt(context.getStack());

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
            NbtCompound entityData = ((IEntityDataSaver)entity).getPersistentData();
            data.putBoolean("hasData", true);
            data.putByte("editSide", direction);

            NbtCompound blockData = new NbtCompound();
            for (int i = 0; i < 3; i++) {
                blockData.putString("layer" + i, entityData.getCompound("layer" + i).getString("" + direction));
                blockData.putString("layer" + i, entityData.getCompound("layer" + i).getString("" + direction));
                blockData.putString("layer" + i, entityData.getCompound("layer" + i).getString("" + direction));
                for (int j = 0; j < 3; j++) {
                    blockData.putInt(i + "_color_" + j, entityData.getCompound("layer" + i).getInt(direction + "_" + j + "_color"));
                }
            }
            data.put("data", blockData);
            context.getStack().apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
                currentNbt.copyFrom(data);
            }));
            PlayerEntity player = context.getPlayer();
            world.playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_SPYGLASS_USE, SoundCategory.PLAYERS, 1, 1, true);
        } else {
            NbtCompound entityData = ((IEntityDataSaver)entity).getPersistentData().copy();
            NbtCompound itemData = nbt.getCompound("data");
            for (int i = 0; i < 3; i++) {

                NbtCompound layerData = entityData.getCompound("layer" + i);
                layerData.putString("" + direction, itemData.getString("layer" + i));
                for (int j = 0; j < 3; j++) {
                    layerData.putInt(direction + "_" + j + "_color", itemData.getInt(i + "_color_" + j));
                }
                entityData.put("layer" + i, layerData);
            }

            if(world.isClient()){
                GoopyNetworkingUtils.saveBlockNbt(entity.getPos(), entityData);
            }
            PlayerEntity player = context.getPlayer();
            world.playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_SPYGLASS_USE, SoundCategory.PLAYERS, 1, 1, true);
            world.playSound(context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ(), SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1, 1, true);
        }
    }
    private long corner2(BlockPos start, BlockPos end){
        int distX = MathHelper.abs(end.getX() - start.getX());
        int distZ = MathHelper.abs(end.getZ() - start.getZ());

        BlockPos pos = end;
        if(distX > distZ){
            pos = new BlockPos(end.getX(), start.getY(), start.getZ());
        }
        else {
            pos = new BlockPos(start.getX(), start.getY(), pos.getZ());
        }
        return pos.asLong();
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

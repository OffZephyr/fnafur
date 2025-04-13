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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.CallableByMesurer;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.item.tablet.TabletItem;

public class TapeMesurerItem extends Item {
    public TapeMesurerItem(Settings settings) {
        super(settings);
    }


    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking() && !user.getOffHandStack().isOf(ItemInit.TABLET)) {
            resetTape(user.getStackInHand(hand), user, world);
            return ActionResult.SUCCESS;
        }
        return super.use(world, user, hand);
    }
    void resetTape(ItemStack stack, PlayerEntity player, World world){
        NbtCompound data = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        data.putBoolean("hasData", false);
        Text resetText = Text.translatable(FnafUniverseRebuilt.MOD_ID + ".tape_measure.reset");
        player.sendMessage(resetText, true);
        world.playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_SPYGLASS_STOP_USING, SoundCategory.PLAYERS, 1, 1, true);
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.copyFrom(data);
        }));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        if(world.getBlockState(context.getBlockPos()).getBlock() instanceof CallableByMesurer callable){
            return callable.ExecuteAction(context);
        }

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
        return super.useOnBlock(context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        NbtCompound data = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();

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

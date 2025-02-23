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
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.zephyr.fnafur.item.tablet.TabletItem;

public class PaintbrushItem extends Item {
    public PaintbrushItem(Settings settings) {
        super(settings);
    }

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
        return super.useOnBlock(context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        NbtCompound data = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();

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
}

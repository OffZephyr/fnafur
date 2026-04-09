package net.zephyr.fnafur.blocks.curtain;

import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public class CurtainBlockItem extends BlockItem {
    public CurtainBlockItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().getBlockState(context.getClickedPos()).getBlock() instanceof CurtainBlock curtainBlock){
            CompoundTag nbt = ItemUtil.getNbt(context.getItemInHand());
            if(nbt.contains("linkPos")){
                BlockPos pos = BlockPos.of(nbt.getLong("linkPos").orElse(0L));
                if(pos.equals(context.getClickedPos())){
                    clearLink(context.getItemInHand());
                    return InteractionResult.SUCCESS;
                }
            }
            else{
                BlockPos pos = context.getClickedPos();
                nbt.putLong("linkPos", pos.asLong());
                ItemUtil.setNbt(context.getItemInHand(), nbt);
                Component text = Component.literal(pos.getX() + " " + pos.getY() + " " + pos.getZ());
                ItemUtil.setLore(context.getItemInHand(), text);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    void clearLink(ItemStack stack){
        CompoundTag nbt = ItemUtil.getNbt(stack);
        nbt.remove("linkPos");
        ItemUtil.setNbt(stack, nbt);
        ItemUtil.setLore(stack, new Component[0]);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        return super.place(context);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level world, @Nullable Player player, ItemStack stack, BlockState state) {
        CompoundTag nbt = ItemUtil.getNbt(stack);
        if(nbt.contains("linkPos")){
            BlockPos prePos = BlockPos.of(nbt.getLong("linkPos").orElse(0L));
            if(world.getBlockEntity(prePos) instanceof CurtainBlockEntity pre) {
                if (world.getBlockEntity(pos) instanceof CurtainBlockEntity post) {
                    ((IEntityDataSaver)pre).getPersistentData().putLong("next", pos.asLong());
                    ((IEntityDataSaver)post).getPersistentData().putLong("previous", prePos.asLong());
                    if(world.isClientSide()){
                        ((IEntityDataSaver)pre).getPersistentData().putBoolean("synced", true);
                        ((IEntityDataSaver)post).getPersistentData().putBoolean("synced", true);
                        GoopyNetworkingUtils.saveBlockNbt(prePos, ((IEntityDataSaver)pre).getPersistentData());
                        GoopyNetworkingUtils.saveBlockNbt(pos, ((IEntityDataSaver)post).getPersistentData());
                    }
                }
            }
            clearLink(stack);
        }
        return super.updateCustomBlockEntityTag(pos, world, player, stack, state);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot slot) {

        if (entity instanceof Player p) {
            CompoundTag nbt = ItemUtil.getNbt(stack);
            if (nbt.contains("linkPos")) {
                if (stack != p.getMainHandItem()) {
                    clearLink(stack);
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot);
    }
}

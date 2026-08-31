package net.zephyr.fnafur.item.tools;

import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.linking.EnergyTarget;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.jetbrains.annotations.Nullable;

public class WrenchItem extends Item {
    public WrenchItem(Properties settings) {
        super(settings);
    }

    @Override
    public boolean canDestroyBlock(ItemStack stack, BlockState state, Level world, BlockPos pos, LivingEntity user) {
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getPlayer() != null && ((IUniversePlayer)context.getPlayer()).isUsingVanniMask() && context.getLevel().getBlockEntity(context.getClickedPos()) instanceof LinkTarget t && !t.getSources().isEmpty()){
            for (int i = 0; i < t.getSources().size(); i++){
                if(t.getSources().get(i) instanceof IEntityDataSaver ent){
                    t.removeSource(ent);
                    ((LinkSource)ent).getTargets().remove(((IEntityDataSaver) t));

                    if(t instanceof EnergyTarget e) {
                        BlockEntity ent2 = (BlockEntity) t;
                        e.updateStatus(ent2.getLevel(), ent2.getBlockPos(), ent);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        else if(context.getPlayer() != null && ((IUniversePlayer)context.getPlayer()).isUsingVanniMask() && context.getLevel().getBlockEntity(context.getClickedPos()) instanceof LinkSource s && !s.getTargets().isEmpty()){
            for (int i = 0; i < s.getTargets().size(); i++){
                if(s.getTargets().get(i) instanceof IEntityDataSaver ent && ent instanceof LinkTarget){

                    ((LinkTarget)ent).removeSource((IEntityDataSaver) s);
                    s.getTargets().remove(ent);

                    if(ent instanceof EnergyTarget e) {
                        BlockEntity ent2 = (BlockEntity) s;
                        e.updateStatus(ent2.getLevel(), ent2.getBlockPos(), ((IEntityDataSaver) s));
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot slot) {
        if(entity instanceof Player p && (!((IUniversePlayer)p).isUsingVanniMask() || !p.getMainHandItem().equals(stack))){
            CompoundTag nbt = ItemUtil.getNbt(stack);
            if(nbt.contains("startLink")){
                nbt.remove("startLink");
                ItemUtil.setNbt(stack, nbt);
            }
        }

        super.inventoryTick(stack, world, entity, slot);
    }
}




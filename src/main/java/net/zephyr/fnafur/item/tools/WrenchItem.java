package net.zephyr.fnafur.item.tools;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.linking.EnergyTarget;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.jetbrains.annotations.Nullable;

public class WrenchItem extends Item {
    public WrenchItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity user) {
        return false;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getPlayer() != null && ((IUniversePlayer)context.getPlayer()).isUsingVanniMask() && context.getWorld().getBlockEntity(context.getBlockPos()) instanceof LinkTarget t && !t.getSources().isEmpty()){
            for (int i = 0; i < t.getSources().size(); i++){
                if(t.getSources().get(i) instanceof IEntityDataSaver ent){
                    t.removeSource(ent);
                    ((LinkSource)ent).getTargets().remove(((IEntityDataSaver) t));

                    if(t instanceof EnergyTarget e) {
                        BlockEntity ent2 = (BlockEntity) t;
                        e.updateStatus(ent2.getWorld(), ent2.getPos(), ent);
                    }
                }
            }
            return ActionResult.SUCCESS;
        }
        else if(context.getPlayer() != null && ((IUniversePlayer)context.getPlayer()).isUsingVanniMask() && context.getWorld().getBlockEntity(context.getBlockPos()) instanceof LinkSource s && !s.getTargets().isEmpty()){
            for (int i = 0; i < s.getTargets().size(); i++){
                if(s.getTargets().get(i) instanceof IEntityDataSaver ent && ent instanceof LinkTarget){

                    ((LinkTarget)ent).removeSource((IEntityDataSaver) s);
                    s.getTargets().remove(ent);

                    if(ent instanceof EnergyTarget e) {
                        BlockEntity ent2 = (BlockEntity) s;
                        e.updateStatus(ent2.getWorld(), ent2.getPos(), ((IEntityDataSaver) s));
                    }
                }
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if(entity instanceof PlayerEntity p && (!((IUniversePlayer)p).isUsingVanniMask() || !p.getMainHandStack().equals(stack))){
            NbtCompound nbt = ItemNbtUtil.getNbt(stack);
            if(nbt.contains("startLink")){
                nbt.remove("startLink");
                ItemNbtUtil.setNbt(stack, nbt);
            }
        }

        super.inventoryTick(stack, world, entity, slot);
    }
}




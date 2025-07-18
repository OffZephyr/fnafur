package net.zephyr.fnafur.blocks.energy.blocks.switches.office_buttons;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.linking.EnergySource;
import net.zephyr.fnafur.blocks.linking.EnergyTarget;
import net.zephyr.fnafur.blocks.linking.links.energy.EnergySourceTargetPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.item.tools.WrenchItem;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;

public class OfficeButtonsBlockEntity extends EnergySourceTargetPropBlockEntity {
    public OfficeButtonsBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.OFFICE_BUTTONS, pos, state);
    }

    public ActionResult tryStartLink(PlayerEntity player, BlockPos pos, int hitButton) {
        if(hitButton != -1){
            ItemStack stack = player.getMainHandStack();
            if(stack.getItem() instanceof WrenchItem && ((IUniversePlayer)player).isUsingVanniMask()){
                NbtCompound nbt = ItemNbtUtil.getNbt(stack);

                nbt.putInt("buttonIndex", hitButton);
                BlockPos startPos = nbt.get("startLink", BlockPos.CODEC).orElse(BlockPos.ORIGIN);
                if(player.isSneaking()){
                    nbt.remove("startLink");
                    for(IEntityDataSaver ent : getTargets()){
                        unlink(ent);
                    }
                }
                else if(startPos.equals(pos) || startPos != BlockPos.ORIGIN){
                    nbt.remove("startLink");
                }
                else{
                    nbt.put("startLink", BlockPos.CODEC, pos);
                }
                ItemNbtUtil.setNbt(stack, nbt);
                return ActionResult.SUCCESS;
            }
        }
        return null;
    }

    @Override
    public boolean canLink(IEntityDataSaver link) {
        return link instanceof EnergyTarget;
    }

    @Override
    public boolean isSendingPower(IEntityDataSaver target) {
        int id = ((EnergyTarget)target).getButtonId((IEntityDataSaver)this);
        if(id > -1){
            BlockState state = getWorld().getBlockState(getPos());
            if(state.getBlock() instanceof OfficeButtons){
                if((id == 0 && state.get(OfficeButtons.DOOR_ON)) || (id == 1 && state.get(OfficeButtons.LIGHT_ON))){
                    return isReceivingPower();
                }
            }
        }
        return false;
    }
}

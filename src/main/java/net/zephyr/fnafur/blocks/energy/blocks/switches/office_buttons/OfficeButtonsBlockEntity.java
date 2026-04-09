package net.zephyr.fnafur.blocks.energy.blocks.switches.office_buttons;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.blocks.linking.EnergyTarget;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.blocks.linking.links.energy.EnergySourceTargetPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.item.tools.WrenchItem;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;

public class OfficeButtonsBlockEntity extends EnergySourceTargetPropBlockEntity {
    public OfficeButtonsBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.OFFICE_BUTTONS, pos, state);
    }

    public InteractionResult tryStartLink(Player player, BlockPos pos, int hitButton) {
        if(hitButton != -1){
            ItemStack stack = player.getMainHandItem();
            if(stack.getItem() instanceof WrenchItem && ((IUniversePlayer)player).isUsingVanniMask()){
                CompoundTag nbt = ItemUtil.getNbt(stack);

                nbt.putInt("buttonIndex", hitButton);
                BlockPos startPos = nbt.read("startLink", BlockPos.CODEC).orElse(BlockPos.ZERO);
                if(player.isShiftKeyDown()){
                    nbt.remove("startLink");
                    for(IEntityDataSaver ent : getTargets()){
                        unlink(ent);
                    }
                }
                else if(startPos.equals(pos) || startPos != BlockPos.ZERO){
                    nbt.remove("startLink");
                }
                else{
                    nbt.store("startLink", BlockPos.CODEC, pos);
                }
                ItemUtil.setNbt(stack, nbt);
                return InteractionResult.SUCCESS;
            }
        }
        return null;
    }

    @Override
    public boolean canLink(IEntityDataSaver link) {
        boolean bl1 = link instanceof EnergyTarget;
        return bl1;
    }

    @Override
    public boolean isSendingPower(IEntityDataSaver target) {
        int id = ((LinkTarget)target).getButtonId((IEntityDataSaver)this);
        if(id > -1){
            BlockState state = getLevel().getBlockState(getBlockPos());
            if(state.getBlock() instanceof OfficeButtons){
                if((id == 0 && state.getValue(OfficeButtons.DOOR_ON)) || (id == 1 && state.getValue(OfficeButtons.LIGHT_ON))){
                    return isReceivingPower();
                }
            }
        }
        return false;
    }
}

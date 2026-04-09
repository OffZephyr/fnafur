package net.zephyr.fnafur.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.zephyr.fnafur.blocks.energy.blocks.switches.office_buttons.OfficeButtons;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(method = "getDirectSignal", at = @At("RETURN"), cancellable = true)
    public void getMixinStrongRedstonePower(BlockState state, BlockGetter world, BlockPos pos, Direction direction, CallbackInfoReturnable<Integer> ci) {

        if(direction.getAxis().isHorizontal()) {
            BlockPos[] poses = OfficeButtons.getLightCheckPoses(pos, direction, false);

            boolean powered = false;
            for (BlockPos pos1 : poses) {
                if (world.getBlockState(pos1).getBlock() instanceof OfficeButtons) {
                    if (world.getBlockState(pos1).getValue(OfficeButtons.FACING) == direction && world.getBlockState(pos1).getValue(OfficeButtons.LIGHT_ON)) powered = true;
                }
            }

            if (powered) {
                ci.setReturnValue(15);
            }
        }
    }
    @Inject(method = "getSignal", at = @At("RETURN"), cancellable = true)
    public void getMixinWeakRedstonePower(BlockState state, BlockGetter world, BlockPos pos, Direction direction, CallbackInfoReturnable<Integer> ci) {

        if(direction.getAxis().isHorizontal()) {
            BlockPos[] poses = OfficeButtons.getLightCheckPoses(pos, direction, true);

            boolean powered = false;
            for (BlockPos pos1 : poses) {
                if (world.getBlockState(pos1).getBlock() instanceof OfficeButtons) {
                    if (world.getBlockState(pos1).getValue(OfficeButtons.FACING) == direction && world.getBlockState(pos1).getValue(OfficeButtons.LIGHT_ON)) powered = true;
                }
            }

            if (powered) {
                ci.setReturnValue(15);
            }
        }
    }
}

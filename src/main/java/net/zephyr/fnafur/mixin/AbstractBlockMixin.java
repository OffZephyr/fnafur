package net.zephyr.fnafur.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.wall_props.office_buttons.OfficeButtons;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {

    @Inject(method = "getStrongRedstonePower", at = @At("RETURN"), cancellable = true)
    public void getMixinStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction, CallbackInfoReturnable<Integer> ci) {

        if(direction.getAxis().isHorizontal()) {
            BlockPos[] poses = OfficeButtons.getLightCheckPoses(pos, direction, false);

            boolean powered = false;
            for (BlockPos pos1 : poses) {
                if (world.getBlockState(pos1).getBlock() instanceof OfficeButtons) {
                    if (world.getBlockState(pos1).get(OfficeButtons.FACING) == direction && world.getBlockState(pos1).get(OfficeButtons.LIGHT_ON)) powered = true;
                }
            }

            if (powered) {
                ci.setReturnValue(15);
            }
        }
    }
    @Inject(method = "getWeakRedstonePower", at = @At("RETURN"), cancellable = true)
    public void getMixinWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction, CallbackInfoReturnable<Integer> ci) {

        if(direction.getAxis().isHorizontal()) {
            BlockPos[] poses = OfficeButtons.getLightCheckPoses(pos, direction, true);

            boolean powered = false;
            for (BlockPos pos1 : poses) {
                if (world.getBlockState(pos1).getBlock() instanceof OfficeButtons) {
                    if (world.getBlockState(pos1).get(OfficeButtons.FACING) == direction && world.getBlockState(pos1).get(OfficeButtons.LIGHT_ON)) powered = true;
                }
            }

            if (powered) {
                ci.setReturnValue(15);
            }
        }
    }
}

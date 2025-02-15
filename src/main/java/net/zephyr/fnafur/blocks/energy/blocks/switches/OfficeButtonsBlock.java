package net.zephyr.fnafur.blocks.energy.blocks.switches;

import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.energy.enums.EnergyNode;
import net.zephyr.fnafur.blocks.energy.enums.EnergyNodeType;

public class OfficeButtonsBlock implements EnergyNode {

    @Override
    public ActionResult addNode(World world, BlockPos pos, BlockPos toAdd, Vec3d hit) {
        return null;
    }

    @Override
    public ActionResult remNode(World world, BlockPos pos, BlockPos toRem, Vec3d hit) {
        return null;
    }

    @Override
    public boolean isPowered(BlockView world, BlockPos pos) {
        return false;
    }
    

    @Override
    public EnergyNodeType nodeType() {
        return EnergyNodeType.SWITCH;
    }
}

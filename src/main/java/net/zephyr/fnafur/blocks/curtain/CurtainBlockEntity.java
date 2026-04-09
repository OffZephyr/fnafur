package net.zephyr.fnafur.blocks.curtain;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tuple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors.TileDoorBlock;
import net.zephyr.fnafur.blocks.linking.EnergySource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.blocks.linking.links.LinkTargetBlockEntity;
import net.zephyr.fnafur.blocks.linking.links.energy.EnergyTargetBlockEntity;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.EasingMathUtil;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.function.Function;

public class CurtainBlockEntity extends EnergyTargetBlockEntity {
    public static int MAX_HEIGHT = 10;
    int height = 0;
    float prevOpenIndex = 0;
    float openIndex = 0;
    boolean isOpen = false;
    boolean isOpening = false;

    public CurtainData frontCurtain;
    public CurtainData backCurtain;
    public CurtainData prevFrontCurtain;
    public CurtainData prevBackCurtain;

    public CurtainBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.CURTAIN, pos, state);

        frontCurtain = new CurtainData(0.25f, 0xFFFFFFFF, false);
        backCurtain = new CurtainData(0.45f, 0xFFDDDDDD, true);
        prevFrontCurtain = frontCurtain;
        prevBackCurtain = backCurtain;
    }

    public void tick(Level world, BlockPos blockPos, BlockState state, CurtainBlockEntity entity) {
        if(!isFirst() && LinkTargetBlockEntity.allTargets.contains((IEntityDataSaver)this)) LinkTargetBlockEntity.allTargets.remove((IEntityDataSaver) this);
        if(world.isClientSide()){
            if(!((IEntityDataSaver)this).getPersistentData().contains("synced")){
                GoopyNetworkingUtils.getNbtFromServer(getBlockPos());
            }
        }
        if(!isFirst()){
            if(getPrevious(world) instanceof CurtainBlockEntity e){
                ((IEntityDataSaver)entity).getPersistentData().putInt("height", ((IEntityDataSaver)e).getPersistentData().getInt("height").orElse(1));
                ((IEntityDataSaver)entity).getPersistentData().putBoolean("left", ((IEntityDataSaver)e).getPersistentData().getBoolean("left").orElse(false));
                ((IEntityDataSaver)entity).getPersistentData().putBoolean("powered", ((IEntityDataSaver)e).getPersistentData().getBooleanOr("powered", false));
            }
        }
        else{
            height = MAX_HEIGHT;
            for(int i = 1; i < MAX_HEIGHT; i++){
                BlockPos checkPos = blockPos.offset(0, -i, 0);
                if(!world.getBlockState(checkPos).isAir() && !world.getBlockState(checkPos).canBeReplaced()){
                    height = i;
                    break;
                }
            }
            ((IEntityDataSaver)entity).getPersistentData().putInt("height", height);
            ((IEntityDataSaver)entity).getPersistentData().putBoolean("left", getNextFacing() == getFacing().getCounterClockWise());


        }

        if(!isFirst() && !(getPrevious(world) instanceof CurtainBlockEntity)) ((IEntityDataSaver)this).getPersistentData().remove("previous");
        if(!isLast() && !(getNext(world) instanceof CurtainBlockEntity)) ((IEntityDataSaver)this).getPersistentData().remove("next");

        if(getLevel().isClientSide() && !isLast()){

            float speed = 0.5f;
            if(((IEntityDataSaver)this).getPersistentData().getBooleanOr("powered", false)){
                open(getLevel(), speed);
            }
            else{
                close(getLevel(), speed);
            }
            float clamp = isNextLast() ? getFacing().getAxis() == Direction.Axis.X ? 19.5f : 18 : 20;
            openIndex =  Math.clamp(openIndex, 0, clamp);

            prevFrontCurtain = frontCurtain.copy();
            prevBackCurtain = backCurtain.copy();
            frontCurtain = updateCurtain(frontCurtain);
            backCurtain = updateCurtain(backCurtain);
        }
    }

    @Override
    public void updateStatus(Level world, BlockPos sourcePos, IEntityDataSaver source) {
        if(world.isClientSide()){
            ((IEntityDataSaver)this).getPersistentData().putBoolean("powered", isReceivingPower());
            GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), ((IEntityDataSaver)this).getPersistentData());
        }
        super.updateStatus(world, sourcePos, source);
    }

    void open(Level world, float speed){
        boolean canOpen = isFirst() || getPrevious(world) instanceof CurtainBlockEntity ent && ent.isOpen;

        isOpening = canOpen;
        if(canOpen && !isOpen && openIndex + speed >= 20f){
            isOpen = true;
            openIndex = 20f;
            if(getNext(world) instanceof CurtainBlockEntity ent){
                ent.openIndex = 0;
            }
        }
        if(!isOpen || !canOpen){
            openIndex += speed;
            if(openIndex >= 20f && !canOpen){
                openIndex = openIndex - 20f;
            }
        }

    }
    void close(Level world, float speed) {
        boolean canClose = isLast() || (getNext(world) instanceof CurtainBlockEntity ent && !ent.isOpen);

        isOpening = canClose;
        if (canClose && isOpen && openIndex - speed <= 0f) {
            isOpen = false;
            openIndex = 0f;
            if (getPrevious(world) instanceof CurtainBlockEntity ent) {
                ent.openIndex = 20f;
            }
        }
        if(!isOpen || canClose) {
            openIndex -= speed;
            if (openIndex <= 0f && !canClose) {
                openIndex = openIndex + 20f;
            }
        }
    }

    @Override
    public void setRemoved() {
        if(!isFirst()){
            if(getPrevious(level) instanceof CurtainBlockEntity c){
                ((IEntityDataSaver)c).getPersistentData().remove("next");
                if(getLevel().isClientSide()){
                    GoopyNetworkingUtils.saveBlockNbt(c.getBlockPos(), ((IEntityDataSaver)c).getPersistentData());
                }
                if(c.isFirst()) LinkTarget.allTargets.add(((IEntityDataSaver)c));
            }
        }
        if(!isLast()){
            if(getNext(level) instanceof CurtainBlockEntity c){
                ((IEntityDataSaver)c).getPersistentData().remove("previous");
                if(getLevel().isClientSide()){
                    GoopyNetworkingUtils.saveBlockNbt(c.getBlockPos(), ((IEntityDataSaver)c).getPersistentData());
                }
            }
        }
        super.setRemoved();
    }

    @Nullable
    public CurtainBlockEntity getNext(Level world){
        CompoundTag nbt = ((IEntityDataSaver)this).getPersistentData();
        if(!isLast()){
            BlockPos pos = BlockPos.of(nbt.getLong("next").orElse(0L));
            if(world.getBlockEntity(pos) instanceof CurtainBlockEntity next){
                return next;
            }
        }
        return null;
    }
    @Nullable
    public CurtainBlockEntity getPrevious(Level world){
        CompoundTag nbt = ((IEntityDataSaver)this).getPersistentData();
        if(!isFirst()){
            BlockPos pos = BlockPos.of(nbt.getLong("previous").orElse(0L));
            if(world.getBlockEntity(pos) instanceof CurtainBlockEntity previous){
                return previous;
            }
        }
        return null;
    }
    @Nullable
    public CurtainBlockEntity getFirst(Level world){
        if(isFirst()) return this;
        CurtainBlockEntity entity = this;
        CompoundTag nbt = ((IEntityDataSaver)entity).getPersistentData();
        while(nbt.contains("previous")){
            BlockPos pos = BlockPos.of(nbt.getLong("previous").orElse(0L));
            if(world.getBlockEntity(pos) instanceof CurtainBlockEntity previous && ((IEntityDataSaver)previous).getPersistentData().contains("previous")){
                entity = previous;
            }
            else{
                break;
            }
            nbt = ((IEntityDataSaver)entity).getPersistentData();
        }
        return entity;
    }

    public boolean isLast(){
        return !((IEntityDataSaver)this).getPersistentData().contains("next");
    }
    public boolean isNextLast(){
        return !isLast() && !((IEntityDataSaver)getNext(getLevel())).getPersistentData().contains("next");
    }

    public boolean isFirst(){
        return !((IEntityDataSaver)this).getPersistentData().contains("previous");
    }

    Direction getFacing(){
        if(getLevel().getBlockState(getBlockPos()).getBlock() instanceof CurtainBlock){
            return getLevel().getBlockState(getBlockPos()).getValue(CurtainBlock.FACING);
        }
        return Direction.NORTH;
    }
    Direction getNextFacing(){
        CompoundTag nbt = ((IEntityDataSaver)this).getPersistentData();
        if(!isLast()){
            BlockPos pos = BlockPos.of(nbt.getLong("next").orElse(0L));
            if(getLevel().getBlockState(pos).getBlock() instanceof CurtainBlock){
                return getLevel().getBlockState(pos).getValue(CurtainBlock.FACING);
            }
        }
        return Direction.NORTH;
    }

    public CurtainData updateCurtain(CurtainData data){
        float backOffset = data.BACK_OFFSET;
        int color = data.COLOR;
        boolean canOpen = data.CAN_OPEN;

        CompoundTag nbt = ((IEntityDataSaver)this).getPersistentData();

        BlockPos previous = isFirst() ? null : getPrevious(getLevel()).getBlockPos();
        BlockPos next = isLast() ? null : getNext(getLevel()).getBlockPos();
        Direction facing = getFacing();
        Direction nextFacing = getNextFacing();
        boolean isOpening = this.isOpening || isOpen;
        float openIndex = this.openIndex/20f;

        if(next != null) {
            float startX = 0.5f + backOffset * facing.getStepX();
            float startZ = 0.5f + backOffset * facing.getStepZ();
            float endX = 0.5f + backOffset * facing.getStepX();
            float endZ = 0.5f + backOffset * facing.getStepZ();

            float yOffset = 0.25f;

            Vec3 difference = next.getCenter().add(getBlockPos().getCenter().scale(-1));

            float startX2 = 0.5f + backOffset * nextFacing.getStepX();
            float startZ2 = 0.5f + backOffset * nextFacing.getStepZ();
            float endX2 = 0.5f + backOffset * nextFacing.getStepX();
            float endZ2 = 0.5f + backOffset * nextFacing.getStepZ();

            startX2 += (float) difference.x();
            endX2 += (float) difference.x();
            startZ2 += (float) difference.z();
            endZ2 += (float) difference.z();

            Vec3 length0 = new Vec3(endX, 0, endZ);
            Vec3 length1 = new Vec3(startX2, 0, startZ2);
            double length = length0.distanceTo(length1);
            Vec3 length3 = new Vec3(startX, 0, startZ);
            Vec3 length4 = new Vec3(endX2, 0, endZ2);
            double length2 = length3.distanceTo(length4);

            length = Math.min(length,length2);
            boolean left = nbt.getBoolean("left").orElse(false);

            float dir = facing.getAxis() == Direction.Axis.X ? -0.5f : 0.5f;
            if(isFirst()){
                startX += dir * facing.getStepZ();
                startZ += dir * facing.getStepX();
                endX -= dir * facing.getStepZ();
                endZ -= dir * facing.getStepX();
            }
            if(!isLast() && getNext(getLevel()).isLast()){
                startX2 -= dir * nextFacing.getStepZ();
                startZ2 -= dir * nextFacing.getStepX();
                endX2 += dir * nextFacing.getStepZ();
                endZ2 += dir * nextFacing.getStepX();
            }

            Tuple<Function<Double, Double>, Function<Double, Double>> easing = getEasing(next);

            length *= 2f;
            double lengthOpen = ((facing.getAxis() == Direction.Axis.X ? easing.getA().apply((double)(openIndex)) : easing.getB().apply((double)(openIndex)) )* length);
            if(!canOpen || !isOpening) lengthOpen = 0;
            //lengthOpen = 0;

            float prevUWidth = 0;
            int intLength = (int) (length);

            data.updateLength(intLength + 1);

            int i = 0;
            float offset = 1;
            for(double l = lengthOpen; l <= intLength ; l ++) {
                float lerpIndex = (float) l /intLength;
                float lerpIndex2 = (float) (l+offset) /intLength;
                lerpIndex = (float) easing.getA().apply((double) lerpIndex).doubleValue();
                lerpIndex2 = (float) easing.getA().apply((double) lerpIndex2).doubleValue();
                float lerpIndex3 = (float) easing.getB().apply((double) lerpIndex).doubleValue();
                float lerpIndex4 = (float) easing.getB().apply((double) lerpIndex2).doubleValue();


                float posX1 = left? startX : endX;
                float posX2 = left? endX2 : startX2;
                float posZ1 = left? startZ : endZ;
                float posZ2 = left? endZ2 : startZ2;

                float x1 = Mth.lerp(lerpIndex2, posX1, posX2);
                float x2 = Mth.lerp(lerpIndex, posX1, posX2);
                float z1 = Mth.lerp(lerpIndex4, posZ1, posZ2);
                float z2 = Mth.lerp(lerpIndex3, posZ1, posZ2);
                float y2 = (float) Mth.lerp((float) (l) /intLength, yOffset, yOffset + difference.y());
                float y1 = (float) Mth.lerp((float) (l+offset) /intLength, yOffset, yOffset + difference.y());

                if(l >= intLength - offset){
                    x1 = posX2;
                    z1 = posZ2;
                }
                float uWidth = (float) new Vec3(x1, y1, z1).distanceTo(new Vec3(x2, y2, z2));
                uWidth/=2f;

                float u = prevUWidth;
                if(canOpen && !isOpening){
                    u -= openIndex;
                }
                prevUWidth += uWidth;

                data.addVert(i, x1, x2, y1, y2, z1, z2, u, uWidth);
                i++;
            }
        }
        data.height = data.CAN_OPEN ? this.height : 1;
        return data;
    }

    Tuple<Function<Double, Double>, Function<Double, Double>> getEasing(BlockPos next){
        Direction direction = getFacing();
        Direction nextDirection = getNextFacing();

        Tuple<Function<Double, Double>, Function<Double, Double>> pair = new Tuple<>((index) -> index, (index) -> index);


        if(direction == nextDirection) {
            if((direction.getAxis() == Direction.Axis.X && getBlockPos().getX() == next.getX()) || (direction.getAxis() == Direction.Axis.Z && getBlockPos().getZ() == next.getZ())){
                return pair;
            }
            return new Tuple<>(EasingMathUtil::easeOutCirc, EasingMathUtil::easeInCirc);
        }
        return switch (direction.getAxis()){
            default -> pair;
            case X -> new Tuple<>(EasingMathUtil::easeInCirc, EasingMathUtil::easeOutCirc);
            case Z -> new Tuple<>(EasingMathUtil::easeOutCirc, EasingMathUtil::easeInCirc);
        };
    }


}


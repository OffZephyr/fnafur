package net.zephyr.fnafur.blocks.geo_doors;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.block_init.GeoBlockEntityInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Objects;

public class GeoDoorEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    boolean open;
    boolean front;
    public GeoDoorEntity(BlockPos pos, BlockState state) {
        super(GeoBlockEntityInit.GEO_DOOR, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 1, this::animController));
    }

    private PlayState animController(AnimationState<GeoDoorEntity> geoDoorEntityAnimationState) {
        String openAnim = front ? "animation.geo_door.open_back" : "animation.geo_door.open";
        String closeAnim = front ? "animation.geo_door.close_back" : "animation.geo_door.close";
        if(open) return geoDoorEntityAnimationState.setAndContinue(RawAnimation.begin().thenPlayAndHold(openAnim));
        else return geoDoorEntityAnimationState.setAndContinue(RawAnimation.begin().thenPlayAndHold(closeAnim));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void tick(World world, BlockPos pos, BlockState state, GeoDoorEntity blockEntity) {
        if(state.get(GeoDoor.MAIN)) {
            Box box = ((GeoDoor) state.getBlock()).getEntityArea(state, pos);
            Entity entity = checkOpen(world, box);
            open = entity != null;

            for(BlockPos pos1 : ((GeoDoor) state.getBlock()).doorPos(state, pos)){
                BlockState doorState = world.getBlockState(pos1);
                if(doorState.contains(GeoDoor.OPEN)) {
                    world.setBlockState(pos1, doorState.with(GeoDoor.OPEN, open));
                }
            }
            if(open) {
                float rot = state.get(GeoDoor.FACING).getPositiveHorizontalDegrees();
                float entityRot = entity.getYaw() + 90 - rot;
                int turns = (int) (entityRot / 360);
                float entityYaw = entityRot - (360 * turns);
                entityYaw = entityRot < 0 ? 360 + entityYaw : entityYaw;
                front = entityYaw > 0 && entityYaw < 180;

            }
        }
    }
    private Entity checkOpen(World world, Box box){
        List<Entity> list = world.getOtherEntities(null, box);
        if(list.isEmpty()) return null;
        for(Entity ent : list){
            BlockHitResult result = (BlockHitResult) ent.raycast(5, 0.0f, true);
            if(world.getBlockState(result.getBlockPos()).getBlock() instanceof GeoDoor && !ent.isSneaking()){
                return ent;
            }
        }
        return null;
    }

    public Identifier getTexture() {
        if(getCachedState().getBlock() instanceof GeoDoor door) {
            return door.getTexture();
        }
        return null;
    }


    public Identifier getWindowTexture() {
        if(getCachedState().getBlock() instanceof GeoDoor door) {
            return door.getWindowTexture();
        }
        return null;
    }

    public Identifier getModel() {
        if(getCachedState().getBlock() instanceof GeoDoor door) {
            return door.getModel();
        }
        return null;
    }
}

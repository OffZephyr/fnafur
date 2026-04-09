package net.zephyr.fnafur.blocks.geo_doors;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.init.block_init.GeoBlockEntityInit;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

import software.bernie.geckolib.animation.object.PlayState;
import software.bernie.geckolib.animation.state.AnimationTest;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class GeoDoorEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    boolean open;
    boolean front;
    public GeoDoorEntity(BlockPos pos, BlockState state) {
        super(GeoBlockEntityInit.GEO_DOOR, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("idle", 1, this::animController));
    }

    private PlayState animController(AnimationTest<GeoAnimatable> geoAnimatableAnimationTest) {
        String openAnim = front ? "animation.geo_door.open_back" : "animation.geo_door.open";
        String closeAnim = front ? "animation.geo_door.close_back" : "animation.geo_door.close";
        if(open) geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenLoop(openAnim));
        else geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenLoop(closeAnim));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void tick(Level world, BlockPos pos, BlockState state, GeoDoorEntity blockEntity) {
        if(state.getValue(GeoDoor.MAIN)) {
            AABB box = ((GeoDoor) state.getBlock()).getEntityArea(state, pos);
            Entity entity = checkOpen(world, box);
            open = entity != null;

            for(BlockPos pos1 : ((GeoDoor) state.getBlock()).doorPos(state, pos)){
                BlockState doorState = world.getBlockState(pos1);
                if(doorState.hasProperty(GeoDoor.OPEN)) {
                    world.setBlockAndUpdate(pos1, doorState.setValue(GeoDoor.OPEN, open));
                }
            }
            if(open) {
                float rot = state.getValue(GeoDoor.FACING).toYRot();
                float entityRot = entity.getYRot() + 90 - rot;
                int turns = (int) (entityRot / 360);
                float entityYaw = entityRot - (360 * turns);
                entityYaw = entityRot < 0 ? 360 + entityYaw : entityYaw;
                front = entityYaw > 0 && entityYaw < 180;

            }
        }
    }
    private Entity checkOpen(Level world, AABB box){
        List<Entity> list = world.getEntities(null, box);
        if(list.isEmpty()) return null;
        for(Entity ent : list){
            BlockHitResult result = (BlockHitResult) ent.pick(5, 0.0f, true);
            if(world.getBlockState(result.getBlockPos()).getBlock() instanceof GeoDoor && !ent.isShiftKeyDown()){
                return ent;
            }
        }
        return null;
    }

    public Identifier getTexture() {
        if(getBlockState().getBlock() instanceof GeoDoor door) {
            return door.getTexture();
        }
        return null;
    }


    public Identifier getWindowTexture() {
        if(getBlockState().getBlock() instanceof GeoDoor door) {
            return door.getWindowTexture();
        }
        return null;
    }

    public Identifier getModel() {
        if(getBlockState().getBlock() instanceof GeoDoor door) {
            return door.getModel();
        }
        return null;
    }
}

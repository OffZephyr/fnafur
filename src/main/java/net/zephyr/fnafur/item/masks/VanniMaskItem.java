package net.zephyr.fnafur.item.masks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.IHasArmPos;
import net.zephyr.fnafur.util.ItemNbtUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class VanniMaskItem extends Item implements GeoItem, IHasArmPos {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public VanniMaskItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isPerspectiveAware() { // state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE).
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {

        if (entity instanceof PlayerEntity p) {
            if (!p.getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET).equals(stack)) {
                NbtCompound nbt = ItemNbtUtil.getNbt(stack);
                nbt.putBoolean("inVanniMask", false);
                ItemNbtUtil.setNbt(stack, nbt);
            }
        }

        super.inventoryTick(stack, world, entity, slot);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("idle", 0, this::animController));
    }

    private PlayState animController(AnimationTest<GeoAnimatable> geoAnimatableAnimationTest) {

        if (geoAnimatableAnimationTest.renderState().hasGeckolibData(CustomDataTickets.IS_MASK_ON)) {
            ItemDisplayContext context = geoAnimatableAnimationTest.getData(DataTickets.ITEM_RENDER_PERSPECTIVE);
            boolean isOn = Boolean.TRUE.equals(geoAnimatableAnimationTest.renderState().getGeckolibData(CustomDataTickets.IS_MASK_ON));
            if (MinecraftClient.getInstance().currentScreen == null && (context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)) {

                RawAnimation putOn = RawAnimation.begin().thenPlayAndHold("animation.ar_mask.open");

                boolean bl = geoAnimatableAnimationTest.controller().getCurrentAnimation() != null && geoAnimatableAnimationTest.controller().getCurrentAnimation().animation().name().equals("animation.ar_mask.open") && geoAnimatableAnimationTest.controller().getAnimationState() == AnimationController.State.PAUSED;
                if (isOn) {
                    if(!geoAnimatableAnimationTest.controller().getCurrentAnimation().animation().name().equals("animation.ar_mask.open")){
                        return geoAnimatableAnimationTest.setAndContinue(putOn);
                    }
                    return PlayState.CONTINUE;
                } else {
                    if (bl) {
                        return geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.ar_mask.close"));
                    } else if (geoAnimatableAnimationTest.controller().getAnimationState() == AnimationController.State.PAUSED) {
                        return geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.ar_mask.idle"));
                    }
                }
                return PlayState.CONTINUE;
            }
            if(isOn){
                return geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenLoop("animation.ar_mask.thirdperson"));
            } else {
                return geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenLoop("animation.ar_mask.idle"));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        return super.use(world, user, hand);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private VanniMaskItemRenderer renderer;

            @Override
            public VanniMaskItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new VanniMaskItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public Vec3d getLeftArmPos(boolean isMainStack) {
        return new Vec3d(20, -50, 0);
    }

    @Override
    public Vec3d getRightArmPos(boolean isMainStack) {
        return new Vec3d(-20, -50, 0);
    }
}

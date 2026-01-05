package net.zephyr.fnafur.item.masks;

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
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.IHasArmPos;
import net.zephyr.fnafur.util.ItemUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.state.AnimationTest;
import software.bernie.geckolib.animation.object.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.state.KeyFrameEvent;
import software.bernie.geckolib.cache.animation.keyframeevent.SoundKeyframeData;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.ClientUtil;
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
                NbtCompound nbt = ItemUtil.getNbt(stack);
                nbt.putBoolean("inVanniMask", false);
                ItemUtil.setNbt(stack, nbt);
            }
        }

        super.inventoryTick(stack, world, entity, slot);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                new AnimationController<>("idle", 0, this::animController)
                        .setSoundKeyframeHandler(new SoundHandler<>()));
    }

    private PlayState animController(AnimationTest<GeoAnimatable> geoAnimatableAnimationTest) {

        boolean isOn = geoAnimatableAnimationTest.renderState().hasGeckolibData(CustomDataTickets.IS_MASK_ON) && Boolean.TRUE.equals(geoAnimatableAnimationTest.renderState().getGeckolibData(CustomDataTickets.IS_MASK_ON));
        boolean isInSlot = geoAnimatableAnimationTest.renderState().hasGeckolibData(CustomDataTickets.IS_MASK_ON) && Boolean.TRUE.equals(geoAnimatableAnimationTest.renderState().getGeckolibData(CustomDataTickets.IS_IN_MASK_SLOT));
        boolean canAnimate = geoAnimatableAnimationTest.renderState().hasGeckolibData(CustomDataTickets.IS_MASK_ON) && Boolean.TRUE.equals(geoAnimatableAnimationTest.renderState().getGeckolibData(CustomDataTickets.CAN_ANIMATE_MASK));

        ItemDisplayContext context = geoAnimatableAnimationTest.getData(DataTickets.ITEM_RENDER_PERSPECTIVE);

        if(!context.isFirstPerson()){
            return geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenLoop("animation.ar_mask.thirdperson"));
        }
        else{
            if(geoAnimatableAnimationTest.controller().getCurrentAnimationPoint() == null || geoAnimatableAnimationTest.controller().getCurrentAnimationPoint().animation().name().equals("animation.ar_mask.thirdperson")){
                geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.ar_mask.idle"));
            }
            if ((context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)) {
                RawAnimation putOn = RawAnimation.begin().thenPlayAndHold("animation.ar_mask.open");

                if(canAnimate) {
                    if (isOn) {
                        if (geoAnimatableAnimationTest.controller().getCurrentAnimationPoint() != null && !geoAnimatableAnimationTest.controller().getCurrentAnimationPoint().animation().name().equals("animation.ar_mask.open")) {
                            geoAnimatableAnimationTest.setAndContinue(putOn);
                        }
                    } else {
                        if (geoAnimatableAnimationTest.controller().getCurrentAnimationPoint() != null && !geoAnimatableAnimationTest.controller().getCurrentAnimationPoint().animation().name().equals("animation.ar_mask.close")) {
                            geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.ar_mask.close"));
                        }
                    }
                }
            }
        }

       //if (MinecraftClient.getInstance().currentScreen == null && (context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) && isInSlot) {

       //    RawAnimation putOn = RawAnimation.begin().thenPlayAndHold("animation.ar_mask.open");

       //    boolean bl = geoAnimatableAnimationTest.controller().getCurrentAnimation() != null && geoAnimatableAnimationTest.controller().getCurrentAnimation().animation().name().equals("animation.ar_mask.open") && geoAnimatableAnimationTest.controller().getAnimationState() == AnimationController.State.PAUSED;

       //    if (isOn) {
       //        if (!geoAnimatableAnimationTest.controller().getCurrentAnimation().animation().name().equals("animation.ar_mask.open")) {
       //            geoAnimatableAnimationTest.setAndContinue(putOn);
       //        }
       //    } else {
       //        if (bl) {
       //            geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.ar_mask.close"));
       //        } else if (geoAnimatableAnimationTest.controller().getAnimationState() == AnimationController.State.PAUSED) {
       //            geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.ar_mask.idle"));
       //        }
       //    }
       //} else {
       //    if (isOn && !context.isFirstPerson()) {
       //        geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenLoop("animation.ar_mask.thirdperson"));
       //    } else {
       //        geoAnimatableAnimationTest.setAndContinue(RawAnimation.begin().thenLoop("animation.ar_mask.idle"));
       //    }
       //}
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

    static class SoundHandler<T extends GeoAnimatable> implements AnimationController.KeyframeEventHandler<T, SoundKeyframeData>{

        @Override
        public void handle(KeyFrameEvent<T, SoundKeyframeData> event) {
            if(event.keyframeData().getSound().equals("vanni_mask_equip")){
                ClientUtil.getClientPlayer().playSound(SoundsInit.VANNI_MASK_EQUIP, 1, 1);
            }
            else if(event.keyframeData().getSound().equals("ar_reveal")){
                ClientUtil.getClientPlayer().playSound(SoundsInit.AR_REVEAL, 1, 1);
            }
            else if(event.keyframeData().getSound().equals("vanni_mask_unequip")){
                ClientUtil.getClientPlayer().playSound(SoundsInit.VANNI_MASK_UNEQUIP, 1, 1);
            }
            else if(event.keyframeData().getSound().equals("ar_leave")){
                ClientUtil.getClientPlayer().playSound(SoundsInit.AR_LEAVE, 1, 1);
            }
        }
    }
}

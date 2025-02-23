package net.zephyr.fnafur.item.tablet;

import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.camera.CameraBlockEntity;
import net.zephyr.fnafur.client.gui.screens.CameraTabletScreen;
import net.zephyr.fnafur.init.ScreensInit;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TabletItem extends Item implements GeoItem {
    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    boolean transition = false;
    boolean closing = false;
    boolean closed = false;
    private static final RawAnimation USE_ANIM = RawAnimation.begin().thenPlayAndHold("animation.tablet.open");
    private static final RawAnimation CLOSE_ANIM = RawAnimation.begin().thenPlay("animation.tablet.close");
    public TabletItem(Settings settings) {
        super(settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (hand == user.preferredHand)
            if (user instanceof ServerPlayerEntity p) {
                if(!transition) {
                    transition = true;
                    closing = false;
                    closed = false;
                }
            }
        else {
                if (!transition) {
                    user.playSound(SoundsInit.CAM_OPEN, 1, 1);
                    transition = true;
                    closing = false;
                    closed = false;
                }
            }
        return super.use(world, user, hand);
    }

    void openCams(World world, PlayerEntity user) {
        NbtCompound data = user.getMainHandStack().getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        long[] cams = data.getLongArray("Cameras");

        List<Long> camsData = new ArrayList<>();
        for (int i = 0; i < cams.length; i++) {
            if(world.getBlockEntity(BlockPos.fromLong(cams[i])) instanceof CameraBlockEntity) camsData.add(cams[i]);
        }
        data.putLongArray("Cameras", camsData);
        if (world.isClient()) {
            GoopyNetworkingUtils.saveItemNbt(EquipmentSlot.MAINHAND.getName(), data);
        }

        boolean bl = data.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).isEmpty();
        if (!bl) {
            NbtList mapNbt = data.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).copy();
            int minX = Integer.MAX_VALUE;
            int minZ = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxZ = Integer.MIN_VALUE;
            for (int i = 0; i < mapNbt.size(); i++) {
                if (mapNbt.get(i).getType() == NbtElement.LONG_ARRAY_TYPE) {
                    long[] line = mapNbt.getLongArray(i);
                    BlockPos pos1 = BlockPos.fromLong(line[0]);
                    BlockPos pos2 = BlockPos.fromLong(line[1]);
                    minX = Math.min(pos1.getX(), Math.min(pos2.getX(), minX));
                    minZ = Math.min(pos1.getZ(), Math.min(pos2.getZ(), minZ));
                    maxX = Math.max(pos1.getX(), Math.max(pos2.getX(), maxX));
                    maxZ = Math.max(pos1.getZ(), Math.max(pos2.getZ(), maxZ));
                }
            }
            for (long cam : cams) {
                BlockPos pos = BlockPos.fromLong(cam);
                minX = Math.min(pos.getX(), minX);
                minZ = Math.min(pos.getZ(), minZ);
                maxX = Math.max(pos.getX(), maxX);
                maxZ = Math.max(pos.getZ(), maxZ);
            }
            BlockPos minPos = new BlockPos(minX, 0, minZ);
            BlockPos maxPos = new BlockPos(maxX, 0, maxZ);
            data.putLong("mapMinCorner", minPos.asLong());
            data.putLong("mapMaxCorner", maxPos.asLong());
        }
        data.put("data", new NbtCompound());
        data.putLong("pos", 0);

        GoopyNetworkingUtils.setScreen(user, ScreensInit.CAMERA_TABLET, data, EquipmentSlot.MAINHAND.getName());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        NbtCompound data = context.getStack().getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        if (context.getStack() == context.getPlayer().getMainHandStack()) {

            if (world.isClient()) {
                if (world.getBlockState(context.getBlockPos()).isOf(BlockInit.CAMERA)) {
                    long[] cams = data.getLongArray("Cameras");
                    long pos = context.getBlockPos().asLong();
                    List<Long> camsData = new ArrayList<>();
                    for(int i = 0; i < cams.length; i++){
                        camsData.add(cams[i]);
                    }

                    if (context.getPlayer().isSneaking()) {
                        for (int i = 0; i < camsData.size(); i++) {
                            if(camsData.get(i) == pos){
                                camsData.remove(i);

                                data.putLongArray("Cameras", camsData);
                                GoopyNetworkingUtils.saveItemNbt(EquipmentSlot.MAINHAND.getName(), data);
                                return ActionResult.SUCCESS;
                            }
                        }

                        return ActionResult.PASS;
                    } else {
                        boolean shouldAdd = true;
                        for (Long cam : camsData) {
                            if (cam == pos) {
                                shouldAdd = false;
                            }
                        }
                        if(shouldAdd) {
                            camsData.add(pos);

                            data.putLongArray("Cameras", camsData);
                            GoopyNetworkingUtils.saveItemNbt(EquipmentSlot.MAINHAND.getName(), data);
                            return ActionResult.SUCCESS;
                        }
                        else {
                            return ActionResult.PASS;
                        }
                    }
                } else {
                    return ActionResult.PASS;
                }
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        NbtCompound data = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        if(world.isClient()){
            if(MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen screen && !data.getBoolean("used")) {
                screen.setAsUsed(true);
            }
        }
        if(entity instanceof PlayerEntity p && p.getMainHandStack() != stack){
            data.putBoolean("used", false);
        }
        if(world.isClient()) {
            if (!data.equals(stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt()))
                GoopyNetworkingUtils.saveItemNbt(EquipmentSlot.MAINHAND.getName(), data);


            if(data.getBoolean("closing")) {
                this.closing = true;
                data.putBoolean("closing", false);
                GoopyNetworkingUtils.saveItemNbt(EquipmentSlot.MAINHAND.getName(), data);
            }
        }

        if(entity instanceof PlayerEntity player) {
            AnimationController<GeoAnimatable> state = cache.getManagerForId(GeoItem.getId(stack)).getAnimationControllers().get("Use");
            boolean bl = state.getAnimationState() == AnimationController.State.PAUSED;
            if(bl && transition){
                transition = false;
                MinecraftClient.getInstance().options.hudHidden = true;
                openCams(world, player);
                state.forceAnimationReset();
                state.stop();
            }
            closed = closing && (bl || state.getAnimationState() == AnimationController.State.STOPPED);
        }

        if(world.isClient() && !data.equals(stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt())){
            GoopyNetworkingUtils.saveItemNbt(EquipmentSlot.MAINHAND.getName(), data);

            if(MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen) {
                transition = false;
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public boolean renderArms(){
        return (this.closing && !this.closed) || this.transition;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private TabletItemRenderer renderer;

            @Override
            public @Nullable GeoItemRenderer<?> getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new TabletItemRenderer(new TabletItemModel());

                return this.renderer;
            }
        });
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtil.getCurrentTick();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Use", 0, this::useController));
    }

    private PlayState useController(AnimationState<TabletItem> event) {
        if(transition && event.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            return event.setAndContinue(USE_ANIM);
        }
        else if(closing){
            return event.setAndContinue(CLOSE_ANIM);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

package net.zephyr.fnafur.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.client.gui.screens.CameraTabletScreen;
import net.zephyr.fnafur.util.mixinAccessing.IEditCamera;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin implements IEditCamera {

    boolean forceThirdPerson = false;
    @Shadow
    private final Vector3f horizontalPlane = new Vector3f(0.0f, 0.0f, 1.0f);
    @Shadow
    private final Vector3f verticalPlane = new Vector3f(0.0f, 1.0f, 0.0f);
    @Shadow
    private final Vector3f diagonalPlane = new Vector3f(1.0f, 0.0f, 0.0f);

    @Shadow
    private final Quaternionf rotation = new Quaternionf();
    @Shadow
    private static final Vector3f HORIZONTAL = new Vector3f(0.0f, 0.0f, -1.0f);
    @Shadow
    private static final Vector3f VERTICAL = new Vector3f(0.0f, 1.0f, 0.0f);
    @Shadow
    private static final Vector3f DIAGONAL = new Vector3f(-1.0f, 0.0f, 0.0f);


    @Shadow
    private boolean ready;

    @Shadow
    private World area;

    @Shadow
    private Entity focusedEntity;

    @Shadow
    private boolean thirdPerson;

    @Shadow
    private float lastTickProgress;

    @Shadow
    private float pitch;

    @Shadow
    private float yaw;

    @Shadow
    protected abstract void setPos(double x, double y, double z);

    private float roll;

    // TODO Illusion Disc
    @Inject(method = "updateEyeHeight", at = @At("HEAD"), cancellable = true)
    public void updateEyeHeight(CallbackInfo ci) {
        //if(((IPlayerCustomModel)MinecraftClient.getInstance().player).getCurrentEntity() instanceof AnimatronicEntity gu) {
        //    this.lastCameraY = this.cameraY;
        //    this.cameraY = this.cameraY + (gu.getBaseDimensions(gu.mimicPlayer.getPose()).eyeHeight() - this.cameraY) * 0.5F;
        //    ci.cancel();
        //}
    }

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void update(World area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickProgress, CallbackInfo ci) {
        this.ready = true;
        this.area = area;
        this.focusedEntity = focusedEntity;
        this.thirdPerson = thirdPerson || this.forceThirdPerson;
        this.lastTickProgress = tickProgress;

        PlayerEntity player = MinecraftClient.getInstance().player;

        //Entity entity = MinecraftClient.getInstance().world.getEntityById(((IEntityDataSaver)player).getPersistentData().getInt("JumpscareID"));

//        if(MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen screen){
//            Vec3d pos = screen.camPos();
//            float yaw = screen.getYaw();
//            float pitch = screen.getPitch();
//            this.setPos(pos.x, pos.y, pos.z);
//            this.setRotation(yaw, pitch);
//            this.moveBy(this.clipToSpace(0.55f), 0.0f, 0.0f);
//
//            info.cancel();
//        }
        //else if(player != null &&
        //        player.isDead() &&
        //        player.getRecentDamageSource() != null &&
        //        player.getRecentDamageSource().getAttacker() instanceof AnimatronicEntity &&
        //        entity instanceof AnimatronicEntity ent &&
        //        ent.hasJumpScare()
        //) {
        //    info.cancel();
        //}
        // TODO Jumpscare
    }

    @Override
    public void setThirsPerson(boolean thirdPerson) {
        this.forceThirdPerson = thirdPerson;
    }

    @Override
    public void setRotation(float yaw, float pitch, float roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.rotation.rotationYXZ((float)Math.PI - yaw * ((float)Math.PI / 180), -pitch * ((float)Math.PI / 180), -roll * ((float)Math.PI / 180));
        HORIZONTAL.rotate(this.rotation, this.horizontalPlane);
        VERTICAL.rotate(this.rotation, this.verticalPlane);
        DIAGONAL.rotate(this.rotation, this.diagonalPlane);
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.setPos(x, y, z);
    }
}

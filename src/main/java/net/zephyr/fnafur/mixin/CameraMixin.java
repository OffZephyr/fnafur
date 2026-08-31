package net.zephyr.fnafur.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.client.ClientHook;
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
    private final Vector3f forwards = new Vector3f(0.0f, 0.0f, 1.0f);
    @Shadow
    private final Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
    @Shadow
    private final Vector3f left = new Vector3f(1.0f, 0.0f, 0.0f);

    @Shadow
    private final Quaternionf rotation = new Quaternionf();
    @Shadow
    private static final Vector3f FORWARDS = new Vector3f(0.0f, 0.0f, -1.0f);
    @Shadow
    private static final Vector3f UP = new Vector3f(0.0f, 1.0f, 0.0f);
    @Shadow
    private static final Vector3f LEFT = new Vector3f(-1.0f, 0.0f, 0.0f);


    @Shadow
    private boolean initialized;

    @Shadow
    private Level level;

    @Shadow
    private Entity entity;

    @Shadow
    private boolean detached;

    @Shadow
    private float partialTickTime;

    @Shadow
    private float xRot;

    @Shadow
    private float yRot;

    @Shadow
    protected abstract void setPosition(double x, double y, double z);

    private float roll;

    // TODO Illusion Disc
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void updateEyeHeight(CallbackInfo ci) {
        //if(((IPlayerCustomModel)Minecraft.getInstance().player).getCurrentEntity() instanceof AnimatronicEntity gu) {
        //    this.lastCameraY = this.cameraY;
        //    this.cameraY = this.cameraY + (gu.getBaseDimensions(gu.mimicPlayer.getPose()).eyeHeight() - this.cameraY) * 0.5F;
        //    ci.cancel();
        //}
    }

    @Inject(method = "setup", at = @At("HEAD"), cancellable = true)
    public void update(Level area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickProgress, CallbackInfo ci) {
        this.initialized = true;
        this.level = area;
        this.entity = focusedEntity;
        this.detached = thirdPerson || this.forceThirdPerson;
        this.partialTickTime = tickProgress;

        Player player = Minecraft.getInstance().player;

        if(ClientHook.updateCamera(((Camera)(Object)this), area, focusedEntity, thirdPerson, inverseView, tickProgress)){
            ci.cancel();
        }

        //Entity entity = Minecraft.getInstance().level.getEntityById(((IEntityDataSaver)player).getPersistentData().getInt("JumpscareID"));

//        if(Minecraft.getInstance().currentScreen instanceof CameraTabletScreen screen){
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
        this.xRot = pitch;
        this.yRot = yaw;
        this.roll = roll;
        this.rotation.rotationYXZ((float)Math.PI - yaw * ((float)Math.PI / 180), -pitch * ((float)Math.PI / 180), -roll * ((float)Math.PI / 180));
        FORWARDS.rotate(this.rotation, this.forwards);
        UP.rotate(this.rotation, this.up);
        LEFT.rotate(this.rotation, this.left);
    }

    @Override
    public void setPos(double x, double y, double z) {
        this.setPosition(x, y, z);
    }
}

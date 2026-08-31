package net.zephyr.fnafur.util;

import net.minecraft.client.renderer.rendertype.RenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Direction;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.entity.animatronic.data.CpuData;
import com.geckolib.constant.dataticket.DataTicket;

public class CustomDataTickets {
    public static final DataTicket<Identifier> TEXTURE = DataTicket.create("texture", Identifier.class);
    public static final DataTicket<Identifier> RE_RENDER_TEXTURE = DataTicket.create("re_render_texture", Identifier.class);
    public static final DataTicket<Identifier> MODEL = DataTicket.create("model", Identifier.class);
    public static final DataTicket<Identifier> RE_RENDER_MODEL = DataTicket.create("re_render_model", Identifier.class);
    public static final DataTicket<Identifier> ANIMATIONS = DataTicket.create("animations", Identifier.class);
    public static final DataTicket<Boolean> USE_EYE_TEXTURE = DataTicket.create("use_eye_texture", Boolean.class);
    public static final DataTicket<Boolean> IS_EYE_BONE = DataTicket.create("is_eye_bone", Boolean.class);
    public static final DataTicket<Identifier> EYE_TEXTURE = DataTicket.create("eye_texture", Identifier.class);
    public static final DataTicket<Identifier> EYE_MAP_TEXTURE = DataTicket.create("eye_map_texture", Identifier.class);
    public static final DataTicket<Identifier> EYE_GLOW_MAP_TEXTURE = DataTicket.create("eye_glow_map_texture", Identifier.class);
    public static final DataTicket<Identifier> EYE_GLOW_COLOR_TEXTURE = DataTicket.create("eye_glow_color_texture", Identifier.class);
    public static final DataTicket<Identifier> SUIT_MAP_TEXTURE = DataTicket.create("suit_map_texture", Identifier.class);
    public static final DataTicket<Float> RENDER_SCALE = DataTicket.create("render_scale", Float.class);
    public static final DataTicket<Boolean> EYE_NONE = DataTicket.create("eye_none", Boolean.class);
    public static final DataTicket<Boolean> EYES_GLOW = DataTicket.create("eyes_glow", Boolean.class);
    public static final DataTicket<CpuData.GlowingEyesMode> EYES_GLOW_MODE = DataTicket.create("eyes_glow_mode", CpuData.GlowingEyesMode.class);
    public static final DataTicket<RenderType> RENDER_LAYER = DataTicket.create("render_layer", RenderType.class);
    public static final DataTicket<Identifier> DOOR_WINDOW_TEXTURE = DataTicket.create("door_window_texture", Identifier.class);
    public static final DataTicket<Float> CLOCK_DELTA_MINUTE = DataTicket.create("clock_delta_minute", Float.class);
    public static final DataTicket<Float> CLOCK_DELTA_HOUR = DataTicket.create("clock_delta_hour", Float.class);
    public static final DataTicket<Boolean> IS_MASK_ON = DataTicket.create("is_mask_on", Boolean.class);
    public static final DataTicket<Boolean> IS_RENDERING_ARMS = DataTicket.create("is_rendering_arms", Boolean.class);
    public static final DataTicket<Boolean> CAN_ANIMATE_MASK = DataTicket.create("can_animate_mask", Boolean.class);
    public static final DataTicket<Boolean> IS_IN_MASK_SLOT = DataTicket.create("in_mask_slot", Boolean.class);
    public static final DataTicket<Boolean> IS_ENTITY_PREVIEW = DataTicket.create("is_entity_preview", Boolean.class);
    public static final DataTicket<Boolean> IS_VISIBLE = DataTicket.create("is_visible", Boolean.class);
    public static final DataTicket<Direction> FACING = DataTicket.create("facing", Direction.class);
    public static final DataTicket<PoseStack.Pose> ENTITY_RENDER_MATRIX_ENTRY = DataTicket.create("entity_matrix_entry", PoseStack.Pose.class);


    public static final DataTicket<Float> FORCED_HEAD_YAW = DataTicket.create("forced_head_yaw", Float.class);
    public static final DataTicket<Float> FORCED_PITCH = DataTicket.create("forced_pitch", Float.class);
    public static final DataTicket<Boolean> IS_FROZEN = DataTicket.create("is_frozen", Boolean.class);

    public static final DataTicket<Float> ROTATION = DataTicket.create("rotation", Float.class);
    public static final DataTicket<Double> X_OFFSET = DataTicket.create("x_offset", Double.class);
    public static final DataTicket<Double> Y_OFFSET = DataTicket.create("y_offset", Double.class);
    public static final DataTicket<Double> Z_OFFSET = DataTicket.create("z_offset", Double.class);

    public static final DataTicket<AnimatronicEntity.AnimatronicPose> ANIMATRONIC_POSE = DataTicket.create("animatronic_pose", AnimatronicEntity.AnimatronicPose.class);



}

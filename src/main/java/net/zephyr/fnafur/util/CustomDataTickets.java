package net.zephyr.fnafur.util;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.constant.dataticket.DataTicket;

public class CustomDataTickets {
    public static final DataTicket<Identifier> TEXTURE = DataTicket.create("texture", Identifier.class);
    public static final DataTicket<Identifier> RE_RENDER_TEXTURE = DataTicket.create("re_render_texture", Identifier.class);
    public static final DataTicket<Identifier> MODEL = DataTicket.create("model", Identifier.class);
    public static final DataTicket<Identifier> RE_RENDER_MODEL = DataTicket.create("re_render_model", Identifier.class);
    public static final DataTicket<Identifier> ANIMATIONS = DataTicket.create("animations", Identifier.class);
    public static final DataTicket<Boolean> USE_EYE_TEXTURE = DataTicket.create("use_eye_texture", Boolean.class);
    public static final DataTicket<Identifier> EYE_TEXTURE = DataTicket.create("eye_texture", Identifier.class);
    public static final DataTicket<Boolean> EYE_NONE = DataTicket.create("eye_none", Boolean.class);
    public static final DataTicket<RenderLayer> RENDER_LAYER = DataTicket.create("render_layer", RenderLayer.class);
    public static final DataTicket<Identifier> DOOR_WINDOW_TEXTURE = DataTicket.create("door_window_texture", Identifier.class);
    public static final DataTicket<NbtCompound> ENTITY_DATA = DataTicket.create("entity_data", NbtCompound.class);
    public static final DataTicket<Float> CLOCK_DELTA_MINUTE = DataTicket.create("clock_delta_minute", Float.class);
    public static final DataTicket<Float> CLOCK_DELTA_HOUR = DataTicket.create("clock_delta_hour", Float.class);
    public static final DataTicket<Boolean> IS_MASK_ON = DataTicket.create("is_mask_on", Boolean.class);
    public static final DataTicket<Boolean> CAN_ANIMATE_MASK = DataTicket.create("can_animate_mask", Boolean.class);
    public static final DataTicket<Boolean> IS_IN_MASK_SLOT = DataTicket.create("in_mask_slot", Boolean.class);
    public static final DataTicket<Boolean> IS_ENTITY_PREVIEW = DataTicket.create("is_entity_preview", Boolean.class);
    public static final DataTicket<String> ENTITY_CHARA = DataTicket.create("entity_chara", String.class);
    public static final DataTicket<String> ENTITY_ALT = DataTicket.create("entity_alt", String.class);
    public static final DataTicket<String> ENTITY_EYES = DataTicket.create("entity_eyes", String.class);
}

package net.zephyr.fnafur.client.gui.screens;

import com.google.common.primitives.Shorts;
import com.google.common.primitives.SignedBytes;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.util.mixinAccessing.IUniverseScreenHandler;
import org.jetbrains.annotations.Nullable;

public class FnafInventoryScreen extends InventoryScreen {
    public static final int SLOTS_OFFSET = 46;
    public static final int SLOTS_X = 77;
    public static final int SLOT_AR_Y = 8;
    public static final int SLOT_IL_Y = 26;
    public static final Identifier EMPTY_AR_DEVICE_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/ar_device");
    public static final Identifier EMPTY_ILLUSION_DISC_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/illusion_disc");
    public static final Identifier TEXTURE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/inventory_mask_slot.png");

    public FnafInventoryScreen(PlayerEntity player) {
        super(player);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        super.drawBackground(context, deltaTicks, mouseX, mouseY);
        int i = this.x;
        int j = this.y;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
    }

    @Override
    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {

        super.onMouseClick(slot, slotId, button, actionType);
    }
}

package net.zephyr.fnafur.client.gui.screens;

import com.google.common.primitives.Shorts;
import com.google.common.primitives.SignedBytes;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.util.mixinAccessing.IUniverseScreenHandler;
import org.jetbrains.annotations.Nullable;

public class FnafInventoryScreen extends InventoryScreen {
    public static final int SLOTS_OFFSET = 46;
    public static final int SLOTS_X = 77;
    public static final int SLOT_AR_Y = 8;
    public static final int SLOT_IL_Y = 26;
    public static final Identifier EMPTY_AR_DEVICE_SLOT_TEXTURE = Identifier.withDefaultNamespace("container/slot/ar_device");
    public static final Identifier EMPTY_ILLUSION_DISC_SLOT_TEXTURE = Identifier.withDefaultNamespace("container/slot/illusion_disc");
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/inventory_mask_slot.png");

    public FnafInventoryScreen(Player player) {
        super(player);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    protected void renderBg(GuiGraphics context, float deltaTicks, int mouseX, int mouseY) {
        super.renderBg(context, deltaTicks, mouseX, mouseY);
        int i = this.leftPos;
        int j = this.topPos;
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int button, ClickType actionType) {

        super.slotClicked(slot, slotId, button, actionType);
    }
}

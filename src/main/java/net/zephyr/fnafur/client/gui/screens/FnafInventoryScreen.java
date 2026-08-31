package net.zephyr.fnafur.client.gui.screens;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

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
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int buttonNum, ContainerInput containerInput) {
        super.slotClicked(slot, slotId, buttonNum, containerInput);
    }
}

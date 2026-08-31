package net.zephyr.fnafur.client.gui.screens.editing;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.networking.block.UpdatePropAltC2SPayload;
import org.joml.Quaternionf;
import com.geckolib.renderer.base.GeoRenderState;

import java.util.List;

public class PaintbrushAltPickerScreen<T extends Enum<T> & ColorEnumInterface & StringRepresentable, R extends BlockEntityRenderState & GeoRenderState> extends GoopyScreen {

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/alt_picker.png");
    private final List<T> alts;
    private final EnumProperty<T> altEnumProperty;
    private int hoveredAltIndex = -1;
    private int selectedAltIndex;
    private float scrollAmount = 0;
    private float rotationIndex = 0;

    public PaintbrushAltPickerScreen(Component title, BlockPos pos, EnumProperty<T> altEnumProperty, T currentAlt) {
        super(title, new CompoundTag(), pos.asLong());

        alts = altEnumProperty.getPossibleValues();
        this.altEnumProperty = altEnumProperty;
        selectedAltIndex = alts.indexOf(currentAlt);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {

        rotationIndex += delta;
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, (this.width / 2) + 64, (this.height / 2) - 40, 0, 32, 128, 128, 128, 128, 256, 256);
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, (this.width / 2) + 64, (this.height / 2) - 88, 0, 0, 128, 32, 128, 32, 256, 256);

        int buttonWidth = 56;
        int buttonOffset = buttonWidth + 2;
        int columns = 4;
        int rows = (alts.size() - 1)/4 + 1;
        hoveredAltIndex = -1;

        for(int i = 0; i < alts.size(); i++){
            T alt = alts.get(i);
            int x = (this.width / 2) - (buttonOffset*3) - 4 + (i % columns) * buttonOffset;
            int y = (int) scrollAmount + Math.max(4, (this.height / 2) - ((buttonOffset*rows)/2)) + (i / columns) * buttonOffset;

            if(y < -buttonOffset || y > height) continue;

            boolean hovered = isOnButton(mouseX, mouseY, x, y, buttonOffset, buttonOffset);
            if(hovered){
                hoveredAltIndex = i;
            }


            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, hovered || i == selectedAltIndex ? 80 : 0, 160, buttonWidth, buttonWidth, 80, 80, 256, 256);

            renderBlock(context, x + 3, y + 3, x + buttonWidth - 3, y + buttonWidth - 3, 20, i, true);
        }

        int index = hoveredAltIndex < 0 ? selectedAltIndex : hoveredAltIndex;
        BlockState selectedState = Minecraft.getInstance().level.getBlockState(getBlockPos()).getBlock().defaultBlockState();
        if(selectedState.hasProperty(altEnumProperty)){
            selectedState = selectedState.setValue(altEnumProperty, alts.get(Math.clamp(index, 0, alts.size()-1)));
        }
        renderBlock(context, (this.width / 2) + 68, (this.height / 2) - 36, (this.width / 2) + 68 + 124, (this.height / 2) - 40 + 124, 50, index, false);

        FontDescription spriteFont = new FontDescription.Resource(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "lemon_bold"));
        Style style = Style.EMPTY.withFont(spriteFont);
        Component name = Component.translatable(selectedState.getBlock().getDescriptionId() + ".alt." + index).toFlatList(style).getFirst();
        drawAutoResizedText(context, font, name, 1, 100, (this.width / 2f) + 128, (this.height / 2f) - 75, 0xFFFFFFFF, 0x00000000, false, true);


        if(rows > 4) {
            float maxScroll = (((alts.size() - 1f)/columns) - 2) * buttonWidth;
            int scrollX = (this.width / 2) - (buttonOffset * 3) - 20;
            int scrollY = 4;
            context.fill(scrollX, scrollY, scrollX + 10, scrollY + height - 8, 0xFF59003F);
            drawOutline(context, scrollX, scrollY, 10, height - 8, 0xFFFF194C);


            int scrollBarY = scrollY + 2;
            int scrollBarHeight = (height - 10) / (rows - 3);
            scrollBarY += Mth.lerpInt(-scrollAmount / maxScroll, 0, height - 10 - scrollBarHeight);
            context.fill(scrollX + 2, Math.clamp(scrollBarY, scrollY + 2, scrollY - 9 + height), scrollX + 9, Math.clamp(scrollBarY + scrollBarHeight, scrollY + 2, scrollY - 9 + height), 0xFFFF194C);


            if (-scrollAmount > maxScroll) {
                scrollAmount = Mth.lerp(delta, scrollAmount, -maxScroll);
            }
            if (-scrollAmount < 0) {
                scrollAmount = Mth.lerp(delta, scrollAmount, 0);
            }
        }
        super.extractRenderState(context, mouseX, mouseY, delta);
    }

    public void renderBlock(GuiGraphicsExtractor context, int x1, int y1, int x2, int y2, int scale, int index, boolean offset) {
        BlockState selectedState = Minecraft.getInstance().level.getBlockState(getBlockPos()).getBlock().defaultBlockState();
        if(selectedState.hasProperty(altEnumProperty)){
            selectedState = selectedState.setValue(altEnumProperty, alts.get(Math.clamp(index, 0, alts.size()-1)));
        }

        float offsetRot = offset ? (index / 10f) : 0;
        Quaternionf quaternionf = new Quaternionf().rotateX(-0.25f).rotateY(Mth.sin(rotationIndex / 20f - offsetRot) / 2.5f);

        if(selectedState.getBlock() instanceof PropBlock<?> block && block instanceof GeoPropBlock) {

            float size = scale/10f;
            context.pose().pushMatrix();
            context.pose().scale(size);
            context.item(block.getCloneItemStack(Minecraft.getInstance().level, getBlockPos(), selectedState, false), (int) (((x1 + x2 - (16*size)) / 2f) / size), (int) (((y1 + y2 - (16*size)) / 2f) / size));
            context.pose().popMatrix();
        }
        else {
            FallingBlockEntity entity = FallingBlockEntity.fall(Minecraft.getInstance().level, new BlockPos(getBlockPos().getX(), 250, getBlockPos().getZ()), selectedState);

            drawEntity(context, x1, y1, x2, y2, scale, 0, quaternionf, entity, 0.5f);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        for(int i = 0; i < alts.size(); i++){
            T alt = alts.get(i);
            int buttonWidth = 56;
            int buttonOffset = buttonWidth + 2;
            int columns = 4;
            int rows = (alts.size() - 1)/4 + 1;
            int x = (this.width / 2) - (buttonOffset*3) - 4 + (i % columns) * buttonOffset;
            int y = (int) scrollAmount + Math.max(4, (this.height / 2) - ((buttonOffset*rows)/2)) + (i / columns) * buttonOffset;

            boolean hovered = isOnButton(click.x(), click.y(), x, y, buttonOffset, buttonOffset);
            if(hovered){
                selectedAltIndex = i;
                onClose();
            }
//            MutableText altText = Text.translatable("alt." + FnafUniverseRebuilt.MOD_ID + "." + alt.asString());
//            int textWidth = textRenderer.getWidth(altText);
//            textRenderer.drawWithShadow(context.getMatrices(), altText, x + 20 - textWidth / 2f, y + 45, 0xFFFFFF);
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {

        int rows = (alts.size() - 1)/4 + 1;
        if(rows > 4) {
            scrollAmount += (float) (verticalAmount * 30);
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {

        int rows = (alts.size() - 1)/4 + 1;
        if(rows > 4) {
            int buttonWidth = 56;
            int buttonOffset = buttonWidth + 2;
            int scrollX = (this.width / 2) - (buttonOffset * 3) - 20;
            int scrollY = 4;
            if (isOnButton(click.x(), click.y(), scrollX, scrollY, 10, height - 8)) {
                scrollAmount -= (float) offsetY;
            }
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public void onClose() {
        ClientPlayNetworking.send(new UpdatePropAltC2SPayload(getBlockPos().asLong(), selectedAltIndex));
        super.onClose();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return super.shouldCloseOnEsc();
    }

    @Override
    public boolean isPauseScreen() {
        return super.isPauseScreen();
    }
}

package net.zephyr.fnafur.client.gui.screens.editing;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.networking.block.UpdatePropAltC2SPayload;
import org.joml.Quaternionf;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.List;

public class PaintbrushAltPickerScreen<T extends Enum<T> & ColorEnumInterface & StringIdentifiable, R extends BlockEntityRenderState & GeoRenderState> extends GoopyScreen {

    public static final Identifier TEXTURE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/alt_picker.png");
    private final List<T> alts;
    private final EnumProperty<T> altEnumProperty;
    private int hoveredAltIndex = -1;
    private int selectedAltIndex;
    private float scrollAmount = 0;
    private float rotationIndex = 0;

    public PaintbrushAltPickerScreen(Text title, BlockPos pos, EnumProperty<T> altEnumProperty, T currentAlt) {
        super(title, new NbtCompound(), pos.asLong());

        alts = altEnumProperty.getValues();
        this.altEnumProperty = altEnumProperty;
        selectedAltIndex = alts.indexOf(currentAlt);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        rotationIndex += delta;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, (this.width / 2) + 64, (this.height / 2) - 40, 0, 32, 128, 128, 128, 128, 256, 256);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, (this.width / 2) + 64, (this.height / 2) - 88, 0, 0, 128, 32, 128, 32, 256, 256);

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


            context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, hovered || i == selectedAltIndex ? 80 : 0, 160, buttonWidth, buttonWidth, 80, 80, 256, 256);

            renderBlock(context, x + 3, y + 3, x + buttonWidth - 3, y + buttonWidth - 3, 20, i);
        }

        int index = hoveredAltIndex < 0 ? selectedAltIndex : hoveredAltIndex;
        BlockState selectedState = MinecraftClient.getInstance().world.getBlockState(getBlockPos()).getBlock().getDefaultState();
        if(selectedState.contains(altEnumProperty)){
            selectedState = selectedState.with(altEnumProperty, alts.get(Math.clamp(index, 0, alts.size()-1)));
        }
        renderBlock(context, (this.width / 2) + 68, (this.height / 2) - 36, (this.width / 2) + 68 + 124, (this.height / 2) - 40 + 124, 50, index);

        StyleSpriteSource spriteFont = new StyleSpriteSource.Font(Identifier.of(FnafUniverseRebuilt.MOD_ID, "lemon_bold"));
        Style style = Style.EMPTY.withFont(spriteFont);
        Text name = Text.translatable(selectedState.getBlock().getTranslationKey() + ".alt." + index).getWithStyle(style).getFirst();
        drawAutoResizedText(context, textRenderer, name, 1, 100, (this.width / 2f) + 128, (this.height / 2f) - 75, 0xFFFFFFFF, 0x00000000, false, true);


        if(rows > 4) {
            float maxScroll = (((alts.size() - 1f)/columns) - 2) * buttonWidth;
            int scrollX = (this.width / 2) - (buttonOffset * 3) - 20;
            int scrollY = 4;
            context.fill(scrollX, scrollY, scrollX + 10, scrollY + height - 8, 0xFF59003F);
            drawOutline(context, scrollX, scrollY, 10, height - 8, 0xFFFF194C);


            int scrollBarY = scrollY + 2;
            int scrollBarHeight = (height - 10) / (rows - 3);
            scrollBarY += MathHelper.lerp(-scrollAmount / maxScroll, 0, height - 10 - scrollBarHeight);
            context.fill(scrollX + 2, Math.clamp(scrollBarY, scrollY + 2, scrollY - 9 + height), scrollX + 9, Math.clamp(scrollBarY + scrollBarHeight, scrollY + 2, scrollY - 9 + height), 0xFFFF194C);


            if (-scrollAmount > maxScroll) {
                scrollAmount = MathHelper.lerp(delta, scrollAmount, -maxScroll);
            }
            if (-scrollAmount < 0) {
                scrollAmount = MathHelper.lerp(delta, scrollAmount, 0);
            }
        }
        super.render(context, mouseX, mouseY, delta);
    }

    public void renderBlock(DrawContext context, int x1, int y1, int x2, int y2, int scale, int index) {
        BlockState selectedState = MinecraftClient.getInstance().world.getBlockState(getBlockPos()).getBlock().getDefaultState();
        if(selectedState.contains(altEnumProperty)){
            selectedState = selectedState.with(altEnumProperty, alts.get(Math.clamp(index, 0, alts.size()-1)));
        }

        Quaternionf quaternionf = new Quaternionf().rotateX(-0.25f).rotateY(MathHelper.sin(rotationIndex / 20f - (index / 10f)) / 2.5f);

        if(selectedState.getBlock() instanceof PropBlock<?> block && block instanceof GeoPropBlock) {

            float size = scale/10f;
            context.getMatrices().pushMatrix();
            context.getMatrices().scale(size);
            context.drawItem(block.getPickStack(MinecraftClient.getInstance().world, getBlockPos(), selectedState, false), (int) (((x1 + x2 - (16*size)) / 2f) / size), (int) (((y1 + y2 - (16*size)) / 2f) / size));
            context.getMatrices().popMatrix();
        }
        else {
            FallingBlockEntity entity = FallingBlockEntity.spawnFromBlock(MinecraftClient.getInstance().world, new BlockPos(getBlockPos().getX(), 250, getBlockPos().getZ()), selectedState);

            drawEntity(context, x1, y1, x2, y2, scale, 0, quaternionf, entity, 0.5f);
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
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
                close();
            }
//            MutableText altText = Text.translatable("alt." + FnafUniverseRebuilt.MOD_ID + "." + alt.asString());
//            int textWidth = textRenderer.getWidth(altText);
//            textRenderer.drawWithShadow(context.getMatrices(), altText, x + 20 - textWidth / 2f, y + 45, 0xFFFFFF);
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(Click click) {
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
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {

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
    public void close() {
        ClientPlayNetworking.send(new UpdatePropAltC2SPayload(getBlockPos().asLong(), selectedAltIndex));
        super.close();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return super.shouldCloseOnEsc();
    }

    @Override
    public boolean shouldPause() {
        return super.shouldPause();
    }
}

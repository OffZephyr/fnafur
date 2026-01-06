package net.zephyr.fnafur.client.gui.screens.editing;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;
import net.zephyr.fnafur.blocks.props.base.PropBlockModel;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.client.rendering.FloorPropPlacingRenderer;
import net.zephyr.fnafur.networking.block.UpdatePropAltC2SPayload;
import org.joml.Quaternionf;

import java.util.List;

public class PaintbrushAltPickerScreen<T extends Enum<T> & ColorEnumInterface & StringIdentifiable> extends GoopyScreen {

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


        hoveredAltIndex = -1;
        for(int i = 0; i < alts.size(); i++){
            T alt = alts.get(i);
            int buttonWidth = 56;
            int buttonOffset = buttonWidth + 2;
            int columns = 4;
            int rows = (alts.size())/4 + 1;
            int x = (this.width / 2) - (buttonOffset*3) - 4 + (i % columns) * buttonOffset;
            int y = Math.max(4, (this.height / 2) - ((buttonOffset*rows)/2)) + (i / columns) * buttonOffset;

            boolean hovered = isOnButton(mouseX, mouseY, x, y, buttonOffset, buttonOffset);
            if(hovered){
                hoveredAltIndex = i;
            }


            context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, hovered || i == selectedAltIndex ? 80 : 0, 160, buttonWidth, buttonWidth, 80, 80, 256, 256);

            BlockState selectedState = MinecraftClient.getInstance().world.getBlockState(getBlockPos()).getBlock().getDefaultState();
            if(selectedState.contains(altEnumProperty)){
                selectedState = selectedState.with(altEnumProperty, alts.get(Math.clamp(i, 0, alts.size()-1)));
            }
            FallingBlockEntity entity = FallingBlockEntity.spawnFromBlock(MinecraftClient.getInstance().world, MinecraftClient.getInstance().player.getBlockPos(), selectedState);

            Quaternionf quaternionf = new Quaternionf().rotateX(-0.25f).rotateY(MathHelper.sin(rotationIndex/20f - (i/10f))/2.5f);
            drawEntity(context, x + 3, y + 3, x + buttonWidth - 3, y + buttonWidth - 3, 20, 0, quaternionf, entity, 0.5f);
//            MutableText altText = Text.translatable("alt." + FnafUniverseRebuilt.MOD_ID + "." + alt.asString());
//            int textWidth = textRenderer.getWidth(altText);
//            textRenderer.drawWithShadow(context.getMatrices(), altText, x + 20 - textWidth / 2f, y + 45, 0xFFFFFF);
        }

        int index = hoveredAltIndex < 0 ? selectedAltIndex : hoveredAltIndex;
        BlockState selectedState = MinecraftClient.getInstance().world.getBlockState(getBlockPos()).getBlock().getDefaultState();
        if(selectedState.contains(altEnumProperty)){
            selectedState = selectedState.with(altEnumProperty, alts.get(Math.clamp(index, 0, alts.size()-1)));
        }
        FallingBlockEntity entity = FallingBlockEntity.spawnFromBlock(MinecraftClient.getInstance().world, new BlockPos(getBlockPos().getX(), 200, getBlockPos().getZ()), selectedState);

        Quaternionf quaternionf = new Quaternionf().rotateX(-0.25f).rotateY(MathHelper.sin(rotationIndex/20f - ((alts.size()/2f)/10f))/2.5f);
        drawEntity(context, (this.width / 2) + 68, (this.height / 2) - 36, (this.width / 2) + 68 + 124, (this.height / 2) - 40 + 124, 45, 0, quaternionf, entity, 0.5f);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        for(int i = 0; i < alts.size(); i++){
            T alt = alts.get(i);
            int buttonWidth = 56;
            int buttonOffset = buttonWidth + 2;
            int columns = 4;
            int rows = (alts.size())/4 + 1;
            int x = (this.width / 2) - (buttonOffset*3) - 4 + (i % columns) * buttonOffset;
            int y = Math.max(4, (this.height / 2) - ((buttonOffset*rows)/2)) + (i / columns) * buttonOffset;

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
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
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

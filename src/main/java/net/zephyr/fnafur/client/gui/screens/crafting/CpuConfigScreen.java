package net.zephyr.fnafur.client.gui.screens.crafting;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.KeyInput;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.cpu_config_panel.CpuConfigPanelBlock;
import net.zephyr.fnafur.client.ClientHook;
import net.zephyr.fnafur.client.gui.screens.InWorldScreen;
import net.zephyr.fnafur.entity.animatronic.data.CpuData;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.item.animatronic.CPUItem;
import net.zephyr.fnafur.networking.entity.player.UpdateMainHandItemC2SPayload;
import net.zephyr.fnafur.util.jsonReaders.animatronics.AnimatronicDataHandler;
import org.joml.Vector3f;

import java.util.*;

public class CpuConfigScreen extends InWorldScreen {

    CpuData data = new CpuData();
    int scrollAmount = 0;
    int selectedIndex = 0;

    int subWindow = 0;
    int animationScrollAmount = 0;

    float age = 0;

    List<String> SettingList = new ArrayList<>();
    List<String> AnimationList = new ArrayList<>();
    List<String> AmbientSoundList = new ArrayList<>();

    public CpuConfigScreen(Text title, NbtCompound nbt, long l) {
        super(title, nbt, l);
        ItemStack stack = MinecraftClient.getInstance().player.getMainHandStack();
        if(stack.isOf(ItemInit.CPU)){
            data = CPUItem.getCpuData(stack);
        }
        selectedIndex++;

        updateList();
    }

    List<String> getList(){
        if(subWindow == 1) return AnimationList;

        if (subWindow == 2) return AmbientSoundList;

        return SettingList;
    }

    void updateList(){
        SettingList.clear();
        SettingList.add("title_visual");
        SettingList.add("animation");
        SettingList.add("ambient_sound");
        for(String string : data.KeyList){
            if(Objects.equals(string, CpuData.MovementSpeed.getDefault().getKey())){
                SettingList.add("title_stats");
            }
            else if(Objects.equals(string, CpuData.SingingRole.getDefault().getKey())){
                SettingList.add("title_base");
            }
            else if(Objects.equals(string, CpuData.ReactionToLight.getDefault().getKey())){
                SettingList.add("title_interactions");
            }
            else if(Objects.equals(string, CpuData.AggressionMode.getDefault().getKey())){
                SettingList.add("title_aggressivity");
            }
            SettingList.add(string);
        }

        AnimationList.clear();
        AnimationList.add("back");
        AnimationList.add("default");
        AnimatronicDataHandler.ANIMATION_NAMES_PER_CATEGORY.forEach((category, names) -> {
            if(!category.toLowerCase().contains("default")){
                AnimationList.add("title_" + category);

                AnimationList.addAll(names);
            }
        });

        AmbientSoundList.clear();
        AmbientSoundList.add("back");
        AmbientSoundList.add("default");
        AnimatronicDataHandler.SOUND_NAMES_PER_CATEGORY.forEach((category, names) -> {
            if(!category.toLowerCase().contains("default")){
                AmbientSoundList.add("title_" + category);

                AmbientSoundList.addAll(names);
            }
        });
    }

    String getArgumentValue(String argument){
        if(subWindow == 1){
            return argument;
        }
        if(Objects.equals(argument, "animation")){
            return data.Animation;
        }
        if(Objects.equals(argument, "ambient_sound")){
            return data.AmbientSound;
        }
        else if(data.DATA_LIST.get(argument) instanceof CpuData.CpuDataRangeArgument range){
            return range.getValue() + " / " + range.getMax();
        }
        else if(data.DATA_LIST.get(argument) instanceof CpuData.CpuDataFloatRangeArgument range){
            return range.getValue() + " / " + range.getMax();
        }
        else if(data.DATA_LIST.containsKey(argument)){
            return data.DATA_LIST.get(argument).getName();
        }
        return argument;
    }

    public CpuConfigScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
        ItemStack stack = MinecraftClient.getInstance().player.getMainHandStack();
        if(stack.isOf(ItemInit.CPU)){
            data = CPUItem.getCpuData(stack);
        }
        selectedIndex++;

        updateList();
    }

    @Override
    public Vec3d getCameraPos() {
        BlockState blockState = MinecraftClient.getInstance().world.getBlockState(getBlockPos());
        if(blockState.isOf(BlockInit.CPU_CONFIG_PANEL)){
            Vec3d offset = blockState.get(CpuConfigPanelBlock.FACING).getDoubleVector().multiply(0.3f);
            Vec3d offset2 = blockState.get(CpuConfigPanelBlock.FACING).rotateYClockwise().getDoubleVector().multiply(-0.175f);
            return getBlockPos().toCenterPos().add(0, -0.04f, 0).add(offset.add(offset2));
        }
        return getBlockPos().toCenterPos().add(new Vec3d(0, 0, 0));
    }

    @Override
    public Vector3f getCameraAngle() {
        BlockState blockState = MinecraftClient.getInstance().world.getBlockState(getBlockPos());
        if(blockState.isOf(BlockInit.CPU_CONFIG_PANEL)){
            return new Vector3f(blockState.get(CpuConfigPanelBlock.FACING).getPositiveHorizontalDegrees() + 180, 0, 0);
        }
        return new Vector3f(0, 0, 0);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        if(ClientHook.tickTransitionToScreen == 1){
            age += delta/20f;
            StyleSpriteSource spriteFont = new StyleSpriteSource.Font(Identifier.of(FnafUniverseRebuilt.MOD_ID, "lemon_terminal"));
            Style style = Style.EMPTY.withFont(spriteFont);

            Text MenuInitials = Text.literal("A.B.C.U.").getWithStyle(style).getFirst();
            Text MenuName = Text.literal("Animatronic Behavior Configuration Unit").getWithStyle(style).getFirst();
            if(age < 2){

                if(age > 1.75f) return;

                drawResizableText(context, textRenderer, MenuInitials, 5, width/2f, height/2f - 20, 0xFF33FF66, 0x00000000, false, true);
                if(age > 0.45f){
                    drawResizableText(context, textRenderer, MenuName, 0.9f, width/2f - 2, height/2f + 20, 0xFF00FF00, 0x00000000, false, true);
                }

                return;
            }

            Text Controls = Text.literal("")
                    .append(Text.literal("[").getWithStyle(style).getFirst())
                    .append(Text.literal("↑↓"))
                    .append(Text.literal("]/[").getWithStyle(style).getFirst())
                    .append(Text.literal("🖱"))
                    .append(Text.literal("] - Navigate").getWithStyle(style).getFirst());

            Text Controls2 = Text.literal("")
                    .append(Text.literal("[").getWithStyle(style).getFirst())
                    .append(Text.literal("←→"))
                    .append(Text.literal("]/[").getWithStyle(style).getFirst())
                    .append(Text.literal("🖱"))
                    .append(Text.literal("]/[Enter] - Cycle Options").getWithStyle(style).getFirst());

            Text Controls3 = Text.literal("- Controls -").getWithStyle(style).getFirst();

            drawResizableText(context, textRenderer, MenuInitials, 2.7f, width/4f - 2, height/28f + 2, 0xFF33FF66, 0x00000000, false, false);
            drawResizableText(context, textRenderer, MenuName, 0.475f, width/4f - 4, height/28f + 23, 0xFF00FF00, 0x00000000, false, false);

            float controlWidth1 = textRenderer.getWidth(Controls) * 0.75f;
            float controlWidth2 = textRenderer.getWidth(Controls2) * 0.75f;
            float controlWidth3 = textRenderer.getWidth(Controls3) * 0.9f;
            drawResizableText(context, textRenderer, Controls3, 0.9f, -12 + width - (width/4f) - controlWidth3, height/28f + 0, 0xFF33FF66, 0x00000000, false, false);
            drawResizableText(context, textRenderer, Controls, 0.75f, -12 + width - (width/4f) - controlWidth1, height/28f + 11, 0xFF00FF00, 0x00000000, false, false);
            drawResizableText(context, textRenderer, Controls2, 0.75f, -12 + width - (width/4f) - controlWidth2, height/28f + 20, 0xFF00FF00, 0x00000000, false, false);

            context.fill(width/4 - 4, height/28 + 31, -12 + width - (width/4), height/28 + 32, 0xFF33FF66);

            int list = age < 2.25f ? 0 : age < 2.321f ? 5 : age < 2.67 ? 9 : 12;
            for(int i = 0; i < Math.min(getList().size(), list); i++){
                int offset = scrollAmount;
                if(i + offset >= getList().size()) continue;
                String name = getList().get(i + offset);
                String value = getArgumentValue(name);
                int selected = selectedIndex - offset;
                boolean title = name.contains("title_");
                boolean isList = subWindow != 0;
                int color = title ? 0xFF33FF66 : i == selected ? 0xFF00FF00 : 0xFF006622;

                Text nameText = Text.translatable("cpu_config.argument." + name).getWithStyle(style).getFirst();
                Text valueText = Text.translatable("cpu_config.value." + value).getWithStyle(style).getFirst();

                if(data.DATA_LIST.get(name) instanceof CpuData.CpuDataRangeArgument ||
                        data.DATA_LIST.get(name) instanceof CpuData.CpuDataFloatRangeArgument){
                    valueText = Text.literal(value);
                }
                if(name.equals("animation")){
                    valueText = Text.translatable("entity.fnafur."+ value).getWithStyle(style).getFirst();
                    if(Objects.equals(value, "default")){
                        valueText = Text.translatable("cpu_config.value." + value).getWithStyle(style).getFirst();
                    }
                }
                else if(name.equals("ambient_sound")){
                    String ambientValue = "sound.fnafur." + value;
                    if(value.isEmpty() || value.equals("default")) ambientValue = "cpu_config.value.none";
                    valueText = Text.translatable(ambientValue).getWithStyle(style).getFirst();
                }
                if(isList && !name.equals("back")) {
                    valueText = Text.translatable("entity.fnafur." + value).getWithStyle(style).getFirst();
                    if(Objects.equals(value, "default")){
                        valueText = Text.translatable("cpu_config.value." + value).getWithStyle(style).getFirst();
                    }
                }

                if(selected == i){
                    Style optionStyle = style.withUnderline(true);
                    valueText = valueText.getWithStyle(optionStyle).getFirst();
                }

                Text selectionText = Text.literal("").append(Text.literal("< ").getWithStyle(style).getFirst()).append(valueText).append(Text.literal(" >").getWithStyle(style).getFirst());
                Text text = Text.literal("").append(nameText).append(Text.literal(": ").getWithStyle(style).getFirst()).append(selectionText);
                if(title){
                    text = nameText;
                }
                else if(isList){
                    text = selectionText;
                }

                float scale = title ? 1.75f : 1f;
                float lowScale = title ? 1.1f : 0.5f;
                float scaleIndex = selected == i ? scale : lowScale;
                scale = i == selected ? scale : MathHelper.lerp(scaleIndex, lowScale, scale)/1.25f;
                scale = Math.max(scale, 0);
                float x = width/4f;
                float y = 3 + height/6f + i * 18;
                context.fill(-4 + (int)x, (int) y - 3, -12 + width - (width/4), (int) y + 11, 0xAA000101);
                drawResizableText(context, textRenderer, text, scale, x, y, color, 0x00000000, false, false);

            }

        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void renderDarkening(DrawContext context) {
        float index = Math.clamp(ClientHook.tickTransitionToScreen, 0, 1);
        int color = ColorHelper.getArgb((int) (MathHelper.lerp(index, 0, 0.6f) * 255f), 0, 0, 0);
        context.fill(0, 0, width, height, color);
        //super.renderDarkening(context);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {

        for(int i = 0; i < Math.min(getList().size(), 12); i++){
            int offset = scrollAmount;
            if(i + offset >= getList().size()) continue;
            String name = getList().get(i + offset);

            float x = width/4f;
            float y = 3 + height/6f + i * 18;
            if(isOnButton(mouseX, mouseY,-4 + (int)x, (int) y - 5, (-12 + width - (width/4)) - (-4 + (int)x), 18)){
                if(name.contains("title_")) continue;
                else{
                    selectedIndex = i + offset;
                    return;
                }

            }

        }
        selectedIndex = -1;
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if(age < 2) {
            age = 2;
            return super.mouseClicked(click, doubled);
        }
        if(selectedIndex == -1) return super.mouseClicked(click, doubled);
        String index = getList().get(selectedIndex);
        if(subWindow == 1){
            subWindow = 0;
            selectedIndex = 1;
            scrollAmount = 0;
            if(!index.equals("back")){
                data.Animation = index;
            }
            mouseMoved(click.x(), click.y());
        }
        else if(subWindow == 2){
            subWindow = 0;
            selectedIndex = 2;
            scrollAmount = 0;
            if (index.equals("default")) {
                data.AmbientSound = "";
            }
            if(!index.equals("back")){
                data.AmbientSound = index;
            }
            mouseMoved(click.x(), click.y());
        }
        else if(index.equals("animation")){
            subWindow = 1;
            selectedIndex = 0;
            scrollAmount = 0;
            mouseMoved(click.x(), click.y());
        }
        else if(index.equals("ambient_sound")){
            subWindow = 2;
            selectedIndex = 0;
            scrollAmount = 0;
            mouseMoved(click.x(), click.y());
        }
        else {
            if (click.button() == 0) {
                data.DATA_LIST.put(index, data.DATA_LIST.get(index).cycleRight());
            } else if (click.button() == 1) {
                data.DATA_LIST.put(index, data.DATA_LIST.get(index).cycleLeft());
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {

        if(verticalAmount < 0){
            scrollAmount++;
        } else if (verticalAmount > 0) {
            scrollAmount--;
        }
        scrollAmount = getList().size() < 12 ? 0 : Math.clamp(scrollAmount, 0, getList().size() - 12);
        mouseMoved(mouseX, mouseY);

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void close() {
        super.close();
        NbtCompound nbt = new NbtCompound();
        ItemStack stack = ItemInit.CPU.getDefaultStack();
        stack = CPUItem.putCpuData(stack, data);
        nbt.put("stack", ItemStack.CODEC, stack);
        MinecraftClient.getInstance().player.setStackInHand(MinecraftClient.getInstance().player.preferredHand, stack);
        ClientPlayNetworking.send(new UpdateMainHandItemC2SPayload(nbt));
    }

    @Override
    public boolean mouseReleased(Click click) {
        return super.mouseReleased(click);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if(age < 2) {
            age = 2;
            return super.keyPressed(input);
        }

        if(input.isDown() || selectedIndex == -1){
            selectedIndex++;

            selectedIndex = selectedIndex > getList().size() - 1 ? 0 : selectedIndex;


            if(selectedIndex < scrollAmount) scrollAmount = selectedIndex;
            if(getList().get(selectedIndex).contains("title_")){
                selectedIndex++;
            }
            if(selectedIndex > scrollAmount + 11) scrollAmount = selectedIndex - 11;
        } else if (input.isUp()) {
            if(selectedIndex == -1) selectedIndex = 0;
            selectedIndex--;

            if(getList().get(selectedIndex).contains("title_")){
                selectedIndex--;
            }

            selectedIndex = selectedIndex < 0 ? getList().size() - 1 : selectedIndex;

            if(selectedIndex < scrollAmount) scrollAmount = selectedIndex;
            if(selectedIndex > scrollAmount + 11) scrollAmount = selectedIndex - 11;
        } else if (input.isRight() || input.isEnterOrSpace()) {

            String index = getList().get(selectedIndex);
            if(subWindow == 1){
                subWindow = 0;
                selectedIndex = 1;
                scrollAmount = 0;
                if(!index.equals("back")){
                    data.Animation = index;
                }
                mouseMoved(0, 0);
            }
            else if(subWindow == 2){
                subWindow = 0;
                selectedIndex = 1;
                scrollAmount = 0;
                if(!index.equals("back")){
                    data.AmbientSound = index;
                }
                mouseMoved(0, 0);
            }
            else if(index.equals("animation")){
                subWindow = 1;
                selectedIndex = 0;
                scrollAmount = 0;
                mouseMoved(0, 0);
            }
            else if(index.equals("ambient_sound")){
                subWindow = 2;
                selectedIndex = 0;
                scrollAmount = 0;
                mouseMoved(0, 0);
            }
            else{
                data.DATA_LIST.put(index, data.DATA_LIST.get(index).cycleRight());
            }
        } else if (input.isLeft()) {
            String index = getList().get(selectedIndex);
            data.DATA_LIST.put(index, data.DATA_LIST.get(index).cycleLeft());
        }

        selectedIndex = Math.clamp(selectedIndex, 0, getList().size() - 1);
        scrollAmount = getList().size() < 12 ? 0 : Math.clamp(scrollAmount, 0, getList().size() - 12);

        if(getList().get(selectedIndex).contains("title_")){
            selectedIndex++;
        }

        updateList();
        return super.keyPressed(input);
    }

    @Override
    public boolean keyReleased(KeyInput input) {
        return super.keyReleased(input);
    }
}

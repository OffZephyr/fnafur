package net.zephyr.fnafur.client.gui.screens.crafting;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
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

    public CpuConfigScreen(Component title, CompoundTag nbt, long l) {
        super(title, nbt, l);
        ItemStack stack = Minecraft.getInstance().player.getMainHandItem();
        if(stack.is(ItemInit.CPU)){
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
        AmbientSoundList.add("none");
        AnimatronicDataHandler.SOUND_NAMES_PER_CATEGORY.forEach((category, names) -> {
            if(!category.toLowerCase().contains("default")){
                AmbientSoundList.add("title_" + category);

                AmbientSoundList.addAll(names);
            }
        });
    }

    String getArgumentValue(String argument){
        if(subWindow != 0){
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

    public CpuConfigScreen(Component text, CompoundTag CompoundTag, Object o) {
        super(text, CompoundTag, o);
        ItemStack stack = Minecraft.getInstance().player.getMainHandItem();
        if(stack.is(ItemInit.CPU)){
            data = CPUItem.getCpuData(stack);
        }
        selectedIndex++;

        updateList();
    }

    @Override
    public Vec3 getCameraPos() {
        BlockState blockState = Minecraft.getInstance().level.getBlockState(getBlockPos());
        if(blockState.is(BlockInit.CPU_CONFIG_PANEL)){
            Vec3 offset = blockState.getValue(CpuConfigPanelBlock.FACING).getUnitVec3().scale(0.3f);
            Vec3 offset2 = blockState.getValue(CpuConfigPanelBlock.FACING).getClockWise().getUnitVec3().scale(-0.175f);
            return getBlockPos().getCenter().add(0, -0.04f, 0).add(offset.add(offset2));
        }
        return getBlockPos().getCenter().add(new Vec3(0, 0, 0));
    }

    @Override
    public Vector3f getCameraAngle() {
        BlockState blockState = Minecraft.getInstance().level.getBlockState(getBlockPos());
        if(blockState.is(BlockInit.CPU_CONFIG_PANEL)){
            return new Vector3f(blockState.getValue(CpuConfigPanelBlock.FACING).toYRot() + 180, 0, 0);
        }
        return new Vector3f(0, 0, 0);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {

        if(ClientHook.tickTransitionToScreen == 1){
            age += delta/20f;
            FontDescription spriteFont = new FontDescription.Resource(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "lemon_terminal"));
            Style style = Style.EMPTY.withFont(spriteFont);

            Component MenuInitials = Component.literal("A.B.C.U.").toFlatList(style).getFirst();
            Component MenuName = Component.literal("Animatronic Behavior Configuration Unit").toFlatList(style).getFirst();
            if(age < 2){

                if(age > 1.75f) return;

                drawResizableText(context, font, MenuInitials, 5, width/2f, height/2f - 20, 0xFF33FF66, 0x00000000, false, true);
                if(age > 0.45f){
                    drawResizableText(context, font, MenuName, 0.9f, width/2f - 2, height/2f + 20, 0xFF00FF00, 0x00000000, false, true);
                }

                return;
            }

            Component Controls = Component.literal("")
                    .append(Component.literal("[").toFlatList(style).getFirst())
                    .append(Component.literal("↑↓"))
                    .append(Component.literal("]/[").toFlatList(style).getFirst())
                    .append(Component.literal("🖱"))
                    .append(Component.literal("] - Navigate").toFlatList(style).getFirst());

            Component Controls2 = Component.literal("")
                    .append(Component.literal("[").toFlatList(style).getFirst())
                    .append(Component.literal("←→"))
                    .append(Component.literal("]/[").toFlatList(style).getFirst())
                    .append(Component.literal("🖱"))
                    .append(Component.literal("]/[Enter] - Cycle Options").toFlatList(style).getFirst());

            Component Controls3 = Component.literal("- Controls -").toFlatList(style).getFirst();

            drawResizableText(context, font, MenuInitials, 2.7f, width/4f - 2, height/28f + 2, 0xFF33FF66, 0x00000000, false, false);
            drawResizableText(context, font, MenuName, 0.475f, width/4f - 4, height/28f + 23, 0xFF00FF00, 0x00000000, false, false);

            float controlWidth1 = font.width(Controls) * 0.75f;
            float controlWidth2 = font.width(Controls2) * 0.75f;
            float controlWidth3 = font.width(Controls3) * 0.9f;
            drawResizableText(context, font, Controls3, 0.9f, -12 + width - (width/4f) - controlWidth3, height/28f + 0, 0xFF33FF66, 0x00000000, false, false);
            drawResizableText(context, font, Controls, 0.75f, -12 + width - (width/4f) - controlWidth1, height/28f + 11, 0xFF00FF00, 0x00000000, false, false);
            drawResizableText(context, font, Controls2, 0.75f, -12 + width - (width/4f) - controlWidth2, height/28f + 20, 0xFF00FF00, 0x00000000, false, false);

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

                Component nameText = Component.translatable("cpu_config.argument." + name).toFlatList(style).getFirst();
                Component valueText = Component.translatable("cpu_config.value." + value).toFlatList(style).getFirst();

                if(data.DATA_LIST.get(name) instanceof CpuData.CpuDataRangeArgument ||
                        data.DATA_LIST.get(name) instanceof CpuData.CpuDataFloatRangeArgument){
                    valueText = Component.literal(value);
                }
                if(name.equals("animation")){
                    valueText = Component.translatable("entity.fnafur."+ value).toFlatList(style).getFirst();
                    if(Objects.equals(value, "default")){
                        valueText = Component.translatable("cpu_config.value." + value).toFlatList(style).getFirst();
                    }
                }
                else if(name.equals("ambient_sound")){
                    String ambientValue = "sounds.fnafur." + value;
                    if(value.isEmpty() || value.equals("none")) ambientValue = "cpu_config.value.none";
                    valueText = Component.translatable(ambientValue).toFlatList(style).getFirst();
                }

                if(isList && !name.equals("back")) {
                    String prefix = subWindow == 1 ? "entity.fnafur." : "sounds.fnafur.";
                    valueText = Component.translatable(prefix + value).toFlatList(style).getFirst();
                    if(Objects.equals(value, "default") || Objects.equals(value, "none")){
                        valueText = Component.translatable("cpu_config.value." + value).toFlatList(style).getFirst();
                    }
                }

                if(selected == i){
                    Style optionStyle = style.withUnderlined(true);
                    valueText = valueText.toFlatList(optionStyle).getFirst();
                }

                Component selectionText = Component.literal("").append(Component.literal("< ").toFlatList(style).getFirst()).append(valueText).append(Component.literal(" >").toFlatList(style).getFirst());
                Component text = Component.literal("").append(nameText).append(Component.literal(": ").toFlatList(style).getFirst()).append(selectionText);
                if(title){
                    text = nameText;
                }
                else if(isList){
                    text = selectionText;
                }

                float scale = title ? 1.75f : 1f;
                float lowScale = title ? 1.1f : 0.5f;
                float scaleIndex = selected == i ? scale : lowScale;
                scale = i == selected ? scale : Mth.lerp(scaleIndex, lowScale, scale)/1.25f;
                scale = Math.max(scale, 0);
                float x = width/4f;
                float y = 3 + height/6f + i * 18;
                context.fill(-4 + (int)x, (int) y - 3, -12 + width - (width/4), (int) y + 11, 0xAA000101);
                drawResizableText(context, font, text, scale, x, y, color, 0x00000000, false, false);

            }

        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void renderMenuBackground(GuiGraphics context) {
        float index = Math.clamp(ClientHook.tickTransitionToScreen, 0, 1);
        int color = ARGB.color((int) (Mth.lerp(index, 0, 0.6f) * 255f), 0, 0, 0);
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
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
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
            if (index.equals("none")) {
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
    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
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
    public void onClose() {
        super.onClose();
        CompoundTag nbt = new CompoundTag();
        ItemStack stack = ItemInit.CPU.getDefaultInstance();
        stack = CPUItem.putCpuData(stack, data);
        nbt.store("stack", ItemStack.CODEC, stack);
        Minecraft.getInstance().player.setItemInHand(Minecraft.getInstance().player.swingingArm, stack);
        ClientPlayNetworking.send(new UpdateMainHandItemC2SPayload(nbt));
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        return super.mouseReleased(click);
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
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
        } else if (input.isRight() || input.isSelection()) {

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
    public boolean keyReleased(KeyEvent input) {
        return super.keyReleased(input);
    }
}

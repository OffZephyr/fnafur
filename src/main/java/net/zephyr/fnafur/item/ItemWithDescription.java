package net.zephyr.fnafur.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ItemWithDescription extends Item {
    public static final int WRENCH = 0;
    public static final int PAINT_BRUSH = 1;
    public static final int TAPE_MEASURE = 2;
    public static final int COIN = 3;
    public static final int FLOPPY_DISK = 4;
    public static final int COMPUTER = 5;
    List<Text> tools;
    public ItemWithDescription(Settings settings, int... tools) {
        super(settings);
        this.tools = getTools(tools);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) throws NumberFormatException {
        tooltip.addAll(ItemWithDescription.getDescription(this.tools, this));
    }

    public static List<Text> getTools(int... tools){
        List<Text> toolList = new ArrayList<>();
        for (int tool: tools) {
            if(tool == ItemWithDescription.WRENCH){
                toolList.add(Text.translatable("fnafur.symbol.wrench"));
            }
            if(tool == ItemWithDescription.PAINT_BRUSH){
                toolList.add(Text.translatable("fnafur.symbol.paintbrush"));
            }
            if(tool == ItemWithDescription.TAPE_MEASURE){
                toolList.add(Text.translatable("fnafur.symbol.tapemeasure"));
            }
            if(tool == ItemWithDescription.COIN){
                toolList.add(Text.translatable("fnafur.symbol.coin"));
            }
            if(tool == ItemWithDescription.FLOPPY_DISK){
                toolList.add(Text.translatable("fnafur.symbol.floppy_disk"));
            }
            if(tool == ItemWithDescription.COMPUTER){
                toolList.add(Text.translatable("fnafur.symbol.computer"));
            }
        }
        return toolList;
    }

    public static List<Text> getDescription(List<Text> tools, Item item){
        KeyBinding sneak = MinecraftClient.getInstance().options.sneakKey;
        String key = sneak.getBoundKeyTranslationKey();
        boolean bl = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey(key).getCode());

        String keyText = "§l" + MinecraftClient.getInstance().options.sneakKey.getBoundKeyLocalizedText().getString();
        List<Text> defaultList = new ArrayList<>();
        StringBuilder toolsString = new StringBuilder();
        for(int i = 0; i < tools.size(); i++){
            if(i < tools.size() - 1){
                toolsString.append(tools.get(i).getString()).append(" ");
            }
            else {
                toolsString.append(tools.get(i).getString());
            }
        }
        defaultList.add(Text.literal(toolsString.toString()));
        defaultList.add(Text.translatable("item.fnafur.default_description", keyText));

        List<Text> expandedList = new ArrayList<>();
        for(int i = 0; i < tools.size(); i++) {
            try{
                Integer.parseInt(Text.translatable(item.getTranslationKey() + ".description." + i + ".length").getString());
            }
            catch(NumberFormatException e) {
                throw new NumberFormatException("No length was set for description #" + i + ". Set a length for it in your lang file.");
            }

            int descriptionLength = Integer.parseInt(Text.translatable(item.getTranslationKey() + ".description." + i + ".length").getString());

            expandedList.add(Text.literal(""));
            for(int j = 0; j < descriptionLength; j++){
                String tabulation = j == 0 ? tools.get(i).getString() + ": " : "   ";
                String line = tabulation + Text.translatable(item.getTranslationKey() + ".description." + i + "." + j).getString();
                expandedList.add(Text.literal(line));
            }
        }
        expandedList.add(Text.literal(""));
        return bl ? expandedList : defaultList;
    }
}

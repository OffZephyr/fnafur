package net.zephyr.fnafur.init.item_init;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.props.ColorEnumInterface;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.init.block_init.PropInit;
import net.zephyr.fnafur.init.item_init.SpawnItemInit;
import net.zephyr.fnafur.init.item_init.StickerInit;
import net.zephyr.fnafur.item.DeathCoin;
import net.zephyr.fnafur.item.FloppyDiskItem;
import net.zephyr.fnafur.item.IllusionDisc;
import net.zephyr.fnafur.item.ZephyrSpawn;
import net.zephyr.fnafur.item.spawn_items.classic.cl_fred_SpawnItem;
import net.zephyr.fnafur.item.tools.PaintbrushItem;
import net.zephyr.fnafur.item.tablet.TabletItem;
import net.zephyr.fnafur.item.tools.TapeMesurerItem;
import net.zephyr.fnafur.item.tools.WrenchItem;

public class ItemInit {

    public static final Item MOD_LOGO = registerItem("fnafur",
            new Item(new Item.Settings().maxCount(0).rarity(Rarity.EPIC)));
    public static final Item WRENCH = registerItem("wrench",
            new WrenchItem(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item PAINTBRUSH = registerItem("paintbrush",
            new PaintbrushItem(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item TAPEMEASURE = registerItem("tapemeasure",
            new TapeMesurerItem(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item TABLET = registerItem("tablet",
            new TabletItem(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item FLOPPYDISK = registerItem("floppy_disk",
            new FloppyDiskItem(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item DEATHCOIN = registerItem("deathcoin",
            new DeathCoin(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item ILLUSIONDISC = registerItem("illusion_disc",
            new IllusionDisc(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(FnafUniverseResuited.MOD_ID, name), item);
    }
    public static void registerItems() {
        StickerInit.registerStickers();
        SpawnItemInit.registerSpawnItems();
        FnafUniverseResuited.LOGGER.info("Registering Items for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }

    public static void clientRegisterItem(){
        FnafUniverseResuited.LOGGER.info("Registering Items on Client for " + FnafUniverseResuited.MOD_ID.toUpperCase());

        ModelPredicateProviderRegistry.register(TABLET, Identifier.of("offhand"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.getOffHandStack() == itemStack ? 1.0F : 0.0F;
        });

        ModelPredicateProviderRegistry.register(TABLET, Identifier.of("on"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT) == null || itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().isEmpty() ? 0.0F : itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("used") ? 1.0F : 0.0F;
        });

        ModelPredicateProviderRegistry.register(TAPEMEASURE, Identifier.of("used"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt() == null || itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().isEmpty() ? 0.0F : itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("used") ? 1.0F : 0.0F;
        });

        for(Item item : PropInit.propItems){
            ModelPredicateProviderRegistry.register(
                    item,
                    Identifier.of("skin"),
                    (stack, world, entity, seed) -> {
                        if(((BlockItem)item).getBlock() instanceof PropBlock<?> block){

                            BlockStateComponent blockStateComponent = stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT);
                            ColorEnumInterface color = blockStateComponent.getValue(block.COLOR_PROPERTY());
                            return color != null ? color.getIndex() : 0;
                        }
                        return 0;
                    });
        }
    }
}

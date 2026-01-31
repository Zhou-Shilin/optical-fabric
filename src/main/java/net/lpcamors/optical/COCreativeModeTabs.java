package net.lpcamors.optical;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.lpcamors.optical.blocks.COBlocks;
import net.lpcamors.optical.data.COLang;
import net.lpcamors.optical.items.COItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class COCreativeModeTabs {

    public static final CreativeModeTab CO_BASE_CREATIVE_TAB = FabricItemGroup.builder()
            .title(Component.translatable("itemGroup." + CreateOptical.ID + ".co_base"))
            .icon(COBlocks.OPTICAL_SOURCE::asStack)
            .displayItems(COCreativeModeTabs::displayItemsGenerator)
            .build();

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CreateOptical.loc("co_base"), CO_BASE_CREATIVE_TAB);
    }

    private static final Predicate<Item> EXCLUSION_PREDICATE;

    static {
        Set<Item> exclusions = new ReferenceOpenHashSet<>();
        List<ItemProviderEntry<?>> excludedItems = List.of(
                COItems.INCOMPLETE_COPPER_COIL,
                COItems.INCOMPLETE_GOLDEN_COIL,
                COItems.INCOMPLETE_ZINC_COIL,
                COItems.INCOMPLETE_QUARTZ_CATALYST_COIL,
                COItems.INCOMPLETE_MIRROR,
                COItems.INCOMPLETE_POLARIZING_FILTER,
                COItems.INCOMPLETE_OPTICAL_DEVICE
        );
        for (ItemProviderEntry<?> entry : excludedItems) {
            exclusions.add(entry.asItem());
        }
        EXCLUSION_PREDICATE = exclusions::contains;
    }

    private static void displayItemsGenerator(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        CreateOptical.REGISTRATE.getAll(Registries.BLOCK).forEach(blockRegistryEntry -> {
            Item item = blockRegistryEntry.get().asItem();
            if (item != Items.AIR && !EXCLUSION_PREDICATE.test(item)) {
                output.accept(item);
            }
        });
        CreateOptical.REGISTRATE.getAll(Registries.ITEM).forEach(itemRegistryEntry -> {
            Item item = itemRegistryEntry.get();
            if (!(item instanceof BlockItem) && !EXCLUSION_PREDICATE.test(item)) {
                output.accept(item);
            }
        });
    }
}

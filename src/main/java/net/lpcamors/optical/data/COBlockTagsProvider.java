package net.lpcamors.optical.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.lpcamors.optical.CreateOptical;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class COBlockTagsProvider extends FabricTagProvider.BlockTagProvider {

    public COBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(COTags.Blocks.PENETRABLE)
                .add(Blocks.AIR, Blocks.LIGHT, Blocks.TRIPWIRE, Blocks.REDSTONE_WIRE, Blocks.WATER, Blocks.REPEATER, Blocks.COMPARATOR);
        getOrCreateTagBuilder(COTags.Blocks.PENETRABLE)
                .addOptionalTag(BlockTags.ALL_HANGING_SIGNS.location())
                .addOptionalTag(BlockTags.ALL_SIGNS.location())
                .addOptionalTag(BlockTags.BANNERS.location())
                .addOptionalTag(BlockTags.SLABS.location())
                .addOptionalTag(BlockTags.WOOL_CARPETS.location())
                .addOptionalTag(BlockTags.BUTTONS.location())
                .addOptionalTag(BlockTags.PRESSURE_PLATES.location())
                .addOptionalTag(BlockTags.WOODEN_TRAPDOORS.location())
                .addOptionalTag(BlockTags.CANDLES.location())
                .addOptionalTag(BlockTags.CLIMBABLE.location())
                .addOptionalTag(BlockTags.FIRE.location())
                .addOptionalTag(BlockTags.LEAVES.location())
                .addOptionalTag(BlockTags.FLOWERS.location())
                .addOptionalTag(BlockTags.CORAL_PLANTS.location())
                .addOptionalTag(BlockTags.SAPLINGS.location())
                .addOptionalTag(BlockTags.CROPS.location())
                .addOptionalTag(BlockTags.RAILS.location())
                .addOptionalTag(BlockTags.REPLACEABLE_BY_TREES.location())
                .addOptionalTag(new net.minecraft.resources.ResourceLocation("c", "glass_blocks"))
                .addOptionalTag(new net.minecraft.resources.ResourceLocation("c", "glass_panes"));
        getOrCreateTagBuilder(COTags.Blocks.IMPENETRABLE)
                .addOptionalTag(new net.minecraft.resources.ResourceLocation("c", "tinted_glass"));
    }
}

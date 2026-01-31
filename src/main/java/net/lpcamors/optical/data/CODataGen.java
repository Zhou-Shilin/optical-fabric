package net.lpcamors.optical.data;

import com.tterrag.registrate.providers.ProviderType;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.lpcamors.optical.CreateOptical;
import net.lpcamors.optical.ponder.COPonderPlugin;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class CODataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        
        // Create ExistingFileHelper for Registrate
        ExistingFileHelper existingFileHelper = ExistingFileHelper.withResourcesFromArg();
        
        // Add Registrate data providers for blockstates, models, lang, etc.
        CreateOptical.REGISTRATE.setupDatagen(pack, existingFileHelper);
        
        // Add custom providers
        pack.addProvider(COBlockTagsProvider::new);
        pack.addProvider(COEntriesProvider::new);
        // Note: Recipe providers need to be adapted for Fabric
        // pack.addProvider(COSequencedAssemblyRecipeProvider::new);
        // pack.addProvider(FocusingRecipeGen::new);

        // Register ponder lang
        CreateOptical.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;
            providePonderLang(langConsumer);
        });
    }

    private static void providePonderLang(BiConsumer<String, String> consumer) {
        PonderIndex.addPlugin(new COPonderPlugin());
        PonderIndex.getLangAccess().provideLang(CreateOptical.ID, consumer);
    }
}

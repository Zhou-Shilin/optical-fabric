package net.lpcamors.optical.data;

import com.simibubi.create.AllKeys;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.ponder.CreatePonderPlugin;
import com.tterrag.registrate.providers.ProviderType;
import net.createmod.ponder.foundation.PonderIndex;
import net.createmod.ponder.foundation.registration.PonderLocalization;
import net.lpcamors.optical.COMod;
import net.lpcamors.optical.ponder.COPonderPlugin;
import net.lpcamors.optical.ponder.COPonderTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class CODataGen {

    public static void dataGen(GatherDataEvent event){
        //addExtraRegistrateData();
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if (event.includeServer()) {
            COEntriesProvider generatedEntriesProvider = new COEntriesProvider(output, lookupProvider);
            generator.addProvider(true, new COBlockTagsProvider(output, lookupProvider, existingFileHelper));
            generator.addProvider(true, generatedEntriesProvider);
            generator.addProvider(true, new COSequencedAssemblyRecipeProvider(output));
            generator.addProvider(true, new FocusingRecipeGen(output));

        }
        COMod.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            //provideDefaultLang("interface", langConsumer);
            //provideDefaultLang("tooltips", langConsumer);
            providePonderLang(langConsumer);
        });

    }


    private static void providePonderLang(BiConsumer<String, String> consumer) {
        PonderIndex.addPlugin(new COPonderPlugin());

        PonderIndex.getLangAccess().provideLang(COMod.ID, consumer);
    }


}

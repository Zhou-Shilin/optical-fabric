package net.lpcamors.optical;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.createmod.catnip.lang.FontHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.lpcamors.optical.blocks.COBlockEntities;
import net.lpcamors.optical.blocks.COBlocks;
import net.lpcamors.optical.config.COConfigs;
import net.lpcamors.optical.data.COLang;
import net.lpcamors.optical.items.COItems;
import net.lpcamors.optical.network.COPackets;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class CreateOptical implements ModInitializer {
    public static final String ID = "create_optical";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public static ResourceLocation loc(String name) {
        return new ResourceLocation(ID, name);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Create Optical initializing!");

        // Load partial models on client
        EnvExecutor.runWhenOn(EnvType.CLIENT, () -> COPartialModels::new);

        // Initialize registrations
        COCreativeModeTabs.register();
        COBlocks.register();
        COItems.register();
        COBlockEntities.register();
        CORecipeTypes.register();
        COLang.register();

        // Register networking
        COPackets.register();

        // Register configs
        COConfigs.register();

        // Finalize Registrate
        REGISTRATE.register();

        LOGGER.info("Create Optical initialized!");
    }
}

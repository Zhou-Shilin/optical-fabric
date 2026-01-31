package net.lpcamors.optical;

import net.createmod.ponder.foundation.PonderIndex;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lpcamors.optical.ponder.COPonderPlugin;

@Environment(EnvType.CLIENT)
public class CreateOpticalClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        COPartialModels.register();
        PonderIndex.addPlugin(new COPonderPlugin());
    }
}

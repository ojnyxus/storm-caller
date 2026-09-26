package com.stormcaller.client.integration;

import com.stormcaller.client.screen.StormcallerConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Only ever invoked by Mod Menu itself, on the client. If the person doesn't have
 * Mod Menu installed, this class is simply never touched - Stormcaller works fine
 * without it, this just adds the little "config" gear icon next to the mod entry.
 */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return StormcallerConfigScreen::new;
    }
}

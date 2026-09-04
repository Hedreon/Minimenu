package org.minimalmenu.fabric.options;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import org.minimalmenu.common.options.ScreenFactory;

public class IntegrationFabric implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ScreenFactory::createScreen;
    }
}
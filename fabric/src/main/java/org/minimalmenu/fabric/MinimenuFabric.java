package org.minimalmenu.fabric;

import net.fabricmc.api.ClientModInitializer;
import org.minimalmenu.common.options.FileHandler;

public class MinimenuFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FileHandler.HANDLER.load();
    }
}
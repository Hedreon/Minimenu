package org.minimalmenu;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.minimalmenu.options.FileHandler;

import java.util.Objects;

public class Minimenu implements ClientModInitializer {
    public static final String MOD_ID = "minimenu";

    @Override
    public void onInitializeClient() {
        FileHandler.HANDLER.load();
    }

    public static boolean buttonMatchesKey(AbstractWidget button, String key) {
        Component buttonMessage = button.getMessage();
        Component keyMessage = Component.translatable(key);

        return Objects.equals(buttonMessage, keyMessage);
    }
}
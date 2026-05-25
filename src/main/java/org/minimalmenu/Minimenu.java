package org.minimalmenu;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.minimalmenu.options.FileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Minimenu implements ClientModInitializer {
    public static final String MOD_ID = "minimenu";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        FileHandler.HANDLER.load();
    }

    public static boolean widgetMatchesKey(AbstractWidget widget, String key) {
        Component widgetMessage = widget.getMessage();
        Component translatableKey = Component.translatable(key);

        return Objects.equals(widgetMessage, translatableKey);
    }
}
package org.minimalmenu.common;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.minimalmenu.common.helpers.ScreenExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class MinimenuCommon {
    public static final String MOD_ID = "minimenu";
    public static final String MOD_NAME = StringUtils.capitalize(MOD_ID);

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static boolean widgetMatchesKey(AbstractWidget widget, String key) {
        Objects.requireNonNull(widget, "Widget cannot be null");

        Component widgetMessage = widget.getMessage();
        Component translatableKey = Component.translatable(key);

        return Objects.equals(widgetMessage, translatableKey);
    }

    public static List<AbstractWidget> getWidgets(Screen screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");

        return ScreenExtensions.getExtensions(screen).GET_AVAILABLE_BUTTONS();
    }

    public static Identifier identify(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
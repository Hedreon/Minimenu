package org.minimalmenu.common.helpers;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

/**
 * Adapted from <a href="https://github.com/FabricMC/fabric-api/blob/HEAD/fabric-screen-api-v1/src/client/java/net/fabricmc/fabric/impl/client/screen/ScreenExtensions.java">Fabric API's ScreenExtensions.java.</a>
 */

public interface ScreenExtensions {
    static ScreenExtensions getExtensions(Screen screen) {
        return (ScreenExtensions) screen;
    }

    List<AbstractWidget> GET_AVAILABLE_BUTTONS();
}
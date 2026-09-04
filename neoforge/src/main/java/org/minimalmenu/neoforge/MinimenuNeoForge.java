package org.minimalmenu.neoforge;

import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.NotNull;
import org.minimalmenu.common.Minimenu;
import org.minimalmenu.common.options.FileHandler;
import org.minimalmenu.common.options.ScreenFactory;

@Mod(value = Minimenu.MOD_ID, dist = Dist.CLIENT)
public class MinimenuNeoForge {
    public MinimenuNeoForge(IEventBus eventBus) {
        eventBus.addListener(this::initialize);
    }

    private static class ScreenFactoryNeoForge implements IConfigScreenFactory {
        @Override
        public @NotNull Screen createScreen(@NotNull ModContainer container, @NotNull Screen previousScreen) {
            return ScreenFactory.createScreen(previousScreen);
        }
    }

    private void initialize(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, ScreenFactoryNeoForge::new);

        FileHandler.HANDLER.load();
    }
}
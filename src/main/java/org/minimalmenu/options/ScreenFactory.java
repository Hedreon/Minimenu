package org.minimalmenu.options;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.StateManager;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ScreenFactory implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::createScreen;
    }

    public Screen createScreen(Screen parentScreen) {
        var modeStateManager = StateManager.createSimple(
                FileHandler.MODES.None,
                () -> FileHandler.REMOVED_MODE,
                newValue -> FileHandler.REMOVED_MODE = newValue
        );

        var builder = YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("minimenu.options.title"));

        builder.save(FileHandler.HANDLER::save);

        // Create categories
        var titleScreen = ConfigCategory.createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.title"));

        var pauseScreen = ConfigCategory.createBuilder()
                .name(Component.translatable("minimenu.options.pause_screen.title"));

        // Build title screen options
        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.subtitle.java_edition"))
                .binding(false, () -> FileHandler.REMOVE_EDITION, newValue -> FileHandler.REMOVE_EDITION = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.button.singleplayer"))
                .stateManager(new RadioStateManager<>(
                        modeStateManager,
                        FileHandler.MODES.Singleplayer,
                        FileHandler.MODES.None
                ))
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.button.multiplayer"))
                .stateManager(new RadioStateManager<>(
                        modeStateManager,
                        FileHandler.MODES.Multiplayer,
                        FileHandler.MODES.None
                ))
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.button.minecraft_realms"))
                .binding(false, () -> FileHandler.REMOVE_REALMS, newValue -> FileHandler.REMOVE_REALMS = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.button.language"))
                .binding(false, () -> FileHandler.REMOVE_LANGUAGE, newValue -> FileHandler.REMOVE_LANGUAGE = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.button.accessibility"))
                .binding(false, () -> FileHandler.REMOVE_ACCESSIBILITY, newValue -> FileHandler.REMOVE_ACCESSIBILITY = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

//        titleScreen.addEntry(entryBuilder.startBooleanToggle(Text.translatable("minimenu.options.title_screen.text.copyright"), ConfigHandler.REMOVE_COPYRIGHT)
//                .setDefaultValue(false)
//                .setSaveConsumer(newValue -> ConfigHandler.REMOVE_COPYRIGHT = newValue)
//                .build());

//        titleScreen.addEntry(entryBuilder.startBooleanToggle(Text.translatable("minimenu.options.title_screen.background"), ConfigHandler.DIRT_BACKGROUND)
//                .setDefaultValue(false)
//                .setSaveConsumer(newValue -> ConfigHandler.DIRT_BACKGROUND = newValue)
//                .build());

        builder.category(titleScreen.build());

        // Build pause screen options
        pauseScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.pause_screen.button.feedback"))
                .binding(false, () -> FileHandler.REMOVE_FEEDBACK, newValue -> FileHandler.REMOVE_FEEDBACK = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        pauseScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.pause_screen.button.report_bugs"))
                .binding(false, () -> FileHandler.REMOVE_BUGS, newValue -> FileHandler.REMOVE_BUGS = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        pauseScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.pause_screen.button.player_reporting"))
                .binding(false, () -> FileHandler.REMOVE_REPORTING, newValue -> FileHandler.REMOVE_REPORTING = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        pauseScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.pause_screen.button.open_to_lan"))
                .binding(false, () -> FileHandler.REMOVE_LAN, newValue -> FileHandler.REMOVE_LAN = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        builder.category(pauseScreen.build());

        return builder.build().generateScreen(parentScreen);
    }
}
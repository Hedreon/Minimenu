package org.minimalmenu.options;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.StateManager;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import net.minecraft.network.chat.Component;

public class ScreenFactory implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::createScreen;
    }

    public Screen createScreen(Screen parentScreen) {
        Minecraft minecraft = Minecraft.getInstance();

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
        titleScreen.option(ButtonOption.createBuilder()
                .name(Component.translatable("options.hideSplashTexts"))
                .action((yaclScreen, _) ->
                        minecraft.setScreen(new AccessibilityOptionsScreen(yaclScreen, minecraft.options)
                ))
                .text(Component.empty())
                .description(OptionDescription.of(Component.translatable("minimenu.options.title_screen.splash_texts.description")))
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.java_edition.name"))
                .binding(false, () -> FileHandler.REMOVE_EDITION, newValue -> FileHandler.REMOVE_EDITION = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.singleplayer.name"))
                .stateManager(new RadioStateManager<>(
                        modeStateManager,
                        FileHandler.MODES.Singleplayer,
                        FileHandler.MODES.None
                ))
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.multiplayer.name"))
                .stateManager(new RadioStateManager<>(
                        modeStateManager,
                        FileHandler.MODES.Multiplayer,
                        FileHandler.MODES.None
                ))
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.minecraft_realms.name"))
                .binding(false, () -> FileHandler.REMOVE_REALMS, newValue -> FileHandler.REMOVE_REALMS = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.language.name"))
                .binding(false, () -> FileHandler.REMOVE_LANGUAGE, newValue -> FileHandler.REMOVE_LANGUAGE = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.accessibility.name"))
                .binding(false, () -> FileHandler.REMOVE_ACCESSIBILITY, newValue -> FileHandler.REMOVE_ACCESSIBILITY = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

//        titleScreen.option(Option.<Boolean>createBuilder()
//                .name(Component.translatable("minimenu.options.title_screen.background.name"))
//                .binding(false, () -> FileHandler.CLASSIC_BACKGROUND, newValue -> FileHandler.CLASSIC_BACKGROUND = newValue)
//                .controller(TickBoxControllerBuilder::create)
//                .build());

//        titleScreen.option(Option.<String>createBuilder()
//                .name(Component.translatable("minimenu.options.title_screen.copyright.name"))
//                .binding(
//                        Component.translatable("title.credits").getString(),
//                        () -> FileHandler.COPYRIGHT_TEXT,
//                        newValue -> FileHandler.COPYRIGHT_TEXT = newValue
//                )
//                .controller(StringControllerBuilder::create)
//                .build());

        builder.category(titleScreen.build());

        // Build pause screen options
        pauseScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.pause_screen.feedback.name"))
                .binding(false, () -> FileHandler.REMOVE_FEEDBACK, newValue -> FileHandler.REMOVE_FEEDBACK = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        pauseScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.pause_screen.report_bugs.name"))
                .binding(false, () -> FileHandler.REMOVE_BUGS, newValue -> FileHandler.REMOVE_BUGS = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        pauseScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.pause_screen.player_reporting.name"))
                .binding(false, () -> FileHandler.REMOVE_REPORTING, newValue -> FileHandler.REMOVE_REPORTING = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        pauseScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimenu.options.pause_screen.open_to_lan.name"))
                .binding(false, () -> FileHandler.REMOVE_LAN, newValue -> FileHandler.REMOVE_LAN = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        builder.category(pauseScreen.build());

        return builder.build().generateScreen(parentScreen);
    }
}
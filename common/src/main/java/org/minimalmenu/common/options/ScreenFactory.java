package org.minimalmenu.common.options;

import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.StateManager;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ScreenFactory {
    public static Screen createScreen(Screen previousScreen) {
        // Set up variables
        var builder = YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("minimenu.options.title"));

        var modeManager = StateManager.createSimple(
                FileHandler.MODES.None,
                () -> FileHandler.REMOVED_MODE,
                newValue -> FileHandler.REMOVED_MODE = newValue
        );

        Minecraft minecraft = Minecraft.getInstance();

        // Define save function
        builder.save(FileHandler.HANDLER::save);

        // Create categories
        var general = ConfigCategory.createBuilder()
                .name(Component.translatable("minimenu.options.general.title"))
                .tooltip(Component.translatable("minimenu.options.general.tooltip"));

        var titleScreen = ConfigCategory.createBuilder()
                .name(Component.translatable("minimenu.options.title_screen.title"))
                .tooltip(Component.translatable("minimenu.options.title_screen.tooltip"));

        var pauseScreen = ConfigCategory.createBuilder()
                .name(Component.translatable("minimenu.options.pause_screen.title"))
                .tooltip(Component.translatable("minimenu.options.pause_screen.tooltip"));

        // Build general options
        general.group(OptionGroup.createBuilder()
                .name(Component.translatable("minimenu.options.group.background.title"))
                .description(OptionDescription.of(Component.translatable("minimenu.options.group.background.description")))
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("minimenu.options.general.background.name"))
                        .description(OptionDescription.of(Component.translatable("minimenu.options.general.background.description")))
                        .binding(false, () -> FileHandler.CLASSIC_BACKGROUND, newValue -> FileHandler.CLASSIC_BACKGROUND = newValue)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .build());

        builder.category(general.build());

        // Build title screen options
        titleScreen.group(OptionGroup.createBuilder()
                .name(Component.translatable("minimenu.options.group.minecraft.title"))
                .description(OptionDescription.of(Component.translatable("minimenu.options.group.minecraft.description")))
                .options(List.of(
                        ButtonOption.createBuilder()
                                .name(Component.translatable("options.hideSplashTexts"))
                                .description(OptionDescription.of(Component.literal(String.join(" ",
                                        Component.translatable(
                                                "minimenu.options.link",
                                                Component.translatable("options.accessibility.title").getString().toLowerCase()
                                        ).getString(),
                                        Component.translatable("minimenu.options.link.splash_texts").getString()
                                ))))
                                .text(Component.empty())
                                .action((yaclScreen, _) ->
                                        minecraft.gui.setScreen(new AccessibilityOptionsScreen(yaclScreen, minecraft.options))
                                )
                                .build(),
                        ButtonOption.createBuilder()
                                .name(Component.translatable("options.accessibility.panorama_speed"))
                                .description(OptionDescription.of(Component.literal(String.join(" ",
                                        Component.translatable(
                                                "minimenu.options.link",
                                                Component.translatable("options.accessibility.title").getString().toLowerCase()
                                        ).getString(),
                                        Component.translatable("minimenu.options.link.panorama_speed").getString()
                                ))))
                                .text(Component.empty())
                                .action((yaclScreen, _) ->
                                        minecraft.gui.setScreen(new AccessibilityOptionsScreen(yaclScreen, minecraft.options))
                                )
                                .build()
                ))
                .build());

        titleScreen.group(OptionGroup.createBuilder()
                .name(Component.translatable("minimenu.options.group.buttons.title"))
                .description(OptionDescription.of(Component.translatable("minimenu.options.group.buttons.description")))
                .options(List.of(
                        Option.<Boolean>createBuilder()
                                .name(Component.translatable("minimenu.options.title_screen.singleplayer.name"))
                                .description(OptionDescription.of(Component.translatable("minimenu.options.title_screen.singleplayer.description")))
                                .stateManager(modeManager.xmap(
                                        newValue -> newValue == FileHandler.MODES.Singleplayer,
                                        oldValue -> oldValue
                                                ? FileHandler.MODES.Singleplayer
                                                : FileHandler.MODES.None
                                ))
                                .controller(TickBoxControllerBuilder::create)
                                .build(),
                        Option.<Boolean>createBuilder()
                                .name(Component.translatable("minimenu.options.title_screen.multiplayer.name"))
                                .description(OptionDescription.of(Component.translatable("minimenu.options.title_screen.multiplayer.description")))
                                .stateManager(modeManager.xmap(
                                        newValue -> newValue == FileHandler.MODES.Multiplayer,
                                        oldValue -> oldValue
                                                ? FileHandler.MODES.Multiplayer
                                                : FileHandler.MODES.None
                                ))
                                .controller(TickBoxControllerBuilder::create)
                                .build(),
                        Option.<Boolean>createBuilder()
                                .name(Component.translatable("minimenu.options.title_screen.minecraft_realms.name"))
                                .description(OptionDescription.of(Component.translatable("minimenu.options.title_screen.minecraft_realms.description")))
                                .binding(false, () -> FileHandler.REMOVE_REALMS, newValue -> FileHandler.REMOVE_REALMS = newValue)
                                .controller(TickBoxControllerBuilder::create)
                                .build(),
                        Option.<Boolean>createBuilder()
                                .name(Component.translatable("minimenu.options.title_screen.friends.name"))
                                .description(OptionDescription.of(Component.translatable("minimenu.options.title_screen.friends.description")))
                                .binding(false, () -> FileHandler.REMOVE_FRIENDS, newValue -> FileHandler.REMOVE_FRIENDS = newValue)
                                .controller(TickBoxControllerBuilder::create)
                                .build(),
                        Option.<Boolean>createBuilder()
                                .name(Component.translatable("minimenu.options.title_screen.language.name"))
                                .description(OptionDescription.of(Component.translatable("minimenu.options.title_screen.language.description")))
                                .binding(false, () -> FileHandler.REMOVE_LANGUAGE, newValue -> FileHandler.REMOVE_LANGUAGE = newValue)
                                .controller(TickBoxControllerBuilder::create)
                                .build(),
                        Option.<Boolean>createBuilder()
                                .name(Component.translatable("minimenu.options.title_screen.accessibility.name"))
                                .description(OptionDescription.of(Component.translatable("minimenu.options.title_screen.accessibility.description")))
                                .binding(false, () -> FileHandler.REMOVE_ACCESSIBILITY, newValue -> FileHandler.REMOVE_ACCESSIBILITY = newValue)
                                .controller(TickBoxControllerBuilder::create)
                                .build()
                ))
                .build());

        titleScreen.group(OptionGroup.createBuilder()
                .name(Component.translatable("minimenu.options.group.texts.title"))
                .description(OptionDescription.of(Component.translatable("minimenu.options.group.texts.description")))
                .options(List.of(
                        Option.<Boolean>createBuilder()
                                .name(Component.translatable("minimenu.options.title_screen.java_edition.name"))
                                .description(OptionDescription.of(Component.translatable("minimenu.options.title_screen.java_edition.description")))
                                .binding(false, () -> FileHandler.REMOVE_EDITION, newValue -> FileHandler.REMOVE_EDITION = newValue)
                                .controller(TickBoxControllerBuilder::create)
                                .build(),
                        Option.<Boolean>createBuilder()
                                .name(Component.translatable("minimenu.options.title_screen.version.name"))
                                .description(OptionDescription.of(Component.translatable("minimenu.options.title_screen.version.description")))
                                .binding(false, () -> FileHandler.REMOVE_VERSION, newValue -> FileHandler.REMOVE_VERSION = newValue)
                                .controller(TickBoxControllerBuilder::create)
                                .build(),
                        Option.<Boolean>createBuilder()
                                .name(Component.translatable("minimenu.options.title_screen.copyright.name"))
                                .description(OptionDescription.of(Component.translatable("minimenu.options.title_screen.copyright.description")))
                                .binding(false, () -> FileHandler.REMOVE_COPYRIGHT, newValue -> FileHandler.REMOVE_COPYRIGHT = newValue)
                                .controller(TickBoxControllerBuilder::create)
                                .build()
                ))
                .build());

        builder.category(titleScreen.build());

        // Build pause screen options
        pauseScreen.group(OptionGroup.createBuilder()
                .name(Component.translatable("minimenu.options.group.buttons.title"))
                .description(OptionDescription.of(Component.translatable("minimenu.options.group.buttons.description")))
                .options(List.of(
                        Option.<Boolean>createBuilder()
                                .name(Component.translatable("minimenu.options.pause_screen.feedback.name"))
                                .binding(false, () -> FileHandler.REMOVE_FEEDBACK, newValue -> FileHandler.REMOVE_FEEDBACK = newValue)
                                .controller(TickBoxControllerBuilder::create)
                                .build(),
                        Option.<Boolean>createBuilder()
                                .name(Component.translatable("minimenu.options.pause_screen.report_bugs.name"))
                                .binding(false, () -> FileHandler.REMOVE_BUGS, newValue -> FileHandler.REMOVE_BUGS = newValue)
                                .controller(TickBoxControllerBuilder::create)
                                .build(),
                        Option.<Boolean>createBuilder()
                                .name(Component.translatable("minimenu.options.pause_screen.player_reporting.name"))
                                .binding(false, () -> FileHandler.REMOVE_REPORTING, newValue -> FileHandler.REMOVE_REPORTING = newValue)
                                .controller(TickBoxControllerBuilder::create)
                                .build(),
                        Option.<Boolean>createBuilder()
                                .name(Component.translatable("minimenu.options.pause_screen.open_to_lan.name"))
                                .binding(false, () -> FileHandler.REMOVE_LAN, newValue -> FileHandler.REMOVE_LAN = newValue)
                                .controller(TickBoxControllerBuilder::create)
                                .build()
                ))
                .build());

        builder.category(pauseScreen.build());

        // Build options screen
        return builder.build().generateScreen(previousScreen);
    }
}
package org.minimalmenu.options;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ScreenFactory {
    public static Screen createScreen(Screen parentScreen) {
        var builder = YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("minimalmenu.config.title"));

        builder.save(FileHandler.HANDLER::save);

        // Create categories
        var titleScreen = ConfigCategory.createBuilder()
                .name(Component.translatable("minimalmenu.config.category.title"));

        var pauseScreen = ConfigCategory.createBuilder()
                .name(Component.translatable("minimalmenu.config.category.pause"));

        // Build title screen options
        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimalmenu.config.option.title.edition"))
                .binding(false, () -> FileHandler.REMOVE_EDITION, newValue -> FileHandler.REMOVE_EDITION = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimalmenu.config.option.title.singleplayer"))
                .binding(false, () -> FileHandler.REMOVE_SINGLEPLAYER, newValue -> FileHandler.REMOVE_SINGLEPLAYER = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimalmenu.config.option.title.multiplayer"))
                .binding(false, () -> FileHandler.REMOVE_MULTIPLAYER, newValue -> FileHandler.REMOVE_MULTIPLAYER = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimalmenu.config.option.title.realms"))
                .binding(false, () -> FileHandler.REMOVE_REALMS, newValue -> FileHandler.REMOVE_REALMS = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimalmenu.config.option.title.accessibility"))
                .binding(false, () -> FileHandler.REMOVE_ACCESSIBILITY, newValue -> FileHandler.REMOVE_ACCESSIBILITY = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        titleScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimalmenu.config.option.title.language"))
                .binding(false, () -> FileHandler.REMOVE_LANGUAGE, newValue -> FileHandler.REMOVE_LANGUAGE = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

//        titleScreen.addEntry(entryBuilder.startBooleanToggle(Text.translatable("minimalmenu.config.option.title.copy"), ConfigHandler.REMOVE_COPYRIGHT)
//                .setDefaultValue(false)
//                .setSaveConsumer(newValue -> ConfigHandler.REMOVE_COPYRIGHT = newValue)
//                .build());

//        titleScreen.addEntry(entryBuilder.startBooleanToggle(Text.translatable("minimalmenu.config.option.title.dirtBackground"), ConfigHandler.DIRT_BACKGROUND)
//                .setDefaultValue(false)
//                .setSaveConsumer(newValue -> ConfigHandler.DIRT_BACKGROUND = newValue)
//                .build());

        builder.category(titleScreen.build());

        // Build pause screen options
        pauseScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimalmenu.config.option.pause.feedback"))
                .binding(false, () -> FileHandler.REMOVE_FEEDBACK, newValue -> FileHandler.REMOVE_FEEDBACK = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        pauseScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimalmenu.config.option.pause.bugs"))
                .binding(false, () -> FileHandler.REMOVE_BUGS, newValue -> FileHandler.REMOVE_BUGS = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        pauseScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimalmenu.config.option.pause.reporting"))
                .binding(false, () -> FileHandler.REMOVE_REPORTING, newValue -> FileHandler.REMOVE_REPORTING = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        pauseScreen.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("minimalmenu.config.option.pause.lanSingle"))
                .binding(false, () -> FileHandler.REMOVE_LAN, newValue -> FileHandler.REMOVE_LAN = newValue)
                .controller(TickBoxControllerBuilder::create)
                .build());

        builder.category(pauseScreen.build());

        return builder.build().generateScreen(parentScreen);
    }
}
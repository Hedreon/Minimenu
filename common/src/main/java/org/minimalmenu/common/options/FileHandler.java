package org.minimalmenu.common.options;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.minimalmenu.common.MinimenuCommon;

import java.nio.file.Path;

public class FileHandler {
    private static final Path CONFIG_DIRECTORY = Minecraft.getInstance().gameDirectory.toPath().resolve("config");

    public static final Path CONFIG_FILE = CONFIG_DIRECTORY.resolve("minimenu.json5");

    public static ConfigClassHandler<FileHandler> HANDLER = ConfigClassHandler.createBuilder(FileHandler.class)
            .id(MinimenuCommon.identify("config"))
            .serializer(options -> GsonConfigSerializerBuilder.create(options)
                    .setPath(CONFIG_FILE)
                    .setJson5(true)
                    .build())
            .build();

    public enum MODES {
        Singleplayer,
        Multiplayer,
        None
    }

    @SerialEntry(value = "removeEdition")
    public static boolean REMOVE_EDITION;

    @SerialEntry(value = "removedMode")
    public static MODES REMOVED_MODE = MODES.None;

    @SerialEntry(value = "removeRealms")
    public static boolean REMOVE_REALMS;

    @SerialEntry(value = "removeFriends")
    public static boolean REMOVE_FRIENDS;

    @SerialEntry(value = "removeLanguage")
    public static boolean REMOVE_LANGUAGE;

    @SerialEntry(value = "removeAccessibility")
    public static boolean REMOVE_ACCESSIBILITY;

    @SerialEntry(value = "copyrightText")
    public static String COPYRIGHT_TEXT = Component.translatable("title.credits").getString();

    @SerialEntry(value = "classicBackground")
    public static boolean CLASSIC_BACKGROUND;

    @SerialEntry(value = "removeFeedback")
    public static boolean REMOVE_FEEDBACK;

    @SerialEntry(value = "removeBugs")
    public static boolean REMOVE_BUGS;

    @SerialEntry(value = "removeReporting")
    public static boolean REMOVE_REPORTING;

    @SerialEntry(value = "removeLAN")
    public static boolean REMOVE_LAN;
}
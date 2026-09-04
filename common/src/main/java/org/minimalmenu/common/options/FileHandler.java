package org.minimalmenu.common.options;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.client.Minecraft;
import org.minimalmenu.common.Minimenu;

import java.nio.file.Path;

public class FileHandler {
    private static final Path CONFIG_DIRECTORY = Minecraft.getInstance().gameDirectory.toPath().resolve("config");

    public static final Path CONFIG_FILE = CONFIG_DIRECTORY.resolve("minimenu.json");

    public static ConfigClassHandler<FileHandler> HANDLER = ConfigClassHandler.createBuilder(FileHandler.class)
            .id(Minimenu.identify("config"))
            .serializer(options -> GsonConfigSerializerBuilder.create(options)
                    .setPath(CONFIG_FILE)
                    .build())
            .build();

    public enum MODES {
        Singleplayer,
        Multiplayer,
        None
    }

    @SerialEntry
    public static boolean REMOVE_EDITION;

    @SerialEntry
    public static MODES REMOVED_MODE = MODES.None;

    @SerialEntry
    public static boolean REMOVE_REALMS;

    @SerialEntry
    public static boolean REMOVE_FRIENDS;

    @SerialEntry
    public static boolean REMOVE_LANGUAGE;

    @SerialEntry
    public static boolean REMOVE_ACCESSIBILITY;

    @SerialEntry
    public static boolean REMOVE_VERSION;

    @SerialEntry
    public static boolean REMOVE_COPYRIGHT;

    @SerialEntry
    public static boolean CLASSIC_BACKGROUND;

    @SerialEntry
    public static boolean REMOVE_FEEDBACK;

    @SerialEntry
    public static boolean REMOVE_BUGS;

    @SerialEntry
    public static boolean REMOVE_REPORTING;

    @SerialEntry
    public static boolean REMOVE_LAN;
}
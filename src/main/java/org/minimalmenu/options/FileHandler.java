package org.minimalmenu.options;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.minimalmenu.Minimenu;
import java.nio.file.Path;

public class FileHandler {
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("minimenu.json5");

    public static ConfigClassHandler<FileHandler> HANDLER = ConfigClassHandler.createBuilder(FileHandler.class)
            .id(Identifier.fromNamespaceAndPath(Minimenu.MOD_ID, "config"))
            .serializer(options -> GsonConfigSerializerBuilder.create(options)
                    .setPath(CONFIG_PATH)
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry(value = "removeEdition")
    public static boolean REMOVE_EDITION;

    @SerialEntry(value = "removeSingleplayer")
    public static boolean REMOVE_SINGLEPLAYER;

    @SerialEntry(value = "removeMultiplayer")
    public static boolean REMOVE_MULTIPLAYER;

    @SerialEntry(value = "removeRealms")
    public static boolean REMOVE_REALMS;

    @SerialEntry(value = "removeLanguage")
    public static boolean REMOVE_LANGUAGE;

    @SerialEntry(value = "removeAccessibility")
    public static boolean REMOVE_ACCESSIBILITY;

//    public static boolean REMOVE_COPYRIGHT;
//    public static boolean DIRT_BACKGROUND;

    @SerialEntry(value = "removeFeedback")
    public static boolean REMOVE_FEEDBACK;

    @SerialEntry(value = "removeBugs")
    public static boolean REMOVE_BUGS;

    @SerialEntry(value = "removeReporting")
    public static boolean REMOVE_REPORTING;

    @SerialEntry(value = "removeLAN")
    public static boolean REMOVE_LAN;
}
package org.minimalmenu.mixins;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.resources.Identifier;
import org.minimalmenu.Minimenu;
import org.minimalmenu.options.FileHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSelectionList.class)
public class AbstractSelectionListMixin {
    @Shadow @Final @Mutable
    private static Identifier MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/menu_list_background.png");

    @Shadow @Final @Mutable
    private static Identifier INWORLD_MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");

    @Inject(method = "extractListBackground", at = @At("HEAD"))
    private void renderListBackground(GuiGraphicsExtractor graphics, CallbackInfo callback) {
        MENU_LIST_BACKGROUND = FileHandler.CLASSIC_BACKGROUND
                ? Minimenu.identify("textures/gui/menu_list_background.png")
                : Identifier.withDefaultNamespace("textures/gui/menu_list_background.png");

        INWORLD_MENU_LIST_BACKGROUND = FileHandler.CLASSIC_BACKGROUND
                ? Minimenu.identify("textures/gui/inworld_menu_list_background.png")
                : Identifier.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");
    }
}
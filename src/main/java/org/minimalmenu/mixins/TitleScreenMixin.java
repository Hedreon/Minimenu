package org.minimalmenu.mixins;

import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.minimalmenu.Minimenu;
import org.minimalmenu.options.FileHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Minecraft minecraft, Font font, Component title) {
        super(minecraft, font, title);
    }

    @Shadow @Final @Mutable
    private static Component COPYRIGHT_TEXT = Component.translatable("title.credits");

    @Inject(method = "init", at = @At("HEAD"))
    protected void replaceCopyrightText(CallbackInfo callback) {
        if (FileHandler.SHORTEN_COPYRIGHT) {
            COPYRIGHT_TEXT = Component.literal("Copyright Mojang AB");

            int copyrightWidth = this.font.width(COPYRIGHT_TEXT);
            int copyrightHeight = 10;
            int copyrightX = this.width - copyrightWidth - 2;
            int copyrightY = this.height + 4;

            this.addRenderableWidget(new PlainTextButton(
                    copyrightX,
                    copyrightY,
                    copyrightWidth,
                    copyrightHeight,
                    COPYRIGHT_TEXT,
                    (_) -> this.minecraft.setScreen(new CreditsAndAttributionScreen(this)),
                    this.font
            ));
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void initializeWidgets(CallbackInfo callback) {
        final int spacing = 24;
        int offset = 0;

        List<AbstractWidget> widgetList = Screens.getWidgets(this);

        for (AbstractWidget widget : widgetList) {
            if (Minimenu.widgetMatchesKey(widget, "options.accessibility")) {
                widget.visible = !FileHandler.REMOVE_ACCESSIBILITY;
            }

            if (Minimenu.widgetMatchesKey(widget, "options.language")) {
                widget.visible = !FileHandler.REMOVE_LANGUAGE;
            }

            if (!Minimenu.widgetMatchesKey(widget, "title.credits")) {
                if (FileHandler.REMOVED_MODE == FileHandler.MODES.Singleplayer) {
                    if (Minimenu.widgetMatchesKey(widget, "menu.singleplayer")) {
                        offset += spacing;
                    }

                    widget.setY(widget.getY() - (widget.getHeight() + (spacing / 6)));
                }

                if (FileHandler.REMOVED_MODE == FileHandler.MODES.Multiplayer) {
                    if (Minimenu.widgetMatchesKey(widget, "menu.multiplayer")) {
                        offset += spacing;
                    }

                    if (!Minimenu.widgetMatchesKey(widget, "menu.singleplayer")) {
                        widget.setY(widget.getY() - (widget.getHeight() + (spacing / 6)));
                    }
                }

                if (FileHandler.REMOVE_REALMS) {
                    if (Minimenu.widgetMatchesKey(widget, "menu.online")) {
                        offset += spacing;
                    }

                    if (!Minimenu.widgetMatchesKey(widget, "menu.singleplayer")
                            && !Minimenu.widgetMatchesKey(widget, "menu.multiplayer")) {
                        widget.setY(widget.getY() - (widget.getHeight() + (spacing / 6)));
                    }
                }
            }
        }

        for (AbstractWidget movableWidget : widgetList) {
            if (!Minimenu.widgetMatchesKey(movableWidget, "title.credits")) {
                movableWidget.setY(movableWidget.getY() + (offset / 2));
            }
        }
    }

    @Inject(method = "createNormalMenuOptions", at = @At("TAIL"))
    private void createMenu(int topPos, int spacing, CallbackInfoReturnable<Integer> cir) {
        Minecraft minecraft = Minecraft.getInstance();

        List<AbstractWidget> widgetList = Screens.getWidgets(this);

        for (AbstractWidget widget : widgetList) {
            if (Minimenu.widgetMatchesKey(widget, "menu.singleplayer")) {
                widget.visible = !(FileHandler.REMOVED_MODE == FileHandler.MODES.Singleplayer);
            }

            if (Minimenu.widgetMatchesKey(widget, "menu.multiplayer")) {
                widget.visible = !(FileHandler.REMOVED_MODE == FileHandler.MODES.Multiplayer);
            }

            if (Minimenu.widgetMatchesKey(widget, "menu.online")) {
                widget.visible = !FileHandler.REMOVE_REALMS;

                minecraft.options.realmsNotifications().set(!FileHandler.REMOVE_REALMS);
            }
        }
    }
}
package org.minimalmenu.common.mixins.title_screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.minimalmenu.common.MinimenuCommon;
import org.minimalmenu.common.options.FileHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Unique
    private PlainTextButton COPYRIGHT_BUTTON;

    @Shadow
    protected abstract int getHorizontalPosition(int currentButton, int numberOfButtons, int buttonWidth);

    protected TitleScreenMixin(Minecraft minecraft, Font font, Component title) {
        super(minecraft, font, title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    protected void changeCopyright(CallbackInfo callback) {
        if (!FileHandler.COPYRIGHT_TEXT.isBlank()) {
            Component copyrightText = Component.literal(FileHandler.COPYRIGHT_TEXT);

            int copyrightWidth = this.font.width(copyrightText);
            int copyrightHeight = 10;

            int copyrightX = this.width - copyrightWidth - 2;
            int copyrightY = this.height + 2;

            COPYRIGHT_BUTTON = new PlainTextButton(
                    copyrightX,
                    copyrightY,
                    copyrightWidth,
                    copyrightHeight,
                    copyrightText,
                    (_) -> this.minecraft.gui.setScreen(new CreditsAndAttributionScreen(this)),
                    this.font
            );

            this.addRenderableWidget(COPYRIGHT_BUTTON);
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void initializeWidgets(CallbackInfo callback) {
        final int spacing = 24;
        int offset = 0;

        List<AbstractWidget> widgetList = MinimenuCommon.getWidgets(this);
        List<AbstractWidget> iconWidgetList = new ArrayList<>();

        for (AbstractWidget widget : widgetList) {
            if (MinimenuCommon.widgetMatchesKey(widget, "gui.friends.open")) {
                widget.visible = !FileHandler.REMOVE_FRIENDS;

                if (!FileHandler.REMOVE_FRIENDS) iconWidgetList.add(widget);
            } else if (MinimenuCommon.widgetMatchesKey(widget, "options.language")) {
                widget.visible = !FileHandler.REMOVE_LANGUAGE;

                if (!FileHandler.REMOVE_LANGUAGE) iconWidgetList.add(widget);
            } else if (MinimenuCommon.widgetMatchesKey(widget, "options.accessibility")) {
                widget.visible = !FileHandler.REMOVE_ACCESSIBILITY;

                if (!FileHandler.REMOVE_ACCESSIBILITY) iconWidgetList.add(widget);
            } else if (widget instanceof SpriteIconButton iconButton && iconButton.getWidth() == 20 && widget.visible) {
                iconWidgetList.add(widget);
            }

            if (widget != COPYRIGHT_BUTTON) {
                if (FileHandler.REMOVED_MODE == FileHandler.MODES.Singleplayer) {
                    if (MinimenuCommon.widgetMatchesKey(widget, "menu.singleplayer")) {
                        offset += spacing;
                    }

                    widget.setY(widget.getY() - (widget.getHeight() + (spacing / 6)));
                }

                if (FileHandler.REMOVED_MODE == FileHandler.MODES.Multiplayer) {
                    if (MinimenuCommon.widgetMatchesKey(widget, "menu.multiplayer")) {
                        offset += spacing;
                    }

                    if (!MinimenuCommon.widgetMatchesKey(widget, "menu.singleplayer")) {
                        widget.setY(widget.getY() - (widget.getHeight() + (spacing / 6)));
                    }
                }

                if (FileHandler.REMOVE_REALMS) {
                    if (MinimenuCommon.widgetMatchesKey(widget, "menu.online")) {
                        offset += spacing;
                    }

                    if (!MinimenuCommon.widgetMatchesKey(widget, "menu.singleplayer")
                        && !MinimenuCommon.widgetMatchesKey(widget, "menu.multiplayer")) {
                        widget.setY(widget.getY() - (widget.getHeight() + (spacing / 6)));
                    }
                }
            }
        }

        for (AbstractWidget movableWidget : widgetList) {
            if (movableWidget != COPYRIGHT_BUTTON) {
                movableWidget.setY(movableWidget.getY() + (offset / 2));
            }
        }

        if (!iconWidgetList.isEmpty()) {
            int totalIconWidgets = iconWidgetList.size();

            for (int iconWidgetIndex = 0; iconWidgetIndex < totalIconWidgets; iconWidgetIndex++) {
                AbstractWidget iconWidget = iconWidgetList.get(iconWidgetIndex);

                iconWidget.setX(getHorizontalPosition(iconWidgetIndex + 1, totalIconWidgets, 20));
            }
        }
    }

    @Inject(method = "createNormalMenuOptions", at = @At("TAIL"))
    private void createMenu(int topPos, int spacing, CallbackInfoReturnable<Integer> cir) {
        List<AbstractWidget> widgetList = MinimenuCommon.getWidgets(this);

        for (AbstractWidget widget : widgetList) {
            if (MinimenuCommon.widgetMatchesKey(widget, "menu.singleplayer")) {
                widget.visible = !(FileHandler.REMOVED_MODE == FileHandler.MODES.Singleplayer);
            }

            if (MinimenuCommon.widgetMatchesKey(widget, "menu.multiplayer")) {
                widget.visible = !(FileHandler.REMOVED_MODE == FileHandler.MODES.Multiplayer);
            }

            if (MinimenuCommon.widgetMatchesKey(widget, "menu.online")) {
                widget.visible = !FileHandler.REMOVE_REALMS;

                this.minecraft.options.realmsNotifications().set(!FileHandler.REMOVE_REALMS);
            }
        }
    }

    @WrapOperation(
            method = "init",
            at = @At(
                    value = "INVOKE:LAST",
                    target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"
            )
    )
    private GuiEventListener removeOriginalCopyright(TitleScreen instance, GuiEventListener eventListener, Operation<GuiEventListener> original) {
        return null;
    }
}
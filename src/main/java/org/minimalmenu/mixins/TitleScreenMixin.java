package org.minimalmenu.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.minimalmenu.Minimenu;
import org.minimalmenu.options.FileHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Shadow protected abstract int getHorizontalPosition(int currentButton, int numberOfButtons, int buttonWidth);

    @Inject(method = "init", at = @At("TAIL"))
    protected void initializeWidgets(CallbackInfo callback) {
        final int spacing = 24;
        int offset = 0;

        List<AbstractWidget> widgetList = Screens.getWidgets(this);
        List<AbstractWidget> iconWidgetList = new ArrayList<>();

        for (AbstractWidget widget : widgetList) {
            if (Minimenu.widgetMatchesKey(widget, "gui.friends.open")) {
                widget.visible = !FileHandler.REMOVE_FRIENDS;

                if (!FileHandler.REMOVE_FRIENDS) iconWidgetList.add(widget);
            } else if (Minimenu.widgetMatchesKey(widget, "options.language")) {
                widget.visible = !FileHandler.REMOVE_LANGUAGE;

                if (!FileHandler.REMOVE_LANGUAGE) iconWidgetList.add(widget);
            } else if (Minimenu.widgetMatchesKey(widget, "options.accessibility")) {
                widget.visible = !FileHandler.REMOVE_ACCESSIBILITY;

                if (!FileHandler.REMOVE_ACCESSIBILITY) iconWidgetList.add(widget);
            } else if (widget instanceof SpriteIconButton iconButton && iconButton.getWidth() == 20 && widget.visible) {
                iconWidgetList.add(widget);
            }

            if (!Minimenu.widgetMatchesKey(widget, "title.credits")
                    && !Minimenu.widgetMatchesKey(widget, "minimenu.title.credits")) {
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
            if (!Minimenu.widgetMatchesKey(movableWidget, "title.credits")
                    && !Minimenu.widgetMatchesKey(movableWidget, "minimenu.title.credits")) {
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

    @ModifyArg(
            method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"
            )
    )
    private String modifyVersionString(String message) {
        String versionText = FileHandler.VERSION_TEXT;

        // $vn - Client version (e.g. 26.1.2).
        String vn = SharedConstants.getCurrentVersion().name();

        // $id - Client identifier (Vanilla or Modded).
        String id = Minecraft.checkModStatus().shouldReportAsModified() ? "Modded" : "Vanilla";

        // $pr - Information inside the parentheses.
        Pattern parenthesesPattern = Pattern.compile("\\(([^)]*)\\)");
        Matcher parenthesesMatcher = parenthesesPattern.matcher(message);

        String pr = parenthesesMatcher.find() ? parenthesesMatcher.group(1) : "";

        if (versionText.isBlank()) return message;

        versionText = versionText.replace("$vn", vn)
                .replace("$id", id)
                .replace("$pr", pr);

        return versionText;
    }

    @WrapOperation(
            method = "init",
            at = @At(
                    value = "INVOKE:LAST",
                    target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"
            )
    )
    private GuiEventListener shouldAddCopyright(TitleScreen instance, GuiEventListener eventListener, Operation<GuiEventListener> original) {
        return !FileHandler.REMOVE_COPYRIGHT ? original.call(instance, eventListener) : null;
    }
}
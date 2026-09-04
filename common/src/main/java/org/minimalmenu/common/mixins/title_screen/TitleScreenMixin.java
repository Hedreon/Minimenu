package org.minimalmenu.common.mixins.title_screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.minimalmenu.common.Minimenu;
import org.minimalmenu.common.options.FileHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    protected abstract int getHorizontalPosition(int currentButton, int numberOfButtons, int buttonWidth);

    @WrapOperation(
            method = "init",
            at = @At(
                    value = "INVOKE:LAST",
                    target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"
            )
    )
    private GuiEventListener shouldRenderCopyright(TitleScreen instance, GuiEventListener eventListener, Operation<GuiEventListener> original) {
        return !FileHandler.REMOVE_COPYRIGHT ? original.call(instance, eventListener) : null;
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void initializeWidgets(CallbackInfo callback) {
        final int spacing = 24;
        int offset = 0;

        List<AbstractWidget> widgetList = Minimenu.getWidgets(this);
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

        if (!iconWidgetList.isEmpty()) {
            int totalIconWidgets = iconWidgetList.size();

            for (int iconWidgetIndex = 0; iconWidgetIndex < totalIconWidgets; iconWidgetIndex++) {
                AbstractWidget iconWidget = iconWidgetList.get(iconWidgetIndex);

                iconWidget.setX(getHorizontalPosition(iconWidgetIndex + 1, totalIconWidgets, 20));
            }
        }
    }

    @Inject(method = "createNormalMenuOptions", at = @At("TAIL"))
    private void createMenu(int topPos, int spacing, CallbackInfoReturnable<Integer> callback) {
        List<AbstractWidget> widgetList = Minimenu.getWidgets(this);

        for (AbstractWidget widget : widgetList) {
            if (Minimenu.widgetMatchesKey(widget, "menu.singleplayer")) {
                widget.visible = !(FileHandler.REMOVED_MODE == FileHandler.MODES.Singleplayer);
            }

            if (Minimenu.widgetMatchesKey(widget, "menu.multiplayer")) {
                widget.visible = !(FileHandler.REMOVED_MODE == FileHandler.MODES.Multiplayer);
            }

            if (Minimenu.widgetMatchesKey(widget, "menu.online")) {
                widget.visible = !FileHandler.REMOVE_REALMS;

                this.minecraft.options.realmsNotifications().set(!FileHandler.REMOVE_REALMS);
            }
        }
    }
}
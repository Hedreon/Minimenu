package minimalmenu.mixin;

import com.terraformersmc.modmenu.mixin.IGridWidgetAccessor;
import minimalmenu.MinimalMenu;
import minimalmenu.config.ConfigHandler;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = PauseScreen.class, priority = 1100)
public abstract class PauseScreenMixin extends Screen {
    protected PauseScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "createPauseMenu", at = @At("TAIL"))
    private void removeFeedbackAndBugs (CallbackInfo info){
        for (GuiEventListener element : this.children() ) {
            System.out.println(element);
            if (element instanceof GridLayout) {
                for (GuiEventListener child : ((GridLayout) element).children()) {
                    if (child instanceof AbstractWidget) {
                        AbstractWidget widget = (AbstractWidget) child;
                        if (MinimalMenu.buttonMatchesKey(widget, "menu.reportBugs") && ConfigHandler.REMOVE_BUGS) {
                            widget.active = false;
                            widget.visible = false;
                        }
                        if (MinimalMenu.buttonMatchesKey(widget, "menu.sendFeedback") && ConfigHandler.REMOVE_BUGS) {
                            widget.setWidth(204);
                        }
                        if (MinimalMenu.buttonMatchesKey(widget, "menu.sendFeedback") && ConfigHandler.REMOVE_FEEDBACK) {
                            widget.active = false;
                            widget.visible = false;
                        }
                        if (MinimalMenu.buttonMatchesKey(widget, "menu.reportBugs") && ConfigHandler.REMOVE_FEEDBACK) {
                            widget.setWidth(204);
                            widget.setX(widget.getX() - 106);
                        }
                        if (ConfigHandler.REMOVE_FEEDBACK && ConfigHandler.REMOVE_BUGS) {
                            String text = this.minecraft.isLocalServer() ? "menu.returnToMenu" : "menu.disconnect";
                            if (MinimalMenu.buttonMatchesKey(widget, text)) {
                                widget.setY(widget.getY() - 24);
                            }
                            if (MinimalMenu.buttonMatchesKey(widget, "menu.options")) {
                                widget.setY(widget.getY() - 24);
                            }
                        }

                        widget.setX(widget.getX() - ConfigHandler.X_OFFSET_PAUSE);
                        widget.setY(widget.getY() - ConfigHandler.Y_OFFSET_PAUSE);
                    }
                }

            }
        }
    }

    @Inject(method = "createPauseMenu", at = @At("HEAD"))
    private void initWidgets(CallbackInfo info) {
        if (ConfigHandler.ADD_FOLDER_PS) {
            this.addRenderableWidget(
                Button.builder(
                    Component.translatable("minimalmenu.common..minecraft"),
                    button -> {
                        MinimalMenu.processButtonFolderClick(minecraft);
                    }
                )
                .pos(this.width / 2 + 104, this.height / 4 + 120 + -16)
                .size(20, 20)
                .build()
            );
        }
    }
}

package minimalmenu.mixin;

import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import minimalmenu.MinimalMenu;
import minimalmenu.config.ConfigHandler;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.components.AbstractWidget;

@Mixin(value = OptionsScreen.class, priority = 1100)
public abstract class OptionsScreenMixin extends ScreenMixin {


    @Inject(method = "init", at = @At("TAIL"))
    protected void init(CallbackInfo info) {
        if (ConfigHandler.REMOVE_ONLINE) {
            for (AbstractWidget button : Screens.getWidgets((Screen)(Object)this)) {
                if (!this.minecraft.isLocalServer()) {
                    if (MinimalMenu.buttonMatchesKey(button, "options.online")) {
                        button.visible = false;
                    }
                    if (button.getMessage().getString().contains(":")) {
                        String[] splitString = button.getMessage().getString().split(":");

                        if (splitString[0].equals(Component.translatable("options.fov").getString())) {
                            button.setWidth(310);
                        }
                    }
                }
            }
        }
    }
}
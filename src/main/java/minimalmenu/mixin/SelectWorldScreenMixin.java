package minimalmenu.mixin;

import java.io.File;
import java.util.List;

import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import minimalmenu.config.ConfigHandler;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.util.Util;

@Mixin(SelectWorldScreen.class)
public abstract class SelectWorldScreenMixin extends ScreenMixin {
    @Shadow @Final protected Screen lastScreen;

    @Inject(method = "init", at = @At("HEAD"))
    public void init(CallbackInfo info) {
        List<AbstractWidget> buttons = Screens.getWidgets((Screen)(Object)this);

        if (ConfigHandler.ADD_SAVES) {
            buttons.add(
                Button.builder(
                    Component.translatable("minimalmenu.screen.singleplayer.saves"),
                    button -> {
                        assert this.minecraft != null;
                        File file = minecraft.gameDirectory.toPath().resolve("saves").toFile(); //Create saves file from current running directory.
                        Util.getPlatform().openFile(file);
                    }
                )
                .pos(this.width / 2 - 232, this.height - 28)
                .size(72, 20)
                .build()
            );
        }
        if (ConfigHandler.ADD_RELOAD_SAVES) {
            buttons.add(
                Button.builder(
                    Component.translatable("minimalmenu.screen.singleplayer.reload"),
                    button -> {
                        assert this.minecraft != null;
                        this.minecraft.setScreenAndShow(new SelectWorldScreen(lastScreen)); //Refresh screen, by creating a new one.
                    }
                )
                .pos(this.width / 2 - 232, this.height - 52)
                .size(72, 20)
                .build()
            );
        }
    }
}

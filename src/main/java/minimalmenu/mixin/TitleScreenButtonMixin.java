package minimalmenu.mixin;

import minimalmenu.MinimalMenu;
import minimalmenu.config.ConfigHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenButtonMixin extends Screen {
    protected TitleScreenButtonMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    protected void init(CallbackInfo info) {
        if (ConfigHandler.ADD_FOLDER_TS) {
            this.addRenderableWidget(
                Button.builder(
                    Component.translatable("minimalmenu.common..minecraft"),
                    button -> {
                        MinimalMenu.processButtonFolderClick(minecraft);
                    }
                )
                .pos(this.width / 2 + 104, (this.height / 4 + 48) + 84)
                .size(20, 20)
                .build()
            );
        }
    }


}

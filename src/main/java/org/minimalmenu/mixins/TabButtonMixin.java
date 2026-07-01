package org.minimalmenu.mixins;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.components.WidgetSprites;
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

@Mixin(TabButton.class)
public class TabButtonMixin {
    @Shadow @Final @Mutable
    private static WidgetSprites SPRITES = new WidgetSprites(
            Identifier.withDefaultNamespace("widget/tab_selected"),
            Identifier.withDefaultNamespace("widget/tab"),
            Identifier.withDefaultNamespace("widget/tab_selected_highlighted"),
            Identifier.withDefaultNamespace("widget/tab_highlighted")
    );

    @Inject(method = "extractWidgetRenderState", at = @At("HEAD"))
    private void renderWidget(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo callback) {
        SPRITES = FileHandler.CLASSIC_BACKGROUND ? new WidgetSprites(
                Minimenu.identify("widget/tab_selected"),
                Minimenu.identify("widget/tab"),
                Minimenu.identify("widget/tab_selected_highlighted"),
                Minimenu.identify("widget/tab_highlighted")
        ) : new WidgetSprites(
                Identifier.withDefaultNamespace("widget/tab_selected"),
                Identifier.withDefaultNamespace("widget/tab"),
                Identifier.withDefaultNamespace("widget/tab_selected_highlighted"),
                Identifier.withDefaultNamespace("widget/tab_highlighted")
        );
    }
}
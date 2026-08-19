package org.minimalmenu.common.mixins.general;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.minimalmenu.common.MinimenuCommon;
import org.minimalmenu.common.helpers.ButtonList;
import org.minimalmenu.common.helpers.ScreenExtensions;
import org.minimalmenu.common.options.FileHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin implements ScreenExtensions {
    @Shadow
    public int width;

    @Shadow
    public int height;

    @Shadow @Final
    private List<NarratableEntry> narratables;

    @Shadow @Final
    private List<GuiEventListener> children;

    @Shadow @Final
    private List<Renderable> renderables;

    @Shadow @Final @Mutable
    public static Identifier MENU_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/menu_background.png");

    @Shadow @Final @Mutable
    public static Identifier HEADER_SEPARATOR = Identifier.withDefaultNamespace("textures/gui/header_separator.png");

    @Shadow @Final @Mutable
    public static Identifier FOOTER_SEPARATOR = Identifier.withDefaultNamespace("textures/gui/footer_separator.png");

    @Shadow @Final @Mutable
    private static Identifier INWORLD_MENU_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_background.png");

    @Shadow @Final @Mutable
    public static Identifier INWORLD_HEADER_SEPARATOR = Identifier.withDefaultNamespace("textures/gui/inworld_header_separator.png");

    @Shadow @Final @Mutable
    public static Identifier INWORLD_FOOTER_SEPARATOR = Identifier.withDefaultNamespace("textures/gui/inworld_footer_separator.png");

    @Unique
    private static final Identifier CLASSIC_MENU_BACKGROUND = MinimenuCommon.identify("textures/gui/menu_background.png");

    @Unique
    private static final Identifier CLASSIC_HEADER_SEPARATOR = MinimenuCommon.identify("textures/gui/header_separator.png");

    @Unique
    private static final Identifier CLASSIC_FOOTER_SEPARATOR = MinimenuCommon.identify("textures/gui/footer_separator.png");

    @Unique
    private List<AbstractWidget> AVAILABLE_BUTTONS;

    @Override
    public List<AbstractWidget> GET_AVAILABLE_BUTTONS() {
        if (this.AVAILABLE_BUTTONS == null) {
            this.AVAILABLE_BUTTONS = new ButtonList(this.renderables, this.narratables, this.children);
        }

        return this.AVAILABLE_BUTTONS;
    }

    @WrapMethod(method = "extractPanorama")
    private void shouldRenderPanorama(GuiGraphicsExtractor graphics, float a, Operation<Void> original) {
        if (FileHandler.CLASSIC_BACKGROUND) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, CLASSIC_MENU_BACKGROUND, 0, 0, 0.0F, 0.0F, this.width, this.height, 32, 32);
        } else {
            original.call(graphics, a);
        }
    }

    @Inject(method = "extractMenuBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIII)V", at = @At("HEAD"))
    private void renderMenuBackground(GuiGraphicsExtractor graphics, int x, int y, int width, int height, CallbackInfo callback) {
        MENU_BACKGROUND = FileHandler.CLASSIC_BACKGROUND
                ? CLASSIC_MENU_BACKGROUND
                : Identifier.withDefaultNamespace("textures/gui/menu_background.png");

        HEADER_SEPARATOR = FileHandler.CLASSIC_BACKGROUND
                ? CLASSIC_HEADER_SEPARATOR
                : Identifier.withDefaultNamespace("textures/gui/header_separator.png");

        FOOTER_SEPARATOR = FileHandler.CLASSIC_BACKGROUND
                ? CLASSIC_FOOTER_SEPARATOR
                : Identifier.withDefaultNamespace("textures/gui/footer_separator.png");

        INWORLD_MENU_BACKGROUND = FileHandler.CLASSIC_BACKGROUND
                ? MinimenuCommon.identify("textures/gui/inworld_menu_background.png")
                : Identifier.withDefaultNamespace("textures/gui/inworld_menu_background.png");

        INWORLD_HEADER_SEPARATOR = FileHandler.CLASSIC_BACKGROUND
                ? CLASSIC_HEADER_SEPARATOR
                : Identifier.withDefaultNamespace("textures/gui/inworld_header_separator.png");

        INWORLD_FOOTER_SEPARATOR = FileHandler.CLASSIC_BACKGROUND
                ? CLASSIC_FOOTER_SEPARATOR
                : Identifier.withDefaultNamespace("textures/gui/inworld_footer_separator.png");
    }
}
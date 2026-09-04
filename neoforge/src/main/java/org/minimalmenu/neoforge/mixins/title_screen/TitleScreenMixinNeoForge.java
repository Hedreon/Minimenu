package org.minimalmenu.neoforge.mixins.title_screen;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.gui.screens.TitleScreen;
import org.minimalmenu.common.options.FileHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;

@Mixin(TitleScreen.class)
public class TitleScreenMixinNeoForge {
    /**
     * Adapted from <a href="https://github.com/Wilyicaro/Legacy-Minecraft/blob/HEAD/src/main/java/wily/legacy/mixin/base/client/title/TitleScreenMixin.java">Legacy4J's TitleScreenMixin.java.</a>
     */
    @WrapWithCondition(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/internal/BrandingControl;forEachLine(ZZLjava/util/function/BiConsumer;)V", remap = false))
    private boolean shouldRenderVersion(boolean includeMC, boolean reverse, BiConsumer<Integer, String> lineConsumer) {
        return !FileHandler.REMOVE_VERSION;
    }

    @WrapWithCondition(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/internal/BrandingControl;forEachAboveCopyrightLine(Ljava/util/function/BiConsumer;)V", remap = false))
    private boolean shouldRenderBranding(BiConsumer<Integer, String> lineConsumer) {
        return !FileHandler.REMOVE_VERSION;
    }
}
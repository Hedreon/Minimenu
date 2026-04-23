package minimalmenu.mixin;

import minimalmenu.MinimalMenu;
import minimalmenu.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GridLayout.RowHelper.class)
public abstract class GridWidgetAdderMixin {

    @Shadow public abstract <T extends AbstractWidget> T add(T widget, int occupiedColumns, LayoutSettings positioner);

    @Shadow public abstract LayoutSettings newCellSettings();

    // TODO(Ravel): target method add with the signature not found
// TODO(Ravel): target method add with the signature not found
    @Inject(method = "add(Lnet/minecraft/client/gui/widget/ClickableWidget;ILnet/minecraft/client/gui/widget/Positioner;)Lnet/minecraft/client/gui/widget/ClickableWidget;", at = @At("HEAD"), cancellable = true, remap = false)
    public <T extends AbstractWidget> void add (T widget, int occupiedColumns, LayoutSettings positioner, CallbackInfoReturnable<T> cir) {

        boolean isInSingleplayer = Minecraft.getInstance().isLocalServer();
        boolean isLocalLan = Minecraft.getInstance().getSingleplayerServer() != null && Minecraft.getInstance().getSingleplayerServer().isPublished();
        boolean removeLan = ConfigHandler.REMOVE_LANSP && isInSingleplayer && !isLocalLan;
        boolean removeReporting = ConfigHandler.REMOVE_REPORTING && (!isInSingleplayer || isLocalLan);

        if (removeLan) { //REMOVE LAN
            if (MinimalMenu.buttonMatchesKey(widget, "menu.shareToLan")) {
                cir.cancel();
            }
            if (MinimalMenu.buttonMatchesKey(widget, "menu.options") && occupiedColumns == 1) {
                widget.setWidth(204);
                this.add(widget, 2, newCellSettings().alignVerticallyBottom());
                cir.cancel();
            }
        } else if (removeReporting) { //REMOVE REPORTING
            if (MinimalMenu.buttonMatchesKey(widget, "menu.playerReporting")) {
                cir.cancel();
            }
            if (MinimalMenu.buttonMatchesKey(widget, "menu.options") && occupiedColumns == 1) {
                widget.setWidth(204);
                this.add(widget, 2, newCellSettings().alignVerticallyBottom());
                cir.cancel();
            }
        }
    }
}
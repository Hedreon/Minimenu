package minimalmenu.screens;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.components.Button;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import java.io.File;

public class FolderScreen extends Screen {
    private final Screen parent;

    public FolderScreen(Screen parent) {
        super(Component.translatable("minimalmenu.screen.folders"));
        this.parent = parent;
    }

    public void init() {
        assert minecraft != null;
        File file = minecraft.gameDirectory.toPath().toFile();
        String[] directories = file.list((dir, name) -> new File(dir, name).isDirectory());

        assert directories != null;
        int y = (directories.length * 24) / 2;
        for (int i = 0; i <= directories.length; i++) {
            if (i == 0) {
                this.addRenderableWidget(
                    Button.builder(
                    Component.literal(file.getName()),
                        button -> {
                            Util.getPlatform().openFile(file);
                        }
                    )
                    .pos(this.width / 2 - 100, (this.height / 2 + (i-1) * 24) - y)
                    .size(200, 20)
                    .build()
                );

                this.addRenderableWidget(
                    Button.builder(
                        CommonComponents.GUI_DONE,
                        button -> {
                            minecraft.setScreen(parent);
                        }
                    )
                    .pos(this.width / 2 - 100, (this.height / 2 + (directories.length + 1) * 24) - y)
                    .size(200, 20)
                    .build()
                );
            }
            if (i < directories.length) {
                int x = i;
                this.addRenderableWidget(
                    Button.builder(
                        Component.literal(directories[x]),
                        button -> {
                            File fileToOpen = new File(file.getAbsolutePath() + File.separator + directories[x]);
                            System.out.println(fileToOpen.getAbsolutePath());
                            Util.getPlatform().openFile(fileToOpen);
                        }
                    )
                    .pos(this.width / 2 - 100, (this.height / 2 + i * 24) - y)
                    .size(200, 20)
                    .build()
                );
            }
        }
    }

    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.font, this.title, this.width / 2, 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
}

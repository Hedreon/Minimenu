package org.minimalmenu.common.helpers;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.AbstractList;
import java.util.List;

/**
 * Adapted from <a href="https://github.com/FabricMC/fabric-api/blob/HEAD/fabric-screen-api-v1/src/client/java/net/fabricmc/fabric/impl/client/screen/ButtonList.java">Fabric API's ButtonList.</a>
 */
public final class ButtonList extends AbstractList<AbstractWidget> {
    private final List<Renderable> renderables;
    private final List<NarratableEntry> narratables;
    private final List<GuiEventListener> children;

    public ButtonList(List<Renderable> renderables, List<NarratableEntry> narratables, List<GuiEventListener> children) {
        this.renderables = renderables;
        this.narratables = narratables;
        this.children = children;
    }

    @Override
    public AbstractWidget get(int index) {
        int remaining = index;

        for (Renderable renderable : renderables) {
            if (renderable instanceof AbstractWidget widget) {
                if (remaining == 0) {
                    return widget;
                }

                remaining--;
            }
        }

        throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size()));
    }

    @Override
    public AbstractWidget set(int index, AbstractWidget element) {
        AbstractWidget existingWidget = get(index);

        int widgetIndex = renderables.indexOf(existingWidget);
        if (widgetIndex >= 0) renderables.set(widgetIndex, element);

        widgetIndex = narratables.indexOf(existingWidget);
        if (widgetIndex >= 0) narratables.set(widgetIndex, element);

        widgetIndex = children.indexOf(existingWidget);
        if (widgetIndex >= 0) children.set(widgetIndex, element);

        return existingWidget;
    }

    @Override
    public void add(int index, AbstractWidget element) {
        // Remove any existing occurrence and adjust the target index accordingly.
        int duplicateIndex = listIndexOf(element);

        if (duplicateIndex >= 0) {
            renderables.remove(element);
            narratables.remove(element);
            children.remove(element);

            if (duplicateIndex < index) {
                index--;
            }
        }

        if (index > size()) {
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size()));
        } else if (index == size()) {
            renderables.add(element);
            narratables.add(element);
            children.add(element);
        } else {
            // Use an anchor widget and insert before it.
            AbstractWidget anchorWidget = get(index);

            int widgetIndex = renderables.indexOf(anchorWidget);
            renderables.add(widgetIndex >= 0 ? widgetIndex : renderables.size(), element);

            widgetIndex = narratables.indexOf(anchorWidget);
            narratables.add(widgetIndex >= 0 ? widgetIndex : narratables.size(), element);

            widgetIndex = children.indexOf(anchorWidget);
            children.add(widgetIndex >= 0 ? widgetIndex : children.size(), element);
        }
    }

    private int listIndexOf(AbstractWidget element) {
        int index = 0;

        for (Renderable renderable : renderables) {
            if (renderable instanceof AbstractWidget widget) {
                if (widget == element) {
                    return index;
                }

                index++;
            }
        }

        return -1;
    }

    @Override
    public AbstractWidget remove(int index) {
        AbstractWidget removedButton = get(index);

        renderables.remove(removedButton);
        narratables.remove(removedButton);
        children.remove(removedButton);

        return removedButton;
    }

    @Override
    public int size() {
        int size = 0;

        for (Renderable renderable : renderables) {
            if (renderable instanceof AbstractWidget) {
                size++;
            }
        }

        return size;
    }
}
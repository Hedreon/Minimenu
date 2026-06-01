package org.minimalmenu.options;

import dev.isxander.yacl3.api.StateManager;
import org.jetbrains.annotations.Nullable;

public class RadioStateManager<T extends Enum<T>> implements StateManager<Boolean> {
    private final StateManager<T> manager;
    private final T option;
    private final @Nullable T deselectedOption;

    public RadioStateManager(StateManager<T> manager, T option, @Nullable T deselectedOption) {
        this.manager = manager;
        this.option = option;
        this.deselectedOption = deselectedOption;
    }

    @Override
    public void set(Boolean value) {
        if (value) {
            manager.set(option);
        } else if (deselectedOption != null) {
            manager.set(deselectedOption);
        }
    }

    @Override
    public Boolean get() {
        return manager.get() == option;
    }

    @Override
    public void apply() {
        manager.apply();
    }

    @Override
    public void resetToDefault(ResetAction action) {
        manager.resetToDefault(action);
    }

    @Override
    public void sync() {
        manager.sync();
    }

    @Override
    public boolean isSynced() {
        return manager.isSynced();
    }

    @Override
    public boolean isAlwaysSynced() {
        return manager.isAlwaysSynced();
    }

    @Override
    public boolean isDefault() {
        return manager.isDefault();
    }

    @Override
    public void addListener(StateListener<Boolean> listener) {
        manager.addListener((oldValue, newValue) ->
                listener.onStateChange(oldValue == option, newValue == option));
    }
}
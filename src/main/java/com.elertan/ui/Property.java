package com.elertan.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public final class Property<T> {
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private volatile T value;

    public Property(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        T oldValue = this.value;
        if (Objects.equals(oldValue, value)) {
            return;
        }
        this.value = value;
        propertyChangeSupport.firePropertyChange("value", oldValue, value);
    }

    public void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}

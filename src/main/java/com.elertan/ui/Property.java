package com.elertan.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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

    public <U> Property<U> derive(Function<T, U> transform) {
        Objects.requireNonNull(transform, "transform");
        // Initialize derived property with the transformed current value
        Property<U> derived = new Property<>(transform.apply(this.value));

        // Update the derived property whenever this property's value changes
        PropertyChangeListener listener = evt -> {
            if ("value".equals(evt.getPropertyName())) {
                @SuppressWarnings("unchecked")
                T newValue = (T) evt.getNewValue();
                derived.set(transform.apply(newValue));
            }
        };
        this.addListener(listener);

        return derived;
    }

    public static <R> Property<R> deriveMany(
            List<? extends Property<?>> properties,
            Function<List<?>, R> combiner
    ) {
        Objects.requireNonNull(properties, "properties");
        Objects.requireNonNull(combiner, "combiner");

        if (properties.contains(null)) {
            throw new NullPointerException("properties contains null");
        }

        // Initialize the derived property using the current values
        final List<Object> initialValues = new ArrayList<>(properties.size());
        for (Property<?> p : properties) {
            initialValues.add(p.get());
        }
        final Property<R> derived = new Property<>(
                combiner.apply(Collections.unmodifiableList(initialValues))
        );

        // Recompute the combined value whenever any source property changes
        final PropertyChangeListener listener = evt -> {
            if ("value".equals(evt.getPropertyName())) {
                final List<Object> values = new ArrayList<>(properties.size());
                for (Property<?> p : properties) {
                    values.add(p.get());
                }
                derived.set(combiner.apply(Collections.unmodifiableList(values)));
            }
        };

        for (Property<?> p : properties) {
            p.addListener(listener);
        }

        return derived;
    }
}

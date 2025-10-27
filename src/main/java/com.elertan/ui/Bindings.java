package com.elertan.ui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Bindings {
    public static AutoCloseable bindEnabled(JComponent component, Property<Boolean> property) {
        Consumer<Boolean> valueConsumer = (Boolean value) -> {
            if (Objects.equals(component.isEnabled(), value)) {
                return;
            }
            component.setEnabled(value);
        };

        PropertyChangeListener listener = (event) -> invokeOnEDT(() -> {
            @SuppressWarnings("unchecked")
            Boolean newValue = (Boolean) event.getNewValue();
            valueConsumer.accept(newValue);
        });

        property.addListener(listener);
        valueConsumer.accept(property.get());

        return () -> property.removeListener(listener);
    }

    public static AutoCloseable bindTextFieldText(JTextField component, Property<String> property) {
        Consumer<String> valueConsumer = (String value) -> {
            String textValue = value == null ? "" : value;
            if (Objects.equals(component.getText(), textValue)) {
                return;
            }
            int caretPosition = component.getCaretPosition();
            component.setText(textValue);
            int newCaretPosition = Math.min(caretPosition, textValue.length());
            if (caretPosition == newCaretPosition) {
                return;
            }
            component.setCaretPosition(newCaretPosition);
        };

        PropertyChangeListener listener = (event) -> invokeOnEDT(() -> {
            String newValue = (String) event.getNewValue();
            valueConsumer.accept(newValue);
        });

        property.addListener(listener);
        valueConsumer.accept(property.get());

        return () -> property.removeListener(listener);
    }

    public static <E extends Enum<E>> AutoCloseable bindCardLayout(JPanel host, CardLayout cardLayout, Property<E> property, Function<E, JPanel> build) {
        final Set<E> builtPanels = new HashSet<>();

        Consumer<E> valueConsumer = (E value) -> {
            if (value == null) {
                throw new IllegalArgumentException("property must have a non-null value");
            }

            String key = value.name();

            if (!builtPanels.contains(value)) {
                builtPanels.add(value);
                host.add(build.apply(value), key);
            }

            cardLayout.show(host, key);
        };

        PropertyChangeListener listener = (event) -> invokeOnEDT(() -> {
            @SuppressWarnings("unchecked")
            E newValue = (E) event.getNewValue();
            valueConsumer.accept(newValue);
        });

        property.addListener(listener);
        valueConsumer.accept(property.get());

        return () -> property.removeListener(listener);
    }

    private static void invokeOnEDT(Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }
}

package com.elertan.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Bindings {
    public static AutoCloseable bindEnabled(JComponent component, Property<Boolean> property) {
        Supplier<Boolean> getter = component::isEnabled;
        Consumer<Boolean> setter = component::setEnabled;

        return bind(property, getter, setter);
    }

    public static AutoCloseable bindVisible(JComponent component, Property<Boolean> property) {
        Supplier<Boolean> getter = component::isVisible;
        Consumer<Boolean> setter = component::setVisible;

        return bind(property, getter, setter);
    }

    public static AutoCloseable bindSelected(AbstractButton component, Property<Boolean> property) {
        Supplier<Boolean> getter = component::isSelected;
        Consumer<Boolean> setter = component::setSelected;

        return bind(property, getter, setter);
    }

    public static AutoCloseable bindLabelText(JLabel component, Property<String> property) {
        Supplier<String> getter = component::getText;
        Consumer<String> setter = (String value) -> {
            String textValue = value == null ? "" : value;
            component.setText(textValue);
        };

        return bind(property, getter, setter);
    }

    public static AutoCloseable bindTextFieldText(JTextField component, Property<String> property) {
        Supplier<String> getter = component::getText;

        Consumer<String> setter = (String value) -> {
            String textValue = value == null ? "" : value;
            int caretPosition = component.getCaretPosition();
            component.setText(textValue);
            int newCaretPosition = Math.min(caretPosition, textValue.length());
            if (caretPosition == newCaretPosition) {
                return;
            }
            component.setCaretPosition(newCaretPosition);
        };

        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                property.set(component.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                property.set(component.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                property.set(component.getText());
            }
        };

        Document document = component.getDocument();
        document.addDocumentListener(documentListener);

        @SuppressWarnings("resource")
        AutoCloseable binding = bind(property, getter, setter);

        return () -> {
            binding.close();
            document.removeDocumentListener(documentListener);
        };
    }

    public static <E extends Enum<E>, P extends JPanel> AutoCloseable bindCardLayout(JPanel host, CardLayout cardLayout, Property<E> property, Function<E, P> build) {
        AtomicReference<E> lastEnum = new AtomicReference<>(null);
        final Map<E, P> builtPanels = new HashMap<>();

        Supplier<E> getter = lastEnum::get;

        Consumer<E> setter = (E enumValue) -> {
            if (enumValue == null) {
                throw new IllegalArgumentException("property must have a non-null value");
            }

            String key = enumValue.name();

            if (!builtPanels.containsKey(enumValue)) {
                P panel = build.apply(enumValue);
                builtPanels.put(enumValue, panel);
                host.add(panel, key);
            }

            lastEnum.set(enumValue);
            cardLayout.show(host, key);
        };

        return bind(property, getter, setter);
    }

    public static <T> AutoCloseable bind(Property<T> property, Supplier<T> getter, Consumer<T> setter) {
        PropertyChangeListener listener = (event) -> invokeOnEDT(() -> {
            @SuppressWarnings("unchecked")
            T newValue = (T) event.getNewValue();

            T oldValue = getter.get();
            if (Objects.equals(oldValue, newValue)) {
                return;
            }
            setter.accept(newValue);
        });

        property.addListener(listener);
        setter.accept(property.get());

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

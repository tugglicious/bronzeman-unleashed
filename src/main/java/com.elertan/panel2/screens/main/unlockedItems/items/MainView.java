package com.elertan.panel2.screens.main.unlockedItems.items;

import com.google.inject.ImplementedBy;

import javax.swing.*;

public class MainView extends JPanel {
    @ImplementedBy(FactoryImpl.class)
    public interface Factory {
        MainView create();
    }

    private static final class FactoryImpl implements Factory {
        @Override
        public MainView create() {
            return new MainView();
        }
    }

    private MainView() {}
}

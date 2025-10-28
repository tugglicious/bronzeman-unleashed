package com.elertan.panel2.screens.main.unlockedItems.items;

import com.elertan.ui.Bindings;
import com.google.inject.ImplementedBy;

import javax.swing.*;

public class MainView extends JPanel {
    @ImplementedBy(FactoryImpl.class)
    public interface Factory {
        MainView create(MainViewViewModel viewModel);
    }

    private static final class FactoryImpl implements Factory {
        @Override
        public MainView create(MainViewViewModel viewModel) {
            return new MainView(viewModel);
        }
    }

    private MainView(MainViewViewModel viewModel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel();
        Bindings.bindLabelText(lbl, viewModel.unlockedItems.derive((list) -> list == null ? "loading" : list.isEmpty() ? "no items" : list.size() + " items"));
        add(lbl);
    }
}

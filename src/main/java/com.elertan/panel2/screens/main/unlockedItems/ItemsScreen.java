package com.elertan.panel2.screens.main.unlockedItems;

import com.elertan.panel2.screens.main.UnlockedItemsScreenViewModel;
import com.elertan.panel2.screens.main.unlockedItems.items.HeaderView;
import com.elertan.panel2.screens.main.unlockedItems.items.MainView;
import com.elertan.ui.Property;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.swing.*;
import java.awt.*;

public class ItemsScreen extends JPanel {
    @ImplementedBy(FactoryImpl.class)
    public interface Factory {
        ItemsScreen create(Property<UnlockedItemsScreenViewModel.SortedBy> sortedBy);
    }

    @Singleton
    private static final class FactoryImpl implements Factory {
        @Inject
        private HeaderView.Factory headerViewFactory;
        @Inject
        private MainView.Factory mainViewFactory;

        @Override
        public ItemsScreen create(Property<UnlockedItemsScreenViewModel.SortedBy> sortedBy) {
            return new ItemsScreen(headerViewFactory, mainViewFactory, sortedBy);
        }
    }

    private ItemsScreen(HeaderView.Factory headerViewFactory, MainView.Factory mainViewFactory, Property<UnlockedItemsScreenViewModel.SortedBy> sortedBy) {
        setLayout(new BorderLayout());

        HeaderView headerView = headerViewFactory.create(sortedBy);
        add(headerView, BorderLayout.NORTH);

        MainView mainView = mainViewFactory.create();
        add(mainView, BorderLayout.CENTER);
    }
}

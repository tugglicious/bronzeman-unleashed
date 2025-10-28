package com.elertan.panel2.screens.main;

import com.elertan.data.UnlockedItemsDataProvider;
import com.elertan.models.UnlockedItem;
import com.elertan.ui.Property;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UnlockedItemsScreenViewModel implements AutoCloseable {

    @ImplementedBy(FactoryImpl.class)
    public interface Factory {
        UnlockedItemsScreenViewModel create();
    }

    @Singleton
    private static final class FactoryImpl implements Factory {
        @Inject
        private UnlockedItemsDataProvider unlockedItemsDataProvider;

        @Override
        public UnlockedItemsScreenViewModel create() {
            return new UnlockedItemsScreenViewModel(unlockedItemsDataProvider);
        }
    }

    public enum Screen {
        LOADING,
        ITEMS
    }

    public final Property<List<UnlockedItem>> unlockedItems;

    private final UnlockedItemsDataProvider unlockedItemsDataProvider;
    private final UnlockedItemsDataProvider.UnlockedItemsMapListener unlockedItemsMapListener;

    private UnlockedItemsScreenViewModel(UnlockedItemsDataProvider unlockedItemsDataProvider) {
        this.unlockedItemsDataProvider = unlockedItemsDataProvider;

        // TODO: Change this into something that runs on the background
        Map<Integer, UnlockedItem> unlockedItemsMap = unlockedItemsDataProvider.getUnlockedItemsMap();
        unlockedItems = new Property<>(new ArrayList<>(unlockedItemsMap.values()));
        unlockedItemsMapListener = new UnlockedItemsDataProvider.UnlockedItemsMapListener() {
            @Override
            public void onUpdate(UnlockedItem unlockedItem) {
                unlockedItems.set(new ArrayList<>(unlockedItemsMap.values()));
            }

            @Override
            public void onDelete(UnlockedItem unlockedItem) {
                unlockedItems.set(new ArrayList<>(unlockedItemsMap.values()));
            }
        };
        unlockedItemsDataProvider.addUnlockedItemsMapListener(unlockedItemsMapListener);
    }

    @Override
    public void close() throws Exception {
        unlockedItemsDataProvider.removeUnlockedItemsMapListener(unlockedItemsMapListener);
    }
}

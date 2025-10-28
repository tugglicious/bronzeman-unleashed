package com.elertan.panel2.screens.main;

import com.elertan.data.UnlockedItemsDataProvider;
import com.elertan.models.UnlockedItem;
import com.elertan.ui.Property;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
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

    public enum SortedBy {
        UNLOCKED_AT_ASC,
        ALPHABETICAL_ASC,
        PLAYER_ASC,
        UNLOCKED_AT_DESC,
        ALPHABETICAL_DESC,
        PLAYER_DESC,
    }

    public final Property<List<UnlockedItem>> unlockedItems;
    public final Property<SortedBy> sortedBy = new Property<>(SortedBy.UNLOCKED_AT_DESC);

    private final UnlockedItemsDataProvider unlockedItemsDataProvider;
    private final UnlockedItemsDataProvider.UnlockedItemsMapListener unlockedItemsMapListener;

    private final PropertyChangeListener sortedByListener = this::sortedByListener;

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

        sortedBy.addListener(sortedByListener);
    }

    @Override
    public void close() throws Exception {
        sortedBy.removeListener(sortedByListener);
        unlockedItemsDataProvider.removeUnlockedItemsMapListener(unlockedItemsMapListener);
    }

    private void sortedByListener(PropertyChangeEvent event) {
        SortedBy sortedByValue = (SortedBy) event.getNewValue();
        log.info("sorted by changed to: {}", sortedByValue);
    }
}

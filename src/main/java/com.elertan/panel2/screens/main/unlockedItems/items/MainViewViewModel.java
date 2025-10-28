package com.elertan.panel2.screens.main.unlockedItems.items;

import com.elertan.models.UnlockedItem;
import com.elertan.panel2.screens.main.UnlockedItemsScreenViewModel;
import com.elertan.ui.Property;
import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainViewViewModel implements AutoCloseable {
    @ImplementedBy(FactoryImpl.class)
    public interface Factory {
        MainViewViewModel create(Property<List<UnlockedItem>> allUnlockedItems, Property<UnlockedItemsScreenViewModel.SortedBy> sortedBy, Property<Long> unlockedByAccountHash);
    }

    @Singleton
    private static final class FactoryImpl implements Factory {
        @Override
        public MainViewViewModel create(Property<List<UnlockedItem>> allUnlockedItems, Property<UnlockedItemsScreenViewModel.SortedBy> sortedBy, Property<Long> unlockedByAccountHash) {
            return new MainViewViewModel(allUnlockedItems, sortedBy, unlockedByAccountHash);
        }
    }

    public final Property<List<UnlockedItem>> unlockedItems;

    private MainViewViewModel(Property<List<UnlockedItem>> allUnlockedItems, Property<UnlockedItemsScreenViewModel.SortedBy> sortedBy, Property<Long> unlockedByAccountHash) {
        unlockedItems = Property.deriveManyAsync(
                Arrays.asList(allUnlockedItems, sortedBy, unlockedByAccountHash),
                (values) -> {
                    @SuppressWarnings("unchecked")
                    List<UnlockedItem> allUnlockedItemsValue = (List<UnlockedItem>) values.get(0);
                    UnlockedItemsScreenViewModel.SortedBy sortedByValue = (UnlockedItemsScreenViewModel.SortedBy) values.get(1);
                    Long unlockedByAccountHashValue = (Long) values.get(2);

                    if (allUnlockedItemsValue == null) {
                        return null;
                    }

                    return allUnlockedItemsValue.stream()
                            .filter(item -> {
                               if (unlockedByAccountHashValue != null)  {
                                   return item.getAcquiredByAccountHash() == unlockedByAccountHashValue;
                               }

                               return true;
                            })
                            .collect(Collectors.toList());
                }
        );
    }

    @Override
    public void close() throws Exception {

    }
}

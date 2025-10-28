package com.elertan.panel2.screens.main.unlockedItems.items;

import com.elertan.BUResourceService;
import com.elertan.panel2.BUPanel2;
import com.elertan.panel2.screens.main.UnlockedItemsScreenViewModel;
import com.elertan.ui.Bindings;
import com.elertan.ui.Property;
import com.google.common.collect.ImmutableMap;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.IconTextField;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HeaderView extends JPanel implements AutoCloseable {
    @ImplementedBy(FactoryImpl.class)
    public interface Factory {
        HeaderView create(Property<UnlockedItemsScreenViewModel.SortedBy> sortedBy);
    }

    private static final class FactoryImpl implements Factory {
        @Inject
        private BUResourceService buResourceService;

        @Override
        public HeaderView create(Property<UnlockedItemsScreenViewModel.SortedBy> sortedBy) {
            return new HeaderView(buResourceService, sortedBy);
        }
    }

    private final AutoCloseable sortedByComboBoxBinding;

    private HeaderView(BUResourceService buResourceService, Property<UnlockedItemsScreenViewModel.SortedBy> sortedBy) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 3));

        IconTextField searchField = new IconTextField();
        searchField.setIcon(IconTextField.Icon.SEARCH);
        searchField.setPreferredSize(new Dimension(BUPanel2.PANEL_WIDTH - 20, 30));
        searchField.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        searchField.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
        searchField.setMinimumSize(new Dimension(0, 30));

        JButton configButton = new JButton();
        configButton.setIcon(new ImageIcon(buResourceService.getConfigureIconBufferedImage()));
        configButton.setToolTipText("Open configuration");
        configButton.setPreferredSize(new Dimension(30, 30));
        configButton.setFocusable(false);
        configButton.setBorderPainted(false);
        configButton.setContentAreaFilled(false);
        configButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        configButton.addActionListener(e -> {

        });

        JPanel searchbarHeader = new JPanel(new BorderLayout(5, 0));
        searchbarHeader.add(searchField, BorderLayout.CENTER);
        searchbarHeader.add(configButton, BorderLayout.EAST);

        add(searchbarHeader);

        add(Box.createVerticalStrut(8));

        JPanel filterAndSortPanel = new JPanel();
        filterAndSortPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 7));
        filterAndSortPanel.setLayout(new BoxLayout(filterAndSortPanel, BoxLayout.Y_AXIS));

        JPanel sortedByRow = new JPanel(new BorderLayout(5, 0));
        JLabel sortedByLabel = new JLabel("Sorted by:");
        sortedByLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        JComboBox<UnlockedItemsScreenViewModel.SortedBy> sortedByComboBox = new JComboBox<>();

        Map<UnlockedItemsScreenViewModel.SortedBy, String> sortedByEnumToString = ImmutableMap.<UnlockedItemsScreenViewModel.SortedBy, String>builder()
                .put(UnlockedItemsScreenViewModel.SortedBy.UNLOCKED_AT_ASC, "Unlocked at (asc)")
                .put(UnlockedItemsScreenViewModel.SortedBy.ALPHABETICAL_ASC, "Alphabetical (asc)")
                .put(UnlockedItemsScreenViewModel.SortedBy.PLAYER_ASC, "Player (asc)")
                .put(UnlockedItemsScreenViewModel.SortedBy.UNLOCKED_AT_DESC, "Unlocked at (desc)")
                .put(UnlockedItemsScreenViewModel.SortedBy.ALPHABETICAL_DESC, "Alphabetical (desc)")
                .put(UnlockedItemsScreenViewModel.SortedBy.PLAYER_DESC, "Player (desc)")
                .build();
        Property<List<UnlockedItemsScreenViewModel.SortedBy>> sortedByOptions = new Property<>(
                new ArrayList<>(sortedByEnumToString.keySet())
        );
        sortedByComboBoxBinding = Bindings.bindComboBox(sortedByComboBox, sortedByOptions, sortedBy, sortedByEnumToString);

        sortedByRow.add(sortedByLabel, BorderLayout.WEST);
        sortedByRow.add(sortedByComboBox, BorderLayout.CENTER);

        filterAndSortPanel.add(sortedByRow);

        filterAndSortPanel.add(Box.createVerticalStrut(5));

        JPanel unlockedByRow = new JPanel(new BorderLayout(5, 0));
        JLabel unlockedByLabel = new JLabel("Unlocked by:");
        unlockedByLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        JComboBox<String> unlockedByComboBox = new JComboBox<>();

        unlockedByRow.add(unlockedByLabel, BorderLayout.WEST);
        unlockedByRow.add(unlockedByComboBox, BorderLayout.CENTER);

        filterAndSortPanel.add(unlockedByRow);

        add(filterAndSortPanel);
    }

    @Override
    public void close() throws Exception {
        sortedByComboBoxBinding.close();
    }

}

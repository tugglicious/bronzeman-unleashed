package com.elertan.panel2.screens.setup;

import com.elertan.panel2.components.GameRulesEditor;
import com.elertan.panel2.components.GameRulesEditorViewModel;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import javax.swing.*;
import java.awt.*;

public class GameRulesStepView extends JPanel {
    @ImplementedBy(FactoryImpl.class)
    public interface Factory {
        GameRulesStepView create(GameRulesStepViewViewModel viewModel);
    }

    @Slf4j
    @Singleton
    private static final class FactoryImpl implements Factory {
        @Inject
        private Client client;
        @Inject
        GameRulesEditor.Factory gameRulesEditorFactory;
        @Inject
        GameRulesEditorViewModel.Factory gameRulesEditorViewModelFactory;

        @Override
        public GameRulesStepView create(GameRulesStepViewViewModel viewModel) {
            GameRulesEditorViewModel.Props props = new GameRulesEditorViewModel.Props(
                    client.getAccountHash(),
                    null,
                    (newGameRules) -> {
                        log.info("Game rules updated: {}", newGameRules);
                    },
                    false
            );
            GameRulesEditorViewModel gameRulesEditorViewModel = gameRulesEditorViewModelFactory.create(props);
            GameRulesEditor gameRulesEditor = gameRulesEditorFactory.create(gameRulesEditorViewModel);
            return new GameRulesStepView(viewModel, gameRulesEditor);
        }
    }

    private GameRulesStepView(GameRulesStepViewViewModel viewModel, GameRulesEditor gameRulesEditor) {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JLabel titleLabel = new JLabel("Game Rules");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 15f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.add(titleLabel);
        header.add(Box.createVerticalStrut(10));

        add(gameRulesEditor, BorderLayout.CENTER);

        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        buttonRow.setOpaque(false);

        JButton backButton = new JButton("Go back");
        backButton.addActionListener(e -> viewModel.onBackButtonClicked());
        buttonRow.add(backButton);

        buttonRow.add(Box.createHorizontalGlue());

        JButton finishButton = new JButton("Finish");
        finishButton.addActionListener(e -> viewModel.onFinishButtonClicked());
        buttonRow.add(finishButton);

        // TODO: might not be correct
//        add(buttonRow);

        // Filler to keep everything pinned to the top
//        JPanel filler = new JPanel();
//        filler.setOpaque(false);
//        gbc.gridy++;
//        gbc.weighty = 1.0;
//        center.add(filler, gbc);
    }
}

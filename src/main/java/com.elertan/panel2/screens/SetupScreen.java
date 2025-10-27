package com.elertan.panel2.screens;

import com.elertan.panel2.screens.setup.SetupViewModel;
import com.elertan.ui.Bindings;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.swing.*;
import java.awt.*;

public class SetupScreen extends JPanel implements AutoCloseable {
    private final SetupViewModel viewModel;
    private final AutoCloseable cardLayoutBinding;

    @Inject
    public SetupScreen(Provider<SetupViewModel> viewModelProvider) {
        viewModel = viewModelProvider.get();

        CardLayout cardLayout = new CardLayout();
        setLayout(cardLayout);

        cardLayoutBinding = Bindings.bindCardLayout(this, cardLayout, viewModel.step, this::buildStep);
    }


    @Override
    public void close() throws Exception {
        cardLayoutBinding.close();
        viewModel.close();
    }

    private JPanel buildStep(SetupViewModel.Step step) {
        switch (step) {
            case REMOTE:
                return new JPanel();
            case GAME_RULES:
                return new JPanel();
        }

        throw new IllegalStateException("Unknown step: " + step);
    }
}

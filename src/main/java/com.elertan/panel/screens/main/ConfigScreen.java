package com.elertan.panel.screens.main;

import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;
import javax.swing.JPanel;

public class ConfigScreen extends JPanel {

    private ConfigScreen(ConfigScreenViewModel viewModel) {

    }

    @ImplementedBy(FactoryImpl.class)
    public interface Factory {

        ConfigScreen create(ConfigScreenViewModel viewModel);
    }

    @Singleton
    private static final class FactoryImpl implements Factory {

        @Override
        public ConfigScreen create(ConfigScreenViewModel viewModel) {
            return new ConfigScreen(viewModel);
        }
    }
}

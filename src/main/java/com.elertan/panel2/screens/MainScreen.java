package com.elertan.panel2.screens;

import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;

import javax.swing.*;

public class MainScreen extends JPanel implements AutoCloseable {
    @ImplementedBy(FactoryImpl.class)
    public interface Factory {
        MainScreen create(MainScreenViewModel viewModel);
    }

    @Singleton
    private static final class FactoryImpl implements Factory {
        @Override
        public MainScreen create(MainScreenViewModel viewModel) {
            return new MainScreen(viewModel);
        }
    }

    private MainScreen(MainScreenViewModel viewModel) {
    }

    @Override
    public void close() throws Exception {

    }
}

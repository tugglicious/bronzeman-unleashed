package com.elertan.panel2.screens;

import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;

public class MainScreenViewModel {
    @ImplementedBy(FactoryImpl.class)
    public interface Factory {
        MainScreenViewModel create();
    }

    @Singleton
    private static final class FactoryImpl implements Factory {
        @Override
        public MainScreenViewModel create() {
            return new MainScreenViewModel();
        }
    }

    private MainScreenViewModel() {
    }
}

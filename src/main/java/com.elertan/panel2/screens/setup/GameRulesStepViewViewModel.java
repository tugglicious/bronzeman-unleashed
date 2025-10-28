package com.elertan.panel2.screens.setup;

import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;

public class GameRulesStepViewViewModel {
    @ImplementedBy(FactoryImpl.class)
    public interface Factory {
        GameRulesStepViewViewModel create(Listener listener);
    }

    @Singleton
    private static final class FactoryImpl implements Factory {
        @Override
        public GameRulesStepViewViewModel create(Listener listener) {
            return new GameRulesStepViewViewModel(listener);
        }
    }

    public interface Listener {
        void onBack();
        void onFinish();
    }

    private final Listener listener;

    private GameRulesStepViewViewModel(Listener listener) {
        this.listener = listener;
    }

    public void onBackButtonClicked() {
        listener.onBack();
    }

    public void onFinishButtonClicked() {
        listener.onFinish();
    }
}

package com.elertan.panel.screens.setup.remoteStep;

import com.elertan.ui.Property;
import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;

public class CheckingViewViewModel implements AutoCloseable {

    private final Listener listener;
    public Property<Boolean> isCancelled = new Property<>(false);

    private CheckingViewViewModel(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void close() throws Exception {

    }

    public void onCancelButtonClick() {
        isCancelled.set(true);

        listener.onCancel();

        isCancelled.set(false);
    }

    @ImplementedBy(FactoryImpl.class)
    public interface Factory {

        CheckingViewViewModel create(Listener listener);
    }

    public interface Listener {

        void onCancel();
    }

    @Singleton
    private static class FactoryImpl implements Factory {

        @Override
        public CheckingViewViewModel create(Listener listener) {
            return new CheckingViewViewModel(listener);
        }
    }
}

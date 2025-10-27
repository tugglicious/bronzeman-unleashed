package com.elertan.panel2.screens.setup.remoteStep;

import com.elertan.remote.firebase.FirebaseRealtimeDatabaseURL;
import com.elertan.ui.Property;
import com.google.inject.Inject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public final class EntryViewViewModel implements AutoCloseable {
    public final Property<Boolean> isLoading = new Property<>(false);
    public final Property<Boolean> isValid;
    public final Property<String> firebaseRealtimeDatabaseURL = new Property<>("");
    public final Property<String> errorMessage = new Property<>(null);

    @Inject
    public EntryViewViewModel() {
        isValid = firebaseRealtimeDatabaseURL.derive(this::isValidFirebaseRealtimeDatabaseURL);
    }

    @Override
    public void close() throws Exception {
    }

    public void onContinueClick() {
        isLoading.set(true);

//            setState(RemoteConfigurationView.State.CHECKING);
//            checkUrl();

        isLoading.set(false);
    }

    private boolean isValidFirebaseRealtimeDatabaseURL(String value) {
        try {
            new FirebaseRealtimeDatabaseURL(value);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

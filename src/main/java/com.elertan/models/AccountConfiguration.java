package com.elertan.models;

import com.elertan.remote.firebase.FirebaseRealtimeDatabaseURL;
import lombok.Getter;

public class AccountConfiguration {

    @Getter
    private final FirebaseRealtimeDatabaseURL firebaseRealtimeDatabaseURL;

    public AccountConfiguration(FirebaseRealtimeDatabaseURL firebaseRealtimeDatabaseURL) {
        this.firebaseRealtimeDatabaseURL = firebaseRealtimeDatabaseURL;
    }
}

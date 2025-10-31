package com.elertan.remote.firebase.storageAdapters;

import com.elertan.models.AccountHash;
import com.elertan.models.GroundItemOwnedByKey;
import com.elertan.remote.firebase.FirebaseKeyValueStorageAdapterBase;
import com.elertan.remote.firebase.FirebaseRealtimeDatabase;
import com.google.gson.Gson;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GroundItemOwnedByKeyValueStorageAdapter
    extends FirebaseKeyValueStorageAdapterBase<GroundItemOwnedByKey, AccountHash> {

    private final static String BASE_PATH = "/GroundItemOwnedBy";
    private final static Function<String, GroundItemOwnedByKey> stringToKey = GroundItemOwnedByKey::fromKey;
    private final static Function<GroundItemOwnedByKey, String> keyToString = GroundItemOwnedByKey::toKey;

    public GroundItemOwnedByKeyValueStorageAdapter(FirebaseRealtimeDatabase db, Gson gson) {
        super(
            BASE_PATH, db, gson, stringToKey, keyToString, (jsonElement) -> {
                if (jsonElement == null || jsonElement.isJsonNull()) {
                    return null;
                }

                return gson.fromJson(jsonElement, AccountHash.class);
            }
        );
    }
}

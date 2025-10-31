package com.elertan.models;

import com.elertan.gson.AccountHashJsonAdapter;
import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
public class GroundItemOwnedByData {

    @Setter
    @Getter
    @JsonAdapter(AccountHashJsonAdapter.class)
    private long accountHash;

    @Setter
    @Getter
    @NonNull
    private ISOOffsetDateTime despawnsAt;

}

package com.elertan.remote.firebase;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.net.MalformedURLException;

public final class FirebaseRealtimeDatabaseURLAdapter
    implements JsonSerializer<FirebaseRealtimeDatabaseURL>, JsonDeserializer<FirebaseRealtimeDatabaseURL> {

    @Override
    public JsonElement serialize(FirebaseRealtimeDatabaseURL src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public FirebaseRealtimeDatabaseURL deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
        try {
            return new FirebaseRealtimeDatabaseURL(json.getAsString());
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new JsonParseException("Invalid FirebaseRealtimeDatabaseURL: " + json, e);
        }
    }
}
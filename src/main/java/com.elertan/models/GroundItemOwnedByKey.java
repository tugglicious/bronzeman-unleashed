package com.elertan.models;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@AllArgsConstructor
@JsonAdapter(GroundItemOwnedByKey.Adapter.class)
public class GroundItemOwnedByKey {

    @Getter
    private final int itemId;
    @Getter
    private final int world;
    @Getter
    private final int worldViewId;
    @Getter
    private final int plane;
    @Getter
    private final int worldX;
    @Getter
    private final int worldY;

    public static GroundItemOwnedByKey fromKey(String key) throws IllegalArgumentException {
        String[] parts = key.split("_");
        String itemIdStr = parts[0];
        String worldStr = parts[1];
        String worldViewIdStr = parts[2];
        String planeStr = parts[3];
        String worldXStr = parts[4];
        String worldYStr = parts[5];
        try {
            int itemId = Integer.parseInt(itemIdStr);
            int world = Integer.parseInt(worldStr);
            int worldViewId = Integer.parseInt(worldViewIdStr);
            int plane = Integer.parseInt(planeStr);
            int worldX = Integer.parseInt(worldXStr);
            int worldY = Integer.parseInt(worldYStr);
            return new GroundItemOwnedByKey(itemId, world, worldViewId, plane, worldX, worldY);
        } catch (NumberFormatException e) {
            log.error("Invalid key format: {}", key, e);
            throw new IllegalArgumentException("Invalid key format: " + key);
        }
    }

    public String toKey() {
        return String.format(
            "{%d_%d_%d_%d_%d_%d}",
            itemId,
            world,
            worldViewId,
            plane,
            worldX,
            worldY
        );
    }

    @Override
    public String toString() {
        String key = toKey();
        StringBuilder sb = new StringBuilder();
        sb.append("GroundItemOwnedByKey{");
        sb.append("itemId=").append(itemId);
        sb.append(", world=").append(world);
        sb.append(", worldViewId=").append(worldViewId);
        sb.append(", plane=").append(plane);
        sb.append(", worldX=").append(worldX);
        sb.append(", worldY=").append(worldY);
        sb.append(", key='").append(key).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static final class Adapter extends TypeAdapter<GroundItemOwnedByKey> {

        @Override
        public void write(JsonWriter out, GroundItemOwnedByKey val) throws IOException {
            if (val == null) {
                out.nullValue();
                return;
            }
            out.value(val.toKey());
        }

        @Override
        public GroundItemOwnedByKey read(JsonReader in) throws IOException {
            String text = in.nextString();
            if (text == null) {
                return null;
            }
            return GroundItemOwnedByKey.fromKey(text);
        }
    }
}


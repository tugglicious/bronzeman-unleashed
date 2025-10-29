package com.elertan.chat;

import lombok.Getter;

public class TotalLevelParsedGameMessage implements ParsedGameMessage {

    @Getter
    private final int totalLevel;

    public TotalLevelParsedGameMessage(int totalLevel) {
        this.totalLevel = totalLevel;
    }

    @Override
    public ParsedGameMessageType getType() {
        return ParsedGameMessageType.TotalLevel;
    }
}

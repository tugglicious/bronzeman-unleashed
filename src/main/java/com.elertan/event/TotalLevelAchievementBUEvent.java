package com.elertan.event;

import com.elertan.models.ISOOffsetDateTime;
import lombok.Getter;

public class TotalLevelAchievementBUEvent extends BUEvent {

    @Getter
    private final int totalLevel;

    public TotalLevelAchievementBUEvent(
        long dispatchedFromAccountHash,
        ISOOffsetDateTime isoOffsetDateTime,
        int totalLevel
    ) {
        super(dispatchedFromAccountHash, isoOffsetDateTime);
        this.totalLevel = totalLevel;
    }

    @Override
    public BUEventType getType() {
        return BUEventType.TotalLevelAchievement;
    }
}

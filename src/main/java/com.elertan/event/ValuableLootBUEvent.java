package com.elertan.event;

import com.elertan.models.ISOOffsetDateTime;
import lombok.Getter;

public class ValuableLootBUEvent extends BUEvent {

    @Getter
    private final int itemId;
    @Getter
    private final int quantity;
    @Getter
    private final int pricePerItem;
    @Getter
    private final int npcId;

    public ValuableLootBUEvent(long dispatchedFromAccountHash, ISOOffsetDateTime isoOffsetDateTime,
        int itemId, int quantity, int pricePerItem, int npcId) {
        super(dispatchedFromAccountHash, isoOffsetDateTime);
        this.itemId = itemId;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.npcId = npcId;
    }

    @Override
    public BUEventType getType() {
        return BUEventType.ValuableLoot;
    }
}

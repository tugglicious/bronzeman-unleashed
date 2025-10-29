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

    public ValuableLootBUEvent(
        long dispatchedFromAccountHash,
        ISOOffsetDateTime isoOffsetDateTime, int itemId, int quantity, int pricePerItem
    ) {
        super(dispatchedFromAccountHash, isoOffsetDateTime);
        this.itemId = itemId;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
    }

    @Override
    public BUEventType getType() {
        return BUEventType.ValuableLoot;
    }
}

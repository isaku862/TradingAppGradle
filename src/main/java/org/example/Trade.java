package org.example;

import java.time.LocalDateTime;

public class Trade {

    private final LocalDateTime tradedDateTime;
    private final String symbol;
    private final String side;
    private final long quantity;
    private final double unitPrice;

    public Trade(LocalDateTime tradedDateTime, String symbol,
                 String side, long quantity, double unitPrice) {

        this.tradedDateTime = tradedDateTime;
        this.symbol = symbol;
        this.side = side;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public LocalDateTime getTradedDateTime() {
        return tradedDateTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSide() {
        return side;
    }

    public long getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
}
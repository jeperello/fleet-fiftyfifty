package com.jeperello.fleetfiftyfifty.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount) {
    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money of(String value) {
        return new Money(new BigDecimal(value));
    }

    public static Money zero() {
        return Money.of("0.00");
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }

    public Money percentage(int percent) {
        BigDecimal multiplier = BigDecimal.valueOf(percent)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return new Money(this.amount.multiply(multiplier));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0;
    }
}

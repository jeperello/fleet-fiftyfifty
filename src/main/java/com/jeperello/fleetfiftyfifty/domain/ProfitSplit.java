package com.jeperello.fleetfiftyfifty.domain;

public record ProfitSplit(int ownerPercentage) {

    public ProfitSplit {
        if (ownerPercentage < 0 || ownerPercentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
    }

    public static ProfitSplit ofDefault() {
        return new ProfitSplit(50);
    }

    public static ProfitSplit of(int ownerPercentage) {
        return new ProfitSplit(ownerPercentage);
    }

    public int driverPercentage() {
        return 100 - ownerPercentage;
    }
}
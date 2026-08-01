package com.jeperello.fleetfiftyfifty.domain;

public record BalanceResult(
        FleetRole debtor,
        FleetRole creditor,
        Money amount
) {
}